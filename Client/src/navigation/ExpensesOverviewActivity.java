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

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import ch.zhaw.budgettool.R;
import ch.zhaw.database.DatabaseHelper;

public class ExpensesOverviewActivity extends Activity {

	SQLiteOpenHelper database;
	SQLiteDatabase connection;

	private String userIdsString;

	private List<String> valueList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		database = DatabaseHelper.getInstance(this);
		connection = database.getWritableDatabase();
		setContentView(R.layout.view_expenses_overview);

		ActionBarCompat.setDisplayHomeAsUpEnabled(this, true);
		userIdsString = getUserIds();

		// titles
		valueList.add("Description, Amount, Date, Username");

		Cursor expense = connection
				.rawQuery("SELECT * FROM expenses WHERE userId IN "
						+ userIdsString + ";", null);
//
//		if (expense != null) {
//			if (expense.moveToFirst()) {
//				do {
//					String username = getUsername(expense.getInt(2));
//					valueList.add(expense.getString(3) + ", "
//							+ expense.getDouble(4) + ", "
//							+ expense.getString(5) + ", " + username);
//				} while (expense.moveToNext());
//			}
//		}



//		ListAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),
//				android.R.layout.simple_expandable_list_item_1, valueList);
//
//		final ListView lv = (ListView) findViewById(R.id.expenseOverviewList);
//
//		lv.setAdapter(adapter);

		TableLayout table = new TableLayout(this);
		// Java. You succeed!
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		table.setLayoutParams(lp);
		table.setStretchAllColumns(true);

		TableLayout.LayoutParams rowLp = new TableLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
		TableRow.LayoutParams cellLp = new TableRow.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
		
		// titles
				TableRow header = new TableRow(this);
				TextView descriptionTitle = new TextView(this);
				TextView amountTitle = new TextView(this);
				TextView dateTitle = new TextView(this);
				TextView usernameTitle = new TextView(this);

				descriptionTitle.setText("Description");
				amountTitle.setText("Amount");
				dateTitle.setText("Date");
				usernameTitle.setText("Username");
				
				header.addView(descriptionTitle);
				header.addView(amountTitle);
				header.addView(dateTitle);
				header.addView(usernameTitle);
				
				table.addView(header, ViewGroup.LayoutParams.FILL_PARENT, 16); //(header, rowLp);
				
		if (expense != null) {
			if (expense.moveToFirst()) {
				do {
					TableRow row = new TableRow(this);
					TextView description = new TextView(this);
					description.setText(expense.getString(3));
					row.addView(description, ViewGroup.LayoutParams.FILL_PARENT, 35);
					
					TextView amount = new TextView(this);
					amount.setText(expense.getDouble(4) + "");
					row.addView(amount, ViewGroup.LayoutParams.FILL_PARENT, 35);
					
					TextView date = new TextView(this);
					date.setText(expense.getString(5));
					row.addView(date, ViewGroup.LayoutParams.FILL_PARENT, 35);
					
					TextView usernameText = new TextView(this);
					String username = getUsername(expense.getInt(2));
					usernameText.setText(username);
					row.addView(usernameText, ViewGroup.LayoutParams.FILL_PARENT, 35);
					
					table.addView(row, ViewGroup.LayoutParams.FILL_PARENT, 35);

				} while (expense.moveToNext());
			}
		}
		setContentView(table);
		
		expense.close();

	}

	private String getUserIds() {

		String userIds = "(";

		Cursor user = connection.rawQuery(
				"SELECT * FROM users ORDER BY id LIMIT 1", null);
		boolean first = true;
		if (user != null) {
			if (user.moveToFirst()) {
				do {
					if (!first) {
						userIds = userIds + ", ";
					}
					userIds = userIds + user.getInt(2);
					first = false;
				} while (user.moveToNext());
			}
		}

		user.close();
		userIds = userIds + ")";

		return userIds;
	}

	private String getUsername(int userId) {

		String username = "";

		Cursor user = connection.rawQuery(
				"SELECT * FROM users WHERE serverId = " + userId, null);

		if (user.getCount() > 0) {
			user.moveToFirst();
			username = user.getString(3);
		}

		user.close();

		return username;
	}

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
}
