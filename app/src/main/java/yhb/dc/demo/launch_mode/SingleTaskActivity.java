package yhb.dc.demo.launch_mode;

import android.content.Context;
import android.content.Intent;

public class SingleTaskActivity extends LaunchModeBaseActivity{
    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SingleTaskActivity.class);
        intent.putExtra("LAUNCH_MODE", "SingleTask");
        return intent;
    }
}
