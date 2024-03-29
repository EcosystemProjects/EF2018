package com.mentornity.ecosytemfeed;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.mentornity.ecosytemfeed.jsonConnection.FetchData;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.ContentType;
import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Create_post_fragment extends Fragment implements View.OnClickListener {

    private static final int RESULT_LOAD_IMAGE =1 ;
    private static final int RESULT_READ_EXTERNAL_STORAGE =2 ;
    View v;
    private Button createPostBtn,myPostButton,editBtn,addBtn,sendBtn;
    private ScrollView createPostScrllView;
    private RecyclerView contentRecyclerView;
    private List<ContentListItem> listContents;
    private EditText titleEtext,descriptionEtext,linkEtext;
    private ImageView contentIv;
    private FrameLayout myPostsFl;

    private ImageButton regionIB,ecosystemIB,categoryIB;
    private TextView regionTV,ecosystemTV,categoryTV,noContentTv;
    private ListView regionLV,ecosystemLV,categoryLV;
    CustomProgressDialog dialog;

    public List<String> listRegions;
    public HashMap<String,String> mapRegion=new HashMap<>();
    public List<String> listEcosytems;
    public HashMap<String,String> mapEcosystems=new HashMap<>();
    public List<String> listCategories=new ArrayList<>();
    public HashMap<String,String> mapCategories = new HashMap<>();

    private boolean isDataEmpty;

    private Uri filePath;
    private Bitmap imageBitmap;
    public String data="";
    public String myPostsData = "";
    private String TAG="Createpost";
    public Create_post_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //////////////////////////////
        //!!! MY POSTS QUERY HERE!!!//
        // isDeleteVisible is true  //
        // for only my post section //
        //////////////////////////////

        FetchData myPostsfetchData=new FetchData("http://ecosystemfeed.com/Service/Web.php?process=getPostsMe&authid="
                + getContext().getSharedPreferences("Login",Context.MODE_PRIVATE).getString("sessId",null));
        myPostsfetchData.execute();
        while(!myPostsfetchData.fetched && !myPostsfetchData.getErrorOccured()){/*waiting to fetch*/}
        myPostsData=myPostsfetchData.getData();
        listContents = new ArrayList<>();
        JSONArray JA = null;
        try {
            JA = new JSONArray(myPostsData);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        if (JA != null)
        {   isDataEmpty = false;
            for (int i = 0; i < JA.length(); i++) {
                try {
                    JSONObject jO = (JSONObject) JA.get(i);
                    String regionAndEcosystem = jO.getString("region").toUpperCase() + "/" + jO.getString("ecosystem").toUpperCase(),
                            category = jO.getString("category"),
                            title = jO.getString("title"),
                            date = jO.getString("date"),
                            shareUrl = jO.getString("shareurl"),
                            description = jO.getString("description"),
                            seourl = jO.getString("seourl");
                    final String imgUrl = "http://ecosystemfeed.com" + jO.getString("image");
                    Log.d(TAG, "onCreate: " + imgUrl);
                    //isDeleteVisible is true for only my post section
                    listContents.add(new ContentListItem(title,regionAndEcosystem,category,description,true,imgUrl,seourl,date,shareUrl));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else{
            isDataEmpty = true;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_create_post,container,false);
        dialog=new CustomProgressDialog(getActivity(),1);
        contentRecyclerView=v.findViewById(R.id.my_contents_rv);
        noContentTv = v.findViewById(R.id.my_posts_no_content_tv);
        myPostsFl = v.findViewById(R.id.my_posts_fl);
        ContentAdapter content_Adapter=new ContentAdapter(listContents,getContext());
        contentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        contentRecyclerView.setAdapter(content_Adapter);

        init();

        return v;
    }
    public void init()
    {
        createPostBtn=v.findViewById(R.id.create_post_btn);
        myPostButton=v.findViewById(R.id.my_post_btn);
        createPostScrllView=v.findViewById(R.id.create_post_scrll_bar);
        editBtn=v.findViewById(R.id.create_post_edit_btn);
        addBtn=v.findViewById(R.id.create_post_add_btn);
        sendBtn = v.findViewById(R.id.create_post_send_btn);
        titleEtext=v.findViewById(R.id.create_post_title_et);
        descriptionEtext=v.findViewById(R.id.create_post_description_et);
        linkEtext=v.findViewById(R.id.create_post_link_et);
        contentIv=v.findViewById(R.id.create_post_content_pic_iv);

        regionTV=v.findViewById(R.id.create_post_region_tv);
        regionIB=v.findViewById(R.id.create_post_region_Ib);
        regionLV=v.findViewById(R.id.create_post_region_lv);

        ecosystemTV=v.findViewById(R.id.create_post_ecosystem_tv);
        ecosystemIB=v.findViewById(R.id.create_post_ecosystem_Ib);
        ecosystemLV=v.findViewById(R.id.create_post_ecosystem_lv);

        categoryTV=v.findViewById(R.id.create_post_category_tv);
        categoryIB=v.findViewById(R.id.create_post_category_Ib);
        categoryLV=v.findViewById(R.id.create_post_category_lv);

        createPostBtn.setOnClickListener(this);
        myPostButton.setOnClickListener(this);
        editBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
        regionTV.setOnClickListener(this);
        ecosystemTV.setOnClickListener(this);
        categoryTV.setOnClickListener(this);

        //it is to shown regions,ecosystems and categories on regionLV,ecosystemLV and categoryLV.
        showRegionEcosystemandCategories();

    }

    private void showRegionEcosystemandCategories() {
        FetchData fetchData=new FetchData("http://ecosystemfeed.com/Service/Web.php?process=getAllData");
        fetchData.execute();
        while(!fetchData.fetched){/*waiting to fetch*/}
        data=fetchData.getData();
        listRegions = new ArrayList<>();
        try {
            JSONArray JA = new JSONArray(data);
            for(int i=0;i<JA.length();i++) {
                try {
                    JSONObject jO = (JSONObject) JA.get(i);
                    String name = jO.get("name").toString();
                    String id=jO.get("id").toString();
                    if(jO.get("type").toString().compareTo("region")==0) {
                        listRegions.add(name);
                        mapRegion.put(name, id);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "init: Json Exception Error."+e.getMessage(),e);
                }
            }
            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(getContext(), R.layout.listview_item,listRegions);
            regionLV.setAdapter(arrayAdapter);
        } catch (JSONException e) {
            Log.e(TAG, "init: Json Exception"+e.getMessage(),e);
        }
        //when click item on List View, It opens next List View otomaticaly.
        regionLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                regionTV.setText(regionLV.getItemAtPosition(i).toString());
                listEcosytems=new ArrayList<>();
                try {
                    System.out.println("result:"+data);
                    JSONArray JA = new JSONArray(data);
                    for(int j=0;j<JA.length();j++) {
                        try {
                            JSONObject jO = (JSONObject) JA.get(j);
                            String name = jO.get("name").toString();
                            int selectedEcosystemGroupID= Integer.parseInt(jO.get("groupid").toString());
                            String id=jO.get("id").toString();
                            if(jO.get("type").toString().compareTo("ecosystems")==0 &&
                                    Integer.parseInt(mapRegion.get(regionTV.getText().toString()))==selectedEcosystemGroupID)
                            {
                                listEcosytems.add(name);
                                mapEcosystems.put(name, id);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "onItemClick: "+e.getMessage(),e);
                        }
                    }
                    ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(getContext(), R.layout.listview_item,listEcosytems);
                    if(listEcosytems.size()!=0) {
                        regionTV.performClick();
                        ecosystemLV.setAdapter(arrayAdapter);
                        if(ecosystemLV.getVisibility()==View.GONE)ecosystemTV.performClick();
                    }
                    else
                        Toast.makeText(getContext(),"Ecosystem bulunamadı.",Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Log.e(TAG, "onItemClick: "+e.getMessage() ,e);
                }
            }
        });
        ecosystemLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ecosystemTV.setText(ecosystemLV.getItemAtPosition(i).toString());
                listCategories=new ArrayList<>();
                try {
                    JSONArray JA = new JSONArray(data);
                    for(int j=0;j<JA.length();j++) {
                        try {
                            JSONObject jO = (JSONObject) JA.get(j);
                            String name = jO.get("name").toString();
                            String seourl = jO.get("seourl").toString();
                            int selectedCategoryGroupID= Integer.parseInt(jO.get("groupid").toString());
                            if(jO.get("type").toString().compareTo("categories")==0 &&
                                    Integer.parseInt(mapEcosystems.get(ecosystemTV.getText().toString()))==selectedCategoryGroupID)
                            {
                                listCategories.add(name);
                                mapCategories.put(name,seourl);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "onItemClick: "+e.getMessage(),e );
                        }
                    }
                    ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(getContext(), R.layout.listview_item,listCategories);
                    if(listCategories!=null)
                    {
                        ecosystemTV.performClick();
                        categoryLV.setAdapter(arrayAdapter);
                        categoryTV.performClick();
                    }
                    else
                    {
                        Toast.makeText(getContext(),"Ecosystem bulunamadı.",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "onItemClick: "+e.getMessage(),e );
                }
            }
        });
        categoryLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                categoryTV.setText(categoryLV.getItemAtPosition(i).toString());
                categoryTV.performClick();
            }
        });
    }

    private void htmlParse(final String url) {
        if(url.isEmpty())
            Toast.makeText(getContext(),getString(R.string.create_post_empty_url_error),Toast.LENGTH_SHORT).show();
        else
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.show();
                        }
                    });
                    final String[] strings=new String[3];
                    try {
                        Document doc=Jsoup.connect(url).get();
                        Elements elements=doc.select("meta[name=description]");
                        Elements img=doc.select("meta[property=og:image]");
                        strings[0]=doc.title();
                        strings[1]=elements.attr("content");
                        strings[2]=img.attr("content");
                    } catch (IOException e) {
                        Log.e(TAG, "run: errn on Html Parse "+e.getMessage(),e );
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                titleEtext.setText(strings[0]);
                                descriptionEtext.setText(strings[1]);
                                Picasso.get().load(strings[2]).into(contentIv);
                            }catch (Exception e)
                            {
                                Toast.makeText(getContext(),getString(R.string.create_post_url_error)+e.toString(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    dialog.dismiss();
                }
            }).start();
        }
    }

    private void uploadPost(){
        //!!! UPLOAD QUERY HERE!!!
        new Thread(new Runnable() {
            @Override
            public void run() {
                String uploadUrl = "http://ecosystemfeed.com/Service/Web.php?process=setPosts";
                String path = getPath(filePath);
                Log.d(TAG, "uploadPost: path: "+path);
                String uploadID = UUID.randomUUID().toString();
                Log.d(TAG, "uploadPost: uploadID: "+uploadID);
                String imageID = UUID.randomUUID().toString();
                Log.d(TAG, "uploadPost: imageID: "+imageID);
                String title = titleEtext.getText().toString();
                Log.d(TAG, "uploadPost: title: "+title);
                String seourl = mapCategories.get(categoryTV.getText().toString());
                Log.d(TAG, "uploadPost: seourl: "+seourl);
                String description = descriptionEtext.getText().toString();
                Log.d(TAG, "uploadPost: description: "+description);
                String authID = getContext().getSharedPreferences("Login",Context.MODE_PRIVATE).getString("sessId",null);
                Log.d(TAG, "uploadPost: authID: "+authID);
                try {
                    new MultipartUploadRequest(getContext(),uploadID,uploadUrl)
                            .addFileToUpload(path,"image")
                            .addParameter("authid",authID)
                            .addParameter("title",title)
                            .addParameter("seourl",seourl)
                            .addParameter("description",description)
                            .setUtf8Charset()
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(2)
                            .setDelegate(new UploadStatusDelegate() {
                                @Override
                                public void onProgress(Context context, UploadInfo uploadInfo) {
                                    dialog.show();
                                }
                                @Override
                                public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                                    Log.d(TAG, "onError: "+serverResponse.getBodyAsString());
                                }
                                @Override
                                public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                                    Log.d(TAG, "onCompleted: "+serverResponse.getBodyAsString());
                                    dialog.dismiss();
                                    Toast.makeText(context, "Post Created", Toast.LENGTH_SHORT).show();
                                    restartFragment();
                                }
                                @Override
                                public void onCancelled(Context context, UploadInfo uploadInfo) {
                                }
                            })
                            .startUpload();

                    //!!! SENDING NOTIFICATION TO FOLLOWERS !!!
                    //!!! uncomment lines below when web service is ready !!!

                    /*String notificationUrl = "http://ecosystemfeed.com/Service/Web.php?process=getFollowers&seourl="+seourl;
                    FetchData fetchData = new FetchData(notificationUrl);
                    fetchData.execute();
                    for (int k = 0; k < 1; ) {
                        if (fetchData.fetched || fetchData.getErrorOccured()) k++;
                    }
                    final List<FollowerItem> listFollowers=new ArrayList<>();
                    data = fetchData.getData();
                    JSONArray JA= null;
                    try {
                        if(!data.equals("\"{}\"") && !data.equals("{}") && !data.equals("[]")) {
                            JA = new JSONArray(data);
                            Log.d(TAG, "onCreate: JA: " + JA);
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    if(JA!=null) {
                        for (int i = 0; i < JA.length(); i++) {
                            try {
                                JSONObject jO = (JSONObject) JA.get(i);
                                String name = jO.get("name").toString().toUpperCase() + " " + jO.getString("surname").toUpperCase();
                                String profilePictureUrl = jO.get("profilepictureurl").toString();
                                String authid = jO.get("profilepictureurl").toString();
                                String oneSignalUserid = jO.get("onesignaluserid").toString();
                                listFollowers.add(new FollowerItem(profilePictureUrl, name, authid, oneSignalUserid));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    //!!! INCLUDE ONESIGNAL USERID'S, CONTENT OF NOTIFICATION AND ACTION WITH JSON
                    OneSignal.postNotification();
                */

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public String getPath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = getActivity().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

    public void restartFragment(){
        Create_post_fragment create_post_fragment = new Create_post_fragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.main_frame,create_post_fragment).addToBackStack(null);
        ft.commit();

    }

    public void activateButton(Button b)
    { b.setTextColor(getResources().getColor(R.color.black_text)); }
    public void deactivateButton(Button b)
    { b.setTextColor(getResources().getColor(R.color.grayText)); }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == RESULT_READ_EXTERNAL_STORAGE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "onRequestPermissionsResult: permission granted");
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        }
        Log.d(TAG, "onRequestPermissionsResult: ");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RESULT_LOAD_IMAGE && null != data)
        {
            filePath = data.getData();
            try {
                InputStream inInputStream=getActivity().getContentResolver().openInputStream(filePath);
                imageBitmap = BitmapFactory.decodeStream(inInputStream);
                contentIv.setImageBitmap(imageBitmap);
            } catch (FileNotFoundException e) {
                Log.e(TAG, "onActivityResult: "+e.getMessage(),e );
            }
        }
    }

    @Override
    public void onClick(View view) {
        //On click operations
        switch (view.getId())
        {
            case R.id.create_post_region_tv:
                //regionIB.startAnimation(animation);
                if(regionLV.getVisibility()==View.VISIBLE)
                {
                    regionIB.setRotation(0);
                    regionLV.setVisibility(View.GONE);
                }
                else
                {
                    regionIB.setRotation(180);
                    regionLV.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.create_post_ecosystem_tv:
                if(ecosystemLV.getVisibility()==View.VISIBLE)
                {
                    ecosystemIB.setRotation(0);
                    ecosystemLV.setVisibility(View.GONE);
                }else
                {
                    ecosystemIB.setRotation(180);
                    ecosystemLV.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.create_post_category_tv:
                if(categoryLV.getVisibility()==View.VISIBLE)
                {
                    categoryIB.setRotation(0);
                    categoryLV.setVisibility(View.GONE);
                }else
                {
                    categoryIB.setRotation(180);
                    categoryLV.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.create_post_btn:
                myPostsFl.setVisibility(View.GONE);
                createPostScrllView.setVisibility(View.VISIBLE);
                contentRecyclerView.setVisibility(View.GONE);
                activateButton(createPostBtn);
                deactivateButton(myPostButton);
                break;
            case R.id.my_post_btn:
                myPostsFl.setVisibility(View.VISIBLE);
                createPostScrllView.setVisibility(View.GONE);
                if(isDataEmpty){
                    contentRecyclerView.setVisibility(View.GONE);
                    noContentTv.setVisibility(View.VISIBLE);

                }else{
                    contentRecyclerView.setVisibility(View.VISIBLE);
                    noContentTv.setVisibility(View.GONE);
                }
                activateButton(myPostButton);
                deactivateButton(createPostBtn);
                break;
            case R.id.create_post_edit_btn:
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                try {
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                }catch (Exception e){
                    Log.e(TAG, "onClick: Error while hiding keyboard.",e);
                }
                contentIv.setImageBitmap(null);
                htmlParse(linkEtext.getText().toString());
                break;
            case R.id.create_post_add_btn:
                if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},RESULT_READ_EXTERNAL_STORAGE);
                }else {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }
                break;
            case R.id.create_post_send_btn:
                uploadPost();
                break;

        }
    }
}
