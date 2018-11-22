package com.mentornity.ecosytemfeed;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mentornity.ecosytemfeed.jsonConnection.FetchData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 */
public class AllFeed extends Fragment {

    View v;
    private RecyclerView contentRecyclerView;//Recycler view for visualize recieved data.
    private List<ContentListItem> listContents;//it is to hold recieved data.
    private TextView forMeBtn ,noContentTv;
    private Feed feed;
    private FloatingActionButton popupFab;
    CustomProgressDialog dialog;
    private boolean isDataEmty;
    public String TAG="AllFeed";

    public AllFeed() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_all_feed, container, false);
        noContentTv = v.findViewById(R.id.all_feed_no_content_tv);
        forMeBtn=v.findViewById(R.id.allfeed_forme_btn);//it is used to start Feed screen which is followed posts are in there.
        popupFab=v.findViewById(R.id.popup_fab);
        dialog = new CustomProgressDialog(getContext(),1);

        forMeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feed=new Feed();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.show();
                            }
                        });
                        FetchData fetchData=new FetchData("http://ecosystemfeed.com/Service/Web.php?process=getPostsFollow&authid="+
                                getActivity().getSharedPreferences("Login",Context.MODE_PRIVATE).getString("sessId",null));
                        Log.d(TAG, "run: "+getActivity().getSharedPreferences("Login",Context.MODE_PRIVATE).getString("sessId",null));
                        fetchData.execute();
                        for(int k=0;k<1;)
                        {
                            if(fetchData.fetched)k++;
                        }
                        dialog.dismiss();
                        Bundle bundle = new Bundle();
                        bundle.putString("FEED",fetchData.getData());
                        feed.setArguments(bundle);
                        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_frame,feed);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }).start();
            }
        });
        popupFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterDialog filterDialog=new FilterDialog(getActivity());
                filterDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                filterDialog.show();
            }
        });
        Log.d(TAG, "onCreateView: recycler view initialized.");
        //Recycler view initializing.
        contentRecyclerView=v.findViewById(R.id.all_contents_rv);
        ContentAdapter content_Adapter=new ContentAdapter(listContents,getContext());
        contentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        contentRecyclerView.setAdapter(content_Adapter);

        if(isDataEmty){
            contentRecyclerView.setVisibility(View.GONE);
            noContentTv.setVisibility(View.VISIBLE);
        }else{
            contentRecyclerView.setVisibility(View.VISIBLE);
            noContentTv.setVisibility(View.GONE);
        }
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//Contents Adding
        listContents = new ArrayList<>();
        String data = getArguments().getString("ALLFEED");
        Log.d(TAG, "onCreate: " + data);
        JSONArray JA = null;
        try {
            JA = new JSONArray(data);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        if (JA != null)
        {
            isDataEmty = false;
            for (int i = 0; i < JA.length(); i++) {
                try {
                    JSONObject jO = (JSONObject) JA.get(i);
                    String regionAndEcosystem = jO.getString("title"),
                            category = "category",
                            content = jO.getString("description"),
                            //seourl = jO.getString("seourl"),
                            seourl = "seourl";

                    final String imgUrl = "http://ecosystemfeed.com" + jO.getString("image");
                    Log.d(TAG, "onCreate: " + imgUrl);
                    //isDeleteVisible is true for only my post section
                    listContents.add(new ContentListItem(regionAndEcosystem, category, content, false, imgUrl,seourl, jO));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else{
            isDataEmty = true;
        }
    }
}
