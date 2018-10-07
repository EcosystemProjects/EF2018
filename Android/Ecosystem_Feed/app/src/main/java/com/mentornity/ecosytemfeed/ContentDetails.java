package com.mentornity.ecosytemfeed;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
        String data=getArguments().getString(ContentAdapter.JSON_OBJECT);
        JSONObject jsonObject=null;
        try {
            jsonObject=new JSONObject(data);//be careful,import org.json.JSONObject.
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(jsonObject!=null)
        {
            regionCatTv.setText("Canada/Human Resourves");
            subcatTv.setText("Talent Management");
            try {
                titleTv.setText(jsonObject.getString("title"));
                detailTv.setText(jsonObject.getString("description"));
                final String imgUrl="http://ecosystemfeed.com"+jsonObject.getString("image");
                Picasso.get().load(imgUrl).into(contentIv);
                timeTv.setText(jsonObject.getString("date"));
                shareLinkTv.setText(jsonObject.getString("shareurl"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            usernameTv.setText("Emmet Brown");
            resourceLinkTv.setText("this.is.link.com");
        }


        //Setting image
        Bitmap img= BitmapFactory.decodeResource(getResources(),R.drawable.ef_app_send_show_posts1);
        RoundedBitmapDrawable roundedimg= RoundedBitmapDrawableFactory.create(getResources(),img);
        roundedimg.setCircular(true);
        profileIv.setImageDrawable(roundedimg);

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
