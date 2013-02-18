package ch.zhaw.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ch.zhaw.budgettool.datatransfer.JSONClasses.ExpenseJSON;
import ch.zhaw.budgettool.datatransfer.JSONClasses.GroupJSON;
import ch.zhaw.budgettool.datatransfer.JSONClasses.UserJSON;

public class UpdateHelper {
	private SQLiteDatabase connection;
	
	public UpdateHelper(SQLiteDatabase connection){
		this.connection = connection;
	}
	
	public void updateEverything(UserJSON[] users, GroupJSON group, ExpenseJSON[] expenses){
		updateUsers(users);
		updateGroup(group);
		updateExpenses(expenses);
	}
	
	public void updateUsers(UserJSON[] users){
		if(users != null){
			//remove client data
			String sql = "DELETE FROM users WHERE id NOT IN (SELECT id FROM users ORDER BY id LIMIT 1);";
		    connection.execSQL(sql);
		    
		    //insert
		    for(UserJSON user : users){
		    	if(user != null){
			    	Cursor c = connection.rawQuery("SELECT id FROM users WHERE serverId = '"+ user.getId() +"' LIMIT 1", null);
			 	    if (c.getCount() == 0) {
			 	    	sql = "INSERT INTO users (serverId, groupId, username, password) VALUES(" + user.getId() + ", "+ user.getGroup_id() +", \"" + user.getUsername() + "\", \"" + user.getPassword() + "\");";
					    connection.execSQL(sql);
			 	    }
		    	}
		    }
	    }
	}
	
	public void updateGroup(GroupJSON group){
		if(group != null){
			Cursor c = connection.rawQuery("SELECT id FROM groups ORDER BY serverId LIMIT 1", null);
	 	    if (c.getCount() > 0) {
	 	    	c.moveToFirst();
	 	    	int id = c.getInt(c.getColumnIndex("id"));
	 	    	String sql = "UPDATE groups SET groupName=\""+group.getGroup_name()+"\", password=\""+group.getPassword()+"\", budget="+group.getBudget()+" WHERE id = "+id;
	 		    connection.execSQL(sql);
	 	    }else{
	 	    	//create new group
	 	    	String sql = "DELETE FROM groups";
	 	    	connection.execSQL(sql);
	 	    	sql = "INSERT INTO groups (serverId, userId, groupname, password, budget) VALUES(" + group.getId() + ", " + group.getUser_id() + ", \"" + group.getGroup_name()+ "\", \"" + group.getPassword() + "\", " + group.getBudget() + ");";
	 	    	connection.execSQL(sql);
	 	    }
		}
	}
	
	public  void updateExpenses(ExpenseJSON[] expenses){
		//remove client data
		if(expenses != null){
			String sql = "DELETE FROM expenses;"; 
		    connection.execSQL(sql);
		    
		    //insert given expenes
		    for(ExpenseJSON expense : expenses){
		    	if(expense != null){ 
			    	sql = "INSERT INTO expenses (userId, description, value, date) VALUES("
			 				+ expense.getUser_id()
			 				+ ", \""
			 				+ expense.getDescription()
			 				+ "\", "
			 				+ expense.getValue()
			 				+ ", \"" + expense.getDate() + "\");";
			 		connection.execSQL(sql);
		    	}
		    }
		}
	}
}
