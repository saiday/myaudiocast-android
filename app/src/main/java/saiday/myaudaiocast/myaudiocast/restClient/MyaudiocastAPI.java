package saiday.myaudaiocast.myaudiocast.restClient;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface MyaudiocastAPI {
    @GET("/podcast/?format=json")
    void fetchPodcast(@Query("page") int page, Callback<PodcastsResponse> callback);

    @GET("/episode/?format=json")
    void fetchEpisodesByPodcastId(@Query("podcast") String podcastId, Callback<EpisodesResponse> callback);
}
