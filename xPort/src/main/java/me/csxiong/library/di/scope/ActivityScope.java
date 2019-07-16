package me.csxiong.library.di.scope;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Desc : 标记Activity作用范围
 * @Author : csxiong create on 2019/7/16
 */
@Scope
@Retention(RUNTIME)
public @interface ActivityScope {
}
