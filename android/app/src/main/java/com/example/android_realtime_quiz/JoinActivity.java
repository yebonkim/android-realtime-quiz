package com.example.android_realtime_quiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JoinActivity extends AppCompatActivity {

    public final static String EXTRA_NICKNAME = "nickname";

    @BindView(R.id.nicknameET)
    EditText nicknameET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        ButterKnife.bind(this);

    }

    private boolean isValid(String str) {
        if(str.length() == 0) {
            return false;
        }

        return true;
    }

    private void registerGame(String nickname) {
        //TODO
        startActivity(new Intent(this, GameActivity.class).putExtra(EXTRA_NICKNAME, nickname));
    }

    @OnClick(R.id.goToGameBtn)
    public void goToGameBtn() {
        String nickname = nicknameET.getText().toString();

        if(isValid(nickname)) {
            registerGame(nickname);
        } else {
            Toast.makeText(this, getString(R.string.reInput), Toast.LENGTH_SHORT).show();
        }
    }
}
