package me.csxiong.library.integration;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import me.csxiong.library.BuildConfig;
import me.csxiong.library.base.GlobalConfig;

/**
 * @Desc : 解析AndroidManifest文件,主要提供解析部分metadata功能,使用{@link GlobalConfig}
 * @Author : csxiong create on 2019/7/16
 */
public final class ManifestParser<T> {

    private static final String MODULE_VALUE = "GlobalConfig";

    private final Context context;

    public ManifestParser(Context context) {
        this.context = context;
    }

    public List<T> parse(String manifestTargetName) {
        List<T> modules = new ArrayList<T>();
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                for (String key : appInfo.metaData.keySet()) {
                    if (manifestTargetName.equals(appInfo.metaData.get(key))) {
                        modules.add(parseModule(key));
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Unable to find metadata to parse GlobalConfig", e);
        }
        for (T t : modules) {
            if (BuildConfig.DEBUG) {
                Log.e("Manifest", "init APP delegate -> " + t.getClass().getName());
            }
        }
        return modules;
    }

    public String parseString(String manifestTargetName) {
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                for (String key : appInfo.metaData.keySet()) {
                    if (manifestTargetName.equals(key)) {
                        return appInfo.metaData.getString(key);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    private T parseModule(String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unable to find GlobalConfig implementation", e);
        }

        Object module;
        try {
            module = clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Unable to instantiate GlobalConfig implementation for " + clazz, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to instantiate GlobalConfig implementation for " + clazz, e);
        }

        if (!(module instanceof GlobalConfig)) {
            throw new RuntimeException("Expected instanceof GlobalConfig, but found: " + module);
        }
        return (T) module;
    }
}