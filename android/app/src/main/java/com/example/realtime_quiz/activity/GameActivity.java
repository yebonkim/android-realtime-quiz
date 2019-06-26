package com.example.realtime_quiz.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realtime_quiz.R;
import com.example.realtime_quiz.adapter.ChatAdapter;
import com.example.realtime_quiz.model.Chat;
import com.example.realtime_quiz.model.Game;
// TODO : Add WebSocket import


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = GameActivity.class.getSimpleName();

    @BindView(R.id.answerET)
    EditText answerET;
    @BindView(R.id.startLayout)
    ConstraintLayout startLayout;
    @BindView(R.id.chatLayout)
    ConstraintLayout chatLayout;
    @BindView(R.id.chatRV)
    RecyclerView chatRV;
    @BindView(R.id.consonantTV)
    TextView consonantTV;

    String nickname;

    ChatAdapter adapter;

    // TODO : add WebSocket define code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        getIntentData();
        initRecyclerView();
        // TODO : add WebSocket initialization code
    }

    private void getIntentData() {
        nickname = getIntent().getStringExtra(JoinActivity.EXTRA_USERNAME);
    }

    private void initRecyclerView() {
        adapter = new ChatAdapter();
        chatRV.setLayoutManager(new LinearLayoutManager(this));
        chatRV.setAdapter(adapter);
    }


    private boolean isValid(String str) {
        if(str.length() == 0) {
            return false;
        }

        return true;
    }

    private void showChatLayout() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(chatLayout.getVisibility() == View.INVISIBLE) {
                    chatLayout.setVisibility(View.VISIBLE);
                    startLayout.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    // TODO : add WebSocketListener Code

    @OnClick(R.id.startBtn)
    public void onStartBtnClicked() {
        // TODO : add send start code
    }

    @OnClick(R.id.sendBtn)
    public void onSendBtnClicked() {
        String chatMsg = answerET.getText().toString();
        Chat newChat;

        if(isValid(chatMsg) == false) {
            return;
        }

        newChat = new Chat(nickname, chatMsg);

        // TODO : add send code
        answerET.setText("");
    }

    // TODO : add onDestroy code
}
