package com.mentornity.ecosytemfeed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Ecosystemlist extends Fragment {
    View v;
    private RecyclerView ecosystemRecycleView;
    private List<RegionListItem> listEcosystems;

    public Ecosystemlist() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listEcosystems=new ArrayList<>();

        String data=getArguments().getString("ecosystem");
        //data parsing
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
                String name=jO.get("name").toString();
                int id= Integer.parseInt(jO.get("id").toString()),
                        orderIndex=Integer.parseInt(jO.get("orderindex").toString()),
                        groupid= Integer.parseInt(jO.get("groupid").toString());
                listEcosystems.add(new RegionListItem(name,id,orderIndex,groupid));
                System.out.println("name:"+name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_ecosystemlist,container,false);
        ecosystemRecycleView=(RecyclerView)v.findViewById(R.id.ecosystems_rv);
        EcosystemAdapter region_adapter=new EcosystemAdapter(listEcosystems,getContext());
        ecosystemRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ecosystemRecycleView.setAdapter(region_adapter);
        return v;
    }

}
