package yhb.dc.demo.launch_mode;

import android.content.Context;
import android.content.Intent;

public class SingleTopActivity extends LaunchModeBaseActivity{
    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SingleTopActivity.class);
        intent.putExtra("LAUNCH_MODE", "SingleTop");
        return intent;
    }
}
