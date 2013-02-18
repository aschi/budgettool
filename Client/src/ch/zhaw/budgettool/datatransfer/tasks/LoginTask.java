package ch.zhaw.budgettool.datatransfer.tasks;

import android.os.AsyncTask;
import ch.zhaw.budgettool.datatransfer.User;

public class LoginTask extends AsyncTask<String, String, User> {
	private OnTaskCompleted listener;
	private User user;

	public LoginTask(User user, OnTaskCompleted listener) {
		this.listener = listener;
		this.user = user;
	}

	@Override
	protected User doInBackground(String... params) {
		if (user.login(params[0], params[1])) {
			return user;
		} else {
			return null;
		}
	}

	protected void onPostExecute(User user) {
		listener.onTaskCompleted(LoginTask.class, user);
	}

}
