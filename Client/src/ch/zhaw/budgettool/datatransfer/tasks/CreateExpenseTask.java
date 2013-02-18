package ch.zhaw.budgettool.datatransfer.tasks;

import android.os.AsyncTask;
import ch.zhaw.budgettool.datatransfer.Expense;

public class CreateExpenseTask extends AsyncTask<String, String, Expense> {
	private OnTaskCompleted listener;
	private Expense expense;

	public CreateExpenseTask(Expense expense, OnTaskCompleted listener) {
		this.listener = listener;
		this.expense = expense;
	}

	@Override
	protected Expense doInBackground(String... params) {
		if (expense.add(params[0], Double.parseDouble(params[1]), params[2]) != null) {
			return expense;
		} else {
			return null;
		}
	}

	protected void onPostExecute(Expense expense) {
		listener.onTaskCompleted(CreateExpenseTask.class, expense);
	}

}
