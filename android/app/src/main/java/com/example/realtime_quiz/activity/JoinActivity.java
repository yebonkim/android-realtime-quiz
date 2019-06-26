package com.example.realtime_quiz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.realtime_quiz.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JoinActivity extends AppCompatActivity {

    public final static String EXTRA_USERNAME = "username";

    @BindView(R.id.usernameET)
    EditText usernameET;

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

    private void goToGameActivity(String username) {
        startActivity(new Intent(this, GameActivity.class).putExtra(EXTRA_USERNAME, username));
    }

    @OnClick(R.id.goToGameBtn)
    public void goToGameBtn() {
        String username = usernameET.getText().toString();

        if(isValid(username)) {
            goToGameActivity(username);
        } else {
            Toast.makeText(this, getString(R.string.reInput), Toast.LENGTH_SHORT).show();
        }
    }
}
