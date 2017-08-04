package waman2.seyeongstar.se0star;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ExpiryListAdapter extends BaseAdapter {

    public ArrayList<ExpiryListModel> ExpiryList;
    Activity activity;
    Button expiry_done,expiry_minus,expiry_add;

    private static final String SERVER_ADDRESS = "http://woman3.dothome.co.kr/w3";

    public ExpiryListAdapter(Activity activity, ArrayList<ExpiryListModel> ExpiryList) {
        super();
        this.activity = activity;
        this.ExpiryList = ExpiryList;
    }

    @Override
    public int getCount() {
        return ExpiryList.size();
    }

    @Override
    public Object getItem(int position) {
        return ExpiryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView expiry_index;
        TextView expiry_kind;
        TextView expiry_day;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listitem5, null);
            holder = new ViewHolder();
            holder.expiry_index = (TextView) convertView.findViewById(R.id.expiry_index);
            holder.expiry_kind = (TextView) convertView.findViewById(R.id.expiry_kind);
            holder.expiry_day = (TextView) convertView.findViewById(R.id.expiry_day);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ExpiryListModel item = ExpiryList.get(position);

        holder.expiry_index.setText(item.getExpiry_index().toString());
        holder.expiry_kind.setText(item.getExpiry_kind().toString());
        holder.expiry_day.setText(item.getExpiry_day().toString());
        holder.expiry_kind.setHint(item.getExpiry_kind().toString());
        holder.expiry_day.setHint(item.getExpiry_day().toString());

        expiry_done = (Button)convertView.findViewById(R.id.expiry_done);
        expiry_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String days = holder.expiry_day.getText().toString();
                String category = holder.expiry_kind.getText().toString();
                try {
                    URL url = new URL("http://woman3.dothome.co.kr/w3/expiry_update.php?"
                            + "days=" + URLEncoder.encode(days, "UTF-8")
                            + "&category=" + URLEncoder.encode(category, "UTF-8"));
                    url.openStream();
                    String result = getXmlData("expiry_updateresult.xml", "result");
                    if (result.equals("1")) {
                        holder.expiry_day.setText(days);
                        Toast.makeText(activity.getApplicationContext(), "수정 완료!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity.getApplicationContext(), "수정 실패!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("Error : ", e.getMessage());
                }
            }
        });


        expiry_minus = (Button)convertView.findViewById(R.id.expiry_minus);
        expiry_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = holder.expiry_day.getText().toString();
                int i = Integer.valueOf(str);
                i = i  - 1;
                holder.expiry_day.setText(String.valueOf(i));
            }
        });


        expiry_add = (Button)convertView.findViewById(R.id.expiry_add);
        expiry_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = holder.expiry_day.getText().toString();
                int i = Integer.valueOf(str);
                i = i  + 1;
                holder.expiry_day.setText(String.valueOf(i));
            }
        });

        return convertView;
    }

    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@//

    private String getXmlData(String filename, String str) { //태그값 하나를 받아오기위한 String형 함수

        String ret = "";

        try { //XML 파싱을 위한 과정
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            //XmlPullParserFactory의 인스턴스를 얻기위해  XmlPullParserFactory.newInstance()를 호출시킨다.
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            URL server = new URL( SERVER_ADDRESS + "/htdocs/" + filename);
            //parsing하고싶은 url를 셋팅!
            InputStream is = server.openStream();
            xpp.setInput(is, "UTF-8");

            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals(str)) { //태그 이름이 str 인자값과 같은 경우
                        ret = xpp.nextText();
                    }
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            Log.e("Error1", e.getMessage());
        }

        return ret;
    }

}
