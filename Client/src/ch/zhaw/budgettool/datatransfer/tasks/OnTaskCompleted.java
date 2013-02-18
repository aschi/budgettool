package ch.zhaw.budgettool.datatransfer.tasks;

import ch.zhaw.budgettool.datatransfer.TransferClass;

public interface OnTaskCompleted {
	public void onTaskCompleted(Class task, TransferClass obj);
}
