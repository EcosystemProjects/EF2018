package com.mentornity.ecosytemfeed;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CustomProgressDialog extends Dialog {
    private ImageView imageView;
    //it is progres dialog which is created ecosystem feed logo.
    public CustomProgressDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.TransparentProgressDialog);
        WindowManager.LayoutParams windowManager=getWindow().getAttributes();
        windowManager.gravity= Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(windowManager);
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
        LinearLayout linearLayout=new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        //It can be change size with below line.
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(160,160);
        imageView=new ImageView(context);
        imageView.setBackgroundResource(R.drawable.progress_animation);
        linearLayout.addView(imageView,params);
        addContentView(linearLayout,params);
    }

    @Override
    public void show() {
        super.show();
        AnimationDrawable frameAnimation=(AnimationDrawable)imageView.getBackground();
        frameAnimation.start();
    }
}
