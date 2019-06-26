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
import com.example.realtime_quiz.socket.WebSocketManager;


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
    WebSocketManager webSocketManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        getIntentData();
        initRecyclerView();
        // TODO : add WebSocket initialization code
        webSocketManager = new WebSocketManager(webSocketListener);
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
    WebSocketListener webSocketListener = new WebSocketListener() {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            Log.d(TAG, "open");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            Log.d(TAG, text);

            // proper position?
            showChatLayout();

            Chat newChat = Chat.strToChat(text);
            Game newGame = Game.strToGame(text);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(newChat != null) {
                        adapter.addNewChat(newChat);
                    } else if(newGame != null) {
                        consonantTV.setText(newGame.getNowConsonant());
                    }

                    chatRV.smoothScrollToPosition(adapter.getItemCount());
                }
            });
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            super.onMessage(webSocket, bytes);
            Log.d(TAG, bytes.toString());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
            Log.d(TAG, "closing");
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            Log.d(TAG, "closed");
            finish();
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
            Log.d(TAG, t.getMessage());
        }
    };

    @OnClick(R.id.startBtn)
    public void onStartBtnClicked() {
        // TODO : add send start code
        if(webSocketManager != null) {
            webSocketManager.sendMsg("start!");
        }
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
        webSocketManager.sendMsg(newChat.toString());
        answerET.setText("");
    }

    // TODO : add onDestroy code
    @Override
    protected void onDestroy() {
        super.onDestroy();
        webSocketManager.close();
    }
}
