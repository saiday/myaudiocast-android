package saiday.myaudaiocast.myaudiocast.audio;

import android.content.Context;

import saiday.myaudaiocast.myaudiocast.entity.Episode;

/**
 * Created by saiday on 15/6/6.
 */
public class PlayerItem {
    private final Episode mEpisode;
    private final Context mContext;
    private String mURL;

    public PlayerItem(Episode episode, Context context) {
        mContext = context;
        mEpisode = episode;

        mURL = episode.file;
    }

    public String getURL() {
        return mURL;
    }
}
