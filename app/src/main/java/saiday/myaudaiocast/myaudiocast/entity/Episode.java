package saiday.myaudaiocast.myaudiocast.entity;

import java.util.ArrayList;
import java.util.Date;

public class Episode {
    public interface CreateEpisodesHandler {
        void onCreateEpisodesSuccess(ArrayList<Episode> episodes);
        void onCreateEpisodesFailed();
    }

    public String id;
    public String title;
    public String description;
    public String file;
    public String ia_file;
    public int play_count;
    public String old_id;
    public int comments_count;
    public boolean is_synced;
    public boolean is_public;
    public boolean enable;
    public Date created_at;
    public Date last_modified;
    public String user;
    public String podcast;
}
