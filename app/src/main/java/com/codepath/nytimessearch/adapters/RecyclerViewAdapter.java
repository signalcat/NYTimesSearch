package com.codepath.nytimessearch.adapters;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.nytimessearch.Article;
import com.codepath.nytimessearch.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by hezhang on 9/19/17.
 */
// Basic recycler adapter.
// Specify the custom ViewHolder to access our views
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{

    // Create OnItemClickListener
    // Define listener member variable
    private OnItemClickListener listener;
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Define the view holder
    // A direct reference to each views in a data item
    // Cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivThumbnails;
        public TextView tvTitle;

        // a constructor accepts the entire item row and does view lookups to find each subview
        public ViewHolder(final View itemView) {
            // Stores the itemView in a public final member variable
            // can be used to access the context from any ViewHolder instance
            super(itemView);
            ivThumbnails = (ImageView) itemView.findViewById(R.id.ivImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            // Setup the click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }
    }

    // Store a number variable for the articles
    private List<Article> mArticles;
    // Store the context for easy access
    private Context mContext;

    // Pass in the article array into the constructor
    public RecyclerViewAdapter(Context context, List<Article> articles) {
        mArticles = articles;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    // Inflate a layout xml and return the holder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View articleView = inflater.inflate(R.layout.item_article_result, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(articleView);
        return viewHolder;
    }

    // Populate data into the item through holder
    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Article article = mArticles.get(position);
        // Set item views based on views and data model
        TextView textView = viewHolder.tvTitle;
        textView.setText(article.getHeadline());
        ImageView imageView = viewHolder.ivThumbnails;
        // Populate the thumbnail images
        // Remote download the image in the background
        String thumbnail = article.getThumbNail();
        if (!TextUtils.isEmpty(thumbnail)) {
            Picasso.with(getContext()).load(thumbnail).into(imageView);
        }
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }
}
