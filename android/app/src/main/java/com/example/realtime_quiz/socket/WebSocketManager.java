package com.example.realtime_quiz.socket;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.logging.HttpLoggingInterceptor;

public class WebSocketManager {
    private OkHttpClient client;
    private WebSocket socket;

    public WebSocketManager(WebSocketListener webSocketListener) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Request request = new Request.Builder().url(NetDefine.WSS_ADDRESS).build();
        socket = client.newWebSocket(request, webSocketListener);
        client.dispatcher().executorService().shutdown();
    }

    public void sendMsg(String msg) {
        socket.send(msg);
    }

    public void close() {
        socket.close(200, "Successed");
    }
}
