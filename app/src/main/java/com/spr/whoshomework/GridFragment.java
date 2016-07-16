package com.spr.whoshomework;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Created by Spr on 2016/7/16.
 */
public class GridFragment extends Fragment {
    private View view;
    private GridView gridView;
    ImageSearchActivity mainActivity;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (ImageSearchActivity)activity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.grid_fragment, container, false);
        gridView = (GridView) view.findViewById(R.id.gridview);
        gridView.setNumColumns(3);
        gridView.setAdapter(mainActivity.getImageAdapter());
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
