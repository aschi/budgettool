package navigation;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import ch.zhaw.budgettool.R;
import ch.zhaw.database.DatabaseHelper;

public class JoinGroupActivity extends Activity {
	
	
    SQLiteOpenHelper database;
    SQLiteDatabase connection;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        
        database = DatabaseHelper.getInstance(this);
	    connection = database.getWritableDatabase();
	    
        setContentView(R.layout.view_join_group);

        ActionBarCompat.setDisplayHomeAsUpEnabled(this, true);
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