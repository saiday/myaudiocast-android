package saiday.myaudaiocast.myaudiocast;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import de.greenrobot.event.EventBus;
import saiday.myaudaiocast.myaudiocast.audio.AudioPlayer;
import saiday.myaudaiocast.myaudiocast.audio.PlaybackBroadcastReceiver;
import saiday.myaudaiocast.myaudiocast.ui.notification.MediaControlsNotification;

/**
 * Created by saiday on 15/6/7.
 */
public class MyaudiocastCoreService extends Service {
    public enum MediaControlsIntent {
        INTENT_TOGGLE_PLAY, INTENT_PREVIOUS, INTENT_NEXT, INTENT_STOP,
    }

    private static final String TAG = "MyaudiocastCoreService";
    private final IBinder mBinder = new MyBinder();
    private AudioPlayer mAudioPlayer;
    private MyaudiocastApplication mApplication;
    private MediaControlsNotification mMediaControlsNotification;
    protected BroadcastReceiver mPlaybackReceiver;

    public class MyBinder extends Binder {
        public MyaudiocastCoreService getService() {
            return MyaudiocastCoreService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mApplication = (MyaudiocastApplication) getApplication();
        mAudioPlayer = mApplication.getAudioPlayer();
        mAudioPlayer.setService(this);
        mMediaControlsNotification = new MediaControlsNotification(getApplicationContext(), mAudioPlayer);
        mPlaybackReceiver = new PlaybackBroadcastReceiver(mAudioPlayer, mMediaControlsNotification);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MediaControlsNotification.INTENT_TOGGLE_PLAY);
        intentFilter.addAction(MediaControlsNotification.INTENT_NEXT);
        intentFilter.addAction(MediaControlsNotification.INTENT_PREVIOUS);
        intentFilter.addAction(MediaControlsNotification.INTENT_STOP);
        registerReceiver(mPlaybackReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mPlaybackReceiver);

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "MyaudiocastCoreService is started, flags = " + flags + ", startId = " + startId);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
