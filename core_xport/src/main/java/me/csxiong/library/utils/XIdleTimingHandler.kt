package me.csxiong.library.utils

import android.os.MessageQueue
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * @Desc :闲置线程Handler
 * @Author : Bear - 2020/7/4
 */
class XIdleTimingHandler :MessageQueue.IdleHandler {

    /**
     * 闲置runnable队列
     */
    val idleQueue = ConcurrentLinkedQueue<Runnable>()

    override fun queueIdle(): Boolean {
        if (!idleQueue.isEmpty()) {
            var runnable = idleQueue.poll()
            do {
                runnable?.run()
                runnable = idleQueue.poll()
            }while (runnable != null)
        }
        return true
    }
}
