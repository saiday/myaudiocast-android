package saiday.myaudaiocast.myaudiocast;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.poliveira.parallaxrecycleradapter.ParallaxRecyclerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import saiday.myaudaiocast.myaudiocast.audio.AudioPlayer;
import saiday.myaudaiocast.myaudiocast.entity.Episode;
import saiday.myaudaiocast.myaudiocast.entity.Podcast;
import saiday.myaudaiocast.myaudiocast.restClient.HttpClient;
import saiday.myaudaiocast.myaudiocast.ui.RecyclerItemClickListener;
import saiday.myaudaiocast.myaudiocast.ui.viewholder.EpisodeViewHolder;

public class PodcastDetailActivity extends Activity implements Episode.CreateEpisodesHandler {
    private Podcast mPodcast;
    private RecyclerView mRecyclerView;
    private ParallaxRecyclerAdapter<Episode> mAdapter;
    private ArrayList<Episode> mEpisodes;
    private HttpClient mClient;
    private AudioPlayer mAudioPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast_detail);

        mPodcast = (Podcast) getIntent().getExtras().getSerializable("podcast");
        mAudioPlayer = ((MyaudiocastApplication) getApplication()).getAudioPlayer();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mEpisodes = new ArrayList<Episode>();
        mAdapter = new ParallaxRecyclerAdapter<>(mEpisodes);

        mAdapter.implementRecyclerAdapterMethods(new ParallaxRecyclerAdapter.RecyclerAdapterMethods() {
            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
                ((EpisodeViewHolder) viewHolder).setEpisode(mEpisodes.get(i));
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                return new EpisodeViewHolder(getLayoutInflater()
                        .inflate(R.layout.episode_item, viewGroup, false));
            }

            @Override
            public int getItemCount() {
                return mEpisodes.size();
            }
        });

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View headerView = inflater.inflate(R.layout.podcast_detail_header, null);
        ImageView imageView = (ImageView) headerView.findViewById(R.id.image_view);
        if (mPodcast.cover != null) {
            Picasso.with(this).load(mPodcast.cover).into(imageView);
        } else {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.placeholder));
        }

        mAdapter.setParallaxHeader(headerView, mRecyclerView);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.d("test:", "click" + position);
                        mAudioPlayer.setAudioSource(mEpisodes);
                    }
                })
        );
        mRecyclerView.setAdapter(mAdapter);

        mClient = new HttpClient(this);
        getEpisodesFromPodcast(mPodcast);
    }

    private void getEpisodesFromPodcast(Podcast podcast) {
        mClient.getEpisodes(podcast, this);
    }

    @Override
    public  void onCreateEpisodesSuccess(ArrayList<Episode> episodes) {
        mEpisodes = episodes;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateEpisodesFailed() {

    }
}
