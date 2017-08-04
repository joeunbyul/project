package waman2.seyeongstar.se0star;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class RankingListAdapter extends BaseAdapter {
    public ArrayList<RankingListModel> RankingList;
    Activity activity;
    ImageView rank;
    TextView difference1, difference2, difference3;

    public RankingListAdapter(Activity activity, ArrayList<RankingListModel> RankingList) {
        super();
        this.activity = activity;
        this.RankingList = RankingList;
    }

    @Override
    public int getCount() {
        return RankingList.size();
    }

    @Override
    public Object getItem(int position) {
        return RankingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView ranking_list_name;
        TextView ranking_list_num;
        TextView ranking_unit;
        TextView ranking_list_kind;
        TextView ranking_list_finalday;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listitem4, null);
            holder = new ViewHolder();
            rank = (ImageView) convertView.findViewById(R.id.rank);
            holder.ranking_list_name = (TextView) convertView.findViewById(R.id.ranking_list_name);
            holder.ranking_list_num = (TextView) convertView.findViewById(R.id.ranking_list_num);
            holder.ranking_unit = (TextView) convertView.findViewById(R.id.ranking_list_unit);
            holder.ranking_list_kind = (TextView) convertView.findViewById(R.id.ranking_list_kind);
            holder.ranking_list_finalday = (TextView) convertView.findViewById(R.id.ranking_list_finalday);
            difference1 = (TextView) convertView.findViewById(R.id.difference1);
            difference2 = (TextView) convertView.findViewById(R.id.difference2);
            difference3 = (TextView) convertView.findViewById(R.id.difference3);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RankingListModel item = RankingList.get(position);

        holder.ranking_list_name.setText(item.getRanking_list_name().toString());
        holder.ranking_list_num.setText(item.getRanking_list_num().toString());
        holder.ranking_unit.setText(item.getRanking_unit().toString());
        holder.ranking_list_kind.setText(item.getRanking_list_kind().toString());
        holder.ranking_list_finalday.setText(item.getRanking_list_finalday().toString());

        if(Integer.parseInt(item.getDifference().toString()) <= 0) {
            difference2.setVisibility(View.GONE);
            difference1.setVisibility(View.GONE);
            difference3.setText("유통기한이 지났습니다");
        }
        else {
            difference1.setText(item.getDifference().toString());
        }

        switch (position) {
            case 0:
                rank.setImageResource(R.drawable.rank1);
                break;
            case 1:
                rank.setImageResource(R.drawable.rank2);
                break;
            case 2:
                rank.setImageResource(R.drawable.rank3);
                break;
            case 3:
                rank.setImageResource(R.drawable.rank4);
                break;
            case 4:
                rank.setImageResource(R.drawable.rank5);
                break;

        }
        return convertView;
    }
}
