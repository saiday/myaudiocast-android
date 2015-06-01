package saiday.myaudaiocast.myaudiocast.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import saiday.myaudaiocast.myaudiocast.R;

/**
 * Created by saiday on 15/5/31.
 */
public class PodcastViewHolder extends RecyclerView.ViewHolder {
    public View mView;
    public TextView mTextView;
    public ImageView mImageView;
    public PodcastViewHolder(View v) {
        super(v);
        mView = v;
        mTextView = (TextView) v.findViewById(R.id.text_view);
        mImageView = (ImageView) v.findViewById(R.id.image_view);
    }
}
