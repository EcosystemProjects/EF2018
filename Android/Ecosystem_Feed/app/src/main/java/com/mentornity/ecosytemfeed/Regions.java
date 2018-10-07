package com.mentornity.ecosytemfeed;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Regions extends Fragment {
    View v;
    private RecyclerView regionRecycleView;
    public List<RegionListItem> listRegions;
    CustomProgressDialog dialog;

    public Regions() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_ecosytems, container, false);
        //recycler view initializng
        regionRecycleView=v.findViewById(R.id.regions_rv);
        RegionAdapter region_adapter=new RegionAdapter(listRegions,getContext());
        regionRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        regionRecycleView.setAdapter(region_adapter);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("this", "onNavigationItemSelected: started");
        dialog=new CustomProgressDialog(getActivity(),1);

        listRegions=new ArrayList<>();
        String data=getArguments().getString("region");
        //data parsing
        JSONArray JA= null;
        try {
            JA = new JSONArray(data);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        for(int i=0;i<JA.length();i++)
        {
            try {
                JSONObject jO=(JSONObject)JA.get(i);
                String name=jO.get("name").toString();
                int id= Integer.parseInt(jO.get("id").toString()),
                        orderIndex=Integer.parseInt(jO.get("orderindex").toString()),
                        groupid= Integer.parseInt(jO.get("groupid").toString());
                listRegions.add(new RegionListItem(name,id,orderIndex,groupid));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}