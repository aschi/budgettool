package ch.zhaw.database;

import navigation.ActionBarCompat;
import navigation.StartActivity;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ch.zhaw.budgettool.datatransfer.User;

public class UserManagementHelper{
	
	public User getUserFromDb(SQLiteDatabase connection, Activity activity){
		   	Cursor user = connection.rawQuery("SELECT id, username, password FROM users ORDER BY id LIMIT 1", null);
	 	    User u = null;
		   	
	 	    if (user.getCount() > 0) {
	 	    	user.moveToFirst();
		    	String username = user.getString(user.getColumnIndex("username"));
		    	String password = user.getString(user.getColumnIndex("username"));
                int id = user.getInt(user.getColumnIndex("id"));
                
                u = new User();
                u.setId(id);
                u.setUsername(username);
                u.setPassword(password);
	 	    } else {
	 	    	return null;
	 	    }
	 	    user.close();
	    	return u;
	}

	public void logoutFromDB(SQLiteDatabase connection, Activity activity){
		String sql = "DELETE FROM users;"; 
	    connection.execSQL(sql);
	    
    	String sql2 = "DELETE FROM groups;"; 
	    connection.execSQL(sql2);
	    
    	String sql3 = "DELETE FROM expenses;"; 
	    connection.execSQL(sql3);
	    
        ActionBarCompat.setDisplayHomeAsUpEnabled(activity, true);
        
        Intent target = new Intent(activity, StartActivity.class);
        activity.startActivity(target);	
	}
}
