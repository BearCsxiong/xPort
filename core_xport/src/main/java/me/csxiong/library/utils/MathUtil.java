package me.csxiong.library.utils;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Random;

/**
 * @Desc : 数学部分计算工具类
 * @Author : csxiong - 2020-02-26
 */
public class MathUtil {

    /**
     * 判断字符串是否是正数字
     *
     * @param str
     * @return
     */
    public static boolean isNumber(@Nullable String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取两个向量之间的角度
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static float getAngle(float x1, float y1, float x2, float y2) {
        float len = (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        float angle = (float) Math.acos((y2 - y1) / len);
        if (x2 - x1 < 0) {
            angle = 2 * (float) Math.PI - angle;
        }
        return angle;
    }

    /**
     * 获取两点之间距离
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static float getDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    /**
     * 判断两个浮点数是否相等
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean isEqual(float a, float b) {
        return Math.abs(a - b) < 0.001f;
    }

    /**
     * 判断两个浮点数组是否相等。
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean isEqual(float[] a, float[] b) {
        if (a == null || b == null) {
            return false;
        }
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if (!isEqualInRange(a[i], b[i], 0.00001f)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断两个浮点数在给定误差范围内是否相等
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean isEqualInRange(float a, float b, float range) {
        return Math.abs(a - b) < range;
    }

    /**
     * 判断两个矩形是否相等
     *
     * @param r1
     * @param r2
     * @return
     */
    public static boolean isEqual(RectF r1, RectF r2) {
        if (r1 == null || r2 == null) {
            return false;
        }
        return isEqual(r1.left, r2.left) && isEqual(r1.right, r2.right) && isEqual(r1.top, r2.top)
                && isEqual(r1.bottom, r2.bottom);
    }

    /**
     * 缩放矩阵
     *
     * @param rectF
     * @param scaleX
     * @param scaleY
     */
    public static void scaleRectF(RectF rectF, float scaleX, float scaleY) {
        if (rectF != null) {
            rectF.left *= scaleX;
            rectF.right *= scaleX;
            rectF.top *= scaleY;
            rectF.bottom *= scaleY;
        }
    }

    public static void rotateRectF(RectF rectF, float width, float height, int orientation) {
        if (rectF == null) {
            return;
        }
        orientation = (orientation + 360) % 360;
        if (orientation == 90) {
            float left = height - rectF.bottom;
            float right = left + rectF.height();
            float top = rectF.left;
            float bottom = top + rectF.width();
            rectF.set(left, top, right, bottom);
        } else if (orientation == 180) {
            float left = width - rectF.left;
            float right = left + rectF.width();
            float top = height - rectF.bottom;
            float bottom = top + rectF.height();
            rectF.set(left, top, right, bottom);
        } else if (orientation == 270) {
            float left = rectF.top;
            float right = left + rectF.height();
            float top = width - rectF.right;
            float bottom = top + rectF.width();
            rectF.set(left, top, right, bottom);
        }
    }

    /**
     * 将一个长数字的字符串转换为int型。
     *
     * @param intString
     * @return
     */
    public static int toInt(String intString) {
        return toInt(intString, -1);
    }

    /**
     * 将一个长数字的字符串转换为int型。
     *
     * @param intString
     * @return
     */
    public static int toInt(String intString, int defaultValue) {
        if (!isNumber(intString)) {
            return defaultValue;
        }
        // 32位int型大概是一个10位数，区字符串后9位，不超过int的取值范围。
        return Integer.parseInt(intString.substring(Math.max(0, intString.length() - 9), intString.length()));
    }

    /**
     * @param startColor
     * @param endColor
     * @param value
     * @return
     */
    public static int getGradientColor(int startColor, int endColor, float value) {
        int a = (startColor & 0xff000000) >> 24;
        int r = (startColor & 0x00ff0000) >> 16;
        int g = (startColor & 0x0000ff00) >> 8;
        int b = (startColor & 0x000000ff);
        int ea = (endColor & 0xff000000) >> 24;
        int er = (endColor & 0x00ff0000) >> 16;
        int eg = (endColor & 0x0000ff00) >> 8;
        int eb = (endColor & 0x000000ff);
        int na = Math.round((ea - a) * value + a);
        int nr = Math.round((er - r) * value + r);
        int ng = Math.round((eg - g) * value + g);
        int nb = Math.round((eb - b) * value + b);
        return Color.argb(na, nr, ng, nb);
    }

    public static int getRatioValue(int a, int b, float r) {
        return Math.round(a + (b - a) * r);
    }

    public static boolean closeToFirst(float a, float b, float c) {
        return Math.abs(a - b) <= Math.round(a - c);
    }

    /**
     * 求最大公约数
     *
     * @param a
     * @param b
     * @return
     */
    public static int getMaxDivisor(int a, int b) {
        while (b != 0) {
            int r = a % b;
            a = b;
            b = r;
        }
        if (a == 0) {
            a = 1;
        }
        return a;
    }

    /**
     * Arrays.toString(float[]) 逆函数。
     *
     * @param string
     * @return
     */
    public static float[] str2Floats(String string) {
        try {
            String[] strings = string.replace("[", "").replace("]", "").split(", ");
            float result[] = new float[strings.length];
            for (int i = 0; i < result.length; i++) {
                result[i] = Float.parseFloat(strings[i]);
            }
            return result;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 将图片在ImageView中的Matrix转换成在OpenGL中使用的Matrix。
     *
     * @param matrix
     * @param imageSize
     * @param viewSize
     * @return
     */
    public static float[] transformViewMatrixToGLMatrix4f(@NonNull Matrix matrix, @NonNull Point imageSize,
                                                          @NonNull Point viewSize) {
        float[] matrix4f = new float[16];
        android.opengl.Matrix.setIdentityM(matrix4f, 0);
        if (imageSize.x == 0 || imageSize.y == 0 || viewSize.x == 0 || viewSize.y == 0) {
            return matrix4f;
        }
        float[] matrix3f = new float[9];
        matrix.getValues(matrix3f);
        // 计算图片缩放。
        float ivRatio = Math.max(imageSize.x / (float) viewSize.x, imageSize.y / (float) viewSize.y);
        float scale = matrix3f[0] * ivRatio;
        // 计算图片偏移。
        float ivTranslateX = matrix3f[2] + (imageSize.x * matrix3f[0] - viewSize.x) / 2;
        float ivTranslateY = matrix3f[5] + (imageSize.y * matrix3f[4] - viewSize.y) / 2;
        android.opengl.Matrix.translateM(matrix4f, 0, ivTranslateX / viewSize.x * 2, -ivTranslateY / viewSize.y * 2, 1);
        android.opengl.Matrix.scaleM(matrix4f, 0, scale, scale, 1);
        if (matrix4f[0] < 1 && matrix4f[1] < 1) {
            android.opengl.Matrix.setIdentityM(matrix4f, 0);
        }
        return matrix4f;
    }

    /**
     * 取中间值。
     *
     * @param x
     * @param min
     * @param max
     * @return
     */
    public static float clamp(float x, float min, float max) {
        if (x < min) {
            return min;
        } else if (x > max) {
            return max;
        } else {
            return x;
        }
    }

    public static int clamp(int x, int min, int max) {
        if (x < min) {
            return min;
        } else if (x > max) {
            return max;
        } else {
            return x;
        }
    }

    public static int getInt(Integer integer) {
        return integer != null ? integer : 0;
    }

    public static float getFloat(Float floatValue) {
        return floatValue != null ? floatValue : 0;
    }

    public static long getLong(Long longValue) {
        return longValue != null ? longValue : 0L;
    }

    public static boolean getBool(Boolean boolValue) {
        return boolValue != null ? boolValue : false;
    }

    /**
     * 对比String的大小，
     *
     * @param s1
     * @param s2
     * @return
     */
    public static int compareString(String s1, String s2) {
        if (s1 == s2) {
            return 0;
        }
        if (TextUtils.isEmpty(s1)) {
            return 1;
        }
        if (TextUtils.isEmpty(s2)) {
            return -1;
        }
        if (s1.length() == s2.length()) {
            return s2.compareTo(s1);
        } else {
            return s1.length() > s2.length() ? -1 : 1;
        }
    }

    /**
     * 角度转弧度。
     *
     * @param rad
     * @return
     */
    public static float toDegree(float rad) {
        return rad / (float) Math.PI * 180;
    }

    /**
     * 是否是基础的归一化矩形
     *
     * @param rectF
     * @return
     */
    public static boolean isIdentifyRectF(RectF rectF) {
        if (rectF == null) {
            return false;
        }
        return isEqual(rectF.left, 0) && isEqual(rectF.top, 0) && isEqual(rectF.bottom, 1) && isEqual(rectF.right, 1);
    }

    /**
     * 获取一个在size中的随机数
     *
     * @param size
     * @return
     */
    public static int getRandomInSize(int size) {
        Random random = new Random();
        return random.nextInt(size);
    }
}
