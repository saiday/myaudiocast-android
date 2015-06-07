package saiday.myaudaiocast.myaudiocast.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import saiday.myaudaiocast.myaudiocast.R;
import saiday.myaudaiocast.myaudiocast.entity.Podcast;
import saiday.myaudaiocast.myaudiocast.ui.viewholder.PodcastViewHolder;

/**
 * Created by saiday on 15/5/23.
 */
public class PodcastAdapter extends RecyclerView.Adapter<PodcastViewHolder> {
    private ArrayList<Podcast> mDataset;
    private Context mContext;

    public PodcastAdapter(Context context, ArrayList<Podcast> myDataset) {
        mContext = context;
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PodcastViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.podcast_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        PodcastViewHolder vh = new PodcastViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final PodcastViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Podcast podcast = mDataset.get(position);
        holder.mTextView.setText(podcast.title);
        Picasso.with(mContext).load(podcast.cover).resize(100, 100).placeholder(R.drawable.podcast_placeholder).into(holder.mImageView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public Podcast getPodcast(int position) {
        return mDataset.get(position);
    }
}
