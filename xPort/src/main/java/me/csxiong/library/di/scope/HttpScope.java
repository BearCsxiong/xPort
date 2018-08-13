package me.csxiong.library.di.scope;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by csxiong on 2018/8/9.
 */

@Scope
@Retention(RUNTIME)
public @interface HttpScope {
}
