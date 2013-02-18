/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package navigation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import ch.zhaw.budgettool.R;
import ch.zhaw.budgettool.datatransfer.Expense;
import ch.zhaw.budgettool.datatransfer.TransferClass;
import ch.zhaw.budgettool.datatransfer.User;
import ch.zhaw.budgettool.datatransfer.tasks.CreateExpenseTask;
import ch.zhaw.budgettool.datatransfer.tasks.LoginTask;
import ch.zhaw.budgettool.datatransfer.tasks.OnTaskCompleted;
import ch.zhaw.budgettool.datatransfer.tasks.UpdateEverythingTask;
import ch.zhaw.database.DatabaseHelper;
import ch.zhaw.database.UserManagementHelper;

public class CreateExpenseActivity extends Activity implements OnTaskCompleted{

	private SQLiteOpenHelper database;
	private SQLiteDatabase connection;

	private String description;
	private double amount;
	private String date;
	private int userId;
	
	private Button datePickerButton;

	private UserManagementHelper umh;
	private User user;
	private Expense expense;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		database = DatabaseHelper.getInstance(this);
		connection = database.getWritableDatabase();
		umh = new UserManagementHelper(connection);
		user = umh.getUserFromDb(this);
		
		setContentView(R.layout.view_create_expense);
		Calendar cal = new GregorianCalendar();  

		ActionBarCompat.setDisplayHomeAsUpEnabled(this, true);

		datePickerButton = (Button) findViewById(R.id.dateButton);
		datePickerButton.setText(cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR));

		final OnDateSetListener odsl = new OnDateSetListener() {

			public void onDateSet(DatePicker arg0, int year, int month,
					int dayOfMonth) {
				CreateExpenseActivity.this.datePickerButton.setText(dayOfMonth + "/" + (month + 1) + "/"
						+ year);
			}

		};

		datePickerButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Calendar cal = Calendar.getInstance();
				DatePickerDialog datePickDiag = new DatePickerDialog(
						CreateExpenseActivity.this, odsl, cal.get(Calendar.YEAR),
						cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
				datePickDiag.show();
			}

		}

		);

		Button createButton = (Button) findViewById(R.id.createNewExpenseButton);

		createButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				EditText descriptionText, amountText;
				descriptionText = (EditText) findViewById(R.id.createExpenseDesctiptionText);
				amountText = (EditText) findViewById(R.id.createExpenseExpenseAmountText);
				
				description = descriptionText.getText().toString();
				amount = Double.parseDouble(amountText.getText().toString());
				String dateText = datePickerButton.getText().toString();
				int day = Integer.parseInt(dateText.substring(0,dateText.indexOf("/")));
				int month = Integer.parseInt(dateText.substring(dateText.indexOf("/") + 1,dateText.indexOf("/", dateText.indexOf("/") + 1)));
				int year = Integer.parseInt(dateText.substring(dateText.indexOf("/", dateText.indexOf("/", dateText.indexOf("/") + 1)) + 1,dateText.indexOf("/", dateText.indexOf("/", dateText.indexOf("/") + 1)) + 5));
				Calendar cal = new GregorianCalendar(year, month -1 , day);
				SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		        date = df.format(cal.getTime());

		        //check login
				new LoginTask(user, CreateExpenseActivity.this).execute(user.getUsername(), user.getPassword());
			}

		});

		ActionBarCompat.setDisplayHomeAsUpEnabled(this, true);
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {

		// if (database != null) {
		// database.close();
		// }
		// if (connection != null) {
		// connection.close();
		// }
		super.onDestroy();
	}

	public void onTaskCompleted(Class task, TransferClass obj) {
		if(task == LoginTask.class){
			if(obj == null){
				//username / password invalid -> logout
				umh.logoutFromDB(this);
			}else{
				//update everything
				user = (User)obj;
				expense = new Expense(user);
				new CreateExpenseTask(expense, this).execute(description, Double.toString(amount), date);
			}
		}else if(task == UpdateEverythingTask.class){
			if(obj != null){
				expense = (Expense)obj;
				
				String sql = "INSERT INTO expenses (serverId, userId, description, value, date) VALUES("
						+ expense.getId()
						+ ", \""
						+ expense.getUserId()
						+ ", \""
						+ expense.getDescription()
						+ "\", "
						+ expense.getValue()
						+ ", \"" + expense.getDate() + "\");";
				connection.execSQL(sql);
				
				Intent target = new Intent(CreateExpenseActivity.this,
						AppNavHomeActivity.class);
				startActivity(target);
			}else{
	    		AlertDialog.Builder builder = new AlertDialog.Builder(CreateExpenseActivity.this);
	    		builder.setMessage(R.string.expensecreationfailed);
	    		AlertDialog dialog = builder.create();
	    		dialog.show();
			}
		}
	}
}
