package com.mentornity.ecosytemfeed;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.TextView;

import com.mentornity.ecosytemfeed.jsonConnection.FetchData;

import java.util.List;

public class RegionAdapter extends RecyclerView.Adapter<RegionAdapter.ViewHolder> {
    private List<RegionListItem> listItems;
    private Context context;
    private Ecosystemlist ecosystemlist_fragment;
    public String TAG="RegionAdapter";
    /*
     * Adapters are built same way.
     * You can find tutorial here:https://developer.android.com/guide/topics/ui/layout/recyclerview
     **/
    public RegionAdapter(List<RegionListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        final View v= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.region_list_item,viewGroup,false);
        ecosystemlist_fragment=new Ecosystemlist();

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        final RegionListItem listItem=listItems.get(position);
        viewHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Dialog dialog=new CustomProgressDialog(context,1);

                String url="http://ecosystemfeed.com/Service/Web.php?process=getEcosystem&groupid="+String.valueOf(listItem.getId());
                System.out.println("URL:"+url);
                final FetchData fetchData=new FetchData(url);
                //after fetching,Ecosystems page opens.
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.show();
                            }
                        });
                        fetchData.execute();
                        for(int k=0;k<1;)
                        {
                            if(fetchData.fetched)k++;
                        }
                        Log.d(TAG, "run: "+fetchData.fetched+" "+fetchData.getFetched());
                        Bundle bundle=new Bundle();
                        bundle.putString("ecosystem",fetchData.getData());
                        ecosystemlist_fragment.setArguments(bundle);
                        dialog.dismiss();
                        FragmentTransaction fragmentTransaction=((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_frame,ecosystemlist_fragment).addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }).start();

            }
        });
        viewHolder.title.setText(listItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title=(TextView)itemView.findViewById(R.id.Region_title);
        }
    }
}
