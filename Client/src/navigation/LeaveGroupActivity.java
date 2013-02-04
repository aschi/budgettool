package navigation;

import ch.zhaw.database.DatabaseHelper;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

public class LeaveGroupActivity extends Activity {
	
	
    SQLiteOpenHelper database;
    SQLiteDatabase connection;
    
    private int userId;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

    	super.onCreate(savedInstanceState);
		
		database = DatabaseHelper.getInstance(this);
	    connection = database.getWritableDatabase();
	    
        ActionBarCompat.setDisplayHomeAsUpEnabled(this, true);

	    // note we never called setContentView()
	    

    	//TODO Pris: Im Server auch ausloggen
        
        userId = getUserId();
    	String sql2 = "UPDATE users SET groupId = -1 WHERE serverId = " + userId + ";"; 
	    connection.execSQL(sql2);
	    
    	String sql3 = "DELETE FROM expenses;"; 
	    connection.execSQL(sql3);
	   
        
        Intent target = new Intent(this, StartActivity.class);
        startActivity(target);
	}
	
    private int getUserId() {
    	int id = 0;

 	    Cursor user = connection.rawQuery("SELECT * FROM users ORDER BY id LIMIT 1", null);
 	    
 	    if (user.getCount() > 0) {
 	    	user.moveToFirst();
	    	id = user.getInt(1);
 	    } else {
 	    	id = -1;
 	    }
 	    
 	    user.close();
 	    
 	    return id;
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
