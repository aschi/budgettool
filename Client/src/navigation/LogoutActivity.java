package navigation;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import ch.zhaw.database.DatabaseHelper;

public class LogoutActivity extends Activity {
	
    SQLiteOpenHelper database;
    SQLiteDatabase connection;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	database = DatabaseHelper.getInstance(this);
	    connection = database.getWritableDatabase();
	    
        
//        setContentView(R.layout.view_join_group);


    	//TODO Pris: Alles auf Server laden 
	    
    	String sql = "DELETE FROM users;"; 
	    connection.execSQL(sql);
	    
    	String sql2 = "DELETE FROM groups;"; 
	    connection.execSQL(sql2);
	    
    	String sql3 = "DELETE FROM expenses;"; 
	    connection.execSQL(sql3);
	    
        ActionBarCompat.setDisplayHomeAsUpEnabled(this, true);
        
        Intent target = new Intent(this, StartActivity.class);
        startActivity(target);
    }

    @Override
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
