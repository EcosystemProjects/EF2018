package com.mentornity.ecosytemfeed;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryDetails extends Fragment implements View.OnClickListener {

    View v;
    private RecyclerView postsRecyclerView,followerRecyclerView;
    private List<ContentListItem> listContents;
    private List<FollowerItem> listFollowers;
    private Button followCategoryBtn;
    private TextView postsTV,followersTV,titleTV,noContentTv,noFollowerTv;
    private ImageButton closeIBtn;
    private String TAG="CategoryDetails";
    private Boolean isContentEmpty,isFollowersEmpty;

    public CategoryDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_category_details, container, false);

        init();
        Bundle bundle=getArguments();
        titleTV.setText(bundle.getString("title"));
        //changed only visibility and colors.
        if (bundle.getBoolean("showPosts"))
        {
            postsRecyclerView.setVisibility(View.VISIBLE);
            followerRecyclerView.setVisibility(View.GONE);
            postsTV.setTextColor(getResources().getColor(R.color.black_text));
            followersTV.setTextColor(getResources().getColor(R.color.grayText));
        }
            else
        {
            postsRecyclerView.setVisibility(View.GONE);
            followerRecyclerView.setVisibility(View.VISIBLE);
            postsTV.setTextColor(getResources().getColor(R.color.grayText));
            followersTV.setTextColor(getResources().getColor(R.color.black_text));
        }

        return v;
    }

    private void init() {
        //initialing view compenents.
        postsRecyclerView=v.findViewById(R.id.category_details_posts_rv);
        ContentAdapter content_Adapter=new ContentAdapter(listContents,getContext());
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postsRecyclerView.setAdapter(content_Adapter);

        followerRecyclerView=v.findViewById(R.id.category_details_followers_rv);
        FollowerAdapter followerAdapter=new FollowerAdapter(listFollowers,getContext());
        followerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        followerRecyclerView.setAdapter(followerAdapter);

        followCategoryBtn=v.findViewById(R.id.frg_category_follow_btn);
        closeIBtn=v.findViewById(R.id.category_details_close_ibtn);
        postsTV=v.findViewById(R.id.frg_category_posts_tv);
        followersTV=v.findViewById(R.id.frg_category_followers_tv);
        titleTV=v.findViewById(R.id.category_details_title_tv);
        noContentTv = v.findViewById(R.id.category_details_no_content_tv);
        noFollowerTv = v.findViewById(R.id.category_details_no_follower_tv);

        followCategoryBtn.setOnClickListener(this);
        postsTV.setOnClickListener(this);
        followersTV.setOnClickListener(this);
        closeIBtn.setOnClickListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.ef_app_send_show_posts1);
        //There are two json parsing for followers and posts.
        listContents = new ArrayList<>();
        String data = getArguments().getString("POSTS");
        JSONArray JA = null;
        try {
            JA = new JSONArray(data);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        if (JA != null)
        {   isContentEmpty = false;
            for (int i = 0; i < JA.length(); i++) {
                try {
                    JSONObject jO = (JSONObject) JA.get(i);
                    String regionAndEcosystem = jO.getString("region").toUpperCase() + "/" + jO.getString("ecosystem").toUpperCase(),
                            category = jO.getString("category"),
                            content = jO.getString("title");

                    final String imgUrl = "http://ecosystemfeed.com" + jO.getString("image");
                    Log.d(TAG, "onCreate: " + imgUrl);
                    //isDeleteVisible is true for only my post section
                    listContents.add(new ContentListItem(regionAndEcosystem, category, content, false, imgUrl, jO));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else{
            isContentEmpty = true;
        }


        listFollowers=new ArrayList<>();
        data=getArguments().getString("FOLLOWERS");
        JA= null;
        try {
            JA = new JSONArray(data);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        if(JA!=null) {
            isFollowersEmpty = false;
            for (int i = 0; i < JA.length(); i++) {
                try {
                    JSONObject jO = (JSONObject) JA.get(i);
                    String name = jO.get("name").toString().toUpperCase() + " " + jO.getString("surname").toUpperCase();
                    listFollowers.add(new FollowerItem(img, name));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else{
            isFollowersEmpty = true;
        }

    }

    @Override
    public void onClick(View view) {
        postsTV=v.findViewById(R.id.frg_category_posts_tv);
        followersTV=v.findViewById(R.id.frg_category_followers_tv);
        switch (view.getId())
            {
                case R.id.frg_category_follow_btn:
                    Toast.makeText(getContext(),"Follow is not defined.",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.frg_category_posts_tv:
                    followerRecyclerView.setVisibility(View.GONE);
                    noFollowerTv.setVisibility(View.GONE);
                    if (isContentEmpty){
                        postsRecyclerView.setVisibility(View.GONE);
                        noContentTv.setVisibility(View.VISIBLE);
                    }else{
                        postsRecyclerView.setVisibility(View.VISIBLE);
                        noContentTv.setVisibility(View.GONE);
                    }

                    postsTV.setTextColor(getResources().getColor(R.color.black_text));
                    followersTV.setTextColor(getResources().getColor(R.color.grayText));
                    break;
                case R.id.frg_category_followers_tv:
                    postsRecyclerView.setVisibility(View.GONE);
                    noContentTv.setVisibility(View.GONE);
                    if(isFollowersEmpty) {
                        followerRecyclerView.setVisibility(View.GONE);
                        noFollowerTv.setVisibility(View.VISIBLE);
                    }else{
                        followerRecyclerView.setVisibility(View.VISIBLE);
                        noFollowerTv.setVisibility(View.GONE);
                    }
                    postsTV.setTextColor(getResources().getColor(R.color.grayText));
                    followersTV.setTextColor(getResources().getColor(R.color.black_text));
                    break;
                case R.id.category_details_close_ibtn:
                    getActivity().onBackPressed();
            }
    }

}
