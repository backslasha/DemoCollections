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

    /**
     * @return 启动应用后是否自动跳转到该 Demo
     */
    boolean autoJumpIn() default false;
}
