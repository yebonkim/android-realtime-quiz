package com.example.realtime_quiz.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class Chat {
    private static final String USERNAME = "username";
    private static final String CONTENT = "content";

    private String username;
    private String content;

    public Chat() {

    }

    public Chat(String username, String content) {
        this.username = username;
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    @NonNull
    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(USERNAME, username);
            jsonObject.put(CONTENT, content);
        } catch(JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    @Nullable
    private static JSONObject toJsonObject(String jsonStr) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonStr);
        } catch(JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    @Nullable
    public static Chat strToChat(String jsonStr) {
        JSONObject jsonObject = toJsonObject(jsonStr);

        if (jsonObject == null) {
            return null;
        }

        Chat newChat = new Chat();

        try {
            newChat.username = jsonObject.getString(USERNAME);
            newChat.content = jsonObject.getString(CONTENT);
        } catch(JSONException e) {
            e.printStackTrace();
            return null;
        }

        return newChat;
    }

    public String toString() {
        JSONObject jsonObject = toJsonObject();

        if (jsonObject == null) {
            return "[Error] Object to JsonObject is invalid";
        } else {
            return jsonObject.toString();
        }
    }
}
