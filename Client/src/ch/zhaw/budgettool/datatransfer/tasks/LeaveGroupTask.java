package ch.zhaw.budgettool.datatransfer.tasks;

import android.os.AsyncTask;
import ch.zhaw.budgettool.datatransfer.Group;

public class LeaveGroupTask extends AsyncTask<String, String, Group> {
	private OnTaskCompleted listener;
	private Group group;

	public LeaveGroupTask(Group group, OnTaskCompleted listener) {
		this.listener = listener;
		this.group = group;
	}

	@Override
	protected Group doInBackground(String... params) {
		group.leaveGroup();
		return group;
	}

	protected void onPostExecute(Group group) {
		listener.onTaskCompleted(LeaveGroupTask.class, group);
	}
}
