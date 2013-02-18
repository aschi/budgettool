package ch.zhaw.budgettool.datatransfer.tasks;

import android.os.AsyncTask;
import ch.zhaw.budgettool.datatransfer.Group;
import ch.zhaw.budgettool.datatransfer.User;

public class CreateGroupTask extends AsyncTask<String, String, Group> {
	private OnTaskCompleted listener;
	private Group group;

	public CreateGroupTask(Group group, OnTaskCompleted listener) {
		this.listener = listener;
		this.group = group;
	}

	@Override
	protected Group doInBackground(String... params) {
		//check if groupName is available
		if(!group.groupNameAvailable(params[0])){
			group.setErrorMsg("Groupname not available");
			return group;
		}
		
		//if so create it
		if (group.add(params[0], params[1], Double.parseDouble(params[2])) != null) {
			return group;
		} else {
			return null;
		}
	}

	protected void onPostExecute(Group group) {
		listener.onTaskCompleted(CreateGroupTask.class, group);
	}

}
