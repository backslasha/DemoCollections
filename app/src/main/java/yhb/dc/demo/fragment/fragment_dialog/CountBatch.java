package yhb.dc.demo.fragment.fragment_dialog;

import android.text.format.DateUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import yhb.dc.common.ThreadUtils;


/**
 * 用来批量执行若干个 Runnable，允许设计最小执行间隔
 * 具体看 {@link CountBatch#batchRun(Runnable),CountBatch#cancelAll()}
 */
public class CountBatch {

    private static final String TAG = "CountBatch";
    private long minDelay; // 初始化该实例到执行 delayTasks 的时间不少于 minDelay
    private int minCount;  // 执行 delayTasks 时，delayTasks 的个数不少于 minCount
    private long initialTime; // 用来记录初始化该实例的时间
    private boolean batchExecuted; // 标记此实例是否已被使用过，若使用过后 batchRun 不做任何事
    private boolean scheduled; // delayTasks 是否已经进入消息队列
    private Runnable batchRunnable;
    private List<Runnable> delayTasks = new ArrayList<>();

    public CountBatch(int minCount, long minDelay) {
        this.minCount = minCount;
        this.minDelay = minDelay;
        this.initialTime = System.currentTimeMillis();
        this.batchExecuted = false;
        Log.d(TAG, "init with minCount=" + minCount + ", minDelay=" + minDelay);
    }


    /**
     * 添加一个 runnable 到 delayTasks 中，当满足以下两个条件时，delayTasks 的 task 会被批量执行并清空
     * 1. delayTasks 的 runnable 个数大于等于 minCount；
     * 2. 初始化该对象至少已经过去了 minDelay 毫秒。
     * <p>
     * 当 delayTasks 的任务批量执行后，调用该方法直接执行该 Runnable
     */
    public void batchRun(Runnable runnable) {
        if (batchExecuted) {
            ThreadUtils.runOnUiThread(runnable);
            Log.d(TAG, "batch already executed! execute task directly -> " + runnable);
            return;
        }
        delayTasks.add(runnable);
        Log.d(TAG, "add runnable -> " + runnable + ", now task count=" + minCount + ", delay=" + minDelay);
        if (!scheduled && delayTasks.size() >= minCount) {
            scheduleTasks();
            scheduled = true;
        }
    }

    /**
     * 取消所有已添加的 runnable
     */
    public void cancelAll() {
        ThreadUtils.removeCallbacks(batchRunnable);
        delayTasks.clear();
        batchExecuted = true;
    }

    private void scheduleTasks() {
        long elapsed = System.currentTimeMillis() - initialTime;
        long delay = Math.max(0, minDelay - elapsed);
        batchRunnable = () -> {
            Log.d(TAG, "batch start with task count=" + minCount + ", delay=" + minDelay
                    + ", elapsed=" + DateUtils.formatElapsedTime((System.currentTimeMillis() - initialTime) / 1000));
            for (Runnable delayTask : delayTasks) {
                delayTask.run();
                Log.d(TAG, "execute task -> " + delayTask);
            }
            batchRunnable = null;
            batchExecuted = true;
            delayTasks.clear();
        };
        ThreadUtils.runOnUiThread(batchRunnable, delay);
    }

}
