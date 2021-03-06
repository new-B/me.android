package me.java.library.common.service;

import com.github.rholder.retry.*;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.*;
import me.java.library.utils.base.ConcurrentUtils;

import java.util.concurrent.*;

/**
 * File Name             :  ConcurrentService
 *
 * @author :  sylar
 * Create :  2019-10-18
 * Description           :
 * Reviewed By           :
 * Reviewed On           :
 * Version History       :
 * Modified By           :
 * Modified Date         :
 * Comments              :
 * CopyRight             : COPYRIGHT(c) me.iot.com   All Rights Reserved
 * *******************************************************************************************
 */
@SuppressWarnings({"UnstableApiUsage"})
public class ConcurrentService implements Serviceable {

    private ThreadPoolExecutor executor = ConcurrentUtils.simpleThreadPool();
    private ScheduledThreadPoolExecutor scheduledExecutor = ConcurrentUtils.simpleScheduledThreadPool();

    private ListeningExecutorService service = MoreExecutors.listeningDecorator(executor);
    private ListeningScheduledExecutorService scheduledService = MoreExecutors.listeningDecorator(scheduledExecutor);

    private ConcurrentService() {
        executor.allowCoreThreadTimeOut(true);
    }

    public static ConcurrentService getInstance() {
        return SingletonHolder.instance;
    }

    @Override
    public void dispose() {
        dispose(service);
        dispose(scheduledService);
    }

    private void dispose(ExecutorService service) {
        if (!service.isShutdown()) {
            service.shutdown();
            try {
                service.awaitTermination(200, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                service.shutdownNow();
            }
        }
    }

    /**
     * 执行异步任务,任务无返回值
     *
     * @param runnable 可执行任务
     * @return ListenableFuture<T>
     */
    public ListenableFuture<?> postRunnable(Runnable runnable) {
        Preconditions.checkNotNull(runnable);
        return service.submit(runnable);
    }

    /**
     * 执行异步任务,任务无返回值
     *
     * @param runnable 可执行任务
     * @param callback 异步回调
     * @return ListenableFuture<T>
     */
    public ListenableFuture<?> postRunnable(Runnable runnable, FutureCallback<Object> callback) {
        Preconditions.checkNotNull(runnable);
        ListenableFuture<?> future = service.submit(runnable);
        addCallback(future, callback, executor);
        return future;
    }

    /**
     * 执行异步任务，任务有返回值
     *
     * @param callable 可执行任务
     * @param <T>
     * @return ListenableFuture<T>
     */
    public <T> ListenableFuture<T> postCallable(Callable<T> callable) {
        Preconditions.checkNotNull(callable);
        return service.submit(callable);
    }

    /**
     * 执行异步任务，任务有返回值
     *
     * @param callable 可执行任务
     * @param callback 异步回调
     * @param <T>
     * @return ListenableFuture<T>
     */
    public <T> ListenableFuture<T> postCallable(Callable<T> callable, FutureCallback<T> callback) {
        Preconditions.checkNotNull(callable);
        ListenableFuture<T> future = service.submit(callable);
        addCallback(future, callback, executor);
        return future;
    }

    /**
     * 执行异步任务（有超时及重试机制），任务有返回值
     *
     * @param callable
     * @param timeout
     * @param unit
     * @param tryTimes
     * @param callback
     * @param <T>
     */
    public <T> ListenableFuture<T> postCallable(Callable<T> callable, FutureCallback<T> callback, long timeout, TimeUnit unit, int tryTimes, RetryListener listener) {
        Preconditions.checkNotNull(callable);
        Preconditions.checkNotNull(callback);
        Preconditions.checkState(timeout > 0);
        Preconditions.checkState(tryTimes > 0);

        Retryer<T> retryer = RetryerBuilder.<T>newBuilder()
                .retryIfException()
                .withRetryListener(listener)
                .withWaitStrategy(WaitStrategies.fixedWait(timeout, unit))
                .withStopStrategy(StopStrategies.stopAfterAttempt(tryTimes))
                .build();

        ListenableFuture<T> future = null;
        try {
            future = postCallable(() -> retryer.call(callable), callback);
        } catch (Exception e) {
            callback.onFailure(e);
        }

        return future;
    }

    /**
     * 执行异步任务（有超时及重试机制），并等待执行结果返回
     *
     * @param callable 可执行任务
     * @param timeout  超时时间
     * @param unit     时间单位
     * @param tryTimes 重试次数
     * @param callback 同步回调
     * @param <T>
     * @return SettableFuture<T>
     */
    public <T> SettableFuture<T> syncPost(Callable<T> callable, FutureCallback<T> callback, long timeout, TimeUnit unit, int tryTimes) {
        Preconditions.checkNotNull(callback);
        Preconditions.checkState(timeout > 0);
        Preconditions.checkState(tryTimes > 0);

        T res = null;
        Throwable t = null;

        SettableFuture<T> settableFuture = createSettableFuture(callable);
        for (int i = 0; i < tryTimes; i++) {
            try {
                res = settableFuture.get(timeout, unit);
                if (res != null) {
                    break;
                }
            } catch (Exception e) {
                t = e;
            }
        }

        if (res != null) {
            callback.onSuccess(res);
        } else {
            callback.onFailure(t);
        }

        return settableFuture;
    }

    /**
     * 执行异步任务（有超时机制），并等待执行结果返回
     *
     * @param callable 可执行任务
     * @param timeout  超时时间
     * @param unit     时间单位
     * @param callback 同步回调
     * @param <T>
     * @return SettableFuture<T>
     */
    public <T> SettableFuture<T> syncPost(Callable<T> callable, FutureCallback<T> callback, long timeout, TimeUnit unit) {
        Preconditions.checkNotNull(callback);
        Preconditions.checkState(timeout > 0);

        SettableFuture<T> settableFuture = createSettableFuture(callable);
        try {
            T t = settableFuture.get(timeout, unit);
            callback.onSuccess(t);
        } catch (Exception e) {
            callback.onFailure(e);
        }
        return settableFuture;
    }

    /**
     * 执行异步任务，并等待执行结果返回
     *
     * @param callable 可执行任务
     * @param callback 同步回调
     * @param <T>
     * @return SettableFuture<T>
     */
    public <T> SettableFuture<T> syncPost(Callable<T> callable, FutureCallback<T> callback) {
        Preconditions.checkNotNull(callback);
        SettableFuture<T> settableFuture = createSettableFuture(callable);
        try {
            T t = settableFuture.get();
            callback.onSuccess(t);
        } catch (Exception e) {
            callback.onFailure(e);
        }

        return settableFuture;
    }

    private <T> SettableFuture<T> createSettableFuture(Callable<T> callable) {
        Preconditions.checkNotNull(callable);
        SettableFuture<T> settableFuture = SettableFuture.create();
        postCallable(callable, new FutureCallback<T>() {
            @Override
            public void onSuccess(T result) {
                settableFuture.set(result);
            }

            @Override
            public void onFailure(Throwable t) {
                settableFuture.setException(t);
            }
        });

        return settableFuture;
    }

    /**
     * 延时执行
     *
     * @param runnable 可执行任务
     * @param delay    延时
     * @param unit     时间单位
     * @return Future
     */
    public ListenableScheduledFuture<?> schedule(Runnable runnable, long delay, TimeUnit unit) {
        return scheduledService.schedule(runnable, delay, unit);
    }

    /**
     * 延时执行
     *
     * @param callable 可执行任务，有返回值
     * @param delay    延时
     * @param unit     时间单位
     * @param <T>
     * @return Future
     */
    public <T> ListenableScheduledFuture<T> schedule(Callable<T> callable, long delay, TimeUnit unit) {
        return scheduledService.schedule(callable, delay, unit);
    }

    /**
     * 以固定频率执行任务
     *
     * @param runnable     可执行任务
     * @param initialDelay 初始延时
     * @param period       固定频率
     * @param unit         时间单位
     * @return Future
     */
    public ListenableScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long initialDelay, long period, TimeUnit unit) {
        return scheduledService.scheduleAtFixedRate(runnable, initialDelay,
                period, unit);
    }

    /**
     * 以固定延时执行任务
     *
     * @param runnable     可执行任务
     * @param initialDelay 初始延时
     * @param delay        固定延时
     * @param unit         时间单位
     * @return Future
     */
    public ListenableScheduledFuture<?> scheduleWithFixedDelay(Runnable runnable, long initialDelay, long delay, TimeUnit unit) {
        return scheduledService.scheduleWithFixedDelay(runnable, initialDelay,
                delay, unit);
    }

    private <T> void addCallback(ListenableFuture<T> future, FutureCallback<? super T> callback, Executor executor) {
        if (callback != null) {
            Futures.addCallback(future, callback, executor);
        }
    }

    private static class SingletonHolder {
        private static ConcurrentService instance = new ConcurrentService();
    }
}
