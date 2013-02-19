package ch.zhaw.budgettool.datatransfer.tasks;

import android.os.AsyncTask;
import ch.zhaw.budgettool.datatransfer.Group;

public class JoinGroupTask extends AsyncTask<String, String, Group> {
	private OnTaskCompleted listener;
	private Group group;

	public JoinGroupTask(Group group, OnTaskCompleted listener) {
		this.listener = listener;
		this.group = group;
	}

	@Override
	protected Group doInBackground(String... params) {
		//if so create it
		if (group.joinGroup(params[0], params[1]) != null) {
			return group;
		} else {
			return null;
		}
	}

	protected void onPostExecute(Group group) {
		listener.onTaskCompleted(JoinGroupTask.class, group);
	}
}
