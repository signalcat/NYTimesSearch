package com.codepath.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.codepath.nytimessearch.Article;
import com.codepath.nytimessearch.adapters.ArticleArrayAdapter;
import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.adapters.RecyclerViewAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    EditText etQuery;
    //GridView gvResults;
    RecyclerView rvArticles;
    Button btnSearch;

    ArrayList<Article> articles;
    //ArticleArrayAdapter adapter;
    RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();

        // Lookup the recyclerview in activity layout
        rvArticles = (RecyclerView) findViewById(R.id.rvArticles);
        // Create adapter passing the data
        adapter = new RecyclerViewAdapter(this, articles);
        // Attach a click handler to the adapter
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                // Create an intent to display the article
                Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
                // Get the article to display
                Article article = articles.get(position);
                // Pass in the article into intent
                i.putExtra("article", article);
                // Launch the activity
                startActivity(i);
            }
        });
        // Initialize articles
        rvArticles.setAdapter(adapter);
        // Set layout manager to position the items
        rvArticles.setLayoutManager(new GridLayoutManager(this, 4));
    }

    public void setupViews() {
        etQuery = (EditText) findViewById(R.id.etQuery);
        //gvResults = (GridView) findViewById(R.id.gvResults);
        rvArticles = (RecyclerView) findViewById(R.id.rvArticles);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        articles = new ArrayList<>();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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

    public void onArticleSearch(View view) {
        String query = etQuery.getText().toString();
//        Toast.makeText(this, "Searching for" + query, Toast.LENGTH_LONG).show();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams params = new RequestParams();
        params.put("api-key","b006b8bc57b343a490fa8ecaf1e04c97");
        params.put("page", 0);
        params.put("q", query);

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
        });

    }
}
