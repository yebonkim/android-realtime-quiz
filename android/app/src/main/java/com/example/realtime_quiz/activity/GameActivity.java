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

import com.example.realtime_quiz.IntentConstant;
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

    @BindView(R.id.edit_answer)
    EditText mAnswerEdit;
    @BindView(R.id.layout_start)
    ConstraintLayout mStartLayout;
    @BindView(R.id.layout_chat)
    ConstraintLayout mChatLayout;
    @BindView(R.id.list_chat)
    RecyclerView mChatList;
    @BindView(R.id.text_consonant)
    TextView mConsonant;

    ChatAdapter mAdapter;

    String mNickname;

    // TODO : add WebSocket define code
    WebSocketManager mWebSocketManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        getIntentData();
        initRecyclerView();
        // TODO : add WebSocket initialization code
        mWebSocketManager = new WebSocketManager(mWebSocketListener);
    }

    private void getIntentData() {
        mNickname = getIntent().getStringExtra(IntentConstant.USERNAME);
    }

    private void initRecyclerView() {
        mAdapter = new ChatAdapter();
        mChatList.setLayoutManager(new LinearLayoutManager(this));
        mChatList.setAdapter(mAdapter);
    }

    private boolean isValid(String str) {
        if (str.length() == 0) {
            return false;
        }

        return true;
    }

    private void showChatLayout() {
        runOnUiThread(() -> {
                if (mChatLayout.getVisibility() == View.INVISIBLE) {
                    mChatLayout.setVisibility(View.VISIBLE);
                    mStartLayout.setVisibility(View.INVISIBLE);
                }
            }
        );
    }

    // TODO : add WebSocketListener Code
    WebSocketListener mWebSocketListener = new WebSocketListener() {
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
                    if (newChat != null) {
                        mAdapter.addNewChat(newChat);
                    } else if (newGame != null) {
                        mConsonant.setText(newGame.getNowConsonant());
                    }

                    mChatList.smoothScrollToPosition(mAdapter.getItemCount());
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
        if (mWebSocketManager != null) {
            mWebSocketManager.sendMsg("start!");
        }
    }

    @OnClick(R.id.btn_send)
    public void onSendBtnClicked() {
        String chatMsg = mAnswerEdit.getText().toString();
        Chat newChat;

        if (isValid(chatMsg) == false) {
            return;
        }

        newChat = new Chat(mNickname, chatMsg);

        // TODO : add send code
        mWebSocketManager.sendMsg(newChat.toString());
        mAnswerEdit.setText("");
    }

    // TODO : add onDestroy code
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebSocketManager.close();
    }
}
