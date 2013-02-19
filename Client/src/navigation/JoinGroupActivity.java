package navigation;

import android.app.Activity;
import android.app.AlertDialog;
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
import ch.zhaw.budgettool.datatransfer.Group;
import ch.zhaw.budgettool.datatransfer.TransferClass;
import ch.zhaw.budgettool.datatransfer.User;
import ch.zhaw.budgettool.datatransfer.tasks.EditGroupTask;
import ch.zhaw.budgettool.datatransfer.tasks.JoinGroupTask;
import ch.zhaw.budgettool.datatransfer.tasks.LoginTask;
import ch.zhaw.budgettool.datatransfer.tasks.OnTaskCompleted;
import ch.zhaw.budgettool.datatransfer.tasks.UpdateEverythingTask;
import ch.zhaw.database.DatabaseHelper;
import ch.zhaw.database.UpdateHelper;
import ch.zhaw.database.UserManagementHelper;

public class JoinGroupActivity extends Activity implements OnTaskCompleted{
	
	
    SQLiteOpenHelper database;
    SQLiteDatabase connection;
    
    private int userId;
    private int groupServerId;
    
    private String groupname;
    private String password;
    
    private UserManagementHelper umh;
    private UpdateHelper uh;
    private User user;
    private Group group;
    
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        
        database = DatabaseHelper.getInstance(this);
	    connection = database.getWritableDatabase();
	    
	    umh = new UserManagementHelper(connection);
	    user = umh.getUserFromDb(this);
	    uh = new UpdateHelper(connection);
	    
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
				
		    	new LoginTask(user, JoinGroupActivity.this).execute(user.getUsername(), user.getPassword());
		   }
        });
        
       
    }

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

	public void onTaskCompleted(Class task, TransferClass obj) {
		if(task == LoginTask.class){
			if(obj == null){
				//username / password invalid -> logout
				umh.logoutFromDB(this);
			}else{
				this.user = (User)obj;
				group = new Group(user);
				new JoinGroupTask(group, this).execute(groupname, password);
			}
		}else if(task == JoinGroupTask.class){
			if(obj != null){
				Group g = (Group)obj;
				if(g.getErrorMsg() == null){
					groupServerId = g.getId();
			        String sql2 = "UPDATE users SET groupId = " + groupServerId + " WHERE serverId = " + user.getId() + ";"; 
				    connection.execSQL(sql2);
					
					//alles aktualisieren
					new UpdateEverythingTask(g, uh, this).execute();
			    	
			        Intent target = new Intent(JoinGroupActivity.this, AppNavHomeActivity.class);
			        startActivity(target);
				}else{
					AlertDialog.Builder builder = new AlertDialog.Builder(JoinGroupActivity.this);
		    		builder.setMessage(g.getErrorMsg());
		    		AlertDialog dialog = builder.create();
		    		dialog.show();
				}
	    	}else{
	    		AlertDialog.Builder builder = new AlertDialog.Builder(JoinGroupActivity.this);
	    		builder.setMessage(R.string.groupjoiningfailed);
	    		AlertDialog dialog = builder.create();
	    		dialog.show();
	    	}
		}
	}
}