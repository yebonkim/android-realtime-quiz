package com.example.realtime_quiz.view;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;


public abstract class ViewHolderFactory {
    public interface Updateable<T> {
        void update(T data);
    }
}
