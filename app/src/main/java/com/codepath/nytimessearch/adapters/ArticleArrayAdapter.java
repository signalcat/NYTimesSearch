package com.codepath.nytimessearch.adapters;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.nytimessearch.Article;
import com.codepath.nytimessearch.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by hezhang on 9/18/17.
 */

public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for position
        Article article = this.getItem(position);

        // Check if existing view is being reused/recycled
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article_result, parent, false);
        }
        // Not using a recycled view, inflate the layout
        // Find the image view
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivImage);
        // Clear out the recycled images
        imageView.setImageResource(0);

        // Populate the headlines
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(article.getHeadline());

        // Populate the thumbnail images
        // Remote download the image in the background
        String thumbnail = article.getThumbNail();
        if (!TextUtils.isEmpty(thumbnail)) {
            Picasso.with(getContext()).load(thumbnail).into(imageView);
        }

        return convertView;
    }
}
