package com.example.realtime_quiz.socket;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.logging.HttpLoggingInterceptor;

public class WebSocketManager {
    private OkHttpClient mClient;
    private WebSocket mSocket;

    public WebSocketManager(WebSocketListener webSocketListener) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        mClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Request request = new Request.Builder().url(NetDefine.WSS_ADDRESS).build();
        mSocket = mClient.newWebSocket(request, webSocketListener);
        mClient.dispatcher().executorService().shutdown();
    }

    public void sendMsg(String msg) {
        mSocket.send(msg);
    }

    public void close() {
        mSocket.close(200, "Successed");
    }
}
