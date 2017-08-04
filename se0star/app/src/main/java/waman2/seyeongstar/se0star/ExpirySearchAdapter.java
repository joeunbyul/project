package waman2.seyeongstar.se0star;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ExpirySearchAdapter extends BaseAdapter {

    public ArrayList<ExpirySearchModel> List;
    Activity activity;

    public ExpirySearchAdapter(Activity activity, ArrayList<ExpirySearchModel> List) {
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
        TextView modify_list_name;
        TextView modify_list_num;
        TextView unit;
        TextView dayTextVeiw1, dayTextVeiw2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listitem7, null);
            holder = new ViewHolder();
            holder.modify_list_name = (TextView) convertView.findViewById(R.id.modify_list_name);
            holder.modify_list_num = (TextView) convertView.findViewById(R.id.modify_list_num);
            holder.unit = (TextView) convertView.findViewById(R.id.unit);
            holder.dayTextVeiw1 = (TextView) convertView.findViewById(R.id.dayTextVeiw1);
            holder.dayTextVeiw2 = (TextView) convertView.findViewById(R.id.dayTextVeiw2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ExpirySearchModel item = List.get(position);

        holder.modify_list_name.setText(item.getModify_list_name().toString());
        holder.modify_list_num.setText(item.getModify_list_num().toString());
        holder.unit.setText(item.getunit().toString());

        if(item.getdiff() <= 0)
        {
            holder.dayTextVeiw1.setVisibility(View.GONE);
            holder.dayTextVeiw2.setText("유통기한이 지났습니다");
        }
        else {
            holder.dayTextVeiw1.setVisibility(View.VISIBLE);
            holder.dayTextVeiw1.setText(String.valueOf(item.getdiff()));
            holder.dayTextVeiw2.setText("일 남았습니다");
        }
        return convertView;
    }
}
