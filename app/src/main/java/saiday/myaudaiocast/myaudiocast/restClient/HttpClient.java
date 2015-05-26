package saiday.myaudaiocast.myaudiocast.restClient;

import android.content.Context;
import android.util.Log;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import saiday.myaudaiocast.myaudiocast.entity.Episode;
import saiday.myaudaiocast.myaudiocast.entity.Podcast;

public class HttpClient {
    private Context mContext;
    private MyaudiocastAPI mMyaudiocastAPI;
    private int mPodcastPages = 1;

    public HttpClient(Context context) {
        mContext = context;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://www.myaudiocast.com/api").build();
        mMyaudiocastAPI = restAdapter.create(MyaudiocastAPI.class);
    }

    public void getPodcasts(final Podcast.CreatePodcastsHandler handler) {
        mMyaudiocastAPI.fetchPodcast(mPodcastPages, new Callback<PodcastsResponse>() {
            @Override
            public void success(PodcastsResponse podcastsResponse, Response response) {
                mPodcastPages ++;
                handler.onCreatePodcastsSuccess(podcastsResponse.results);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("test", error.toString());
                handler.onCreatePodcastsFailed();
            }
        });
    }
}
