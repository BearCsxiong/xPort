package me.csxiong.camera;

/**
 * @Desc : Cover组
 * @Author : csxiong - 2019-11-22
 */
public interface ICoverGroup {

    /**
     * 提供数据保存器
     *
     * @return
     */
    DataProviders getDataProviders();

    /**
     * 发送Cover信息
     *
     * @param cover          发送消息的cover
     * @param coverEventCode 发送事件消息码
     * @param datas          数据
     */
    void postCoverEvent(ICover cover, int coverEventCode, Object... datas);

}
