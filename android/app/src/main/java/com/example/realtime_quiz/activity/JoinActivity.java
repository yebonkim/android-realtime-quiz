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

// TODO : Add WebSocket import

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JoinActivity extends AppCompatActivity {
    @BindView(R.id.edit_username)
    EditText mUsernameEdit;

    String mUsername;

    // TODO : add WebSocket define code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        ButterKnife.bind(this);

        // TODO : add WebSocket initialization code
    }

    // TODO : add WebSocketMessageListener

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
    }

    private boolean isValid(@NonNull String str) {
        return !str.isEmpty();
    }

    private void goToGameActivity(String username) {
        startActivity(new Intent(this, GameActivity.class).putExtra(
                IntentConstant.USERNAME, username));
    }
}