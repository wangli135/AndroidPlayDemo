package com.qiyi.openapi.demo.activity;

import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.qiyi.apilib.utils.StringUtils;
import com.qiyi.apilib.utils.UiUtils;
import com.qiyi.openapi.demo.R;
import com.qiyi.video.playcore.ErrorCode;
import com.qiyi.video.playcore.IQYPlayerHandlerCallBack;
import com.qiyi.video.playcore.QiyiVideoView;

import java.util.concurrent.TimeUnit;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

/**
 * Created by zhouxiaming on 2017/5/9.
 */

public class PlayerActivity extends BaseActivity {
    private static final int PERMISSION_REQUEST_CODE = 7171;
    private static final String TAG = PlayerActivity.class.getSimpleName();

    private static final int HANDLER_MSG_UPDATE_PROGRESS = 1;
    private static final int HANDLER_DEPLAY_UPDATE_PROGRESS = 1000; // 1s

    private int[] screen;

    private boolean isShowBtn;
    private boolean isFullScreen;
    private QiyiVideoView mVideoView;
    private SeekBar mSeekBar;
    private ImageView mBackBtn;
    private ImageView mPlayPauseBtn;
    private ImageView mZoomBtn;
    private TextView mCurrentTime;
    private TextView mTotalTime;

    private LinearLayout mBottomBarLv;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_player;
    }

    @Override
    protected void initView() {
        super.initView();

        screen = UiUtils.getScreenWidthAndHeight(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        String aid = getIntent().getStringExtra("aid");
        String tid = getIntent().getStringExtra("tid");
        if (StringUtils.isEmpty(tid)) {
            finish();
            return;
        }

        mBottomBarLv = (LinearLayout) findViewById(R.id.bottom_bar);

        mVideoView = (QiyiVideoView) findViewById(R.id.id_videoview);
        //mVideoView.setPlayData("667737400");
        mVideoView.setPlayData(tid);
        mVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBottomBarLv.getVisibility() == View.INVISIBLE) {

                    mBottomBarLv.setVisibility(View.VISIBLE);
                    mBottomBarLv.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBottomBarLv.setVisibility(View.INVISIBLE);
                        }
                    }, 2000);

                }

                if (mBackBtn.getVisibility() == View.INVISIBLE) {

                    mBackBtn.setVisibility(View.VISIBLE);
                    mBackBtn.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBackBtn.setVisibility(View.INVISIBLE);
                        }
                    }, 2000);

                }

            }
        });
        //设置回调，监听播放器状态
        setPlayerCallback();

        mCurrentTime = (TextView) findViewById(R.id.id_current_time);
        mTotalTime = (TextView) findViewById(R.id.id_total_time);

        mBackBtn = (ImageView) findViewById(R.id.back_iv);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFullScreen) {
                    isFullScreen = false;
                    setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
                    mVideoView.setVideoViewSize(screen[0], screen[0] * 9 / 16);
                } else {
                    finish();
                }
            }
        });

        mPlayPauseBtn = (ImageView) findViewById(R.id.play_pause_iv);
        mPlayPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mVideoView.isPlaying()) {
                    mVideoView.pause();
                    mPlayPauseBtn.setImageResource(R.drawable.ic_play_arrow_white_24dp);
                    mMainHandler.removeMessages(HANDLER_MSG_UPDATE_PROGRESS);
                } else {
                    mVideoView.start();
                    mPlayPauseBtn.setImageResource(R.drawable.ic_pause_white_24dp);
                    mMainHandler.sendEmptyMessageDelayed(HANDLER_MSG_UPDATE_PROGRESS, HANDLER_DEPLAY_UPDATE_PROGRESS);
                }
            }
        });

        mSeekBar = (SeekBar) findViewById(R.id.id_progress);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private int mProgress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mSeekBar.setProgress(mProgress);
                mVideoView.seekTo(mProgress);
            }
        });

        mZoomBtn = (ImageView) findViewById(R.id.zoom_iv);
        mZoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int current = mVideoView.getCurrentPosition();

                if (isFullScreen) {
                    isFullScreen = false;
                    setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
                    mVideoView.setVideoViewSize(screen[0], screen[0] * 9 / 16);

                } else {
                    isFullScreen = true;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    mVideoView.setVideoViewSize(screen[1], screen[0]);
                    mBackBtn.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBackBtn.setVisibility(View.INVISIBLE);
                        }
                    }, 2000);
                }

                mVideoView.seekTo(current);

            }
        });

    }

    private void setPlayerCallback() {
        mVideoView.setPlayerCallBack(mCallBack);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (null != mVideoView) {
            mVideoView.start();
        }
        mMainHandler.sendEmptyMessageDelayed(HANDLER_MSG_UPDATE_PROGRESS, HANDLER_DEPLAY_UPDATE_PROGRESS);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != mVideoView) {
            mVideoView.pause();
        }
        mMainHandler.removeMessages(HANDLER_MSG_UPDATE_PROGRESS);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainHandler.removeCallbacksAndMessages(null);
        mVideoView.release();
        mVideoView = null;
    }


    /**
     * Query and update the play progress every 1 second.
     */
    private Handler mMainHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_MSG_UPDATE_PROGRESS:
                    int duration = mVideoView.getDuration();
                    int progress = mVideoView.getCurrentPosition();
                    if (duration > 0) {
                        mSeekBar.setMax(duration);
                        mSeekBar.setProgress(progress);

                        mTotalTime.setText(ms2hms(duration));
                        mCurrentTime.setText(ms2hms(progress));
                    }
                    mMainHandler.sendEmptyMessageDelayed(HANDLER_MSG_UPDATE_PROGRESS, HANDLER_DEPLAY_UPDATE_PROGRESS);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * Convert ms to hh:mm:ss
     *
     * @param millis
     * @return
     */
    private String ms2hms(int millis) {
        String result = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
        return result;
    }

    IQYPlayerHandlerCallBack mCallBack = new IQYPlayerHandlerCallBack() {
        /**
         * SeekTo 成功，可以通过该回调获取当前准确时间点。
         */
        @Override
        public void OnSeekSuccess(long l) {
        }

        /**
         * 是否因数据加载而暂停播放
         */
        @Override
        public void OnWaiting(boolean b) {
        }

        /**
         * 播放内核发生错误
         */
        @Override
        public void OnError(ErrorCode errorCode) {

            mMainHandler.removeMessages(HANDLER_MSG_UPDATE_PROGRESS);
        }

        /**
         * 播放器状态码 {@link com.iqiyi.player.nativemediaplayer.MediaPlayerState}
         * 0	空闲状态
         * 1	已经初始化
         * 2	调用PrepareMovie，但还没有进入播放
         * 4    可以获取视频信息（比如时长等）
         * 8    广告播放中
         * 16   正片播放中
         * 32	一个影片播放结束
         * 64	错误
         * 128	播放结束（没有连播）
         */
        @Override
        public void OnPlayerStateChanged(int i) {

            switch (i) {
                case 16:
                    dismissBottomBar();
                    break;
            }

        }
    };

    private void dismissBottomBar() {
        mBottomBarLv.setVisibility(View.INVISIBLE);
    }

    private void showBottomBar() {
        mBottomBarLv.setVisibility(View.VISIBLE);
    }

}
