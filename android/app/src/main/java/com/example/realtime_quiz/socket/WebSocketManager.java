package com.example.realtime_quiz.socket;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.realtime_quiz.model.Chat;
import com.example.realtime_quiz.model.Game;
import com.example.realtime_quiz.model.WebSocketMessage;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.ByteString;

public class WebSocketManager {
    private static final String TAG = WebSocketManager.class.getSimpleName();

    private Game mGame;

    private OkHttpClient mClient;
    private WebSocket mSocket;
    private WebSocketMessageListener mWsMsgListener;

    private static class Singleton {
        private static final WebSocketManager instance = new WebSocketManager();
    }

    private WebSocketManager() {
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BASIC);
        mClient = new OkHttpClient.Builder()
                .addInterceptor(logger)
                .build();
        Request request = new Request.Builder().url(NetDefine.WSS_ADDRESS).build();

        mSocket = mClient.newWebSocket(request, mWebSocketListener);

        mClient.dispatcher().executorService().shutdown();
    }

    public static WebSocketManager getInstance (WebSocketMessageListener wsMsgListener) {
        Singleton.instance.setWebSocketMessageListener(wsMsgListener);
        return Singleton.instance;
    }

    @Nullable
    public Game getGame() {
        return mGame;
    }

    public void sendMsg(String msg) {
        mSocket.send(msg);
    }

    public void setWebSocketMessageListener(WebSocketMessageListener wsMsgListener) {
        mWsMsgListener = wsMsgListener;
    }

    public void close() {
        mSocket.close(200, "succeed");
        mClient.dispatcher().executorService().shutdown();
    }

    private WebSocketListener mWebSocketListener = new WebSocketListener() {
        WebSocketMessage msg;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            Log.d(TAG, "open");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            Log.d(TAG, "msg(str) : " + text);

            if (mWsMsgListener == null) {
                return;
            }

            msg = new Chat().strToObj(text);

            if (msg != null) {
                mWsMsgListener.onChatDataReceived((Chat) msg);
                return;
            }

            msg = new Game().strToObj(text);

            if (msg != null) {
                mGame = (Game) msg;
                mWsMsgListener.onGameDataReceived((Game) msg);
                return;
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            super.onMessage(webSocket, bytes);
            Log.d(TAG, "msg(byte) : " + bytes.toString());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
            Log.d(TAG, "closing");
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            Log.d(TAG, "closed");
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
            Log.d(TAG, "socket fail : " + t.getMessage());
        }
    };
}
