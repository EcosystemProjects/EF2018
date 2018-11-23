package com.mentornity.ecosytemfeed;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mentornity.ecosytemfeed.jsonConnection.FetchData;

import java.util.List;
/*
 * Adapters are built same way.
 * You can find tutorial here:https://developer.android.com/guide/topics/ui/layout/recyclerview
 **/
public class EcosystemAdapter extends RecyclerView.Adapter<EcosystemAdapter.ViewHolder>{
    private List<RegionListItem> listItems;
    private Context context;
    private Categories categories_fragment;
    Bundle bundle=new Bundle();
    public String TAG="EcosystemAdapter";

    //it is used in Ecosystemlist.java
    public EcosystemAdapter(List<RegionListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.region_list_item,parent,false);
        categories_fragment=new Categories();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final RegionListItem listItem=listItems.get(position);
        final CustomProgressDialog dialog = new CustomProgressDialog(context, 1);
        holder.title.setText(listItem.getTitle());
        holder.title.setBackgroundColor(get_color(position));
        holder.title.setTextColor(this.context.getResources().getColor(R.color.white));

        bundle.putString("title",holder.title.getText().toString());
        //on click item data fetching is done.And Categories screen opens
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View v = view;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.show();
                            }
                        });
                        String url="http://ecosystemfeed.com/Service/Web.php?process=getCategories&groupid="+String.valueOf(listItem.getId());
                        System.out.println("URL:"+url);
                        FetchData fetchData=new FetchData(url);
                        fetchData.execute();
                        for(int k=0;k<1;)
                        {
                            if(fetchData.fetched)k++;
                        }
                        bundle.putString("Categories",fetchData.getData());
                        System.out.println("DATA:"+fetchData.getData());
                        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        //!!! PUT URL HERE !!!!!!!!!!!!
                        url = "http://ecosystemfeed.com/Service/Web.php?process=getMeFollowCategories" + // CHANGE HERE
                                "&authid=" + context.getSharedPreferences("Login",Context.MODE_PRIVATE).getString("sessId",null);
                        System.out.println("URL:"+url);
                        fetchData = new FetchData(url);
                        fetchData.execute();
                        for(int k=0;k<1;)
                        {
                            if(fetchData.fetched)k++;
                        }
                        bundle.putString("FollowedCategories",fetchData.getData());
                        System.out.println("DATA:"+fetchData.getData());
                        categories_fragment.setArguments(bundle);

                        dialog.dismiss();
                        FragmentTransaction fragmentTransaction=((FragmentActivity)v.getContext()).getSupportFragmentManager().beginTransaction();
                        categories_fragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.main_frame,categories_fragment).addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }).start();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder  extends  RecyclerView.ViewHolder{

        public TextView title;
        public ViewHolder(View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.Region_title);
        }
    }
    public int get_color(int i)
    {//There are ten colors for ecosystem items.
        switch (i%11)
        {
            case 0:return this.context.getResources().getColor(R.color.color1);
            case 1:return this.context.getResources().getColor(R.color.color2);
            case 2:return this.context.getResources().getColor(R.color.color3);
            case 3:return this.context.getResources().getColor(R.color.color4);
            case 4:return this.context.getResources().getColor(R.color.color5);
            case 5:return this.context.getResources().getColor(R.color.color6);
            case 6:return this.context.getResources().getColor(R.color.color7);
            case 7:return this.context.getResources().getColor(R.color.color8);
            case 8:return this.context.getResources().getColor(R.color.color9);
            case 9:return this.context.getResources().getColor(R.color.color10);
            case 10:return this.context.getResources().getColor(R.color.color11);
            default: return this.context.getResources().getColor(R.color.color11);
        }

    }
}
