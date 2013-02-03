package navigation;

import ch.zhaw.database.DatabaseHelper;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

public class LeaveGroupActivity extends Activity {
	
	
    SQLiteOpenHelper database;
    SQLiteDatabase connection;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		database = DatabaseHelper.getInstance(this);
	    connection = database.getWritableDatabase();

	    // note we never called setContentView()
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
