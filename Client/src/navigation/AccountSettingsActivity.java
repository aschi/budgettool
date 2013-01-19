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

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import ch.zhaw.budgettool.R;
import ch.zhaw.database.DatabaseHelper;

public class AccountSettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_change_settings);

        ActionBarCompat.setDisplayHomeAsUpEnabled(this, true);
	    SQLiteOpenHelper database = new DatabaseHelper(this);
	    SQLiteDatabase connection = database.getWritableDatabase();
	    
	    Cursor user = connection.rawQuery("SELECT * FROM users ORDER BY id LIMIT 1", null);
	    
	    if (user.getCount() > 0) {
	    	user.moveToFirst();
	    	EditText mText; 
	    	mText = (EditText) findViewById(R.id.username_value);
	    	mText.setText(user.getString(3));
	    	EditText mText2; 
	    	mText2 = (EditText) findViewById(R.id.userpassword_value);
	    	mText2.setText(user.getString(4));
	    }
	    
	    database.close();
	    user.close();
	    connection.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return false;
    }

    public void onLaunchOtherTask(View v) {
        Intent target = new Intent(this, AppNavHomeActivity.class);
        startActivity(target);
    }
}
