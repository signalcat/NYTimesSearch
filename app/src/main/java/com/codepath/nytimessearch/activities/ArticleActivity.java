package com.codepath.nytimessearch.activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.codepath.nytimessearch.myclass.Article;
import com.codepath.nytimessearch.R;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_article);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // Extract the url
        Article article = (Article) getIntent().getSerializableExtra("article");
        String url = article.getWebUrl();

        // Create te bitmap for the sharing icon
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_sharing);
        // Create the intent
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, url);
        // Create a pending intent to wake up the app
        int requestCode = 100;
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Use a CustomTabsIntent.Builder to configure CustomTabsIntent.
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        // set toolbar color
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorAccent));
        // add share action to menu list
        builder.addDefaultShareMenuItem();
        // Map the bitmap, text, and pending intent to this icon
        // Set tint to be true so it matches the toolbar color
        builder.setActionButton(bitmap, "Share Link", pendingIntent, true);
        // Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
        CustomTabsIntent customTabsIntent = builder.build();

        // and launch the desired Url with CustomTabsIntent.launchUrl()
        customTabsIntent.launchUrl(this, Uri.parse(url));

//        WebView webView = (WebView) findViewById(R.id.wvArticle);
//        // Create web view client to show the webpage embedded in the app
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//        });
//        webView.loadUrl(article.getWebUrl());

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate menu resource file.
//        getMenuInflater().inflate(R.menu.menu_article, menu);
//        // Locate MenuItem with ShareActionProvider
//        MenuItem item = menu.findItem(R.id.menu_item_share);
//        // Fetch reference to the share action provider
//        ShareActionProvider miShare = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareIntent.setType("text/plain");
//        // get reference to WebView
//        WebView wvArticle = (WebView) findViewById(R.id.wvArticle);
//        // pass in the URL currently being used by the WebView
//        shareIntent.putExtra(Intent.EXTRA_TEXT, wvArticle.getUrl());
//        miShare.setShareIntent(shareIntent);
//        return super.onCreateOptionsMenu(menu);
//    }
}
