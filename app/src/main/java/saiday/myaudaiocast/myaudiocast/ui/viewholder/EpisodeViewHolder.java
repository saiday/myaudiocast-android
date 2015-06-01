package saiday.myaudaiocast.myaudiocast.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import saiday.myaudaiocast.myaudiocast.R;
import saiday.myaudaiocast.myaudiocast.entity.Episode;

/**
 * Created by saiday on 15/5/31.
 */
public class EpisodeViewHolder extends RecyclerView.ViewHolder {
    public View mView;
    public TextView mTitleView;
    public TextView mDetailTitleView;
    public TextView mLengthView;

    public EpisodeViewHolder(View itemView) {
        super(itemView);

        mView = itemView;
        mTitleView = (TextView) itemView.findViewById(R.id.title);
        mDetailTitleView = (TextView) itemView.findViewById(R.id.detail_title);
        mLengthView = (TextView) itemView.findViewById(R.id.length);
    }

    public void setEpisode(Episode episode) {
        mTitleView.setText(episode.title);
        mDetailTitleView.setText(episode.description);
    }
}
