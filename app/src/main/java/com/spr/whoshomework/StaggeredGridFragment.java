package com.spr.whoshomework;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.support.v7.widget.RecyclerView;

/**
 * Created by Spr on 2016/7/16.
 */
public class StaggeredGridFragment extends Fragment {
    ImageSearchActivity mainActivity;
    private View view;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (ImageSearchActivity)activity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.staggered_grid_fragment, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mainActivity.getRecyclerAdapter());

        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
