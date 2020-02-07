package com.example.realtime_quiz.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realtime_quiz.IntentConstant;
import com.example.realtime_quiz.R;
import com.example.realtime_quiz.adapter.ChatAdapter;
import com.example.realtime_quiz.model.Chat;
import com.example.realtime_quiz.model.Game;
// TODO : Add WebSocket import

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        getIntentData();
        initRecyclerView();
        // TODO : add WebSocket initialization code

        // TODO : add getting game data code
    }

    // TODO : add WebSocketMessageListener

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
        mAnswerEdit.setText("");
    }

    // TODO : add onDestroy code
}