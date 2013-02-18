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
import ch.zhaw.budgettool.datatransfer.tasks.CreateGroupTask;
import ch.zhaw.budgettool.datatransfer.tasks.LoginTask;
import ch.zhaw.budgettool.datatransfer.tasks.OnTaskCompleted;
import ch.zhaw.database.DatabaseHelper;
import ch.zhaw.database.UserManagementHelper;

public class CreateGroupActivity extends Activity implements OnTaskCompleted{
	
	private String groupName;
	private String password;
	private double budget;
	private int userId;
	private int serverId;
	
	private UserManagementHelper umh;
	private User user;
	
    SQLiteOpenHelper database;
    SQLiteDatabase connection;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        database = DatabaseHelper.getInstance(this);
	    connection = database.getWritableDatabase();
	    umh = new UserManagementHelper(connection);
	    
        setContentView(R.layout.view_create_group);

        ActionBarCompat.setDisplayHomeAsUpEnabled(this, true);
        
        Button createButton = (Button) findViewById(R.id.newGroupButton);
        
        createButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				user = umh.getUserFromDb(CreateGroupActivity.this);
				
		    	EditText groupNameText, passwordText, budgetText; 
		    	groupNameText = (EditText) findViewById(R.id.createGroupnameText);
		    	passwordText = (EditText) findViewById(R.id.createGroupPasswordText);
		    	budgetText = (EditText) findViewById(R.id.createGroupBudgetText);
		    	
		    	groupName = groupNameText.getText().toString();
		    	password = passwordText.getText().toString();
		    	budget = Double.parseDouble(budgetText.getText().toString());
		    	
		    	new LoginTask(user, CreateGroupActivity.this).execute(user.getUsername(), user.getPassword());
			}

        });
    }
    
    private void createGroup(User user, String groupName, String password, double budget){
    	Group group = new Group(user);
    	new CreateGroupTask(group, this).execute(groupName, password, Double.toString(budget));
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

	public void onTaskCompleted(Class task, TransferClass obj) {
		if(task == LoginTask.class){
			if(obj == null){
				//username / password invalid -> logout
				umh.logoutFromDB(this);
			}else{
				this.user = (User)obj;
				createGroup(user, groupName, password, budget);
			}
		}else if(task == CreateGroupTask.class){
			if(obj != null){
				Group g = (Group)obj;
				if(g.getErrorMsg() == null){
					//Server-ID holen
			    	serverId = g.getId();
			    	userId = user.getId();
				    
			    	String sql = "INSERT INTO groups (serverId, userId, groupname, password, budget) VALUES(" + serverId + ", " + userId + ", \"" + groupName + "\", \"" + password + "\", " + budget + ");";
				    connection.execSQL(sql);
				    
				    String sql2 = "UPDATE users SET groupId = " + serverId + " WHERE serverId = " + userId + ";";
				    connection.execSQL(sql2);
				    
			        Intent target = new Intent(CreateGroupActivity.this, AppNavHomeActivity.class);
			        startActivity(target);
				}else{
					AlertDialog.Builder builder = new AlertDialog.Builder(CreateGroupActivity.this);
		    		builder.setMessage(g.getErrorMsg());
		    		AlertDialog dialog = builder.create();
		    		dialog.show();
				}
	    	}else{
	    		AlertDialog.Builder builder = new AlertDialog.Builder(CreateGroupActivity.this);
	    		builder.setMessage(R.string.groupcreationfailed);
	    		AlertDialog dialog = builder.create();
	    		dialog.show();
	    	}
		}
		
	}
}
