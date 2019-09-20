package yhb.dc.common;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 使用此注解的 Activity 会自动在主界面创建入口按钮（当然在 AndroidManifest 中注册还是需要的）
         */

@Retention(RetentionPolicy.RUNTIME)
public @interface Demo {

}
