package io.github.hadixlin.springfoxplus;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 创建这个类来解决kotlin调用java方法的交互问题<br>
 * 直接在kotlin中调用 {@link AnnotationUtils#findAnnotation(Method, Class)}无法通过编译
 */
public class AnnotationHelper {
    public static <A extends Annotation> A findAnnotation(
            Method method, @Nullable Class<A> annotationType) {
        return AnnotationUtils.findAnnotation(method, annotationType);
    }
}
