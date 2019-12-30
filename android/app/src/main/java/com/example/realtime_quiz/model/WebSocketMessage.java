package com.example.realtime_quiz.model;

import org.json.JSONObject;

public interface WebSocketMessage<T> {
    JSONObject toJsonObject();
    T strToObj(String jsonStr);
}
