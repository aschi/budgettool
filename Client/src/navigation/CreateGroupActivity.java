package navigation;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import ch.zhaw.budgettool.R;
import ch.zhaw.database.DatabaseHelper;

public class CreateGroupActivity extends Activity {
	
	private String groupName;
	private String password;
	private double budget;
	private int userId;
	private int serverId;
	
    SQLiteOpenHelper database;
    SQLiteDatabase connection;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        database = DatabaseHelper.getInstance(this);
	    connection = database.getWritableDatabase();
	    
        setContentView(R.layout.view_create_group);

        ActionBarCompat.setDisplayHomeAsUpEnabled(this, true);
        
        Button createButton = (Button) findViewById(R.id.newGroupButton);
        
        createButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

		    	userId = getUserId();
				
		    	EditText groupNameText, passwordText, budgetText; 
		    	groupNameText = (EditText) findViewById(R.id.createGroupnameText);
		    	passwordText = (EditText) findViewById(R.id.createGroupPasswordText);
		    	budgetText = (EditText) findViewById(R.id.createGroupBudgetText);
		    	
		    	groupName = groupNameText.getText().toString();
		    	password = passwordText.getText().toString();
		    	budget = Double.parseDouble(budgetText.getText().toString());
		    	
		    	//Prüfen ob Gruppenname bereits vorhanden
		    	//TODO Pris: Netz 
		    	
		    	//Server-ID holen
		    	serverId = 999;
			    
		    	String sql = "INSERT INTO groups (serverId, userId, groupname, password, budget) VALUES(" + serverId + ", " + userId + ", \"" + groupName + "\", \"" + password + "\", " + budget + ");";
			    connection.execSQL(sql);
			    
			    String sql2 = "UPDATE users SET groupId = " + serverId + " WHERE serverId = " + userId + ";";
			    connection.execSQL(sql2);
			    
		        Intent target = new Intent(CreateGroupActivity.this, AppNavHomeActivity.class);
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
