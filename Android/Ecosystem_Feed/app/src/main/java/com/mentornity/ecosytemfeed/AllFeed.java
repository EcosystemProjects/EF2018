package com.mentornity.ecosytemfeed;

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
    private TextView forMeBtn;
    private Feed feed;
    private FloatingActionButton popupFab;
    public String TAG="AllFeed";

    public AllFeed() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_all_feed, container, false);
        forMeBtn=v.findViewById(R.id.allfeed_forme_btn);//it is used to start Feed screen which is followed posts are in there.
        popupFab=v.findViewById(R.id.popup_fab);
        forMeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feed=new Feed();
                FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frame,feed);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
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
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//Contents Adding
        listContents=new ArrayList<>();
        String data=getArguments().getString("ALLFEED");
        Log.d(TAG, "onCreate: "+data);
        JSONArray JA= null;
        try {
            JA = new JSONArray(data);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        if(JA!=null)
            for(int i=0;i<JA.length();i++)
            {
                try {
                    JSONObject jO=(JSONObject)JA.get(i);
                    String regionAndEcosystem=jO.getString("title"),
                            category="category",
                            content=jO.getString("description");

                    final String imgUrl="http://ecosystemfeed.com"+jO.getString("image");
                    Log.d(TAG, "onCreate: "+imgUrl);
                    //isDeleteVisible is true for only my post section
                    listContents.add(new ContentListItem(regionAndEcosystem,category,content,false,imgUrl,jO));
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
    }
}
