package com.codepath.nytimessearch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.nytimessearch.myclass.Article;
import com.codepath.nytimessearch.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by hezhang on 9/19/17.
 */
// Basic recycler adapter.
// Specify the custom ViewHolder to access our views
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    // TAG the article is text only or not
    private final int TEXTONLY = 1;
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

    public class ViewHolderTextOnly extends RecyclerView.ViewHolder {
        private TextView tvTextOnly;

        public ViewHolderTextOnly(final View v) {
            super(v);
            tvTextOnly = (TextView) v.findViewById(R.id.tvTextOnly);
            // Setup the click listener
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(v, position);
                        }
                    }
                }
            });
        }

        public TextView getTextView() {
            return tvTextOnly;
        }
        public void setTextView(TextView tvTextOnly) {
            this.tvTextOnly = tvTextOnly;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == TEXTONLY) {
            View textOnlyView = inflater.inflate(R.layout.item_article_text_only, parent, false);
            viewHolder = new ViewHolderTextOnly(textOnlyView);
        } else {
            // Inflate the custom layout
            View articleView = inflater.inflate(R.layout.item_article_result, parent, false);
            // Return a new holder instance
            viewHolder = new ViewHolder(articleView);
        }
        return viewHolder;
    }

    // Populate data into the item through holder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

          // Without heterogenous:
//        // Get the data model based on position
//        Article article = mArticles.get(position);
//        // Set item views based on views and data model
//        TextView textView = viewHolder.tvTitle;
//        textView.setText(article.getHeadline());
//        ImageView imageView = viewHolder.ivThumbnails;
//        // Populate the thumbnail images
//        // Remote download the image in the background
//        String thumbnail = article.getThumbNail();
//        if (!TextUtils.isEmpty(thumbnail)) {
//            Picasso.with(getContext()).load(thumbnail).into(imageView);
//        }
        if (viewHolder.getItemViewType() == TEXTONLY) {
            ViewHolderTextOnly vhTextOnly = (ViewHolderTextOnly) viewHolder;
            configureViewHolderTextOnly(vhTextOnly, position);
        } else {
            ViewHolder vh = (ViewHolder) viewHolder;
            configureDefaultViewHolder(vh, position);
        }

    }

    // configure the individual viewholder objects
    private void configureDefaultViewHolder(ViewHolder vh, int position) {
        Article article = mArticles.get(position);
        // Set item views based on views and data model
        TextView textView = vh.tvTitle;
        textView.setText(article.getHeadline());
        ImageView imageView = vh.ivThumbnails;
        // Populate the thumbnail images
        // Remote download the image in the background
        String thumbnail = article.getThumbNail();
        Picasso.with(getContext()).load(thumbnail).into(imageView);

    }

    private void configureViewHolderTextOnly(ViewHolderTextOnly vhTextOnly, int position) {
        Article article = mArticles.get(position);
        if (article != null) {
            TextView tvTextOnly = vhTextOnly.getTextView();
            tvTextOnly.setText(article.getHeadline());
        }
    }


    // Return the size of dataset
    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    @Override
    public int getItemViewType(int position) {
        // Check if there is image or not
        if (mArticles.get(position).getThumbNail().isEmpty()) {
            return TEXTONLY;
        }
        return -1;
    }
}
