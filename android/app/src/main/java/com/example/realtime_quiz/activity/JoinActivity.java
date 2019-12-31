package com.example.realtime_quiz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.realtime_quiz.IntentConstant;
import com.example.realtime_quiz.R;
import com.example.realtime_quiz.model.Chat;
import com.example.realtime_quiz.model.Game;
import com.example.realtime_quiz.socket.WebSocketManager;
import com.example.realtime_quiz.socket.WebSocketMessageListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JoinActivity extends AppCompatActivity {
    @BindView(R.id.edit_username)
    EditText mUsernameEdit;

    String mUsername;

    WebSocketManager mWebSocketManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        ButterKnife.bind(this);

        mWebSocketManager = WebSocketManager.getInstance(mWsMsgListener);
    }

    WebSocketMessageListener mWsMsgListener = new WebSocketMessageListener() {
        @Override
        public void onGameDataReceived(Game game) {
            goToGameActivity(mUsername);
        }

        @Override
        public void onChatDataReceived(Chat chat) {
            return;
        }
    };

    @OnClick(R.id.btn_to_game)
    public void goToGame() {
        mUsername = mUsernameEdit.getText().toString();

        if (isValid(mUsername)) {
            joinGame();
        } else {
            Toast.makeText(this, getString(R.string.err_input_again), Toast.LENGTH_SHORT).show();
        }
    }

    private void joinGame() {
        // TODO : add send start code
        if (mWebSocketManager != null) {
            mWebSocketManager.sendMsg("start!");
        }
    }

    private boolean isValid(@NonNull String str) {
        return !str.isEmpty();
    }

    private void goToGameActivity(String username) {
        startActivity(new Intent(this, GameActivity.class).putExtra(
                IntentConstant.USERNAME, username));
    }
}
