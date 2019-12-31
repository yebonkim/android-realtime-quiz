package com.example.realtime_quiz.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realtime_quiz.IntentConstant;
import com.example.realtime_quiz.R;
import com.example.realtime_quiz.adapter.ChatAdapter;
import com.example.realtime_quiz.model.Chat;
// TODO : Add WebSocket import
import com.example.realtime_quiz.model.Game;
import com.example.realtime_quiz.socket.WebSocketManager;
import com.example.realtime_quiz.socket.WebSocketMessageListener;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameActivity extends AppCompatActivity {
    @BindView(R.id.edit_answer)
    EditText mAnswerEdit;
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
        mWebSocketManager = WebSocketManager.getInstance(mWsMsgListener);
        Game game = mWebSocketManager.getGame();

        if (game != null) {
            mWsMsgListener.onGameDataReceived(game);
        }
    }

    private WebSocketMessageListener mWsMsgListener = new WebSocketMessageListener() {
        @Override
        public void onGameDataReceived(Game game) {
            if (game.getNowConsonant() != null) {
                mConsonant.setText(game.getNowConsonant());
            }
        }

        @Override
        public void onChatDataReceived(Chat chat) {
            mAdapter.addNewChat(chat);
            mChatList.smoothScrollToPosition(mAdapter.getItemCount());
        }

        @Override
        public void onSocketClosed(int code) {
            Toast.makeText(GameActivity.this, getString(R.string.err_game_disconnected), Toast.LENGTH_LONG).show();
            finish();
        }
    };

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
