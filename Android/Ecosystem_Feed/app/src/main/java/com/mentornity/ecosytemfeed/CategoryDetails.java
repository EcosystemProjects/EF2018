package com.mentornity.ecosytemfeed;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.mentornity.ecosytemfeed.jsonConnection.FetchData;

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
    Bundle bundle;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_category_details, container, false);
        init();
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
        bundle = getArguments();
        Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.ef_app_send_show_posts1);
        //There are two json parsing for followers and posts.
        listContents = new ArrayList<>();
        String data = bundle.getString("POSTS");
        JSONArray JA = null;
        Log.d(TAG, "onCreate: JA: "+data);
        try {
            if(!data.equals("\"{}\"") && !data.equals("{}") && !data.equals("[]")) {
                JA = new JSONArray(data);
                Log.d(TAG, "onCreate: JA: " + JA);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        if (JA != null)
        {   isContentEmpty = false;
            for (int i = 0; i < JA.length(); i++) {
                try {
                    JSONObject jO = (JSONObject) JA.get(i);
                    String  title = jO.getString("title"),
                            description = jO.getString("description"),
                            category = jO.getString("category"),
                            regionAndEcosystem = jO.getString("region")+"/"+jO.getString("ecosystem"),
                            //seourl = jO.getString("seourl"),//yok
                            seourl = "seourl",
                            date = jO.getString("date"),
                            shareUrl = jO.getString("shareurl");
                    String imgUrl = "http://ecosystemfeed.com" + jO.getString("image");
                    Log.d(TAG, "onCreate: " + imgUrl);
                    //isDeleteVisible is true for only my post section
                    listContents.add(new ContentListItem(title,regionAndEcosystem, category, description, false, imgUrl,seourl, date,shareUrl));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else{
            isContentEmpty = true;
        }


        listFollowers=new ArrayList<>();
        data=bundle.getString("FOLLOWERS");
        JA= null;
        try {
            if(!data.equals("\"{}\"") && !data.equals("{}") && !data.equals("[]")) {
                JA = new JSONArray(data);
                Log.d(TAG, "onCreate: JA: " + JA);
            }
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
        final Dialog dialog = new CustomProgressDialog(getActivity(), 1);
        switch (view.getId())
            {
                case R.id.frg_category_follow_btn:

                    //!!! DO FOLLOW OR UNFOLLOW HERE !!!
                    final String isFollowed;
                    Log.d(TAG, "onClick: isFollowed: " + bundle.getBoolean("isFollowed"));
                    if(bundle.getBoolean("isFollowed")){
                        isFollowed = "unfollow";
                        followCategoryBtn.setText(getResources().getString(R.string.follow));
                        bundle.putBoolean("isFollowed",false);
                    }
                    else{
                        isFollowed = "follow";
                        followCategoryBtn.setText(getResources().getString(R.string.un_follow));
                        bundle.putBoolean("isFollowed",true);
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String url = "http://ecosystemfeed.com/Service/Web.php?process=categoriesFollow"
                                    +"&authid=" + getContext().getSharedPreferences("Login",Context.MODE_PRIVATE).getString("sessId",null)
                                    + "&catid=" + bundle.getInt("categoryId")
                                    +"&type="+ isFollowed; // !!! PUT FOLLOW OR UNFOLLOW REQEST URL HERE !!!
                            Log.d(TAG, "onClick: fetching posts URL:" + url);
                            final FetchData fetchData = new FetchData(url);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.show();//diaolog are shown while fetching.
                                }
                            });
                            fetchData.execute();
                            for (int k = 0; k < 1; ) {
                                if (fetchData.fetched || fetchData.getErrorOccured()) k++;
                            }
                            dialog.dismiss();
                        }
                    }).start();

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
                    //fragment transaction.
                    getActivity().onBackPressed();

            }
    }

}
