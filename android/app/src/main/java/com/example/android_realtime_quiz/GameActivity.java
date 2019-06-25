package com.example.android_realtime_quiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.ByteString;

public class GameActivity extends AppCompatActivity {

    public final static String EXTRA_NICKNAME = "nickname";

    private OkHttpClient client;
    WebSocket ws;

    @BindView(R.id.answerET)
    EditText answerET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Request request = new Request.Builder().url("wss://4zqvmzxfdc.execute-api.ap-northeast-2.amazonaws.com/dev").build();
        ws = client.newWebSocket(request, webSocketListener);
        client.dispatcher().executorService().shutdown();
    }

    WebSocketListener webSocketListener = new WebSocketListener() {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            Log.d("yebon", "open");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            Log.d("yebon", text);
            super.onMessage(webSocket, text);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            Log.d("yebon", "!!!!");
            super.onMessage(webSocket, bytes);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
            Log.d("Yebon", t.getMessage());
        }
    };

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

    @OnClick(R.id.sendBtn)
    public void onSendBtnClicked() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("username", "username2");
            jsonObject.put("content", answerET.getText().toString());
        } catch(JSONException e) {
            e.printStackTrace();
        }
        ws.send(jsonObject.toString());
    }
}
