package com.mentornity.ecosytemfeed;



import android.content.res.Resources;
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

import com.onesignal.OneSignal;

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
    private List<String> listFollowedCategory;
    private TextView titleTv,noContentTv;
    private ImageButton closeIBtn;
    public  String TAG="Categories";
    private boolean isDataEmpty;

    public Categories() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_categories, container, false);
        noContentTv = v.findViewById(R.id.categories_no_content_tv);
        categoryRecyclerView=v.findViewById(R.id.categories_rv);//Recycler view initializing.
        CategoryAdapter categoryAdapter=new CategoryAdapter(listCategory,getContext());
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        categoryRecyclerView.setAdapter(categoryAdapter);

        Bundle bundle=getArguments();//Getting data from intent.
        titleTv=v.findViewById(R.id.categories_title_tv);
        titleTv.setText(bundle.getString("title"));

        if(isDataEmpty){
            noContentTv.setVisibility(View.VISIBLE);
        }else{
            noContentTv.setVisibility(View.GONE);
        }

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
        listCategory = new ArrayList<>();
        listFollowedCategory = new ArrayList<>();

        String followedCategories = getArguments().getString("FollowedCategories");
        JSONArray JA = null;
        JSONObject JO = null;
        try {
            JO = new JSONObject(followedCategories);
            JA = new JSONArray(JO.getString("categories"));
            Log.d(TAG, "onCreate: JA: "+JA.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(JA != null){

            for(int i = 0; i < JA.length(); i++){
                try {
                    JSONObject JO2 = (JSONObject)JA.get(i);
                    Log.d(TAG, "onCreate: JO2: "+JO2.toString());
                    String seourl = JO2.getString("seourl");
                    listFollowedCategory.add(seourl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        String data = getArguments().getString("Categories");
        System.out.println("DATA:" + data);
        JA = null;
        try {
            JA = new JSONArray(data);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        if (JA != null)//Json parsing
        {
            isDataEmpty = false;
            Log.d("Categories", "onCreate: JA="+JA.toString());
            for (int i = 0; i < JA.length(); i++) {
                try {
                    JSONObject jO = (JSONObject) JA.get(i);
                    String name = jO.get("name").toString();
                    int id= Integer.parseInt(jO.get("id").toString()),
                            //orderIndex=Integer.parseInt(jO.get("orderindex").toString()),
                            //groupid= Integer.parseInt(jO.get("groupid").toString()),
                            postsNumber = Integer.parseInt(jO.get("posts").toString()),
                            followerNumber = new JSONObject(jO.get("follower").toString()).getJSONArray("follower").length();
                    String seourl = jO.getString("seourl");
                    Log.d(TAG, "onCreate: " + followerNumber);


                    Boolean isFollowed;
                    if(followedCategories.contains(seourl)){
                        isFollowed = true;
                    }else{
                        isFollowed = false;
                    }

                    listCategory.add(new CategoryItem(id, name, seourl, postsNumber, followerNumber, isFollowed));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else{
            isDataEmpty = true;
        }
    }
}