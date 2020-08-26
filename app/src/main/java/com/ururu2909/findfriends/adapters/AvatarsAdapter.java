package com.ururu2909.findfriends.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.ururu2909.findfriends.R;
import com.ururu2909.findfriends.home.HomeActivity;

import org.jetbrains.annotations.NotNull;

public class AvatarsAdapter extends RecyclerView.Adapter<AvatarsAdapter.AvatarItemViewHolder> {

    private Context context;
    private Integer[] avatars;
    private HomeActivity.AvatarListener callback;

    public AvatarsAdapter(Context context, Integer[] avatars, HomeActivity.AvatarListener callback) {
        this.context = context;
        this.avatars = avatars;
        this.callback = callback;
    }

    @NotNull
    @Override
    public AvatarItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_avatar, parent,false);
        AvatarItemViewHolder holder=new AvatarItemViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(AvatarItemViewHolder holder, int position) {
        holder.imageView.setImageResource(avatars[position]);
        holder.imageView.setOnClickListener(v -> callback.onAvatarChoosed(avatars[position]));
    }

    @Override
    public int getItemCount() {
        return avatars.length;
    }

    static class AvatarItemViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public AvatarItemViewHolder(View itemView) {
            super(itemView);
            imageView= itemView.findViewById(R.id.avatarImage);
        }
    }
}