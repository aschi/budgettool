package ch.zhaw.budgettool.datatransfer.JSONClasses;

public class UserData {
	private DataWrapper user;
	
	public class DataWrapper{
		private UserJSON User;
		private GroupJSON Group;
		private ExpenseJSON[] Expense;
		public UserJSON getUser() {
			return User;
		}
		public GroupJSON getGroup() {
			return Group;
		}
		public ExpenseJSON[] getExpense() {
			return Expense;
		}
	}

	public DataWrapper getUser() {
		return user;
	}
}
