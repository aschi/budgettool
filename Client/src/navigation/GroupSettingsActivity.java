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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import ch.zhaw.budgettool.R;
import ch.zhaw.budgettool.datatransfer.Expense;
import ch.zhaw.budgettool.datatransfer.Group;
import ch.zhaw.budgettool.datatransfer.TransferClass;
import ch.zhaw.budgettool.datatransfer.User;
import ch.zhaw.budgettool.datatransfer.tasks.CreateExpenseTask;
import ch.zhaw.budgettool.datatransfer.tasks.CreateGroupTask;
import ch.zhaw.budgettool.datatransfer.tasks.EditGroupTask;
import ch.zhaw.budgettool.datatransfer.tasks.LoginTask;
import ch.zhaw.budgettool.datatransfer.tasks.OnTaskCompleted;
import ch.zhaw.database.DatabaseHelper;
import ch.zhaw.database.UserManagementHelper;

public class GroupSettingsActivity extends Activity implements OnTaskCompleted {
	
	private int id = -1;
	private int serverId = -1;
	private String groupName = "";
	private String password = "";
	private double monthlyBudget = 0;
//	private int userId;
	
    SQLiteOpenHelper database;
    SQLiteDatabase connection;
    
    private String oldGroupname;
    private UserManagementHelper umh;
    private User user;
    private Group group;
    
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	    
        super.onCreate(savedInstanceState);
        
        database = DatabaseHelper.getInstance(this);
	    connection = database.getWritableDatabase();
	    

	    umh = new UserManagementHelper(connection);
	    user = umh.getUserFromDb(this);
	    
        setContentView(R.layout.view_change_group_settings);
        
        Button changeGroupButton = (Button) findViewById(R.id.changeGroupButton);

        ActionBarCompat.setDisplayHomeAsUpEnabled(this, true);
	    
	    Cursor group = connection.rawQuery("SELECT * FROM groups ORDER BY id LIMIT 1", null);
	    
	    if (group.getCount() > 0) {
	    	group.moveToFirst();
	    	serverId = group.getInt(group.getColumnIndex("serverId"));
	    	id = group.getInt(0);
	    	EditText mText; 
	    	mText = (EditText) findViewById(R.id.groupNameText);
	    	groupName = group.getString(3);
	    	oldGroupname = groupName;
	    	mText.setText(groupName);
	    	EditText mText2; 
	    	password = group.getString(4);
	    	mText2 = (EditText) findViewById(R.id.groupPasswordText);
	    	mText2.setText(password);
	    	EditText mText3;
	    	monthlyBudget = Double.parseDouble(group.getString(5));
	    	mText3 = (EditText) findViewById(R.id.monthlyBudgetText);
	    	mText3.setText("" + monthlyBudget);
	    }
	    
	    group.close();
	    
	    changeGroupButton.setOnClickListener(new OnClickListener() {
	    	
			public void onClick(View arg0) {
				EditText groupNameText, passwordgroupText, monthlyBudgetText; 
		    	groupNameText = (EditText) findViewById(R.id.groupNameText);
		    	passwordgroupText = (EditText) findViewById(R.id.groupPasswordText);
		    	monthlyBudgetText = (EditText) findViewById(R.id.monthlyBudgetText);
		    	groupName = groupNameText.getText().toString();
		    	password = passwordgroupText.getText().toString();
		    	monthlyBudget = Double.parseDouble(monthlyBudgetText.getText().toString());
		    	
		    	password = password == null ? "" : password;
			    
		    	//check login & initiate data transfer
				new LoginTask(user, GroupSettingsActivity.this).execute(user.getUsername(), user.getPassword());
			}
		});
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
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

	public void onTaskCompleted(Class task, TransferClass obj) {
		if(task == LoginTask.class){
			if(obj == null){
				//username / password invalid -> logout
				umh.logoutFromDB(this);
			}else{
				//create expense
				user = (User)obj;
				group = new Group(user);
				group.setId(serverId);
				group.setGroupname(oldGroupname);
				
				new EditGroupTask(group, this).execute(groupName, password, Double.toString(monthlyBudget));
			}
		}else if(task == EditGroupTask.class){
			if(obj != null){
				Group g = (Group)obj;
				if(g.getErrorMsg() == null){
					String sql = "UPDATE groups SET groupName=\""+groupName+"\", password=\""+password+"\", budget="+monthlyBudget+" WHERE id = "+id;
				    connection.execSQL(sql);
				    
			        Intent target = new Intent(GroupSettingsActivity.this, AppNavHomeActivity.class);
			        startActivity(target);
				}else{
					AlertDialog.Builder builder = new AlertDialog.Builder(GroupSettingsActivity.this);
		    		builder.setMessage(g.getErrorMsg());
		    		AlertDialog dialog = builder.create();
		    		dialog.show();
				}
			}else{
	    		AlertDialog.Builder builder = new AlertDialog.Builder(GroupSettingsActivity.this);
	    		builder.setMessage(R.string.editgroupfailed);
	    		AlertDialog dialog = builder.create();
	    		dialog.show();
			}
		}
	}
}
