package com.walmartlabs.agupta.gridimagesearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.walmartlabs.agupta.gridimagesearch.R;
import com.walmartlabs.agupta.gridimagesearch.adapters.ImageResultArrayAdapter;
import com.walmartlabs.agupta.gridimagesearch.listner.EndlessScrollListener;
import com.walmartlabs.agupta.gridimagesearch.models.ImageResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ImageSearchActivity extends AppCompatActivity {
    private String size;
    private String color;
    private String type;
    private String site;
    private final int REQUEST_CODE = 20;
    private EditText etQuery;
    private GridView gvResults;
    ArrayList<ImageResult> imageResults;
    ImageResultArrayAdapter imageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);
        setupViews();
        //ActionBar actionBar = getActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.image_search_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(getApplicationContext(), FilterActivity.class);
        i.putExtra("type", type);
        i.putExtra("color", color);
        i.putExtra("type", type);
        i.putExtra("site", site);
        startActivityForResult(i, REQUEST_CODE);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            color = data.getExtras().getString("color");
            type = data.getExtras().getString("type");
            size = data.getExtras().getString("size");
            site = data.getExtras().getString("site");
            searchWithParams(0, true);
            imageAdapter.notifyDataSetChanged();
        }

    }

    public void searchWithParams(int offset, final boolean isNew) {
        String query = etQuery.getText().toString();
        AsyncHttpClient client = new AsyncHttpClient();
        String extras = "&start=" + offset;
        if (color != null && !color.equals("any")) {
            extras += "&imgcolor=" + color;
        }
        if (type != null && !type.equals("any")) {
            extras += "&imgtype=" + type;
        }
        if (size != null && !size.equals("any")) {
            extras += "&imgsz=" + size;
        }
        if (site != null && !site.equals("")) {
            extras += "&as_sitesearch=" + site;
        }
        String searchURL = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query + "&rsz=8" + extras;
        System.out.println(searchURL);
        client.get(searchURL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray imageResultsJSON = null;
                try {
                    imageResultsJSON = response.getJSONObject("responseData").getJSONArray("results");
                    if (isNew) {
                        imageResults.clear(); // clear only on initial search for pagination
                    }
                    imageAdapter.addAll(ImageResult.fromJSONArray(imageResultsJSON));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setupViews(){
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResult);
        imageResults = new ArrayList<>();
        imageAdapter = new ImageResultArrayAdapter(this, imageResults);
        gvResults.setAdapter(imageAdapter);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
                ImageResult imageResult1 = imageResults.get(position);
                i.putExtra("result", imageResult1);
                startActivity(i);
            }
        });
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                loadMoreData(totalItemsCount);
                return true;
            }
        });
    }


    public void loadMoreData(int offset){
        String query = etQuery.getText().toString();
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Accept-Encoding", "identity");
        client.get("https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&q=" + query + "&start=" + offset, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray imageJsonResults;
                try {
                    imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
                    //imageAdapter.clear();
                    imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
                    Log.d("debug", imageJsonResults.toString());
                    //imageAdapter.notifyDataSetChanged();
                } catch (JSONException e){
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });


    }

    public void onClickSearch(View v){
        String query = etQuery.getText().toString();
        //Toast.makeText(this, "Search for : " + query, Toast.LENGTH_SHORT).show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Accept-Encoding", "identity");
        client.get("https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&q=" + query, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray imageJsonResults;
                try {
                    imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
                    imageAdapter.clear();
                    imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
                    Log.d("debug", imageJsonResults.toString());
                    //imageAdapter.notifyDataSetChanged();
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
