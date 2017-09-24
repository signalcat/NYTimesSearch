package com.codepath.nytimessearch.activities;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Parcel;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SearchView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.nytimessearch.SpacesItemDecoration;
import com.codepath.nytimessearch.listeners.EndlessRecyclerViewScrollListener;
import com.codepath.nytimessearch.fragments.FilterDialogFragment;
import com.codepath.nytimessearch.myclass.Article;
import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.adapters.RecyclerViewAdapter;
import com.codepath.nytimessearch.myclass.Filters;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity
    implements FilterDialogFragment.OnFilterSearchListener {

    String searchQuery;
    //EditText etQuery;
    //GridView gvResults;
    RecyclerView rvArticles;
    Button btnSearch;

    ArrayList<Article> articles;
    //ArticleArrayAdapter adapter;
    RecyclerViewAdapter adapter;

    // Initialize the filter object
    Filters filter = new Filters();

    // Search query strings
    ArrayList<String> categories = new ArrayList<>();

    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();
        checkNetWork();
        // Lookup the recyclerview in activity layout
        rvArticles = (RecyclerView) findViewById(R.id.rvArticles);
        // Create adapter passing the data
        adapter = new RecyclerViewAdapter(this, articles);
        // Attach a click handler to the adapter
        adapter.setOnItemClickListener((itemView, position) -> {
//
//                // Create an intent to display the article
//                Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
//                // Get the article to display
//                Article article = articles.get(position);
//                // Pass in the article into intent
//                i.putExtra("article", article);
//                // Launch the activity
//                startActivity(i);

            // Extract the url
            Article article = articles.get(position);
            //Article article = (Article) getIntent().getSerializableExtra("article");
            String url = article.getWebUrl();

            // Create te bitmap for the sharing icon
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_sharing);
            // Create the intent
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, url);
            // Create a pending intent to wake up the app
            int requestCode = 100;
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            // Use a CustomTabsIntent.Builder to configure CustomTabsIntent.
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            // set toolbar color
            builder.setToolbarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            // add share action to menu list
            builder.addDefaultShareMenuItem();
            // Map the bitmap, text, and pending intent to this icon
            // Set tint to be true so it matches the toolbar color
            builder.setActionButton(bitmap, "Share Link", pendingIntent, true);
            // Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
            CustomTabsIntent customTabsIntent = builder.build();

            // and launch the desired Url with CustomTabsIntent.launchUrl()
            customTabsIntent.launchUrl(getApplicationContext(), Uri.parse(url));

        });

        // Initialize articles
        rvArticles.setAdapter(adapter);

        // Decor the recycler view
        SpacesItemDecoration itemDecoration = new SpacesItemDecoration(1);
        rvArticles.addItemDecoration(itemDecoration);

        // Set layout manager to position the items
        // Display items in staggered grid
        // para: number of columns, orientation
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvArticles.setLayoutManager(gridLayoutManager);

        // Retain an instance
        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Load data process
                loadNextDataFromApi(searchQuery,page);
            }
        };
        // Adds the scroll listener to recyclerview
        rvArticles.addOnScrollListener(scrollListener);
    }

    public void setupViews() {
        //etQuery = (EditText) findViewById(R.id.etQuery);
        //gvResults = (GridView) findViewById(R.id.gvResults);
        rvArticles = (RecyclerView) findViewById(R.id.rvArticles);
        //btnSearch = (Button) findViewById(R.id.btnSearch);
        articles = new ArrayList<>();

        final Calendar c = Calendar.getInstance();
        String currentDate = Integer.toString(c.get(Calendar.YEAR)) + Integer.toString(c.get(Calendar.MONTH))
                + Integer.toString(c.get(Calendar.DAY_OF_MONTH));
        // Get the beginDate here from the calendar parsed to correct format
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String urlDate = format.format(c.getTime());
        filter.setDate(urlDate);
        //adapter = new ArticleArrayAdapter(this, articles);
        //gvResults.setAdapter(adapter);
        // hook up listener for recycler view click
//        rvArticles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // Create an intent to display the article
//                Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
//                // Get the article to display
//                Article article = articles.get(position);
//                // Pass in the article into intent
//                i.putExtra("article", article);
//                // Launch the activity
//                startActivity(i);
//            }
//        });
    }

    public void checkNetWork() {
        if (isNetworkAvailable()) {
            Toast.makeText(getApplicationContext(),"Connected to Internet",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(),"Network unavailable",Toast.LENGTH_LONG).show();
        }
    }

    public Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_article; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                searchQuery = query;
                onArticleSearch(query);

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // Show filters dialog
            FragmentManager fm = getSupportFragmentManager();
            FilterDialogFragment filterDialogFragment = FilterDialogFragment.newInstance(Parcels.wrap(filter));
            filterDialogFragment.show(fm, "fragment_filters");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onArticleSearch(String query) {
        if (!isNetworkAvailable()) {
            Toast.makeText(getApplicationContext(),"Network unavailable. Check your connection.",Toast.LENGTH_LONG).show();
        }

        // Clear the array of data
        articles.clear();
        // Notify the adapter of the update
        adapter.notifyDataSetChanged();
        // 3. Reset endless scroll listener when performing a new search
        scrollListener.resetState();

        // search
//        String query = etQuery.getText().toString();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams params = new RequestParams();
        params.put("api-key","b006b8bc57b343a490fa8ecaf1e04c97");
        params.put("page", 0);
        params.put("q", query);
        params.put("begin_date", filter.getDate());
        params.put("sort", filter.getOrder());

        StringBuilder builder = new StringBuilder();
        builder.append("");
        if (!categories.isEmpty()) {
            builder.append(categories.get(0));
            for ( int i = 1; i < categories.size(); i++) {
                if (builder.length() == 0) {
                    builder.append(categories.get(i));
                } else {
                    builder.append("%20" + categories.get(i));
                }
            }
            params.put("fq", "news_desk:(" + builder.toString() + ")");
        }

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());

                // Parse JSON object
                JSONArray articalJsonRsesults = null;

                try {
                    articalJsonRsesults = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(articalJsonRsesults));
                    adapter.notifyDataSetChanged();
                    Log.d("DEBUG", articles.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("Failed: ", "" + statusCode);
                Log.d("Error : ", "" + throwable);
            }
        });

    }

    public void loadNextDataFromApi(String query, int offset) {
//        String query = etQuery.getText().toString();
        // Send an API request to retrieve appropriate paginated data
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams params = new RequestParams();
        params.put("api-key","b006b8bc57b343a490fa8ecaf1e04c97");
        params.put("page", offset);
        params.put("q", query);
        params.put("begin_date", filter.getDate());
        params.put("sort", filter.getOrder());

        StringBuilder builder = new StringBuilder();
        builder.append("");
        if (!categories.isEmpty()) {
            builder.append(categories.get(0));
            for ( int i = 1; i < categories.size(); i++) {
                if (builder.length() == 0) {
                    builder.append(categories.get(i));
                } else {
                    builder.append("%20" + categories.get(i));
                }
            }
            params.put("fq", "news_desk:(" + builder.toString() + ")");
        }

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());

                // Parse JSON object
                JSONArray articalJsonRsesults = null;

                try {
                    articalJsonRsesults = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(articalJsonRsesults));
                    adapter.notifyItemRangeInserted(adapter.getItemCount(), 10);
                    Log.d("DEBUG", articles.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("Failed: ", "" + statusCode);
                Log.d("Error : ", "" + throwable);
            }
        });
    }

    @Override
    public void onUpdateFilters(Filters filter) {
        // 1. Access the updated filters here and store them in member variable
        String newDate = filter.getDate();
        String newOrder = filter.getOrder();
        // 2. Initiate a fresh search with these filters updated and same query value
        categories = new ArrayList<>();

        if (filter.isArt()) {
            categories.add("\"Art\"") ;
        }
        if (filter.isFashion()) {
            categories.add("\"Fashion\"");
        }
        if (filter.isSport()) {
            categories.add("\"Sport\"");
        }
    }
}
