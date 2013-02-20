package navigation;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import ch.zhaw.budgettool.datatransfer.Group;
import ch.zhaw.budgettool.datatransfer.TransferClass;
import ch.zhaw.budgettool.datatransfer.User;
import ch.zhaw.budgettool.datatransfer.tasks.EditUserTask;
import ch.zhaw.budgettool.datatransfer.tasks.LeaveGroupTask;
import ch.zhaw.budgettool.datatransfer.tasks.LoginTask;
import ch.zhaw.budgettool.datatransfer.tasks.OnTaskCompleted;
import ch.zhaw.database.DatabaseHelper;
import ch.zhaw.database.UserManagementHelper;

public class LeaveGroupActivity extends Activity implements OnTaskCompleted {
	
	
    SQLiteOpenHelper database;
    SQLiteDatabase connection;
    
    private int userId;
    
    private User user;
    private UserManagementHelper umh;
    
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

    	super.onCreate(savedInstanceState);
		
		database = DatabaseHelper.getInstance(this);
	    connection = database.getWritableDatabase();
	    
	    umh = new UserManagementHelper(connection);
	    user = umh.getUserFromDb(this);
	    
	    ActionBarCompat.setDisplayHomeAsUpEnabled(this, true);

	    // note we never called setContentView()
	    
        new LoginTask(user, this).execute(user.getUsername(), user.getPassword());
        
        userId = getUserId();
    	String sql2 = "UPDATE users SET groupId = NULL WHERE serverId = " + userId + ";"; 
	    connection.execSQL(sql2);
	    
    	String sql3 = "DELETE FROM expenses;"; 
	    connection.execSQL(sql3);
	   
        
        Intent target = new Intent(this, StartActivity.class);
        startActivity(target);
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

	public void onTaskCompleted(Class task, TransferClass obj) {
		// TODO Auto-generated method stub
		if(task == LoginTask.class){
			if(obj == null){
				//username / password invalid -> logout
				umh.logoutFromDB(this);
			}else{
				//create expense
				user = (User)obj;
				Group group = new Group(user);
				group.setId(Integer.parseInt(user.getData().getUser().getGroup().getId()));
				new LeaveGroupTask(group, this).execute();
			}
		}
	}
}
