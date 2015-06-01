package saiday.myaudaiocast.myaudiocast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import saiday.myaudaiocast.myaudiocast.entity.Episode;
import saiday.myaudaiocast.myaudiocast.entity.Podcast;
import saiday.myaudaiocast.myaudiocast.ui.RecyclerItemClickListener;
import saiday.myaudaiocast.myaudiocast.ui.viewholder.EpisodeViewHolder;

public class PodcastDetailActivity extends Activity {
    private Podcast mPodcast;
    private RecyclerView mRecyclerView;
    private ParallaxRecyclerAdapter<Episode> mAdapter;
    private ArrayList<Episode> mEpisodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast_detail);

        mPodcast = (Podcast) getIntent().getExtras().getSerializable("podcast");

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mEpisodes = new ArrayList<Episode>();
        Episode fake1 = new Episode();
        fake1.title = "fake1";
        fake1.description = "faka ff";

        Episode fake2 = new Episode();
        fake2.title = "fake2";
        fake2.description = "faka ffff";

        mEpisodes.add(fake1);
        mEpisodes.add(fake2);
        mEpisodes.add(fake1);
        mEpisodes.add(fake2);
        mEpisodes.add(fake1);
        mEpisodes.add(fake2);
        mEpisodes.add(fake1);
        mEpisodes.add(fake2);
        mEpisodes.add(fake1);
        mEpisodes.add(fake2);
        mEpisodes.add(fake1);
        mEpisodes.add(fake2);
        mEpisodes.add(fake1);
        mEpisodes.add(fake2);

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
        Picasso.with(this).load(mPodcast.cover).into(imageView);
        mAdapter.setParallaxHeader(headerView, mRecyclerView);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.d("test:", "click" + position);
                    }
                })
        );
        mRecyclerView.setAdapter(mAdapter);
    }
}
