package navigation;

import android.app.Activity;
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
import ch.zhaw.database.DatabaseHelper;

public class JoinGroupActivity extends Activity {
	
	
    SQLiteOpenHelper database;
    SQLiteDatabase connection;
    
    private int userId;
    private int groupServerId;
    
    private String groupname;
    private String password;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        
        database = DatabaseHelper.getInstance(this);
	    connection = database.getWritableDatabase();
	    
        setContentView(R.layout.view_join_group);

        ActionBarCompat.setDisplayHomeAsUpEnabled(this, true);
        
        Button joinGroupButton = (Button) findViewById(R.id.joinGroupButton);
        
        joinGroupButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				
				EditText groupNameText, passwordgroupText; 
		    	groupNameText = (EditText) findViewById(R.id.joinGroupnameText);
		    	passwordgroupText = (EditText) findViewById(R.id.joinGroupPasswordText);
		    	groupname = groupNameText.getText().toString();
		    	password = passwordgroupText.getText().toString();
				
		        //todo Pris: Gruppe vom Server holen
		        groupServerId = 999;
		        
		        
		        userId = getUserId();
		    	String sql2 = "UPDATE users SET groupId = " + groupServerId + " WHERE serverId = " + userId + ";"; 
			    connection.execSQL(sql2);
			    
		        Intent target = new Intent(JoinGroupActivity.this, AppNavHomeActivity.class);
		        startActivity(target);
			}
			

        	
        });
        
       
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