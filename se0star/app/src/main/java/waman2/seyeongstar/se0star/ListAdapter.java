package waman2.seyeongstar.se0star;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    public ArrayList<ListModel> List;
    Activity activity;

    public ListAdapter(Activity activity, ArrayList<ListModel> List) {
        super();
        this.activity = activity;
        this.List = List;
    }

    @Override
    public int getCount() {
        return List.size();
    }

    @Override
    public Object getItem(int position) {
        return List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        ImageView image;
        TextView modify_list_name;
        TextView modify_list_num;
        TextView unit;
        TextView modify_list_kind;
        TextView modify_list_startday;
        TextView modify_list_finalday;
        TextView list_index;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listitem, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.modify_list_name = (TextView) convertView.findViewById(R.id.modify_list_name);
            holder.modify_list_num = (TextView) convertView.findViewById(R.id.modify_list_num);
            holder.unit = (TextView) convertView.findViewById(R.id.unit);
            holder.modify_list_kind = (TextView) convertView.findViewById(R.id.modify_list_kind);
            holder.modify_list_startday = (TextView) convertView.findViewById(R.id.modify_list_startday);
            holder.modify_list_finalday = (TextView) convertView.findViewById(R.id.modify_list_finalday);
            holder.list_index = (TextView) convertView.findViewById(R.id.list_index);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ListModel item = List.get(position);

        holder.modify_list_name.setText(item.getModify_list_name().toString());
        holder.modify_list_num.setText(item.getModify_list_num().toString());
        holder.unit.setText(item.getunit().toString());
        holder.modify_list_kind.setText(item.getModify_list_kind().toString());
        holder.modify_list_startday.setText(item.getModify_list_startday().toString());
        holder.modify_list_finalday.setText(item.getModify_list_finalday().toString());
        holder.list_index.setText(item.getModify_list_finalday().toString());

        String name = item.getModify_list_kind().toString();

        switch (name){
            case "육류" :
                holder.image.setImageResource(R.drawable.image0);
                break;
            case "채소류" :
                holder.image.setImageResource(R.drawable.image1);
                break;
            case "과일" :
                holder.image.setImageResource(R.drawable.image2);
                break;
            case "냉동식품" :
                holder.image.setImageResource(R.drawable.image3);
                break;
            case "해산물" :
                holder.image.setImageResource(R.drawable.image4);
                break;
            case "나물반찬" :
                holder.image.setImageResource(R.drawable.image5);
                break;
            case "마른반찬" :
                holder.image.setImageResource(R.drawable.image6);
                break;
            case "기타반찬" :
                holder.image.setImageResource(R.drawable.image7);
                break;
            case "유제품" :
                holder.image.setImageResource(R.drawable.image8);
                break;
            default :
                holder.image.setImageResource(R.drawable.image9);
                break;
        }

        return convertView;
    }
}
