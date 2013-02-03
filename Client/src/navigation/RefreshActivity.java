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
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;
import android.view.View;
import ch.zhaw.budgettool.R;
import ch.zhaw.database.DatabaseHelper;

public class RefreshActivity extends Activity {
	
    SQLiteOpenHelper database;
    SQLiteDatabase connection;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
//	    database = new DatabaseHelper(this);
    	database = DatabaseHelper.getInstance(this);
	    connection = database.getWritableDatabase();
	    
        
//        setContentView(R.layout.notifications);

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

    public void onPostDirect(View v) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setTicker("Direct Notification")
                .setSmallIcon(android.R.drawable.stat_notify_chat)
                .setContentTitle("Direct Notification")
                .setContentText("This will open the content viewer")
                .setAutoCancel(true)
                .setContentIntent(TaskStackBuilder.from(this)
                        .addParentStack(GroupSettingsActivity.class)
                        .addNextIntent(new Intent(this, GroupSettingsActivity.class)
                                .putExtra(GroupSettingsActivity.NFC_SERVICE, "From Notification"))
                        .getPendingIntent(0, 0));
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify("direct_tag", R.id.direct_notification, builder.getNotification());
    }

    public void onPostInterstitial(View v) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setTicker("Interstitial Notification")
                .setSmallIcon(android.R.drawable.stat_notify_chat)
                .setContentTitle("Interstitial Notification")
                .setContentText("This will show a detail page")
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(this, 0,
                        new Intent(this, GroupSettingsActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK), 0));
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify("interstitial_tag", R.id.interstitial_notification, builder.getNotification());
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
