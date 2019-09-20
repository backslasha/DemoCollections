package yhb.dc.demo.window;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Locale;

import yhb.dc.R;
import yhb.dc.common.Demo;

import static android.view.Gravity.CENTER;

@Demo
public class WindowActivity extends AppCompatActivity  {

    private WindowManager mWindowManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
    }

    public void onClick0(View view) {
        addWindow(WindowManager.LayoutParams.TYPE_APPLICATION, this);
    }

    public void onClick1(View view) {
        addWindow(WindowManager.LayoutParams.FIRST_SUB_WINDOW, this);
    }

    public void onClick2(View view) {
        if (checkPermission()) {
            addWindow(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onClick2(null);
        }
    }

    /**
     * 请求用户给予悬浮窗的权限
     */
    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "当前无权限，请授权！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 0);
                return false;
            }
        }
        return true;
    }

    /**
     * 用户返回
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "权限授予失败，无法开启悬浮窗", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "权限授予成功！", Toast.LENGTH_SHORT).show();
                    onClick2(null);
                }
            }

        }
    }


    private void addWindow(int type, Context context) {
        final View root = createWindowRootView(context, type);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = type;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.width = 100;
        layoutParams.height = 100;
        layoutParams.token = null;
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindowManager.removeViewImmediate(root);
            }
        });
        mWindowManager.addView(root, layoutParams);
    }

    private Ball createWindowRootView(Context context, int type) {
        Ball ball = new Ball(context);
        ball.setText(String.format(Locale.CHINA, "type=%d", type));
        ball.setGravity(CENTER);
        ball.setBackgroundColor(Color.BLUE);
        ball.setTextColor(Color.WHITE);
        return ball;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
