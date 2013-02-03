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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import ch.zhaw.budgettool.R;
import ch.zhaw.database.DatabaseHelper;

public class ExpensesOverviewActivity extends Activity {
	
    SQLiteOpenHelper database;
    SQLiteDatabase connection;
	
    private static final String EXTRA_PEER_COUNT =
            "com.example.android.appnavigation.EXTRA_PEER_COUNT";

    private int mPeerCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        database = DatabaseHelper.getInstance(this);
	    connection = database.getWritableDatabase();
        setContentView(R.layout.view_expenses_overview);

        ActionBarCompat.setDisplayHomeAsUpEnabled(this, true);

        mPeerCount = getIntent().getIntExtra(EXTRA_PEER_COUNT, 0) + 1;
//        TextView tv = (TextView) findViewById(R.id.titleRow);
//        tv.setText(getResources().getText(R.string.peer_count).toString() + mPeerCount);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onLaunchPeer(View v) {
        Intent target = new Intent(this, ExpensesOverviewActivity.class);
        target.putExtra(EXTRA_PEER_COUNT, mPeerCount);
        startActivity(target);
    }
    
    @Override
    protected void onDestroy() {
    	
//    	if (database != null) {
//    		database.close();
//    	}
//    	if (connection != null) {
//    		connection.close();
//    	}
	    super.onDestroy();
    }
}
