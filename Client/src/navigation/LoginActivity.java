package navigation;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import ch.zhaw.budgettool.R;
import ch.zhaw.database.DatabaseHelper;

public class LoginActivity extends Activity {
	
	private String username;
	private String password;
	private int serverId;
	
    SQLiteOpenHelper database;
    SQLiteDatabase connection;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	database = DatabaseHelper.getInstance(this);
	    connection = database.getWritableDatabase();
	    
        
        setContentView(R.layout.view_login);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        Button newAccButton = (Button) findViewById(R.id.newAccButton);
        
        loginButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				
		    	EditText usernameText, passwordText; 
		    	usernameText = (EditText) findViewById(R.id.usernameNewAccText);
		    	passwordText = (EditText) findViewById(R.id.passwordNewAccText);
		    	
		    	//TODO Pris: Netz!!!
			    
		        Intent target = new Intent(LoginActivity.this, StartActivity.class);
		        startActivity(target);
			}

        });
        
        newAccButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
			    
		    	EditText usernameText, passwordText; 
		    	usernameText = (EditText) findViewById(R.id.usernameNewAccText);
		    	passwordText = (EditText) findViewById(R.id.passwordNewAccText);
		    	username = usernameText.getText().toString();
		    	password = passwordText.getText().toString();
		    	
		    	//Prüfen ob Username bereits vorhanden
		    	//TODO Pris: Netz 
		    	
		    	//Server-ID holen
		    	serverId = 999;
			    
		    	String sql = "INSERT INTO users (serverId, groupId, username, password) VALUES(" + serverId + ", NULL, \"" + username + "\", \"" + password + "\");";
			    connection.execSQL(sql);
			    
		        Intent target = new Intent(LoginActivity.this, StartActivity.class);
		        startActivity(target);
			}

        });

        ActionBarCompat.setDisplayHomeAsUpEnabled(this, true);
    }

//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            NavUtils.navigateUpFromSameTask(this);
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
    
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
