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

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import ch.zhaw.budgettool.R;
import ch.zhaw.database.DatabaseHelper;

public class ExpensesOverviewActivity extends Activity {
	
    SQLiteOpenHelper database;
    SQLiteDatabase connection;
    
    private String userIdsString;


    private List<String> valueList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        database = DatabaseHelper.getInstance(this);
	    connection = database.getWritableDatabase();
        setContentView(R.layout.view_expenses_overview);

        ActionBarCompat.setDisplayHomeAsUpEnabled(this, true);
        userIdsString = getUserIds();
        
        //titles
        valueList.add("Description, Amount, Date, Username");
        
	    Cursor expense = connection.rawQuery("SELECT * FROM expenses WHERE userId IN " + userIdsString + ";", null);
	    
	    if (expense != null ) {
	        if  (expense.moveToFirst()) {
	            do {
	            	String username = getUsername(expense.getInt(2));
	            	valueList.add(expense.getString(3) + ", " + expense.getDouble(4) + ", " + expense.getString(5) + ", " + username);
	            }while (expense.moveToNext());
	        }
	    }

	    expense.close();
        
        ListAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, valueList);
       
        final ListView lv = (ListView)findViewById(R.id.expenseOverviewList);

        lv.setAdapter(adapter);


    }
    
    
	private String getUserIds() {
		
		String userIds = "(";
	    
	    Cursor user = connection.rawQuery("SELECT * FROM users ORDER BY id LIMIT 1", null);
	    boolean first = true;
	    if (user != null ) {
	        if  (user.moveToFirst()) {
	            do {
	            	if (!first) {
	            		userIds = userIds + ", ";
	            	}
	            	userIds =userIds + user.getInt(2);
	            	first = false;
	            }while (user.moveToNext());
	        }
	    }
	    
	    user.close();
	    userIds = userIds + ")";
	    
	    return userIds;
	}
	
	private String getUsername(int userId) {
		
		String username = "";
	    
	    Cursor user = connection.rawQuery("SELECT * FROM users WHERE serverId = " + userId, null);
	    
	    if (user.getCount() > 0) {
	    	user.moveToFirst();
	    	username = user.getString(3);
	    }
	    
	    user.close();
	    
	    return username;
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
}
