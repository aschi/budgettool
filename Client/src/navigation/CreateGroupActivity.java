package navigation;

import ch.zhaw.budgettool.R;
import android.app.Activity;
import android.os.Bundle;

public class CreateGroupActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_create_group);

        ActionBarCompat.setDisplayHomeAsUpEnabled(this, true);
    }
}
