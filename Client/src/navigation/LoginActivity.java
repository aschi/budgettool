package navigation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import ch.zhaw.budgettool.R;
import ch.zhaw.budgettool.datatransfer.Group;
import ch.zhaw.budgettool.datatransfer.TransferClass;
import ch.zhaw.budgettool.datatransfer.User;
import ch.zhaw.budgettool.datatransfer.tasks.CreateUserTask;
import ch.zhaw.budgettool.datatransfer.tasks.LoginTask;
import ch.zhaw.budgettool.datatransfer.tasks.OnTaskCompleted;
import ch.zhaw.budgettool.datatransfer.tasks.UpdateEverythingTask;
import ch.zhaw.database.DatabaseHelper;
import ch.zhaw.database.UpdateHelper;

public class LoginActivity extends Activity implements OnTaskCompleted{
	
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
		    	usernameText = (EditText) findViewById(R.id.usernameLoginText);
		    	passwordText = (EditText) findViewById(R.id.passwordLoginText);
		    	username = usernameText.getText().toString();
		    	password = passwordText.getText().toString();
		    	
		    	User user = new User();
		    	new LoginTask(user, LoginActivity.this).execute(username, password);
			}

        });
        
        newAccButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
			    
		    	EditText usernameText, passwordText; 
		    	usernameText = (EditText) findViewById(R.id.usernameNewAccText);
		    	passwordText = (EditText) findViewById(R.id.passwordNewAccText);
		    	username = usernameText.getText().toString();
		    	password = passwordText.getText().toString();
		    	
		    	User user = new User();
		    	new CreateUserTask(user, LoginActivity.this).execute(username, password);
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

	public void onTaskCompleted(Class task, TransferClass obj) {
		if(task == LoginTask.class){
			if(obj != null){
				User u = (User)obj;
				serverId = u.getId();
				
				String groupId = "NULL";
				if(u.getData().getUser().getUser().getGroup_id() != null){
					groupId = u.getData().getUser().getUser().getGroup_id();
				}
				
				
			    String sql = "INSERT INTO users (serverId, groupId, username, password) VALUES(" + serverId + ", "+ groupId +", \"" + username + "\", \"" + password + "\");";
				connection.execSQL(sql);
				
				if(groupId != "NULL"){
					//get serverdata
					UpdateHelper uh = new UpdateHelper(connection);
					Group group = new Group(u);
					group.setId(Integer.parseInt(groupId));
					new UpdateEverythingTask(group, uh, this).execute();
				}
				
				Intent target = new Intent(LoginActivity.this, StartActivity.class);
			    startActivity(target);
			}else{
	    		AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
	    		builder.setMessage(R.string.loginfailed);
	    		AlertDialog dialog = builder.create();
	    		dialog.show();
			}
		}else if(task == CreateUserTask.class){
			if(obj != null){
				User u = (User)obj;
				if(u.getErrorMsg() == null){
					serverId = u.getId();
				    
			    	String sql = "INSERT INTO users (serverId, groupId, username, password) VALUES(" + serverId + ", NULL, \"" + username + "\", \"" + password + "\");";
				    connection.execSQL(sql);
				    
				    Intent target = new Intent(LoginActivity.this, StartActivity.class);
			        startActivity(target);
				}else{
					AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
		    		builder.setMessage(u.getErrorMsg());
		    		AlertDialog dialog = builder.create();
		    		dialog.show();
				}
	    	}else{
	    		AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
	    		builder.setMessage(R.string.usercreationfailed);
	    		AlertDialog dialog = builder.create();
	    		dialog.show();
	    	}
		}
		// TODO Auto-generated method stub
		
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
