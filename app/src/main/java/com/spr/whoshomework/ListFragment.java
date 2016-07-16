package com.spr.whoshomework;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by Spr on 2016/7/16.
 */
public class ListFragment extends Fragment {
    ImageSearchActivity mainActivity;
    private ListView mListView;
    private View view;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (ImageSearchActivity)activity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.list_fragment, container, false);
        mListView = (ListView) view.findViewById(R.id.listview);
        mListView.setAdapter(mainActivity.getImageAdapter());
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        TextView txtResult = (TextView) this.getView().findViewById(R.id.textView1);
//        txtResult.setText(value);
    }
}
