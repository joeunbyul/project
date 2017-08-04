
package waman2.seyeongstar.se0star;

import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import org.w3c.dom.Text;
        import org.xmlpull.v1.XmlPullParser;
        import org.xmlpull.v1.XmlPullParserFactory;

        import java.io.InputStream;
        import java.net.URL;
        import java.net.URLEncoder;
        import java.util.ArrayList;

public class AddListAdapter extends BaseAdapter {

    public ArrayList<AddListModel> AddList;
    Context context;
    int str = 0; //AddList안의 값이 변화되면 1을 저장해주는 변수

    public AddListAdapter(Context context, ArrayList<AddListModel> AddList) {
        super();
        this.context = context;
        this.AddList = AddList;
    }

    @Override
    public int getCount() {
        return AddList.size();
    }

    @Override
    public Object getItem(int position) {
        return AddList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        ImageView add_image;
        TextView add_list_name;
        TextView add_list_num;
        TextView add_unit;
        TextView add_list_kind;
        TextView add_list_startday;
        TextView add_list_finalday;

        public ViewHolder(View item){
            add_image = (ImageView) item.findViewById(R.id.add_image);
            add_list_name = (TextView) item.findViewById(R.id.add_list_name);
            add_list_num = (TextView) item.findViewById(R.id.add_list_num);
            add_unit = (TextView) item.findViewById(R.id.add_unit);
            add_list_kind = (TextView) item.findViewById(R.id.add_list_kind);
            add_list_startday = (TextView) item.findViewById(R.id.add_list_startday);
            add_list_finalday = (TextView) item.findViewById(R.id.add_list_finalday);

        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Context context = parent.getContext();
        final ViewHolder holder;
        convertView = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listitem2, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final AddListModel item = AddList.get(position);

        holder.add_list_name.setText(item.getAdd_list_name().toString());
        holder.add_list_num.setText(item.getAdd_list_num().toString());
        holder.add_unit.setText(item.getAdd_unit().toString());
        holder.add_list_kind.setText(item.getAdd_list_kind().toString());
        holder.add_list_startday.setText(item.getAdd_list_startday().toString());
        holder.add_list_finalday.setText(item.getAdd_list_finalday().toString());

        //image설정
        String name = item.getAdd_list_kind().toString();
        switch (name){
            case "육류" :
                holder.add_image.setImageResource(R.drawable.image0);
                break;
            case "채소류" :
                holder.add_image.setImageResource(R.drawable.image1);
                break;
            case "과일" :
                holder.add_image.setImageResource(R.drawable.image2);
                break;
            case "냉동식품" :
                holder.add_image.setImageResource(R.drawable.image3);
                break;
            case "해산물" :
                holder.add_image.setImageResource(R.drawable.image4);
                break;
            case "나물반찬" :
                holder.add_image.setImageResource(R.drawable.image5);
                break;
            case "마른반찬" :
                holder.add_image.setImageResource(R.drawable.image6);
                break;
            case "기타반찬" :
                holder.add_image.setImageResource(R.drawable.image7);
                break;
            case "유제품" :
                holder.add_image.setImageResource(R.drawable.image8);
                break;
            default :
                holder.add_image.setImageResource(R.drawable.image9);
                break;
        }

        Button listaddButton = (Button)convertView.findViewById(R.id.listaddButton);   //추가 버튼 클릭
        listaddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str=1; //상태 변경

                int int_food_num = Integer.parseInt(holder.add_list_num.getText().toString());    //현재 식품의 수량을 불러옴

                if (int_food_num >= 50) {
                    int_food_num = int_food_num + 10;
                    String food_num = String.valueOf(int_food_num);
                    String food_name = holder.add_list_name.getText().toString();
                    try {
                        URL url = new URL("http://woman3.dothome.co.kr/w3/foodplus.php?"  //리퀘스트 방식
                                + "food_name=" + URLEncoder.encode(food_name, "UTF-8")
                                + "&food_num=" + URLEncoder.encode(food_num, "UTF-8"));
                        url.openStream(); //php파일 열기.
                        String result = getXmlData("plusresult.xml", "result"); //insert.php에서 DB입력 결과
                        if (result.equals("1")) { //DB입력이 성공되면
                            holder.add_list_num.setText(food_num.toString());
                            Toast.makeText(context.getApplicationContext(),"수량 +10",Toast.LENGTH_SHORT).show();
                        } else {}
                    } catch (Exception e) {
                        Log.e("Error : ", e.getMessage());
                    }
                } else if (int_food_num <= 50) {
                    int_food_num = int_food_num + 1;  // 버튼을 누르면 1씩 수량이 추가됨
                    String food_num = String.valueOf(int_food_num);   //DB로 보내주기위해 음식의 수량을 String 타입에 넣어줌
                    String food_name = holder.add_list_name.getText().toString();  //DB로 보내주기위해 음식의 이름을 String 타입에 넣어줌
                    try {
                        URL url = new URL("http://woman3.dothome.co.kr/w3/foodplus.php?"  //음식의 수량을 추가해주는 php파일에 연결
                                + "food_name=" + URLEncoder.encode(food_name, "UTF-8")
                                + "&food_num=" + URLEncoder.encode(food_num, "UTF-8"));              // 연결과 동시에 음식의 이름과 수량을 같이 보내줌
                        url.openStream(); //php파일 열기.
                        String result = getXmlData("plusresult.xml", "result"); //insert.php에서 DB입력 결과
                        if (result.equals("1")) { //DB입력이 성공되면!!
                            holder.add_list_num.setText(food_num.toString());   //DB입력이 성공되면 어플 상에서의 수량도 setText로 바꿔줌
                            Toast.makeText(context.getApplicationContext(),"수량 +1",Toast.LENGTH_SHORT).show();
                        } else {}
                    } catch (Exception e) {
                        Log.e("Error : ", e.getMessage());
                    }
                }
            }
        });


        Button listdelButton = (Button)convertView.findViewById(R.id.listdelButton); //삭제 버튼 클릭
        listdelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str=1; //상태 변경

                int int_food_num = Integer.parseInt(holder.add_list_num.getText().toString());

                if (int_food_num >= 50) {
                    int_food_num = int_food_num - 10;
                    String food_num = String.valueOf(int_food_num);
                    String food_name = holder.add_list_name.getText().toString();
                    try {
                        URL url = new URL("http://woman3.dothome.co.kr/w3/foodminus.php?"  // 리퀘스트 방식
                                + "food_name=" + URLEncoder.encode(food_name, "UTF-8")
                                + "&food_num=" + URLEncoder.encode(food_num, "UTF-8"));
                        url.openStream(); //php파일 열기.
                        String result = getXmlData("minusresult.xml", "result"); //insert.php에서 DB입력 결과
                        if (result.equals("1")) { //DB입력이 성공되면!!
                            holder.add_list_num.setText(food_num.toString());
                            Toast.makeText(context.getApplicationContext(), "수량 -10", Toast.LENGTH_SHORT).show();
                        } else {}
                    } catch (Exception e) {
                        Log.e("Error : ", e.getMessage());
                    }
                } else if (int_food_num <= 50 && int_food_num>1) {
                    int_food_num = int_food_num - 1;
                    String food_num = String.valueOf(int_food_num);
                    String food_name = holder.add_list_name.getText().toString();
                    try {
                        URL url = new URL("http://woman3.dothome.co.kr/w3/foodminus.php?"  // 리퀘스트 방식
                                + "food_name=" + URLEncoder.encode(food_name, "UTF-8")
                                + "&food_num=" + URLEncoder.encode(food_num, "UTF-8"));
                        url.openStream(); //php파일 열기.
                        String result = getXmlData("minusresult.xml", "result"); //insert.php에서 DB입력 결과
                        if (result.equals("1")) { //DB입력이 성공되면!!
                            holder.add_list_num.setText(food_num.toString());
                            Toast.makeText(context.getApplicationContext(), "수량 -1", Toast.LENGTH_SHORT).show();
                        } else {}
                    } catch (Exception e) {
                        Log.e("Error : ", e.getMessage());
                    }
                } else if (int_food_num == 1 || int_food_num == 0) {
                    String food_name = holder.add_list_name.getText().toString();
                    try {
                        URL url = new URL("http://woman3.dothome.co.kr/w3/fooddelete.php?"  // 라퀘스트 방식
                                + "food_name=" + URLEncoder.encode(food_name, "UTF-8"));
                        url.openStream(); //php파일 열기.
                        String result = getXmlData("deleteresult.xml", "result"); //insert.php에서 DB입력 결과
                        if (result.equals("1")) { //DB입력이 성공되면!!
                            AddList.remove(position);
                            Toast.makeText(context.getApplicationContext(), "재료 소진!", Toast.LENGTH_SHORT).show();
                        } else {
                        }
                    } catch (Exception e) {
                        Log.e("Error : ", e.getMessage());
                    }
                }
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
            URL server = new URL( "http://woman3.dothome.co.kr/w3/htdocs/" + filename);
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

