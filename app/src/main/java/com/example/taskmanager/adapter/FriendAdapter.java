package com.example.taskmanager.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.taskmanager.R;
import com.example.taskmanager.model.UserProfile;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendHolder> {

    private List<UserProfile> userProfiles = new ArrayList<>();
    private onItemClickListener listener;

    @NonNull
    @Override
    public FriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_profile_item, parent, false);

        return new FriendHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendHolder holder, int position) {
        UserProfile userProfile = userProfiles.get(position);
        Glide.with(holder.itemView.getContext())
                .load(Uri.parse(userProfile.getProfileUri()))
                .into(holder.userProfile);

        holder.userNickName.setText(userProfile.getNickName());
    }

    @Override
    public int getItemCount() {
        return userProfiles == null ? 0 : userProfiles.size();
    }

    public void setFriends(List<UserProfile> userProfiles) {
        this.userProfiles = userProfiles;
        notifyDataSetChanged();
    }

    public UserProfile getUserProfileAt(int position) {
        return userProfiles.get(position);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public interface onItemClickListener {
        void onItemClick(UserProfile userProfile, int position);
    }

    class FriendHolder extends RecyclerView.ViewHolder {
        private final CircleImageView userProfile;
        private final TextView userNickName;

        public FriendHolder(View itemView) {
            super(itemView);
            userProfile = itemView.findViewById(R.id.user_profile);
            userNickName = itemView.findViewById(R.id.user_nick_name);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(userProfiles.get(position), position);
                }
            });
        }
    }
}
