package saiday.myaudaiocast.myaudiocast.restClient;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import saiday.myaudaiocast.myaudiocast.entity.Episode;

public interface MyaudiocastAPI {
    @GET("/podcast/?format=json")
    void fetchPodcast(@Query("page") int page, Callback<PodcastsResponse> callback);

    @GET("/episode/?format=json")
    void fetchEpisodeByPodcastId(@Query("podcast") String podcastId, Callback<Episode> callback);
}
