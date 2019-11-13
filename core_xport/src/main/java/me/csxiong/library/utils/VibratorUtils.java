package me.csxiong.library.utils;

import android.app.Service;
import android.os.VibrationEffect;
import android.os.Vibrator;

import me.csxiong.library.base.APP;

/**
 * -------------------------------------------------------------------------------
 * |
 * | desc : 震动工具
 * |
 * |--------------------------------------------------------------------------------
 * | on 2019/2/18 created by csxiong
 * |--------------------------------------------------------------------------------
 */
public class VibratorUtils {

    public static void vibrator(long[] times) {
        // 震动周期，数组表时间（等待+执行，单位毫秒）
        // 此处等待100，执行1000，等待100，执行3000
        // 后面数字-1表执行一次后不重复（其它重复）
        // 0表从数组下标0开始
        Vibrator mVibrator = (Vibrator) APP.get().getSystemService(Service.VIBRATOR_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            VibrationEffect vibrationEffect = VibrationEffect.createWaveform(times, -1);
            mVibrator.vibrate(vibrationEffect);
        } else {
            mVibrator.vibrate(times, -1);
        }
    }

    public static void vibrator() {
        vibrator(new long[]{100, 200, 100, 400});
    }

    public static void onShot(long times) {
        Vibrator mVibrator = (Vibrator) APP.get().getSystemService(Service.VIBRATOR_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            VibrationEffect vibrationEffect = VibrationEffect.createOneShot(times, -1);
            mVibrator.vibrate(vibrationEffect);
        } else {
            mVibrator.vibrate(times);
        }
    }

    public static void onShot() {
        onShot(50);
    }
}
