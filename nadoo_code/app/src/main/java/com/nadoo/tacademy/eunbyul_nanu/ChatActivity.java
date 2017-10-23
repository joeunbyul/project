package com.nadoo.tacademy.eunbyul_nanu;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by eunbyul on 2017-09-17.
 */

public class ChatActivity extends AppCompatActivity {

    private String CHAT_NAME;
    private String USER_NAME;

    private ListView chat_view;
    private ChatAdapter mAdapter;
    private EditText chat_edit;
    private Button chat_send;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // 위젯 ID 참조
        chat_view = (ListView) findViewById(R.id.chat_view);
        chat_edit = (EditText) findViewById(R.id.chat_edit);
        chat_send = (Button) findViewById(R.id.chat_sent);


        // 로그인 화면에서 받아온 채팅방 이름, 유저 이름 저장
        Intent intent = getIntent();
        CHAT_NAME = intent.getStringExtra("chatName");
        USER_NAME = intent.getStringExtra("userName");

        mAdapter = new ChatAdapter(this,0);
        chat_view.setAdapter(mAdapter);
        mAdapter.setName(USER_NAME);
        mAdapter.notifyDataSetChanged();
        // 채팅 방 입장
        openChat(CHAT_NAME);

        // 메시지 전송 버튼에 대한 클릭 리스너 지정
        chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chat_edit.getText().toString().equals(""))
                    return;

                ChatDTO chat = new ChatDTO();
                chat.message = chat_edit.getText().toString();
                chat.userName = USER_NAME;
                chat.time = System.currentTimeMillis();
                //ChatDTO를 이용하여 데이터를 묶는다.
                databaseReference.child("chat").child(CHAT_NAME).push().setValue(chat); // 데이터 푸쉬
                chat_edit.setText(""); //입력창 초기화

            }
        });
    }

 /*   private void addMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
        adapter.add(chatDTO.getUserName() + " : " + chatDTO.getMessage());
    }

    private void removeMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
        adapter.remove(chatDTO.getUserName() + " : " + chatDTO.getMessage());
    }*/

    private void openChat(String chatName) {
        // 리스트 어댑터 생성 및 세팅


        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.child("chat").child(chatName).addChildEventListener(new ChildEventListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
               // addMessage(dataSnapshot, adapter);
                ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
                chatDTO.firebaseKey = dataSnapshot.getKey();
                mAdapter.add(chatDTO);
                chat_view.smoothScrollToPosition(mAdapter.getCount());
                Log.e("LOG", "s:"+s);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //removeMessage(dataSnapshot, adapter);
                String firebaseKey = dataSnapshot.getKey();
                int count = mAdapter.getCount();
                for (int i = 0; i < count; i++) {
                    if (mAdapter.getItem(i).firebaseKey.equals(firebaseKey)) {
                        mAdapter.remove(mAdapter.getItem(i));
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
