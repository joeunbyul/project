package waman2.seyeongstar.se0star;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class RegistListAdapter extends BaseAdapter {

    public ArrayList<RegistListModel> RegistList;
    Activity activity;

    public RegistListAdapter(Activity activity, ArrayList<RegistListModel> RegistList) {
        super();
        this.activity = activity;
        this.RegistList = RegistList;
    }

    @Override
    public int getCount() {
        return RegistList.size();
    }

    @Override
    public Object getItem(int position) {
        return RegistList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        ImageView image3;
        TextView regist_list_name;
        TextView regist_list_num;
        TextView regist_unit;
        TextView regist_list_kind;
        TextView regist_list_startday;
        TextView regist_list_finalday;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listitem3, null);
            holder = new ViewHolder();
            holder.image3 = (ImageView) convertView.findViewById(R.id.image3);
            holder.regist_list_name = (TextView) convertView.findViewById(R.id.regist_list_name);
            holder.regist_list_num = (TextView) convertView.findViewById(R.id.regist_list_num);
            holder.regist_unit = (TextView) convertView.findViewById(R.id.regist_unit);
            holder.regist_list_kind = (TextView) convertView.findViewById(R.id.regist_list_kind);
            holder.regist_list_startday = (TextView) convertView.findViewById(R.id.regist_list_startday);
            holder.regist_list_finalday = (TextView) convertView.findViewById(R.id.regist_list_finalday);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RegistListModel item = RegistList.get(position);

        holder.regist_list_name.setText(item.getRegist_list_name().toString());
        holder.regist_list_num.setText(item.getRegist_list_num().toString());
        holder.regist_unit.setText(item.getRegist_unit().toString());
        holder.regist_list_kind.setText(item.getRegist_list_kind().toString());
        holder.regist_list_startday.setText(item.getRegist_list_startday().toString());
        holder.regist_list_finalday.setText(item.getRegist_list_finalday().toString());

        String name = item.getRegist_list_kind().toString();
        switch (name){
            case "육류" :
                holder.image3.setImageResource(R.drawable.image0);
                break;
            case "채소류" :
                holder.image3.setImageResource(R.drawable.image1);
                break;
            case "과일" :
                holder.image3.setImageResource(R.drawable.image2);
                break;
            case "냉동식품" :
                holder.image3.setImageResource(R.drawable.image3);
                break;
            case "해산물" :
                holder.image3.setImageResource(R.drawable.image4);
                break;
            case "나물반찬" :
                holder.image3.setImageResource(R.drawable.image5);
                break;
            case "마른반찬" :
                holder.image3.setImageResource(R.drawable.image6);
                break;
            case "기타반찬" :
                holder.image3.setImageResource(R.drawable.image7);
                break;
            case "유제품" :
                holder.image3.setImageResource(R.drawable.image8);
                break;
            default :
                holder.image3.setImageResource(R.drawable.image9);
                break;
        }
        return convertView;
    }
}
