package com.mentornity.ecosytemfeed;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;//Be careful v4.app.FragmentTransaction must be imported.
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.mentornity.ecosytemfeed.jsonConnection.FetchData;

import java.util.List;
/*
* Adapters are built same way.
* You can find tutorial here:https://developer.android.com/guide/topics/ui/layout/recyclerview
**/
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<CategoryItem> listItems;
    private Context context;
    private CategoryDetails categoryDetails=new CategoryDetails();
    Bundle bundle=new Bundle();
    private String TAG="CategoryAdapter";

    public CategoryAdapter(List<CategoryItem> listItems, Context context) {//constructor
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.category_item,parent,false);// create a new view
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final CategoryItem listItem=listItems.get(position);
        holder.title.setText(listItem.getTitle());
        holder.posts.setText(listItem.getPostNumber());
        holder.followers.setText(listItem.getFollowerNumber());
        Log.d(TAG, "onBindViewHolder: "+holder.title.getText().toString());
        if(listItem.getIsFollowed())
            holder._un_FollowBtn.setText(Resources.getSystem().getString(R.string.follow));
        else
            holder._un_FollowBtn.setText("Unfollow");
        holder._un_FollowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                * do follow or unfollow
                **/
            }
        });
        Listener listener = new Listener(position);
        holder.title.setOnClickListener(listener);
        holder.posts.setOnClickListener(listener);
        holder.followers.setOnClickListener(listener);

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        //View are initializing
        public TextView title,posts,followers;
        public Button _un_FollowBtn;
        public ViewHolder(View v) {
            super(v);
            title=v.findViewById(R.id.category_title_tv);
            posts=v.findViewById(R.id.category_post_num_tv);
            followers=v.findViewById(R.id.category_follower_num_tv);
            _un_FollowBtn=v.findViewById(R.id.category_un_fallow_btn);
        }
    }
    //Listener created to pass position data into onClickListener
    private class Listener implements View.OnClickListener {
        private int position;
        public Listener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            //On clicking recylerView item, New fragment is created.New fragment is change depends on clicked view component.
            //It opens CategoryDetails
            final Dialog dialog = new CustomProgressDialog(context, 1);
            String url = "http://ecosystemfeed.com/Service/Web.php?process=getPosts&seourl=" + listItems.get(position).getTitle();
            Log.d(TAG, "onClick: fetching posts URL:" + url);
            final FetchData fetchData = new FetchData(url);
            dialog.show();//diaolog are shown while fetching.
            fetchData.execute();
            for (int k = 0; k < 1; ) {
                if (fetchData.fetched || fetchData.getErrorOccured()) k++;
            }
            bundle.putString("title", listItems.get(position).getTitle());
            bundle.putString("POSTS", fetchData.getData());

            Log.d(TAG, "onClick:DATA:" + fetchData.getData());
            url = "http://ecosystemfeed.com/Service/Web.php?process=getFollowers&seourl=" + listItems.get(position).getTitle();
            Log.d(TAG, "onClick: fetching followers URL:" + url);
            final FetchData fetchDataForFollowers = new FetchData(url);
            dialog.show();
            fetchDataForFollowers.execute();
            for (int k = 0; k < 1; ) {
                if (fetchDataForFollowers.fetched || fetchData.getErrorOccured()) k++;
            }
            bundle.putString("FOLLOWERS", fetchDataForFollowers.getData());

            dialog.dismiss();
            switch (view.getId()) {

                case R.id.category_title_tv:
                    bundle.putBoolean("showPosts", true);//Posts are shown, when clicked title and posts.Otherwise followers are shown.
                    break;
                case R.id.category_post_num_tv:
                    bundle.putBoolean("showPosts", true);
                    break;
                case R.id.category_follower_num_tv:
                    bundle.putBoolean("showPosts", false);
                    break;
            }//fragment transaction.
            FragmentTransaction fragmentTransaction = ((FragmentActivity) view.getContext()).getSupportFragmentManager().beginTransaction();
            categoryDetails.setArguments(bundle);
            fragmentTransaction.replace(R.id.main_frame, categoryDetails).addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}
