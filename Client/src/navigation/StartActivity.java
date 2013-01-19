package navigation;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import ch.zhaw.database.DatabaseHelper;

public class StartActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		boolean hasUser = false;
	    
	    SQLiteOpenHelper database = new DatabaseHelper(this);
	    SQLiteDatabase connection = database.getWritableDatabase();
	    
	    connection.execSQL("insert into users (serverId, username, password) values (1, \"prisi\", \"test\")");
	    Cursor user = connection.rawQuery("SELECT * FROM users ORDER BY id LIMIT 1", null);
	    
	    hasUser = user.getCount() > 0;
	    
	    database.close();
	    user.close();
	    connection.close();
	    
	    super.onCreate(savedInstanceState);
	    Intent intent;
	    //User eingeloggt?
	    if (hasUser) {
	       intent = new Intent(this, AppNavHomeActivity.class);
	    } else {
	       intent = new Intent(this, LoginActivity.class);
	    }
	    startActivity(intent);
	    finish();
	    // note we never called setContentView()
	}
}
