package com.example.realtime_quiz.view;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;


public abstract class ViewHolderFactory {
    public abstract RecyclerView.ViewHolder createViewHolder(ViewGroup parent, int viewType);


    public static interface Updateable<T> {
        public void update(T data);
    }
}
