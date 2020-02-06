package com.example.realtime_quiz.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class Game implements WebSocketMessage<Game> {
    private static final String ID = "id";
    private static final String ANSWERED_WORDS = "answeredWords";
    private static final String NOW_CONSONANT = "nowConsonant";
    private static final String NOW_WORD_IDX = "nowWordIdx";

    private int id;
    private String answeredWords;
    private String nowConsonant;
    private String nowWordIdx;

    public Game() {

    }

    public Game(int id, String answeredWords, String nowConsonant, String nowWordIdx) {
        this.id = id;
        this.answeredWords = answeredWords;
        this.nowConsonant = nowConsonant;
        this.nowWordIdx = nowWordIdx;
    }

    public String getNowConsonant() {
        return nowConsonant;
    }

    @NonNull
    @Override
    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(ID, id);
            jsonObject.put(ANSWERED_WORDS, answeredWords);
            jsonObject.put(NOW_CONSONANT, nowConsonant);
            jsonObject.put(NOW_WORD_IDX, nowWordIdx);
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
    @Override
    public Game strToObj(String jsonStr) {
        JSONObject jsonObject = toJsonObject(jsonStr);

        if (jsonObject == null) {
            return null;
        }

        Game newGame = new Game();

        try {
            newGame.id = jsonObject.getInt(ID);
            newGame.answeredWords = jsonObject.getString(ANSWERED_WORDS);
            newGame.nowConsonant = jsonObject.getString(NOW_CONSONANT);
            newGame.nowWordIdx = jsonObject.getString(NOW_WORD_IDX);
        } catch(JSONException e) {
            e.printStackTrace();
            return null;
        }

        return newGame;
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