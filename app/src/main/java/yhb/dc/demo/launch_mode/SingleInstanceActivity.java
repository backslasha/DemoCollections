package yhb.dc.demo.launch_mode;

import android.content.Context;
import android.content.Intent;

public class SingleInstanceActivity extends LaunchModeBaseActivity{
    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SingleInstanceActivity.class);
        intent.putExtra("LAUNCH_MODE", "SingleInstance");
        return intent;
    }
}
