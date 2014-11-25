package com.codepath.gridimagesearch.activities;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;

import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.adapters.EndlessScrollListener;
import com.codepath.gridimagesearch.adapters.ImageResultsAdapter;
import com.codepath.gridimagesearch.models.ImageResult;
import com.codepath.gridimagesearch.models.SearchSetting;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {

    private EditText etQuery;
    private GridView gvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;
    private SearchSetting searchSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        handleIntent(getIntent());
        setupViews();
        imageResults = new ArrayList<ImageResult>();
        aImageResults = new ImageResultsAdapter(this, imageResults);
        gvResults.setAdapter(aImageResults);
        searchSetting = new SearchSetting();
        
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d("INFO", "page="+page+", total count:" + totalItemsCount);
                doImageSearch(searchSetting);
                //googleImageClient.requestNewImage(currentSearch, 8, totalItemsCount);
            }
        });
    }

    private void setupViews() {
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        gvResults.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
                ImageResult result = imageResults.get(position);
//                i.putExtra("url", result.fullUrl);
                i.putExtra("result", result);
                startActivity(i);
            }
            
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        /*
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
               (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        */
        return true;
    }

    public void onImageSearch(View v) {
        // responseData -> results -> [x] => title
        // responseData -> results -> [x] => tbUrl
        // responseData -> results -> [x] => url
        // responseData -> results -> [x] => width
        // responseData -> results -> [x] => height
        String query = etQuery.getText().toString();
        // Toast.makeText(this, "Search for: " + query, Toast.LENGTH_SHORT).show();
        
        searchSetting.keyWord = query;        
        doImageSearch(searchSetting);
    }
    
    public void doImageSearch(SearchSetting setting) {
        // responseData -> results -> [x] => title
        // responseData -> results -> [x] => tbUrl
        // responseData -> results -> [x] => url
        // responseData -> results -> [x] => width
        // responseData -> results -> [x] => height
        // Toast.makeText(this, "Search for: " + query, Toast.LENGTH_SHORT).show();
        
        AsyncHttpClient client = new AsyncHttpClient();
        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&q="
                + setting.keyWord;
        if (setting.colorFilter != null && !setting.colorFilter.isEmpty()) {
            searchUrl += "&imgcolor=" + setting.colorFilter;    
        }
        if (setting.imageSize != null && !setting.imageSize.isEmpty()) {
            searchUrl += "&imgsz=" + setting.imageSize;    
        }
        if (setting.imageType != null && !setting.imageType.isEmpty()) {
            searchUrl += "&imgtype=" + setting.imageType;    
        }
        if (setting.siteFilter != null && !setting.siteFilter.isEmpty()) {
            searchUrl += "&as_sitesearch=" + setting.siteFilter;    
        }
        Log.i("INFO", searchUrl.toString());
        
        client.get(searchUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
                //Log.d("DEBUG", response.toString());
                JSONArray imageResultsJson = null;
                try {
                    imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    imageResults.clear();
                    aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
                    aImageResults.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Log.i("INFO", imageResults.toString());
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void handleIntent(Intent intent) {
        Log.i("INFO", "## handle intent start action=" + intent.getAction());

//        SearchSetting imageQuery = null;
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            Log.i("INFO", "## handle intent image query");
//            imageQuery = (SearchSetting) intent.getSerializableExtra("image_query");
//            doImageSearch(imageQuery);
//            return ;
//        }
          if (intent.getAction() == null) {
              searchSetting = (SearchSetting) intent.getSerializableExtra("image_query");
              Log.i("INFO", "## handle intent setting=");
              Log.i("INFO", "## filterSite=" + searchSetting.siteFilter);

//              searchSetting.colorFilter = setting.colorFilter;
//              searchSetting.imageSize = setting.imageSize;
//              searchSetting.imageType = setting.imageType;
//              searchSetting.siteFilter = setting.siteFilter;
              return;
          }
        //SearchSetting imageQuery = (SearchSetting) intent.getSerializableExtra("image_query");
//        searchSetting = (SearchSetting) intent.getSerializableExtra("image_query");
//        if (searchSetting != null) {
//            doImageSearch(searchSetting);
//        }
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        Log.i("INFO", "## on new intent");
        handleIntent(intent);
    }
}
