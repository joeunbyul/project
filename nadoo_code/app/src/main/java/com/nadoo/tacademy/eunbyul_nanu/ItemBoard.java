package com.nadoo.tacademy.eunbyul_nanu;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ItemBoard extends Fragment {

    /////////////////////////////////////////////////////////
    /*
    private ImageView[] imageViews = null;
    private ImageView imageView = null;
    private ViewPager advPager = null;
    private AtomicInteger what = new AtomicInteger(0);
    private boolean isContinue = true;
    ViewGroup group;*/
    /////////////////////////////////////////////////////////
    RecyclerView Items;

    SharedPreferences preferences;
    TextView school_textview;

    public static ItemBoard newInstance(int initValue){
        ItemBoard ItemBoardFrag = new ItemBoard();
        return ItemBoardFrag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_item_board,container,false);
        //advPager = (ViewPager) view.findViewById(R.id.adv_pager);
        //group = (ViewGroup) view.findViewById(R.id.viewGroup);
        //initViewPager();


        school_textview = (TextView)view.findViewById(R.id.Item_school_name);

        Items = (RecyclerView) view.findViewById(R.id.Items);
        Items.setLayoutManager(new GridLayoutManager(MyApplication.getmContext(),2));


        preferences = getActivity().getSharedPreferences("user_pref",Context.MODE_PRIVATE);
        String school = preferences.getString("school","");
        school_textview.setText(school);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        AsyncItemJSONList asyncItemJSONList= new ItemBoard.AsyncItemJSONList();
        asyncItemJSONList.execute();
    }
    ///////////////////////////////////////////////////////////////////////////////////////
    /*
        banner

    private void initViewPager() {
        List<View> advPics = new ArrayList<View>();

        ImageView img1 = new ImageView(MyApplication.getmContext());
        img1.setBackgroundResource(R.drawable.image1);
        advPics.add(img1);

        ImageView img2 = new ImageView(MyApplication.getmContext());
        img2.setBackgroundResource(R.drawable.image2);
        advPics.add(img2);

        ImageView img3 = new ImageView(MyApplication.getmContext());
        img3.setBackgroundResource(R.drawable.image3);
        advPics.add(img3);

        ImageView img4 = new ImageView(MyApplication.getmContext());
        img4.setBackgroundResource(R.drawable.image4);
        advPics.add(img4);


        imageViews = new ImageView[advPics.size()];

        for (int i = 0; i < advPics.size(); i++) {
            imageView = new ImageView(MyApplication.getmContext());
            //imageView.setLayoutParams(new LayoutParams(20, 20));
            imageView.setPadding(5, 5, 5, 5);
            imageViews[i] = imageView;
            if (i == 0) {
                imageViews[i]
                        .setBackgroundResource(R.drawable.navigation_1);
            } else {
                imageViews[i]
                        .setBackgroundResource(R.drawable.navigation_2);
            }
            group.addView(imageViews[i]);
        }

        advPager.setAdapter(new AdvAdapter(advPics));
        advPager.setOnPageChangeListener(new GuidePageChangeListener());
        advPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        isContinue = false;
                        break;
                    case MotionEvent.ACTION_UP:
                        isContinue = true;
                        break;
                    default:
                        isContinue = true;
                        break;
                }
                return false;
            }
        });
        advPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    if (isContinue) {
                        viewHandler.sendEmptyMessage(what.get());
                        whatOption();
                    }
                }
            }

        }).start();
    }
    private void whatOption() {
        what.incrementAndGet();
        if (what.get() > imageViews.length - 1) {
            what.getAndAdd(-4);
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

        }
    }
    private Handler viewHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            advPager.setCurrentItem(msg.what);
            super.handleMessage(msg);
        }

    };
    private final class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            what.getAndSet(arg0);
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[arg0]
                        .setBackgroundResource(R.drawable.navigation_1);
                if (arg0 != i) {
                    imageViews[i]
                            .setBackgroundResource(R.drawable.navigation_2);
                }
            }

        }

    }
      private final class AdvAdapter extends PagerAdapter {
        private List<View> views = null;
        public AdvAdapter(List<View> views) {
            this.views = views;
        }
        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(views.get(arg1));
        }
        @Override
        public void finishUpdate(View arg0) {        }
        @Override
        public int getCount() {
            return views.size();
        }
        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(views.get(arg1), 0);
            return views.get(arg1);
        }
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {        }
        @Override
        public Parcelable saveState() {
            return null;
        }
        @Override
        public void startUpdate(View arg0) {

        }
    } */
    ///////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////
    /*
        RecyclerviewAdapter
*/
    public static class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder>{

        ArrayList<ItemObject> items;
        Context context;
        public ItemRecyclerViewAdapter(Context context, ArrayList<ItemObject> objects){
            this.context =context;
            this.items = objects;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview,parent, false);
            return new ItemBoard.ItemRecyclerViewAdapter.ViewHolder(v);
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            public final View mView;
            ImageView item_photo;
            ImageView item_state;
            TextView item_name;
            TextView item_price;
            TextView item_time;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                item_photo = (ImageView)view.findViewById(R.id.item_photo);
                item_state = (ImageView)view.findViewById(R.id.item_state);
                item_name = (TextView)view.findViewById(R.id.item_name);
                item_price = (TextView)view.findViewById(R.id.item_price);
                item_time = (TextView)view.findViewById(R.id.item_time);

            }
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            Glide.with(MyApplication.getmContext()).load(items.get(position).item_photo.toString()).into(holder.item_photo);
           // holder.item_state.setImageResource(R.drawable.item_condition_1);
            //1:대여가능,2:대여중,3:대여불가
            switch (items.get(position).item_state){
                case 1:
                    holder.item_state.setImageResource(R.drawable.item_condition_2);
                    break;
                case 2:
                    holder.item_state.setImageResource(R.drawable.item_condition_1);
                    break;
            }
            holder.item_name.setText(items.get(position).item_name);
            holder.item_price.setText(items.get(position).item_price+"원");
            if(items.get(position).item_time.equalsIgnoreCase("시간")){
                holder.item_time.setText(" /시간");
            }else if(items.get(position).item_time.equalsIgnoreCase("일")){
                holder.item_time.setText(" /일");
            }else if(items.get(position).item_time.equalsIgnoreCase("협의")){
                holder.item_price.setVisibility(View.GONE);
                holder.item_time.setText("협의");
            }


            //물품상세페이지로 이동!
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent itemPage = new Intent(MyApplication.getmContext(),ItemPage.class);
                    itemPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    itemPage.putExtra("item_state", items.get(position).item_state);
                    itemPage.putExtra("item_id", items.get(position).item_id);
                    itemPage.putExtra("item_photo", items.get(position).item_photo);
                    itemPage.putExtra("item1", items.get(position).item1);
                    itemPage.putExtra("item2", items.get(position).item2);
                    itemPage.putExtra("item3", items.get(position).item3);
                    itemPage.putExtra("item_name", items.get(position).item_name);
                    itemPage.putExtra("item_price", items.get(position).item_price);
                    itemPage.putExtra("item_time", items.get(position).item_time);
                    itemPage.putExtra("item_userid", items.get(position).item_userid);
                    itemPage.putExtra("item_nickname", items.get(position).item_nickname);
                    itemPage.putExtra("item_category", items.get(position).item_category);
                    itemPage.putExtra("item_article", items.get(position).item_article);
                    itemPage.putExtra("item_check", items.get(position).item_check);
                    itemPage.putExtra("item_location", items.get(position).item_location);
                    itemPage.putExtra("item_userphoto", items.get(position).item_userphoto);

                    ActivityCompat.startActivity(MyApplication.getmContext(), itemPage,null);
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////

    public class AsyncItemJSONList extends AsyncTask<String, Integer, ArrayList<ItemObject>> {
        ItemBoard.ItemRecyclerViewAdapter itemListAdapter;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected ArrayList<ItemObject> doInBackground(String... params) {
            return Item_HttpAPIHelperHandler.ItemAllSelect();
        }

        @Override
        protected void onPostExecute(ArrayList<ItemObject> result) {
            super.onPostExecute(result);
            if(result != null && result.size() > 0){
                itemListAdapter =
                        new ItemBoard.ItemRecyclerViewAdapter(MyApplication.getmContext(),result);
                itemListAdapter.notifyDataSetChanged();
                Items.setAdapter(itemListAdapter);

            }
        }
    }
}
