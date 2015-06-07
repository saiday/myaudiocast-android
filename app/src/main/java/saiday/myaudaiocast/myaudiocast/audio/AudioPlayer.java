package saiday.myaudaiocast.myaudiocast.audio;

import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.telephony.PhoneStateListener;
import android.util.Log;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.FrameworkSampleSource;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.SampleSource;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import de.greenrobot.event.EventBus;
import saiday.myaudaiocast.myaudiocast.MyaudiocastCoreService;
import saiday.myaudaiocast.myaudiocast.entity.Episode;

/**
 * Created by saiday on 15/6/6.
 */
public class AudioPlayer implements PhoneListener.IncomingCallListener, ExoPlayer.Listener {
    /**
     * add the player state by the definition in https://developer.android.com/reference/android/media/MediaPlayer.html.
     * Because Google doesn't provided the API and often throw the IllegalState exception........
     */
    public enum PlayerState {
        RELEASED,
        IDLE,
        DOWNLOADING,
        INITIALIZED,
        PREPARING,
        PREPARED,
        STARTED,
        PAUSED,
        STOPPED,
        COMPLETE,
        ERROR
    }

    private static final String TAG = "AudioPlayer";
    private EventBus mEventBus;
    private ExoPlayer mExoPlayer;
    private Context mContext;
    private PlayerItem mCurrentItem;
    private ListIterator<Episode> mEpisodeIterator;
    private AudioManager mAudioManager;
    private AudioFocusHelper mAudioFocusHelper;
    private Handler mUpdateHandler;
    private long mLastPosition;
    private float mRate = 1.f;
    private PlayerState mPlayerState;
    private WeakReference<MyaudiocastCoreService> mService;

    public AudioManager getAudioManager() {
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        }

        return mAudioManager;
    }

    public void registerNotification(Object receiver) {
        if (!mEventBus.isRegistered(receiver)) {
            mEventBus.register(receiver);
        }
    }

    public void unregisterNotification(Object receiver) {
        if (mEventBus.isRegistered(receiver)) {
            mEventBus.unregister(receiver);
        }
    }

    public void setService(MyaudiocastCoreService service) {
        mService = new WeakReference<>(service);
    }

    public AudioPlayer (Context context) {
        this(context, null);
    }

    public AudioPlayer (Context context, MyaudiocastCoreService service) {
        mContext = context;
        mService = new WeakReference<>(service);

        mEventBus = EventBus.getDefault();
        Looper looper;
        if ((looper = Looper.myLooper()) != null) {
            mUpdateHandler = new Handler(looper);
        } else if ((looper = Looper.getMainLooper()) != null) {
            mUpdateHandler = new Handler(looper);
        } else {
            mUpdateHandler = null;
        }

        changeState(PlayerState.IDLE);
        mAudioManager = getAudioManager();
        mAudioFocusHelper = new AudioFocusHelper(this);
    }

    private void changeState(PlayerState state) {
        Log.e(TAG, "change state: " + state);
        mPlayerState = state;
        mEventBus.post(state);
    }

    public void interruptionTakeFocus() {
        pause();
    }

    public void setAudioSource(List<Episode> episodes) {
        mEpisodeIterator = episodes.listIterator();
        mCurrentItem = new PlayerItem(mEpisodeIterator.next(), mContext);

        setDataSource(mCurrentItem.getURL());
    }

    private void initPlayer() {
        mExoPlayer = ExoPlayer.Factory.newInstance(1);
        mExoPlayer.addListener(this);
        changeState(PlayerState.INITIALIZED);
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.removeListener(this);
            mExoPlayer.release();
            mExoPlayer = null;
        }

        mEpisodeIterator = null;
        changeState(PlayerState.RELEASED);
    }

    private void setDataSource(String url) {
        //replace .mp4 to m3u8 because android only support .m3u8 for HLS
        if (mExoPlayer == null) {
            initPlayer();
        } else {
            mExoPlayer.stop();
            mExoPlayer.seekTo(0);
            mLastPosition = 0;
        }

        if (mPlayerState != PlayerState.IDLE) {
            stop(false);
        }
        Log.d(TAG, "play remote audio" + url);

        FrameworkSampleSource sampleSource = new FrameworkSampleSource(mContext, Uri.parse(url), null, 1);
        TrackRenderer audioRenderer = new MediaCodecAudioTrackRenderer(sampleSource);
        mExoPlayer.setPlayWhenReady(true);
        mExoPlayer.prepare(audioRenderer);
        changeState(PlayerState.PREPARING);
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {
            case ExoPlayer.STATE_READY:
                if (mPlayerState == PlayerState.PREPARING) {
                    changeState(PlayerState.PREPARED);
                    start();
                }
                break;
            case ExoPlayer.STATE_ENDED:
                next();
                break;
            default:
                break;
        }
        Log.i(TAG, "play when ready:" + playWhenReady + "playback state: " + playbackState);
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        stop(true);
        changeState(PlayerState.ERROR);
    }

    @Override
    public void onPlayWhenReadyCommitted() {
        Log.i(TAG, "on play when ready committed");
    }

    public void start() {
        if (mAudioFocusHelper == null) {
            mAudioFocusHelper = new AudioFocusHelper(this);
        }
        mAudioFocusHelper.requestFocus();
    }

    protected void play() {
        if (mExoPlayer != null) {
            updatePlayProgress();
            changeState(PlayerState.STARTED);
        }
    }

    public boolean isPlaying() {
        return mExoPlayer != null && mExoPlayer.getPlayWhenReady();
    }

    public void stop(boolean manualStop) {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
        }

        changeState(PlayerState.STOPPED);
        if (mAudioFocusHelper != null) {
            mAudioFocusHelper.abandonFocus();
            mAudioFocusHelper = null;
        }
        if (manualStop) {
            releasePlayer();
        }
    }

    public void pause() {
        if (mExoPlayer != null && isPlaying()) {
            mExoPlayer.setPlayWhenReady(false);
            changeState(PlayerState.PAUSED);
        }
    }

    public void next() {
        stop(false);
        Episode nextAudio = getNextAudio();
        if (nextAudio != null) {
            PlayerItem nextPlayerItem = new PlayerItem(nextAudio, mContext);
            mCurrentItem = nextPlayerItem;
            mEventBus.post(new PlayNextEvent(nextAudio));
            setDataSource(mCurrentItem.getURL());
        } else {
            mEventBus.post(new PlayNextEvent(null));
            allComplete();
        }
    }

    public void allComplete() {
        stop(false);
        changeState(PlayerState.COMPLETE);
        releasePlayer();
    }

    private Episode getNextAudio() {
        if (mEpisodeIterator != null && mEpisodeIterator.hasNext()) {
            return mEpisodeIterator.next();
        } else {
            return null;
        }
    }

    private Runnable mUpdateProgressRunnable = new Runnable() {
        @Override
        public void run() {
            updatePlayProgress();
        }
    };
    private void updatePlayProgress() {
        if (isPlaying()) {
            long currentPosition = mExoPlayer.getCurrentPosition();
            if (mLastPosition <= currentPosition) {
                mLastPosition = currentPosition;
            }
            mUpdateHandler.postDelayed(mUpdateProgressRunnable, 100);
        }
    }

    /**
     * The event of playing next item
     * Use this class for more clear meaning
     */
    public static class PlayNextEvent {
        public Episode episode;
        public PlayNextEvent(Episode episode) {
            this.episode = episode;
        }
    }

    @Override
    public void incomingCallStarted() {
        pause();
    }
}
