package com.nadoo.tacademy.eunbyul_nanu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.nadoo.tacademy.eunbyul_nanu.cookie_module.ClearableCookieJar;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.nadoo.tacademy.eunbyul_nanu.R.id.viewpager;

public class MainActivity extends AppCompatActivity {

    int state; //로그인 or 비로그인 체크변수
    int CHECK_SUM = 0;
    View drawerView;
    DrawerLayout drawer_layout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    ActionBar ab;
    DrawLayout_fragment drawLayout_fragment;
    FloatingActionsMenu fab_menu;
    ViewPager viewPager;
    ImageView toolbar_img;
    NaDooPagerAdapter adapter;


    String user,nickname,profile_thumbURL,school;
    int school_check,facebook_check,kakaotalk_check;
    String email,pw;

    TextView profile_schoolcheck,profile_kakaocheck,profile_facebookcheck,
            profile_user,profile_school;
    SharedPreferences prefs;

    FloatingActionButton item_write,want_write;

    ViewPager banner;

    FrameLayout login_after,login_before;

    private final long FINISH_INTERVAL_TIME = 1000;
    private final int LOGIN = 1;
    private long   backPressedTime = 0;

    ImageButton menu_item1,menu_item2,menu_item3;

    private int[] titles = {R.drawable.tab_title_need, R.drawable.tab_title_item, R.drawable.tab_title_feed };
    TextView toolbarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ab = getSupportActionBar();

        toolbarTitle = (TextView)findViewById(R.id.toolbarTitle);
        drawLayout_fragment = DrawLayout_fragment.newInstance();
        drawerView = (LinearLayout)findViewById(R.id.activity_draw_layout_fragment);
        drawer_layout = (DrawerLayout)findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawer_layout,toolbar, R.string.app_name, R.string.app_name);
        drawer_layout.addDrawerListener(actionBarDrawerToggle);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ab.setHomeAsUpIndicator(android.R.drawable.btn_star);
        ab.setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }


        /*유저프로필부분*/////////////////////////////////////////////////////////////////
        login_after = (FrameLayout)findViewById(R.id.login_after);
        login_before = (FrameLayout)findViewById(R.id.login_before);
        prefs = getSharedPreferences("user_pref", MODE_PRIVATE);
        state = prefs.getInt("state",0);
        if(state == 1){
            login_after.setVisibility(View.VISIBLE);
            login_before.setVisibility(View.GONE);
        }else{
            login_after.setVisibility(View.GONE);
            login_before.setVisibility(View.VISIBLE);
        }

        user = prefs.getString("user_id","");
        nickname = prefs.getString("nickname","");
        school = prefs.getString("school","");
        school_check = prefs.getInt("school_check",0);
        kakaotalk_check = prefs.getInt("kakaotalk_check",0);
        facebook_check = prefs.getInt("facebook_check",0);
        profile_thumbURL = prefs.getString("profile_thumbURL","");
        CircleImageView navi_header_image_before = (CircleImageView)findViewById(R.id.navi_header_image2);
        CircleImageView navi_header_image_after = (CircleImageView)findViewById(R.id.navi_header_image);
        profile_school = (TextView)findViewById(R.id.my_profile_school);
        profile_schoolcheck = (TextView)findViewById(R.id.profile_schoolcheck);
        profile_kakaocheck = (TextView)findViewById(R.id.profile_kakaocheck);
        profile_facebookcheck = (TextView)findViewById(R.id.profile_facebookcheck);
        profile_user = (TextView)findViewById(R.id.profile_user);
        profile_school.setText(school);
        profile_user.setText(nickname);



        if (school_check == 1) {
            profile_schoolcheck.setText("인증됨");
            profile_schoolcheck.setTextColor(Color.parseColor("#eb675e"));
        } else {
            profile_schoolcheck.setText("인증안됨");
            profile_schoolcheck.setTextColor(Color.parseColor("#868686"));
        }
        if (kakaotalk_check == 1) {
            profile_kakaocheck.setText("인증됨");
            profile_kakaocheck.setTextColor(Color.parseColor("#eb675e"));
        } else {
            profile_kakaocheck.setText("인증안됨");
            profile_kakaocheck.setTextColor(Color.parseColor("#868686"));
        }
        if (facebook_check == 1) {
            profile_facebookcheck.setText("인증됨");
            profile_facebookcheck.setTextColor(Color.parseColor("#eb675e"));
        } else {
            profile_facebookcheck.setText("인증안됨");
            profile_facebookcheck.setTextColor(Color.parseColor("#868686"));
        }


        if(profile_thumbURL.equals("")){
            navi_header_image_before.setImageResource(R.drawable.nobody);
        }else{
            Glide.with(MyApplication.getmContext()).load(profile_thumbURL).into(navi_header_image_after);
        }


        ////////////////////////////////////////////////////////////////////////////////////

        /*viewpager 상태*///////////////////////////////////////////////////////////////////
        menu_item1 = (ImageButton)findViewById(R.id.menu_item1);
        menu_item2 = (ImageButton)findViewById(R.id.menu_item2);
        menu_item3 = (ImageButton)findViewById(R.id.menu_item3);

        menu_item1.setClickable(true);
        menu_item2.setClickable(false);
        menu_item3.setClickable(true);
        menu_item1.setImageResource(R.drawable.item_tab_default);
        menu_item2.setImageResource(R.drawable.need_tab);
        menu_item3.setImageResource(R.drawable.feed_tab_default);

        toolbar_img = (ImageView)findViewById(R.id.toolbar_img);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                toolbarTitle.setBackgroundResource(titles[position]);
                if(position==2){
                    menu_item1.setClickable(true);
                    menu_item2.setClickable(true);
                    menu_item3.setClickable(false);
                    menu_item1.setImageResource(R.drawable.item_tab_default);
                    menu_item2.setImageResource(R.drawable.need_tab_default);
                    menu_item3.setImageResource(R.drawable.feed_tab);
                    fab_menu.setVisibility(View.INVISIBLE);
                    toolbar_img.setVisibility(View.GONE);

                }else if(position==0){
                    menu_item1.setClickable(true);
                    menu_item2.setClickable(false);
                    menu_item3.setClickable(true);
                    menu_item1.setImageResource(R.drawable.item_tab_default);
                    menu_item2.setImageResource(R.drawable.need_tab);
                    menu_item3.setImageResource(R.drawable.feed_tab_default);
                    fab_menu.setVisibility(View.VISIBLE);
                    toolbar_img.setVisibility(View.VISIBLE);
                }else if(position==1){
                    menu_item1.setClickable(false);
                    menu_item2.setClickable(true);
                    menu_item3.setClickable(true);
                    menu_item1.setImageResource(R.drawable.item_tab);
                    menu_item2.setImageResource(R.drawable.need_tab_default);
                    menu_item3.setImageResource(R.drawable.feed_tab_default);
                    fab_menu.setVisibility(View.VISIBLE);
                    toolbar_img.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////

        //banner = (ViewPager)findViewById(R.id.banner);


        /*플로팅버튼부분*/////////////////////////////////////////////////////////////////////
        final FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        frameLayout.getBackground().setAlpha(0);
        fab_menu = (FloatingActionsMenu)findViewById(R.id.fab_menu);
        fab_menu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                frameLayout.getBackground().setAlpha(90);
                frameLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        fab_menu.collapse();
                        return true;
                    }
                });
            }

            @Override
            public void onMenuCollapsed() {
                frameLayout.getBackground().setAlpha(0);
                frameLayout.setOnTouchListener(null);
            }
        });

        //물품등록
        item_write = (FloatingActionButton)findViewById(R.id.item_write);
        item_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if(state==0){
                    Toast.makeText(MyApplication.getmContext(),"로그인 후 이용가능합니다.",Toast.LENGTH_SHORT).show();
                }else {

                }*/
                Intent itemPage = new Intent(MyApplication.getmContext(), RegisterItem.class);
                itemPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(itemPage);
                frameLayout.getBackground().setAlpha(0);
                fab_menu.collapse();
            }
        });
        //요청글 등록
        want_write = (FloatingActionButton)findViewById(R.id.want_write);
        want_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if(state==0){
                    Toast.makeText(MyApplication.getmContext(),"로그인 후 이용가능합니다.",Toast.LENGTH_SHORT).show();
                }else {

                }*/
                Intent wantPage = new Intent(MyApplication.getmContext(), RegisterWrite.class);
                wantPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(wantPage);
                frameLayout.getBackground().setAlpha(0);
                fab_menu.collapse();
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////

    }


    //두번 누르면 종료
    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        } else if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            /*prefs = getSharedPreferences("user_pref", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.commit();

            ClearableCookieJar clearableCookieJar = OkHttpInitSingtonManager.getCookieJar();
            if (clearableCookieJar != null) clearableCookieJar.clear();*/
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "한번 더 뒤로가기 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
        }
    }


    //메뉴 버튼들
    public void menu_item(View v){
        switch (v.getId()){
            case R.id.notice :
                Intent notice = new Intent(MyApplication.getmContext(), NoticeActivity.class);
                notice.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(notice);
                break;
            case R.id.settings:
                Intent settings = new Intent(MyApplication.getmContext(),SettingActivity.class);
                settings.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(settings);
                break;
            case R.id.login_bth:
                Intent login = new Intent(MyApplication.getmContext(),LoginActivity.class);
                startActivityForResult(login,LOGIN);
                break;

            case R.id.menu_item1:
                viewPager.setCurrentItem(1);
                drawer_layout.closeDrawers();

                break;
            case R.id.menu_item2:
                viewPager.setCurrentItem(0);
                drawer_layout.closeDrawers();

                break;
            case R.id.menu_item3:
                viewPager.setCurrentItem(2);
                drawer_layout.closeDrawers();
                break;
            case R.id.menu_item4:
                Toast.makeText(MyApplication.getmContext(),"이벤트 준비 중 입니다.",Toast.LENGTH_SHORT).show();
                break;

        }
    }

    public void myPage(View v){
        switch (v.getId()){
            case R.id.menu1:
                if(state==0){
                    Toast.makeText(MyApplication.getmContext(),"로그인 후 이용가능합니다.",Toast.LENGTH_SHORT).show();
                }else {
                    Intent mypageItem = new Intent(MyApplication.getmContext(), MypageItem.class);
                    mypageItem.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mypageItem);
                }
                break;
            case R.id.menu2:
                if(state==0){
                    Toast.makeText(MyApplication.getmContext(),"로그인 후 이용가능합니다.",Toast.LENGTH_SHORT).show();
                }else {
                    Intent mypageItemNeed = new Intent(MyApplication.getmContext(), MypageNeed.class);
                    mypageItemNeed.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mypageItemNeed);
                }
                break;
            case R.id.menu3:
                if(state==0){
                    Toast.makeText(MyApplication.getmContext(),"로그인 후 이용가능합니다.",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MyApplication.getmContext(), "게시글 관리 준비중입니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu4:
                if(state==0){
                    Toast.makeText(MyApplication.getmContext(),"로그인후 이용가능합니다.",Toast.LENGTH_SHORT).show();
                }else {
                    Intent mypageFriend = new Intent(MyApplication.getmContext(), MypageFriend.class);
                    mypageFriend.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mypageFriend);
                }
                break;
            case R.id.menu5:
                if(state==0){
                    Toast.makeText(MyApplication.getmContext(),"로그인후 이용가능합니다.",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MyApplication.getmContext(),"채팅 관리 준비중입니다.",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()){
            case R.id.toolbar_search:
                Intent search = new Intent(MyApplication.getmContext(),Search.class);
                startActivity(search);
                break;
            case R.id.toolbar_chatting:
                Intent chattinglist = new Intent(MyApplication.getmContext(),ChattingList.class);
                startActivity(chattinglist);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new NaDooPagerAdapter(getSupportFragmentManager());
        adapter.appendFragment(WantItemBoard.newInstance(1));
        adapter.appendFragment(ItemBoard.newInstance(2));
        adapter.appendFragment(Board.newInstance());
        viewPager.setAdapter(adapter);
    }

    private static class NaDooPagerAdapter extends FragmentPagerAdapter {

        private final ArrayList<Fragment> fragment1 = new ArrayList<>();

        public NaDooPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void appendFragment(Fragment fragment) {
            fragment1.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return fragment1.get(position);
        }

        @Override
        public int getCount() {
            return fragment1.size();
        }
    }

}
