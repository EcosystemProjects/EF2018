package com.mentornity.ecosytemfeed;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
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
public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {
    private List<ContentListItem> listItems;
    private Context context;
    private ContentDetails contentDetails;
    public static String JSON_OBJECT="Json Object";
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ContentListItem listItem=listItems.get(position);
        holder.regionAndEcosystem.setText(listItem.getRegionAndEcosystem());
        holder.category.setText(listItem.getCategory());
        holder.post_content.setText(listItem.getContent());
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
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                notifyItemRangeChanged(position,getItemCount());
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

            }
        });

        holder.post_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                Bundle bundle=new Bundle();
                bundle.putString(JSON_OBJECT,listItem.getJsonObject().toString());
                contentDetails=new ContentDetails();
                contentDetails.setArguments(bundle);
                FragmentTransaction fragmentTransaction=((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frame,contentDetails).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        public TextView regionAndEcosystem,category,post_content;//initializing view components.
        public ImageView contentImg;
        public Button deleteBtn;
        public ViewHolder(View itemView) {
            super(itemView);
            regionAndEcosystem=itemView.findViewById(R.id.region_ecosystem_tv);
            category=itemView.findViewById(R.id.category_tv);
            post_content=itemView.findViewById(R.id.content_tv);
            contentImg=itemView.findViewById(R.id.content_img);
            deleteBtn= itemView.findViewById(R.id.content_delete_btn);
        }
    }
}
