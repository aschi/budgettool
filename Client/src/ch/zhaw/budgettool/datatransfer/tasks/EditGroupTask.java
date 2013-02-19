package ch.zhaw.budgettool.datatransfer.tasks;

import android.os.AsyncTask;
import ch.zhaw.budgettool.datatransfer.Group;

public class EditGroupTask extends AsyncTask<String, String, Group> {
	private OnTaskCompleted listener;
	private Group group;

	public EditGroupTask(Group group, OnTaskCompleted listener) {
		this.listener = listener;
		this.group = group;
	}

	@Override
	protected Group doInBackground(String... params) {
		//check if groupName is available
		if(!group.getGroupname().equals(params[0]) && !group.groupNameAvailable(params[0])){
			group.setErrorMsg("Groupname not available");
			return group;
		}
		//if so create it
		group.edit(params[0], params[1], Double.parseDouble(params[2]));
		return group;
	}
	
	protected void onPostExecute(Group group) {
		listener.onTaskCompleted(EditGroupTask.class, group);
	}

}
