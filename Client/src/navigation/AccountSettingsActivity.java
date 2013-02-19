/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package navigation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import ch.zhaw.budgettool.R;
import ch.zhaw.budgettool.datatransfer.Group;
import ch.zhaw.budgettool.datatransfer.TransferClass;
import ch.zhaw.budgettool.datatransfer.User;
import ch.zhaw.budgettool.datatransfer.tasks.EditGroupTask;
import ch.zhaw.budgettool.datatransfer.tasks.EditUserTask;
import ch.zhaw.budgettool.datatransfer.tasks.LoginTask;
import ch.zhaw.budgettool.datatransfer.tasks.OnTaskCompleted;
import ch.zhaw.database.DatabaseHelper;
import ch.zhaw.database.UserManagementHelper;


public class AccountSettingsActivity extends Activity implements OnTaskCompleted{
	
	private int id = -1;
	private String username = "";
	private String password = "";
	
	private SQLiteOpenHelper database;
	private SQLiteDatabase connection;
	
	private UserManagementHelper umh;
	private User user;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = DatabaseHelper.getInstance(this);
	    connection = database.getWritableDatabase();
	    
	    umh = new UserManagementHelper(connection);
	    user = umh.getUserFromDb(this);
	    
        setContentView(R.layout.view_change_settings);

        ActionBarCompat.setDisplayHomeAsUpEnabled(this, true);
	    
	    Cursor user = connection.rawQuery("SELECT * FROM users ORDER BY id LIMIT 1", null);
	    
	    if (user.getCount() > 0) {
	    	user.moveToFirst();
	    	id = user.getInt(0);
	    	EditText mText; 
	    	mText = (EditText) findViewById(R.id.username_value);
	    	username = user.getString(3);
	    	mText.setText(username);
	    	EditText mText2; 
	    	password = user.getString(4);
	    	mText2 = (EditText) findViewById(R.id.userpassword_value);
	    	mText2.setText(password);
	    }
	    
	    user.close();
    }
    
    @Override
    protected void onDestroy() {
    	
//    	if (database != null) {
//    		database.close();
//    	}
//    	if (connection != null) {
//    		connection.close();
//    	}
	    super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return false;
    }

    public void onLaunchOtherTask(View v) {
    	
    	EditText usernameText, passwordText; 
    	usernameText = (EditText) findViewById(R.id.username_value);
    	passwordText = (EditText) findViewById(R.id.userpassword_value);
    	username = usernameText.getText().toString();
    	password = passwordText.getText().toString();

    	password = password == null ? "" : password;
    	
	    new LoginTask(user, this).execute(user.getUsername(), user.getPassword());
    }

	public void onTaskCompleted(Class task, TransferClass obj) {
		if(task == LoginTask.class){
			if(obj == null){
				//username / password invalid -> logout
				umh.logoutFromDB(this);
			}else{
				//create expense
				user = (User)obj;
				
				new EditUserTask(user, this).execute(username, password);
			}
		}else if(task == EditUserTask.class){
			if(obj != null){
				User u = (User)obj;
				if(u.getErrorMsg() == null){
					String sql = "UPDATE users SET username=\""+username+"\", password=\""+password+"\" WHERE id = "+id;
				    connection.execSQL(sql);
				    
			        Intent target = new Intent(AccountSettingsActivity.this, AppNavHomeActivity.class);
			        startActivity(target);
				}else{
					AlertDialog.Builder builder = new AlertDialog.Builder(AccountSettingsActivity.this);
		    		builder.setMessage(user.getErrorMsg());
		    		AlertDialog dialog = builder.create();
		    		dialog.show();
				}
			}else{
	    		AlertDialog.Builder builder = new AlertDialog.Builder(AccountSettingsActivity.this);
	    		builder.setMessage(R.string.editaccountfailed);
	    		AlertDialog dialog = builder.create();
	    		dialog.show();
			}
		}
	}
}
