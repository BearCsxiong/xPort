package me.csxiong.library.utils;

/**
 * @Desc : 一个数值保存计算类
 * @Author : Bear - 2020/5/6
 */
public class XAnimatorCalculateValuer {

    /**
     * 限制value
     * @param value
     * @param min
     * @param max
     * @return
     */
    public static float limit(float value, float min, float max) {
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        }
        return value;
    }

    /**
     * 随便动态计算
     */
    public XAnimatorCalculateValuer() {
    }

    /**
     * 针对这种不需要动态计算的方式建立的初始值
     *
     * @param startValue
     */
    public XAnimatorCalculateValuer(float startValue) {
        this.startValue = startValue;
        this.value = startValue;
        this.endValue = startValue;
    }

    /**
     * 针对这种不需要动态计算的方式建立的初始值
     *
     * @param startValue
     */
    public XAnimatorCalculateValuer(float startValue, float endValue) {
        this.startValue = startValue;
        this.value = startValue;
        this.endValue = endValue;
    }

    /**
     * 计算开始值
     */
    public float startValue;

    /**
     * 计算末尾值
     */
    public float endValue;

    /**
     * 计算当前值
     */
    private float value;

    /**
     * 获取当前值
     *
     * @return
     */
    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
        this.startValue = value;
        this.endValue = value;
    }

    /**
     * 直接使用currentValue作为开始值
     *
     * @param endValue
     */
    public void to(float endValue) {
        this.startValue = value;
        this.endValue = endValue;
    }

    /**
     * 直接标记开始末尾值
     *
     * @param startValue
     * @param endValue
     */
    public void mark(float startValue, float endValue) {
        this.startValue = startValue;
        this.endValue = endValue;
    }

    /**
     * 动画因子计算
     *
     * @param fraction
     * @return
     */
    public float caculateValue(float fraction) {
        value = startValue + (endValue - startValue) * fraction;
        return value;
    }

    /**
     * 倍速计算动画因子计算
     *
     * @param fraction
     * @param multiSpeed
     * @return
     */
    public float caculateValue(float fraction, float multiSpeed) {
        fraction *= multiSpeed;
        if (fraction >= 1.0f) {
            fraction = 1.0f;
        }
        value = caculateValue(fraction);
        return value;
    }

    /**
     * 切换开始结束
     */
    public void exchange() {
        float _endValue = startValue;
        startValue = endValue;
        endValue = _endValue;
    }

    @Override
    public String toString() {
        return "XAnimatorCaculateValuer{" +
                "startValue=" + startValue +
                ", endValue=" + endValue +
                ", value=" + value +
                '}';
    }
}
