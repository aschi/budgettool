package ch.zhaw.budgettool.datatransfer.JSONClasses;

public class GroupData {
	private GroupDataWrapper group;
	private UserDataWrapper[] users;
	private ExpenseDataWrapper[] expenses;
	
	public class GroupDataWrapper{
		private GroupJSON Group;
		private UserJSON User;
		public UserJSON getUser() {
			return User;
		}
		public GroupJSON getGroup() {
			return Group;
		}
	}
	public class UserDataWrapper{
		private GroupJSON Group;
		private UserJSON User;
		public UserJSON getUser() {
			return User;
		}
		public GroupJSON getGroup() {
			return Group;
		}
	}
	public class ExpenseDataWrapper{
		private ExpenseJSON Expense;
		private UserJSON User;
		public UserJSON getUser() {
			return User;
		}
		public ExpenseJSON getExpense() {
			return Expense;
		}
	}
	public GroupDataWrapper getGroup() {
		return group;
	}
	public UserDataWrapper[] getUsers(){
		return users;
	}
	public ExpenseDataWrapper[] getExpenses(){
		return expenses;
	}
}
