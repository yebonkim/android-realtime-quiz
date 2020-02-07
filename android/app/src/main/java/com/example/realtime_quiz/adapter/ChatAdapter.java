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
    private static final int VIEW_TYPE_ITEM = 1;

    private ArrayList<Chat> mData;

    public ChatAdapter() {
        this.mData = new ArrayList<>();
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
            ((ViewHolderFactory.Updateable) holder).update(mData.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_ITEM;
    }

    public void addNewChat(Chat newChat) {
        mData.add(newChat);
        notifyDataSetChanged();
    }

    public class ItemView extends RecyclerView.ViewHolder implements ViewHolderFactory.Updateable<Chat> {
        @BindView(R.id.text_username)
        TextView mUsername;
        @BindView(R.id.text_content)
        TextView mContent;

        public ItemView(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void update(final Chat data) {
            mUsername.setText(data.getUsername());
            mContent.setText(data.getContent());
        }
    }
}