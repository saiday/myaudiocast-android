package saiday.myaudaiocast.myaudiocast.audio;

import android.media.AudioManager;
import android.util.Log;

import java.lang.ref.WeakReference;

public class AudioFocusHelper implements AudioManager.OnAudioFocusChangeListener, AudioFocusProcessor {
    private final String Tag = AudioFocusHelper.class.getSimpleName();
    private final AudioManager mAudioManger;
    private final WeakReference<AudioPlayer> mAudioPlayer;

    public AudioFocusHelper(final AudioPlayer audioPlayer) {
        mAudioManger = audioPlayer.getAudioManager();
        mAudioPlayer = new WeakReference<>(audioPlayer);
    }

    public void requestFocus() {
        Log.i(Tag, "request focus.");
        int result = mAudioManger.requestAudioFocus(this, AudioManager.STREAM_VOICE_CALL, AudioManager.AUDIOFOCUS_GAIN);
        Log.i(Tag, "focus result " + result);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            final AudioPlayer audioPlayer = this.mAudioPlayer.get();
            audioPlayer.play();
        }
    }

    public void abandonFocus() {
        Log.i(Tag, "abandon focus.");
        mAudioManger.abandonAudioFocus(this);
    }

    @Override
    public void onAudioFocusChange(final int focusChange) {
        final AudioPlayer audioPlayer = this.mAudioPlayer.get();
        if (audioPlayer == null) {
            return;
        }
        Log.i(Tag, "focus changed" + focusChange);
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                audioPlayer.interruptionTakeFocus();
                Log.i(Tag, "Interruption began");
                break;
            default:
                break;
        }
    }
}

