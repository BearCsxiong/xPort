package me.csxiong.library.integration.mvp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import me.csxiong.library.base.IView;


/**
 * Created by csxiong on 2018/8/10.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Presenter {
    Class<? extends IView> value();
}
