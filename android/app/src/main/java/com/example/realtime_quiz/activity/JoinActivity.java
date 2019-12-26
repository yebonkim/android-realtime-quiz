package com.example.realtime_quiz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.realtime_quiz.IntentConstant;
import com.example.realtime_quiz.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JoinActivity extends AppCompatActivity {
    @BindView(R.id.edit_username)
    EditText mUsernameEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_to_game)
    public void goToGame() {
        String username = mUsernameEdit.getText().toString();

        if (isValid(username)) {
            goToGameActivity(username);
        } else {
            Toast.makeText(this, getString(R.string.reInput), Toast.LENGTH_SHORT).show();
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
