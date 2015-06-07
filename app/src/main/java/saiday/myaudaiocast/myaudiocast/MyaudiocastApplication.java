package saiday.myaudaiocast.myaudiocast;

import android.app.Application;

import saiday.myaudaiocast.myaudiocast.audio.AudioPlayer;

/**
 * Created by saiday on 15/6/7.
 */
public class MyaudiocastApplication extends Application {
    private AudioPlayer mAudioPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public synchronized AudioPlayer getAudioPlayer() {
        if (mAudioPlayer == null) {
            mAudioPlayer = new AudioPlayer(this);
        }

        return mAudioPlayer;
    }
}
