package com.spr.whoshomework;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class ImageSearchActivity extends AppCompatActivity {
    private Menu mMenu;
    private String TAG = "Spr";

    private String BASE_URL = "https://pixabay.com/";
    // sample https://pixabay.com/api/?key=2906241-7999c5df50f7c9de92cce050c&q=cat&image_type=photo

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
                public boolean onQueryTextSubmit(String text) {
                    text = text.replace(" " , "+");
                    Log.d(TAG, "keyword = " + text);
                    getJson(text);
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

    private void getJson(String keyword) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PixabayService service = retrofit.create(PixabayService.class);
        Call<Pixabay> call = service.pixabays(keyword);

        call.enqueue(new Callback<Pixabay>() {
            @Override
            public void onResponse(Call<Pixabay> call, Response<Pixabay> response) {
                Pixabay pixabay= response.body();
                int responseCode = response.code();
                Log.d(TAG,"response code = " + responseCode);
                if(responseCode == 200) {
                    List<Pixabay.PixabayItem> items = pixabay.hits;
                    for(Pixabay.PixabayItem item:items) {
                        Log.d(TAG,"item " + item.webformatURL);
                    }

                }
            }

            @Override
            public void onFailure(Call<Pixabay> call, Throwable t) {
                Log.d(TAG,"get data fail");
            }
        });


    }


    public interface PixabayService {
        @POST("api/?key=2906241-7999c5df50f7c9de92cce050c&image_type=photo")
        Call<Pixabay> pixabays(
                @Query("q") String keyword);
    }

    public class Pixabay {

        String totalHits;
        String total;

        @Override
        public String toString() {
            return(totalHits);
        }

        List<PixabayItem> hits;


        public class PixabayItem {
            String webformatURL;
            int webformatWidth;
            int webformatHeight;

        }


    }


}
