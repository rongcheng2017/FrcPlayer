package com.example.frc.frcplayer;

/**
 * 模仿ExoPlayer
 * Created by frc on 17-7-25.
 */

public interface FrcPlayer {
    /**
     * 播放器没有资源去播放，处于闲置状态
     */
    int STATE_IDLE = 1;
    /**
     * 播放器不能立即从当前的位置开始播放，造成这样的原因有很多，但是这种状态通常是当有更多的数据需要加载去播放
     * 或者缓存更多的数据用来恢复播放
     */
    int STATE_BUFFERING = 2;
    /**
     * 播放器可以立即从当前位置开始播放，如果{@link #getPlayWhenReady()}返回true,播放器将播放，否则暂停
     */
    int STATE_READY = 3;
    /**
     * 播放器已经结束正在播放的媒体资源
     */
    int STATE_ENDED = 4;

    /**
     * 播放器状态改变
     */
    interface EventListener {
        /**
         * 当时间线或者清单被刷新时调用
         *
         * @param timeline The latest timeline. Never null, but may be empty.
         * @param manifest The latest manifest. May be null.
         */
        void onTimelineChanged(Timeline timeline, Object manifest);

        /**
         * 当可用或选定的轨道发生变化时调用。
         *
         * @param trackGroups
         * @param trackSelections
         */
        void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections);

        /**
         * 播放器启动或停止加载源时调用。
         *
         * @param isLoading
         */
        void onLoadingChanged(boolean isLoading);

        /**
         * 当{@link #getPlaybackState()}或者{@link #getPlaybackState()}返回值发生改变时调用
         *
         * @param playWhenReady
         * @param playbackState
         */
        void onPlayerStateChanged(boolean playWhenReady, int playbackState);


        /**
         * 发生异常
         * @param error
         */
        void onPlayerError(ExoPlaybackException error);

        /**
         * 位置中断
         */
        void onPositionDiscontinuity();

        /**
         * 回放参数改变
         *
         * @param playbackParameters 回放参数
         */
        void onPlaybackParametersChanged(PlaybackParameters playbackParameters);

    }

    interface FrcPlayerComponent {
        void handleMessage(int messageType, Object message) throws ExoPlaybackException;
    }

    final class FrcPlayerMessage {

        /**
         * The target to receive the message.
         */
        public final FrcPlayerComponent target;
        /**
         * The type of the message.
         */
        public final int messageType;
        /**
         * The message.
         */
        public final Object message;

        public FrcPlayerMessage(FrcPlayerComponent target, int messageType, Object message) {
            this.target = target;
            this.messageType = messageType;
            this.message = message;
        }
    }

    /**
     * 通过{@link #getPlaybackState()} == {@link #STATE_READY}判断是否继续播放
     *
     * @return 当准备好了是否播放
     */
    boolean getPlayWhenReady();

    /**
     * 如果播放器在STATE_READY状态那么这个方法恩嗯用来暂停和恢复播放
     *
     * @param playWhenReady 当READY状态下是否应该继续播放
     */
    void setPlayWhenReady(boolean playWhenReady);

    /**
     * 返回播放器的当前状态
     *
     * @return One of the {@code STATE} constants defined in this interface.
     */
    int getPlaybackState();


}