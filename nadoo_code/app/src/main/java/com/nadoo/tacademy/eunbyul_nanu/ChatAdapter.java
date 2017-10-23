package com.nadoo.tacademy.eunbyul_nanu;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by eunbyul on 2017-10-22.
 */

@android.support.annotation.RequiresApi(api = Build.VERSION_CODES.N)
public class ChatAdapter extends ArrayAdapter<ChatDTO> {

        private final SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("HH:mm",Locale.getDefault());
        private final static int TYPE_MY_SELF = 0;
        private final static int TYPE_ANOTHER = 1;
        private String myName;

        public ChatAdapter(Context context, int resource) {
            super(context, resource);
        }

        public void setName(String name){
            myName = name;
        }

        private View setAnotherView(LayoutInflater inflater) {
            View convertView = inflater.inflate(R.layout.list_chatleft, null);
            ViewHolderAnother holder = new ViewHolderAnother();
            holder.bindView(convertView);
            convertView.setTag(holder);
            return convertView;
        }

        private View setMySelfView(LayoutInflater inflater) {
            View convertView = inflater.inflate(R.layout.list_chatright, null);
            ViewHolderMySelf holder = new ViewHolderMySelf();
            holder.bindView(convertView);
            convertView.setTag(holder);
            return convertView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int viewType = getItemViewType(position);
            LayoutInflater inflater = LayoutInflater.from(getContext());
            if (convertView == null) {
                if (viewType == TYPE_ANOTHER) {
                    convertView = setAnotherView(inflater);
                } else {
                    convertView = setMySelfView(inflater);
                }
            }

            if (convertView.getTag() instanceof ViewHolderAnother) {
                if (viewType != TYPE_ANOTHER) {
                    convertView = setAnotherView(inflater);
                }
                ((ViewHolderAnother) convertView.getTag()).setData(position);
            } else {
                if (viewType != TYPE_MY_SELF) {
                    convertView = setMySelfView(inflater);
                }
                ((ViewHolderMySelf) convertView.getTag()).setData(position);
            }

            return convertView;
        }


        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            String name = getItem(position).userName;
            if (!TextUtils.isEmpty(myName) && myName.equals(name)) {
                return TYPE_MY_SELF; // 나의 채팅내용
            } else {
                return TYPE_ANOTHER; // 상대방의 채팅내용
            }
        }

        private class ViewHolderAnother {

            private TextView mTxtUserName;
            private TextView mTxtMessage;
            private TextView mTxtTime;

            private void bindView(View convertView) {

                mTxtUserName = (TextView) convertView.findViewById(R.id.txt_userName);
                mTxtMessage = (TextView) convertView.findViewById(R.id.txt_message);
                mTxtTime = (TextView) convertView.findViewById(R.id.txt_time);
            }

            private void setData(int position) {
                ChatDTO chatData = getItem(position);
                mTxtUserName.setText(chatData.userName);
                mTxtMessage.setText(chatData.message);
                mTxtTime.setText(mSimpleDateFormat.format(chatData.time));
            }
        }

        private class ViewHolderMySelf {
            private TextView mTxtMessage;
            private TextView mTxtTime;

            private void bindView(View convertView) {
                mTxtMessage = (TextView) convertView.findViewById(R.id.txt_message);
                mTxtTime = (TextView) convertView.findViewById(R.id.txt_time);
            }

            private void setData(int position) {
                ChatDTO chatData = getItem(position);
                mTxtMessage.setText(chatData.message);
                mTxtTime.setText(mSimpleDateFormat.format(chatData.time));
            }
        }
    }