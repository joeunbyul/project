package com.nadoo.tacademy.eunbyul_nanu;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Search extends AppCompatActivity {

    ImageButton fillter;
    SearchPagerAdapter adapter;
    int child;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

       for(int i=0; i < tabLayout.getChildCount(); i++)
           tabLayout.getChildAt(i).setBackgroundResource(R.drawable.tablayout_selector);

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new SearchPagerAdapter(getSupportFragmentManager());
        adapter.appendFragment(Search_Item.newInstance(1), "대여 물품");
        adapter.appendFragment(Search_WantItem.newInstance(2),"대여 요청");
        viewPager.setAdapter(adapter);
    }

    private static class SearchPagerAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> fragments = new ArrayList<>();
        private final ArrayList<String> tabTitles = new ArrayList<>();

        public SearchPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void appendFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            tabTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles.get(position);
        }
    }
}
