package yhb.dc.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用此注解的 Activity 会自动在主界面创建入口按钮（当然在 AndroidManifest 中注册还是需要的）
 */

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Demo {

    int DEMO_ID_WEB_VIEW = 0;
    int DEMO_ID_CLEAR_TASK = 1;
    int DEMO_ID_HANDLER_THREAD = 2;
    int DEMO_ID_AIDL_USAGE = 3;
    int DEMO_ID_CLICK_EFFECTS = 4;
    int DEMO_SMS_OBSERVE = 5;
    int DEMO_ID_CANVAS = 6;
    int DEMO_ID_BUBBLE_LAYOUT = 8;
    int DEMO_ID_CUSTOM_VIEW = 9;
    int DEMO_ID_CAROUSEL = 10;
    int DEMO_ID_FRAGMENT_COMMUNICATE = 11;
    int DEMO_ID_FRAGMENT_COMMIT = 12;
    int DEMO_ID_BOTTOM_DIALOG_BUG = 13;
    int DEMO_ID_TAB_FRAGMENT_LIFE = 14;
    int DEMO_ID_ACTIVITY_LAUNCH_MODE = 15;
    int DEMO_ID_FRESCO_USAGE = 16;
    int DEMO_ID_NOTIFICATION = 17;
    int DEMO_ID_COMPLEX_RECYCLER_VIEW = 18;
    int DEMO_ID_WINDOW = 19;
    int DEMO_ID_VIEW_POSITION_FIELD = 20;
    int DEMO_ID_CAMERA = 21;

    int debuggingDemoId = Demo.DEMO_ID_CAMERA;

    int id();

    String name() default "";
}
