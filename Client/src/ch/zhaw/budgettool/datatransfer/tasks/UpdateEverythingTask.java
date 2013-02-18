package ch.zhaw.budgettool.datatransfer.tasks;

import android.os.AsyncTask;
import ch.zhaw.budgettool.datatransfer.Group;
import ch.zhaw.budgettool.datatransfer.JSONClasses.ExpenseJSON;
import ch.zhaw.budgettool.datatransfer.JSONClasses.GroupData;
import ch.zhaw.budgettool.datatransfer.JSONClasses.GroupData.ExpenseDataWrapper;
import ch.zhaw.budgettool.datatransfer.JSONClasses.GroupData.UserDataWrapper;
import ch.zhaw.budgettool.datatransfer.JSONClasses.UserJSON;
import ch.zhaw.database.UpdateHelper;

public class UpdateEverythingTask extends AsyncTask<String, String, Group> {
	private OnTaskCompleted listener;
	private Group group;
	private UpdateHelper uh;

	public UpdateEverythingTask(Group group, UpdateHelper uh, OnTaskCompleted listener) {
		this.listener = listener;
		this.uh = uh;
		this.group = group;
	}

	@Override
	protected Group doInBackground(String... params) {
		GroupData data = group.view();
		
		//users
		int i = 0;
		UserJSON[] users = new UserJSON[data.getUsers().length];
		for(UserDataWrapper udw : data.getUsers()){
			users[i] = udw.getUser();
			i++;
		}
		
		//expenses
		ExpenseJSON[] expenses = new ExpenseJSON[data.getExpenses().length];
		i = 0;
		for(ExpenseDataWrapper edw : data.getExpenses()){
			expenses[i] = edw.getExpense();
			i++;
		}
		
		//update local data
		uh.updateEverything(users, data.getGroup().getGroup(), expenses);
		
		return group;
	}

	protected void onPostExecute(Group group) {
		listener.onTaskCompleted(UpdateEverythingTask.class, group);
	}

}