package com.example.realtime_quiz.socket;

import com.example.realtime_quiz.model.Chat;
import com.example.realtime_quiz.model.Game;

public interface WebSocketMessageListener {
    void onGameDataReceived(Game game);
    void onChatDataReceived(Chat chat);
}
