package com.spr.whoshomework;

import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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

    private ImageAdapter mImageAdapter;
    private RecyclerAdapter mRecyclerAdapter;
    private List<String> mImageList = new ArrayList<>();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getBaseContext();
        setContentView(R.layout.activity_image_search);
        mImageAdapter = new ImageAdapter(this, mImageList);
        mRecyclerAdapter = new RecyclerAdapter(this , mImageList);

        FragmentTabHost tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        tabHost.addTab(tabHost.newTabSpec("Grid")
                        .setIndicator("Grid"),
                GridFragment.class,
                null);

        tabHost.addTab(tabHost.newTabSpec("List")
                        .setIndicator("List"),
                ListFragment.class,
                null);
        tabHost.addTab(tabHost.newTabSpec("StaggeredGrid")
                        .setIndicator("StaggeredGrid"),
                StaggeredGridFragment.class,
                null);
    }

    public ImageAdapter getImageAdapter() {
        return mImageAdapter;
    }

    public RecyclerAdapter getRecyclerAdapter() {
        return mRecyclerAdapter;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchbar, menu);
        this.mMenu = menu;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();
            search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String text) {
                    text = text.replace(" ", "+");
                    Log.d(TAG, "keyword = " + text);
                    getJson(text);
                    String toast = getString(R.string.toast_search_keyword) + text;
                    Toast.makeText(mContext, toast,Toast.LENGTH_SHORT).show();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    return true;
                }

            });

        }

        return super.onPrepareOptionsMenu(menu);

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
                Pixabay pixabay = response.body();
                int responseCode = response.code();
                Log.d(TAG, "response code = " + responseCode);
                if (responseCode == 200) {
                    List<Pixabay.PixabayItem> items = pixabay.hits;
                    mImageList.clear();
                    for (Pixabay.PixabayItem item : items) {
                        Log.d(TAG, "item " + item.webformatURL);
                        mImageList.add(item.webformatURL);
                    }
                    mImageAdapter.updateImageList(mImageList);
                    mImageAdapter.notifyDataSetChanged();

                    mRecyclerAdapter.updateImageList(mImageList);
                    mRecyclerAdapter.notifyDataSetChanged();
                    String toast = getString(R.string.toast_success) + mImageList.size();
                    Toast.makeText(mContext, toast,Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "response success and item size  = " + mImageList.size());
                } else {
                    String toast = getString(R.string.toast_fail_response_code) + responseCode;
                    Toast.makeText(mContext, toast,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Pixabay> call, Throwable t) {
                String toast = getString(R.string.toast_fail_no_response);
                Toast.makeText(mContext, toast,Toast.LENGTH_SHORT).show();
                Log.d(TAG, "get data fail");
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
            return (totalHits);
        }

        List<PixabayItem> hits;


        public class PixabayItem {
            String webformatURL;
            int webformatWidth;
            int webformatHeight;

        }
    }

    private class ImageAdapter extends BaseAdapter {
        private Context mContext;
        List<String> mImageList = new ArrayList<>();
        private LayoutInflater inflater;


        public ImageAdapter(Context context, List<String> imageList) {
            mContext = context;
            mImageList = imageList;

            inflater = LayoutInflater.from(context);
        }

        public void updateImageList(List<String> imageList) {
            mImageList = imageList;
        }

        @Override
        public int getCount() {
            return mImageList.size();
        }

        @Override
        public Object getItem(int i) {
            return mImageList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = inflater.inflate(R.layout.image_item, parent, false);
            }

            Picasso.with(mContext)
                    .load(mImageList.get(i))
                    .fit() // will explain later
                    .into((ImageView) convertView);

            return convertView;
        }
    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
        private Context mContext;
        List<String> mImageList = new ArrayList<>();

        public RecyclerAdapter(Context context,List<String> imageList){
            this.mContext = context;
            this.mImageList = imageList;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            public ImageView mImageView;
            public ViewHolder(View v){
                super(v);
                mImageView = (ImageView)v.findViewById(R.id.image);
            }
        }

        @Override
        public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // Create a new View
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int i){
            Picasso.with(mContext)
                    .load(mImageList.get(i))
                    .noFade()
                    .into(holder.mImageView);
        }

        @Override
        public int getItemCount(){
            return mImageList.size();
        }

        public void updateImageList(List<String> imageList) {
            mImageList = imageList;
        }

    }

}
