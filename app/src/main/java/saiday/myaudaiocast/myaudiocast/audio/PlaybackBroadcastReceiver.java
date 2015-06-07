package saiday.myaudaiocast.myaudiocast.audio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import saiday.myaudaiocast.myaudiocast.MyaudiocastApplication;
import saiday.myaudaiocast.myaudiocast.MyaudiocastCoreService;
import saiday.myaudaiocast.myaudiocast.ui.notification.MediaControlsNotification;

/**
 * Created by saiday on 15/6/7.
 */
public class PlaybackBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = PlaybackBroadcastReceiver.class.getSimpleName();
    private AudioPlayer mAudioPlayer;
    private MediaControlsNotification mMediaControlsNotification;

    public PlaybackBroadcastReceiver(AudioPlayer audioPlayer, MediaControlsNotification mediaControlsNotification) {
        mAudioPlayer = audioPlayer;
        mMediaControlsNotification = mediaControlsNotification;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i(TAG, "service.onReceive: " + action);
        MyaudiocastCoreService.MediaControlsIntent mediaControlsIntent = MyaudiocastCoreService.MediaControlsIntent.valueOf(action);
//        switch (mediaControlsIntent) {
//            case INTENT_TOGGLE_PLAY:
//                if (mAudioPlayer.isPlaying()) {
//                    mAudioPlayer.pause();
//                } else {
//                    mAudioPlayer.start();
//                }
//                break;
//            case INTENT_NEXT:
//                mAudioPlayer.next();
//                break;
//            case INTENT_PREVIOUS:
//                mAudioPlayer.previous();
//                break;
//            case INTENT_STOP:
//                mMediaControlsNotification.cancelNotification();
//                if (!mAudioPlayer.isStopped()) {
//                    mAudioPlayer.stop(true);
//                }
//                break;
//            default:
//                break;
//        }
    }
}
