package ch.zhaw.budgettool.datatransfer.JSONClasses;


public class ExpenseData {
	private DataWrapper expense;
	
	public class DataWrapper{
		private UserJSON User;
		private ExpenseJSON Expense;
		public UserJSON getUser() {
			return User;
		}
		public ExpenseJSON getExpense() {
			return Expense;
		}
	}

	public DataWrapper getExpense() {
		return expense;
	}
}
