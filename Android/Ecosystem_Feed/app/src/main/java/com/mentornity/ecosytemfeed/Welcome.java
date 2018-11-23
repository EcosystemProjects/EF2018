package com.mentornity.ecosytemfeed;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mentornity.ecosytemfeed.jsonConnection.FetchData;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Welcome extends Fragment {
    View v;
    Button letsBeginBtn;
    TextView nameTv;
    Feed feed;
    SharedPreferences sp;
    ImageView profileImage;
    CustomProgressDialog dialog;
    public Welcome() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new CustomProgressDialog(getContext(),1);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_welcome, container, false);
        letsBeginBtn=v.findViewById(R.id.welcome_letsbegin_btn);
        nameTv=v.findViewById(R.id.welcome_name_tv);
        profileImage=v.findViewById(R.id.welcome_profile_iv);
        sp=getActivity().getSharedPreferences("Login",MODE_PRIVATE);
        letsBeginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feed=new Feed();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.show();
                            }
                        });
                        FetchData fetchData=new FetchData("http://ecosystemfeed.com/Service/Web.php?process=getPostsFollow&authid="+
                                getActivity().getSharedPreferences("Login",Context.MODE_PRIVATE).getString("sessId",null));
                        Log.d("Welcome: ", "run: "+getActivity().getSharedPreferences("Login",Context.MODE_PRIVATE).getString("sessId",null));
                        fetchData.execute();
                        for(int k=0;k<1;)
                        {
                            if(fetchData.fetched)k++;
                        }
                        dialog.dismiss();
                        Bundle bundle = new Bundle();
                        bundle.putString("FEED",fetchData.getData());
                        feed.setArguments(bundle);
                        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_frame,feed);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }).start();
            }
        });
        nameTv.setText(sp.getString("name",null));

        Picasso.get().load(sp.getString("pic",null)).into(profileImage, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap imageBitmap=((BitmapDrawable)profileImage.getDrawable()).getBitmap();
                RoundedBitmapDrawable roundedBitmapDrawable=RoundedBitmapDrawableFactory.create(getResources(),imageBitmap);
                roundedBitmapDrawable.setCircular(true);
                roundedBitmapDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(),imageBitmap.getHeight())/2.0f);
                profileImage.setImageDrawable(roundedBitmapDrawable);
            }
            @Override
            public void onError(Exception e) {
                profileImage.setImageResource(R.drawable.app_icon);
            }
        });

        return v;
    }
}