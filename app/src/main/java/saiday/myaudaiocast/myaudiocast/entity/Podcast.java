package saiday.myaudaiocast.myaudiocast.entity;

import java.util.ArrayList;

public class Podcast {
    public interface CreatePodcastsHandler {
        void onCreatePodcastsSuccess(ArrayList<Podcast> podcasts);
        void onCreatePodcastsFailed();
    }

    public String id;
    public String title;
    public String cover;
}
