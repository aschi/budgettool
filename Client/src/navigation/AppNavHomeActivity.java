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

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Home activity for app navigation code samples.
 */
public class AppNavHomeActivity extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setListAdapter(new SampleAdapter(querySampleActivities()));
	}

	@Override
	protected void onListItemClick(ListView lv, View v, int pos, long id) {
		SampleInfo info = (SampleInfo) getListAdapter().getItem(pos);
		startActivity(info.intent);
	}

	protected List<SampleInfo> querySampleActivities() {
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.setPackage(getPackageName());
		intent.addCategory(Intent.CATEGORY_SAMPLE_CODE);

		PackageManager pm = getPackageManager();
		List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);

		ArrayList<SampleInfo> samples = new ArrayList<SampleInfo>();

		final int count = infos.size();
		for (int i = 0; i < count; i++) {
			final ResolveInfo info = infos.get(i);
			

			// Diese beiden Activities nie adden
			if (!info.activityInfo.name.equals("navigation.LoginActivity")
					&& !info.activityInfo.name
							.equals("navigation.StartActivity")) {

				//TODO Pris: test
				boolean groupLeader = true;
				boolean hasGroup = true;
				// Falls Gruppe vorhanden
				if ((hasGroup && 
						(!info.activityInfo.name.equals("navigation.JoinGroupActivity") && 
								!info.activityInfo.name.equals("navigation.CreateGroupActivity") &&
								!info.activityInfo.name.equals("navigation.AppNavHomeActivity") &&
								(!info.activityInfo.name.equals("navigation.GroupSettingsActivity") ||
									groupLeader))) ||
					(!hasGroup && 
						(info.activityInfo.name.equals("navigation.JoinGroupActivity") ||
							info.activityInfo.name.equals("navigation.LogoutActivity") ||
							info.activityInfo.name.equals("navigation.CreateGroupActivity")))) {
					addSample(info, samples, pm);
				} 
			}

		}

		return samples;
	}
	
	private void addSample(ResolveInfo info, ArrayList<SampleInfo> samples, PackageManager pm) {
	final CharSequence labelSeq = info.loadLabel(pm);
	String label = labelSeq != null ? labelSeq.toString()
			: info.activityInfo.name;

	Intent target = new Intent();
	target.setClassName(
			info.activityInfo.applicationInfo.packageName,
			info.activityInfo.name);
	SampleInfo sample = new SampleInfo(label, target);
	samples.add(sample);
	}

	static class SampleInfo {
		String name;
		Intent intent;

		SampleInfo(String name, Intent intent) {
			this.name = name;
			this.intent = intent;
		}
	}

	class SampleAdapter extends BaseAdapter {
		private List<SampleInfo> mItems;

		public SampleAdapter(List<SampleInfo> items) {
			mItems = items;
		}

		public int getCount() {
			return mItems.size();
		}

		public Object getItem(int position) {
			return mItems.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						android.R.layout.simple_list_item_1, parent, false);
				convertView
						.setTag(convertView.findViewById(android.R.id.text1));
			}
			TextView tv = (TextView) convertView.getTag();
			tv.setText(mItems.get(position).name);
			return convertView;
		}

	}
}
