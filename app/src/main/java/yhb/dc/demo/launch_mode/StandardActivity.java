package yhb.dc.demo.launch_mode;

import android.content.Context;
import android.content.Intent;

public class StandardActivity extends LaunchModeBaseActivity{
    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, StandardActivity.class);
        intent.putExtra("LAUNCH_MODE", "Standard");
        return intent;
    }
}
