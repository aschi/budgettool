package ch.zhaw.budgettool.datatransfer.tasks;

import android.os.AsyncTask;
import ch.zhaw.budgettool.datatransfer.User;

public class CreateUserTask extends AsyncTask<String, String, User> {
	private OnTaskCompleted listener;
	private User user;

	public CreateUserTask(User user, OnTaskCompleted listener) {
		this.listener = listener;
		this.user = user;
	}

	@Override
	protected User doInBackground(String... params) {
		if(!user.usernameAvailable(params[0])){
			user.setErrorMsg("Username not available");
			return user;
		}
		
		if (user.add(params[0], params[1]) != null) {
			return user;
		} else {
			return null;
		}
	}

	protected void onPostExecute(User user) {
		listener.onTaskCompleted(CreateUserTask.class, user);
	}

}
