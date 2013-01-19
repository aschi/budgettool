package naviation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class StartActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    Intent intent;
	    //User eingeloggt?
	    if (false) {
	       intent = new Intent(this, AppNavHomeActivity.class);
	    } else {
	       intent = new Intent(this, LoginActivity.class);
	    }
	    startActivity(intent);
	    finish();
	    // note we never called setContentView()
	}
}
