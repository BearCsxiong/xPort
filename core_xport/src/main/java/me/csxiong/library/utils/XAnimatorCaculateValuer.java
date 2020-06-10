package me.csxiong.library.utils;

/**
 * @Desc : 一个数值保存计算类
 * @Author : Bear - 2020/5/6
 */
public class XAnimatorCaculateValuer {

    public float startValue;

    public float endValue;

    public void mark(float startValue, float endValue) {
        this.startValue = startValue;
        this.endValue = endValue;
    }

    public float caculateValue(float fraction) {
        return startValue + (endValue - startValue) * fraction;
    }
}
