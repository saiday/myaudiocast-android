package saiday.myaudaiocast.myaudiocast.restClient;

import android.content.Context;
import android.util.Log;

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
                Log.d("Fetching Error:", error.toString());
                handler.onCreatePodcastsFailed();
            }
        });
    }

    public void getEpisodes(Podcast podcast, final Episode.CreateEpisodesHandler handler) {
        mMyaudiocastAPI.fetchEpisodesByPodcastId(podcast.id, new Callback<EpisodesResponse>() {
            @Override
            public void success(EpisodesResponse episodesResponse, Response response) {
                handler.onCreateEpisodesSuccess(episodesResponse.results);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Fetching Error:", error.toString());
                handler.onCreateEpisodesFailed();
            }
        });
    }
}
