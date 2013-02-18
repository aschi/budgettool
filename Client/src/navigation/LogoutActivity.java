package navigation;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import ch.zhaw.database.DatabaseHelper;
import ch.zhaw.database.UserManagementHelper;

public class LogoutActivity extends Activity {
	
    SQLiteOpenHelper database;
    SQLiteDatabase connection;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	database = DatabaseHelper.getInstance(this);
	    connection = database.getWritableDatabase();
	    
	    UserManagementHelper umh = new UserManagementHelper(connection);
	    
	    //TODO Pris: Alles auf Server laden 
	    
	    
	    //logout
	    umh.logoutFromDB(this);
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
