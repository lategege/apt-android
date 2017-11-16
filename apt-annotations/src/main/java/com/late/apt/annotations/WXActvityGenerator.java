package com.late.apt.annotations;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Created by xuhongliang on 2017/11/15.
 */
//声明注解作用范围是作用在类,接口，注解，枚举上
@Target(ElementType.TYPE)
//声明注解的有效期为源码期
@Retention(RetentionPolicy.SOURCE)
public @interface WXActvityGenerator {
    //声明该注解所要生成的包名规则
    String getPackageName();

    //声明该注解所生成java类需要继承哪个父类
    Class<?> getSupperClass();
}
