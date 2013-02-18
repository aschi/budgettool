package ch.zhaw.budgettool.datatransfer.JSONClasses;

public class Available {
	private String available;
	
	public boolean getAvailable(){
		return available == null ? false : Boolean.parseBoolean(available);
	}
}
