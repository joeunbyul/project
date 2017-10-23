package com.nadoo.tacademy.eunbyul_nanu;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SearchDefault extends Fragment {

    public static SearchDefault newInstance(){
        SearchDefault searchDefault = new SearchDefault();
        return searchDefault;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_default,container,false);
        return view;
    }
}
