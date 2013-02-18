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
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import ch.zhaw.budgettool.datatransfer.Group;
import ch.zhaw.budgettool.datatransfer.TransferClass;
import ch.zhaw.budgettool.datatransfer.User;
import ch.zhaw.budgettool.datatransfer.tasks.LoginTask;
import ch.zhaw.budgettool.datatransfer.tasks.OnTaskCompleted;
import ch.zhaw.budgettool.datatransfer.tasks.UpdateEverythingTask;
import ch.zhaw.database.DatabaseHelper;
import ch.zhaw.database.UpdateHelper;
import ch.zhaw.database.UserManagementHelper;

public class RefreshActivity extends Activity implements OnTaskCompleted{
	
    SQLiteOpenHelper database;
    SQLiteDatabase connection;
    UserManagementHelper umh;
    UpdateHelper uh;
    User user;
    Group group;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
//	    database = new DatabaseHelper(this);
    	database = DatabaseHelper.getInstance(this);
	    connection = database.getWritableDatabase();
	    
	    umh = new UserManagementHelper(connection);
	    uh = new UpdateHelper(connection);
	    
	    user = umh.getUserFromDb(this);
	    new LoginTask(user, this).execute(user.getUsername(), user.getPassword());
	    
        ActionBarCompat.setDisplayHomeAsUpEnabled(this, true);
        
        Intent target = new Intent(this, AppNavHomeActivity.class);
        startActivity(target);
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
				//update everything
				user = (User)obj;
				int grpid = Integer.parseInt(user.getData().getUser().getGroup().getId());
				
				group = new Group(user);
				group.setId(grpid);
				
				new UpdateEverythingTask(group, uh, this).execute();
			}
		}else if(task == UpdateEverythingTask.class){
			//do something
		}
	}
}
