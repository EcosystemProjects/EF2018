package com.mentornity.ecosytemfeed;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
/*
 * Adapters are built same way.
 * You can find tutorial here:https://developer.android.com/guide/topics/ui/layout/recyclerview
 **/
public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.ViewHolder> {
    private List<FollowerItem> listItems;
    private Context context;
    //It is used in Category details->Followers
    public FollowerAdapter(List<FollowerItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public FollowerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.followers_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowerAdapter.ViewHolder holder, int position) {
        final  FollowerItem followerItem=listItems.get(position);
        holder.profileImage.setImageBitmap(followerItem.getProfileImage());
        holder.userName.setText(followerItem.getUserName());

    }

    @Override
    public int getItemCount() { return listItems.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView userName;//initializing
        public ImageView profileImage;
        public ViewHolder(View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.follower_username_tv);
            profileImage=itemView.findViewById(R.id.follower_profile_iv);
        }
    }
}
