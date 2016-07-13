package com.spr.whoshomework;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;

public class ImageSearchActivity extends AppCompatActivity {
    private Menu mMenu;
    private String TAG = "Spr";

    private String PIXABAYURL = "https://pixabay.com/api/?key=2906241-7999c5df50f7c9de92cce050c&q=%s&image_type=photo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);

    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.searchbar, menu);

        this.mMenu = menu;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();
            search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    s = s.replace(" " , "+");
                    Log.d(TAG, "keyword = " + s);
                    String output = String.format(PIXABAYURL, s);

                    Log.d(TAG,"submit url  =  " + output);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    return true;
                }

            });

        }

        return true;

    }
}
