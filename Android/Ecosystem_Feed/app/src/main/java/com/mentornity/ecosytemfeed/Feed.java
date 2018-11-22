package com.mentornity.ecosytemfeed;


import android.os.Bundle;
import android.support.annotation.Nullable;
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
public class Feed extends Fragment {

    View v;
    private RecyclerView contentRecyclerView;
    private List<ContentListItem> listContents;
    private TextView allBtn,noContentTv;
    private AllFeed allFeed;
    CustomProgressDialog dialog;
    private boolean isDataEmty;
    private String TAG="Feed";
    public Feed() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_feed, container, false);
        noContentTv = v.findViewById(R.id.for_me_no_content_tv);
        dialog=new CustomProgressDialog(getContext(),1);
        allBtn=v.findViewById(R.id.feed_see_all_btn);
        //After data fetching,AllFeed page opens.
        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allFeed=new AllFeed();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.show();//Hata burada olabilir
                            }
                        });
                        FetchData fetchData=new FetchData("http://ecosystemfeed.com/Service/Web.php?process=getAllPosts");
                        Log.d(TAG, "run: FetchData varable created");
                        fetchData.execute();
                        for(int k=0;k<1;)
                        {
                            if(fetchData.fetched)k++;
                        }
                        Bundle bundle=new Bundle();
                        bundle.putString("ALLFEED",fetchData.getData());
                        Log.d(TAG, "run: "+bundle.toString());
                        allFeed.setArguments(bundle);
                        Log.d("this", "onNavigationItemSelected: fetched");
                        dialog.dismiss();

                        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_frame,allFeed);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }).start();
            }
        });
        contentRecyclerView=v.findViewById(R.id.for_me_contents_rv);
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
        super.onCreate(savedInstanceState);
        listContents = new ArrayList<>();
        String data = getArguments().getString("FEED");
        Log.d(TAG, "onCreate: " + data);
        //data parsing
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
                    Log.d(TAG, "onCreate: jo:" + jO);
                    String regionAndEcosystem = jO.getString("title"),
                            category = "category",
                            content = jO.getString("description"),
                            //seourl = jO.getString("seourl"),
                            seourl = "seourl";

                    final String imgUrl = "http://ecosystemfeed.com" + jO.getString("image");
                    Log.d(TAG, "onCreate: " + imgUrl);
                    //isDeleteVisible is true for only my post section
                    listContents.add(new ContentListItem(regionAndEcosystem, category, content, false, imgUrl, seourl, jO));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else{
            isDataEmty = true;
        }
    }
}
