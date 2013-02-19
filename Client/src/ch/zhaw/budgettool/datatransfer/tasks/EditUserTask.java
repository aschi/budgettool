package ch.zhaw.budgettool.datatransfer.tasks;

import android.os.AsyncTask;
import ch.zhaw.budgettool.datatransfer.User;

public class EditUserTask extends AsyncTask<String, String, User> {
	private OnTaskCompleted listener;
	private User user;

	public EditUserTask(User user, OnTaskCompleted listener) {
		this.listener = listener;
		this.user = user;
	}

	@Override
	protected User doInBackground(String... params) {
		
		if(!user.getUsername().equals(params[0]) && !user.usernameAvailable(params[0])){
			user.setErrorMsg("Username not available");
			return user;
		}
		
		user.edit(params[0], params[1]);
		return user;
	}

	protected void onPostExecute(User user) {
		listener.onTaskCompleted(EditUserTask.class, user);
	}

}
