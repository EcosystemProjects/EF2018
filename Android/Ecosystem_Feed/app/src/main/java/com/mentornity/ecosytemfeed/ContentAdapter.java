package com.mentornity.ecosytemfeed;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mentornity.ecosytemfeed.jsonConnection.FetchData;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
/*
 * Adapters are built same way.
 * You can find tutorial here:https://developer.android.com/guide/topics/ui/layout/recyclerview
 **/
//It is used in recycler view on Allfeed,feed  etc.
public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> implements View.OnClickListener {
    private List<ContentListItem> listItems;
    private ContentListItem listItem;
    private Context context;
    private ContentDetails contentDetails;
    private String userName, profilePictureUrl;
    String TAG="ContentAdapter";
    public ContentAdapter(List<ContentListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context=context;
    }

    @NonNull
    @Override
    public ContentAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).
                inflate(R.layout.content_list_item,parent,false);
        return new ViewHolder(v);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        listItem=listItems.get(position);
        holder.regionAndEcosystem.setText(listItem.getRegionAndEcosystem());
        holder.category.setText(listItem.getCategory());
        holder.post_content.setText(listItem.getDescription());
        if(listItem.getContent_img()==null&&listItem.getImgUrl()==null)
        {//if there are no image on content,no need to show image.
            holder.contentImg.setVisibility(View.GONE);
        }
        else if(listItem.getImgUrl()!=null)
        {//if there are image,load image.
            Log.d("ADAPTER", "onBindViewHolder: ");
            Picasso.get().load(listItem.getImgUrl()).into(holder.contentImg);
        }
        else
            holder.contentImg.setImageBitmap(listItem.getContent_img());
        if(listItem.getDeleteVisible())//User only delete own contents.It is true for Settings->My publications.
            holder.deleteBtn.setVisibility(View.VISIBLE);
        else
            holder.deleteBtn.setVisibility(View.GONE);

        //get username and profile picture url

        //!!! uncomment and edit lines below when web service is ready !!!
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://ecosystemfeed.com/Service/Web.php?process=???????????&seourl=" + listItem.getSeourl(); // !!! EDIT QUERY !!!
                Log.d(TAG, "onClick: fetching user data :" + url);
                FetchData fetchData = new FetchData(url);
                fetchData.execute();
                for (int k = 0; k < 1; ) {
                    if (fetchData.fetched || fetchData.getErrorOccured()) k++;
                }

                //!!!!!!!!!!!!!!!!!!!!!!!
                //!!! FETCH JSON HERE !!!
                //!!!!!!!!!!!!!!!!!!!!!!!

                userName = fetchData.getData();
                profilePictureUrl = fetchData.getData();
            }
        });
        */
        holder.deleteBtn.setOnClickListener(this);
        holder.cardView.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.content_list_item_cardview:
                Log.d(TAG, "onClick: ");
                Bundle bundle=new Bundle();
                bundle.putString("title",listItem.getTitle());
                bundle.putString("regionAndEcosystem",listItem.getRegionAndEcosystem());
                bundle.putString("description",listItem.getDescription());
                bundle.putString("image",listItem.getImgUrl());
                bundle.putString("category",listItem.getCategory());
                bundle.putString("date",listItem.getDate());
                bundle.putString("seourl",listItem.getSeourl());
                bundle.putString("shareurl",listItem.getShareUrl());
                //!!! uncomment lines below when web service ready !!!
                //bundle.putString("username",userName);
                //bundle.putString("profilePictureUrl",profilePictureUrl);
                contentDetails=new ContentDetails();
                contentDetails.setArguments(bundle);
                FragmentTransaction fragmentTransaction=((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frame,contentDetails).addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.content_delete_btn:
                //!!! DELETE OPETATIONS HERE !!!
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String url ="http://ecosystemfeed.com/Service/Web.php?process=deletePostsMe" +
                                "&authid=" + context.getSharedPreferences("Login",Context.MODE_PRIVATE).getString("sessId",null) +
                                "&seourl=" + listItem.getSeourl();
                        Log.d(TAG, "run: url: "+url);
                        FetchData fetchData = new FetchData(url);
                        fetchData.execute();
                        while(!fetchData.fetched && !fetchData.getErrorOccured()){/*waiting to fetch*/}
                        try {
                            JSONObject JO = new JSONObject(fetchData.getData());
                            Log.d(TAG, "run: "+ JO.getString("status"));
                            Log.d(TAG, "run: "+ JO.getString("Function"));
                            if(JO.getString("status").equals("Delete Operation Success")){
                                listItems.remove(listItem);
                                notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
        }
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        TextView regionAndEcosystem,category,post_content;//initializing view components.
        ImageView contentImg;
        Button deleteBtn;
        CardView cardView;
        ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.content_list_item_cardview);
            regionAndEcosystem=itemView.findViewById(R.id.region_ecosystem_tv);
            category=itemView.findViewById(R.id.category_tv);
            post_content=itemView.findViewById(R.id.content_tv);
            contentImg=itemView.findViewById(R.id.content_img);
            deleteBtn= itemView.findViewById(R.id.content_delete_btn);
        }
    }
}
