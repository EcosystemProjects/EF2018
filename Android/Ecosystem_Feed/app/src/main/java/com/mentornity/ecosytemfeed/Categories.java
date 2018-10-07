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
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Categories extends Fragment {

    View v;
    private RecyclerView categoryRecyclerView;
    private List<CategoryItem> listCategory;
    private TextView titleTv;
    private ImageButton closeIBtn;
    public  String TAG="Categories";

    public Categories() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_categories, container, false);
        categoryRecyclerView=v.findViewById(R.id.categories_rv);//Recycler view initializing.
        CategoryAdapter categoryAdapter=new CategoryAdapter(listCategory,getContext());
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        categoryRecyclerView.setAdapter(categoryAdapter);

        Bundle bundle=getArguments();//Getting data from intent.
        titleTv=v.findViewById(R.id.categories_title_tv);
        titleTv.setText(bundle.getString("title"));

        closeIBtn=v.findViewById(R.id.categories_close_btn);
        closeIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listCategory=new ArrayList<>();
        String data=getArguments().getString("Categories");
        System.out.println("DATA:"+data);
        JSONArray JA= null;
        try {
            JA = new JSONArray(data);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        if(JA!=null)//Json parsing
            for(int i=0;i<JA.length();i++)
            {
                try {
                    JSONObject jO=(JSONObject)JA.get(i);
                    String name=jO.get("name").toString();
                    int id= Integer.parseInt(jO.get("id").toString()),
                            orderIndex=Integer.parseInt(jO.get("orderindex").toString()),
                            groupid= Integer.parseInt(jO.get("groupid").toString()),
                            postsNumber=Integer.parseInt(jO.get("posts").toString()),
                            followerNumber=new JSONObject(jO.get("follower").toString()).getJSONArray("follower").length();
                    Log.d(TAG, "onCreate: "+followerNumber);
                    listCategory.add(new CategoryItem(name,postsNumber+" Posts",followerNumber+" Fallowers",false));
                    System.out.println("name:"+name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
    }
}
