package saiday.myaudaiocast.myaudiocast;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.ArrayList;
import java.util.List;

import saiday.myaudaiocast.myaudiocast.entity.Podcast;
import saiday.myaudaiocast.myaudiocast.restClient.HttpClient;
import saiday.myaudaiocast.myaudiocast.ui.RecyclerItemClickListener;
import saiday.myaudaiocast.myaudiocast.ui.adapter.PodcastAdapter;


public class MainActivity extends ActionBarActivity implements Podcast.CreatePodcastsHandler {
    private SuperRecyclerView mRecyclerView;
    private PodcastAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private HttpClient mClient;
    private ArrayList<Podcast> mPodcasts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (SuperRecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever
                        Log.d("test", mAdapter.getPodcast(position).title);
                    }
                })
        );

        mRecyclerView.setupMoreListener(new OnMoreListener() {
            @Override
            public void onMoreAsked(int i, int i1, int i2) {
                getPodcasts();
            }
        }, 5);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mPodcasts = new ArrayList<Podcast>();
        mAdapter = new PodcastAdapter(this, mPodcasts);
        mRecyclerView.setAdapter(mAdapter);

        mClient = new HttpClient(this);
        getPodcasts();
    }

    private void getPodcasts() {
        mClient.getPodcasts(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreatePodcastsSuccess(ArrayList<Podcast> podcasts) {
        mPodcasts.addAll(podcasts);
        mRecyclerView.hideMoreProgress();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreatePodcastsFailed() {
        Log.d("log:", "fail");
    }
}
