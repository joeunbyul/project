package com.nadoo.tacademy.eunbyul_nanu;


import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DrawLayout_fragment extends Fragment {

    public DrawLayout_fragment() {
    }

    public static DrawLayout_fragment newInstance() {
        DrawLayout_fragment fragment = new DrawLayout_fragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.draw_layout_fragment,container,false);
        return view;
    }
}
