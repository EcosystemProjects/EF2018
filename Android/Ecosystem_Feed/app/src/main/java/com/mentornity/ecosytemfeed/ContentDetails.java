package com.mentornity.ecosytemfeed;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.linkedin.platform.LISessionManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;



/**
 * A simple {@link Fragment} subclass.
 */
public class ContentDetails extends android.support.v4.app.Fragment implements View.OnClickListener {
    View v;
    private Button shareOnlinkedin;
    private TextView regionCatTv,subcatTv,titleTv,detailTv,usernameTv,timeTv,shareLinkTv,resourceLinkTv;
    private ImageView profileIv,contentIv;
    private ImageButton closeIBtn;
    String TAG="ContentDetails";
    ClipboardManager clipboardManager;
    ClipData clipData;

    public ContentDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_content_details, container, false);

        init();
        Log.d(TAG, "onCreateView: initialized");
        Bundle bundle = getArguments();

            regionCatTv.setText(bundle.getString("regionAndEcosystem"));
            subcatTv.setText(bundle.getString("category"));
            titleTv.setText(bundle.getString("title"));
            detailTv.setText(bundle.getString("description"));
            final String imgUrl=bundle.getString("image");
        Log.d(TAG, "onCreateView: imgurl: "+imgUrl);
            Picasso.get().load(imgUrl).into(contentIv, new Callback() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "onSuccess: success");
                }

                @Override
                public void onError(Exception e) {
                    Log.d(TAG, "onError: error");
                }
            });
            timeTv.setText(bundle.getString("date"));
            shareLinkTv.setText(bundle.getString("shareurl"));

            usernameTv.setText("user");
            resourceLinkTv.setText("this.is.link.com");


        //Setting image
        Picasso.get().load(R.drawable.app_icon).into(profileIv);
        shareLinkTv.setOnClickListener(this);
        resourceLinkTv.setOnClickListener(this);
        closeIBtn.setOnClickListener(this);

        return v;
    }

    private void init() {
        //initializing view components.
        shareOnlinkedin=v.findViewById(R.id.content_details_linkedin_btn);

        regionCatTv=v.findViewById(R.id.content_details_header_region_cat_tv);
        subcatTv=v.findViewById(R.id.cantent_details_header_subcategory_tv);
        titleTv=v.findViewById(R.id.content_details_title_tv);
        detailTv=v.findViewById(R.id.content_details_detail_tv);
        usernameTv=v.findViewById(R.id.content_details_user_name_tv);
        timeTv=v.findViewById(R.id.content_details_time_tv);
        shareLinkTv=v.findViewById(R.id.content_details_share_link_tv);
        resourceLinkTv=v.findViewById(R.id.content_details_resource_link_tv);

        profileIv=v.findViewById(R.id.content_details_user_profile_iv);
        contentIv=v.findViewById(R.id.content_details_iv);

        closeIBtn=v.findViewById(R.id.content_details_close_ibtn);
    }

    @Override
    public void onClick(View view) {
        clipboardManager=(ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

        switch(view.getId())
        {
            case R.id.content_details_resource_link_tv:
                clipData=ClipData.newPlainText("text",resourceLinkTv.getText().toString());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getContext(),"Copied to ClipBoard",Toast.LENGTH_SHORT).show();
                break;
            case R.id.content_details_share_link_tv:
                clipData=ClipData.newPlainText("text",shareLinkTv.getText().toString());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getContext(),"Copied to ClipBoard",Toast.LENGTH_SHORT).show();
                break;
            case R.id.content_details_close_ibtn:
                getActivity().onBackPressed();
        }
    }
}
