package com.example.realtime_quiz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realtime_quiz.R;
import com.example.realtime_quiz.model.Chat;
import com.example.realtime_quiz.view.MyRecyclerView;
import com.example.realtime_quiz.view.ViewHolderFactory;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatAdapter extends RecyclerView.Adapter<MyRecyclerView.ViewHolder> {

    public static final int VIEW_TYPE_ITEM = 1;

    ArrayList<Chat> data;

    public ChatAdapter() {
        this.data = new ArrayList<>();
    }

    public void addNewChat(Chat newChat) {
        data.add(newChat);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view;
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                view = LayoutInflater.from(context).inflate(R.layout.viewholder_chat, parent, false);
                return new ItemView(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderFactory.Updateable) {
            ((ViewHolderFactory.Updateable) holder).update(data.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_ITEM;
    }


    public class ItemView extends RecyclerView.ViewHolder implements ViewHolderFactory.Updateable<Chat> {
        View view;
        Context context;

        @BindView(R.id.usernameTV)
        TextView usernameTV;
        @BindView(R.id.contentTV)
        TextView contentTV;

        public ItemView(View view) {
            super(view);
            this.view = view;
            context = view.getContext();
            ButterKnife.bind(this, view);
        }

        @Override
        public void update(final Chat data) {
            usernameTV.setText(data.getUsername());
            contentTV.setText(data.getContent());
        }
    }
}
