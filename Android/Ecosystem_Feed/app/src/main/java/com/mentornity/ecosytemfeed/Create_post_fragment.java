package com.mentornity.ecosytemfeed;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Create_post_fragment extends Fragment implements View.OnClickListener {

    private static final int RESULT_LOAD_IMAGE =1 ;
    View v;
    private Button createPostBtn,myPostButton,editBtn,addBtn;
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

    private boolean isDataEmty;

    public String data="";
    private String TAG="Createpost";
    public Create_post_fragment() {
        // Required empty public constructor
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
                            int selectedCategoryGroupID= Integer.parseInt(jO.get("groupid").toString());
                            if(jO.get("type").toString().compareTo("Categories")==0 &&
                                    Integer.parseInt(mapEcosystems.get(ecosystemTV.getText().toString()))==selectedCategoryGroupID)
                            {listCategories.add(name);}
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RESULT_LOAD_IMAGE && null != data)
        {
            Uri selectedImage = data.getData();
            //String[] filePathColumn = { MediaStore.Images.Media.DATA };

            try {
                InputStream inInputStream=getActivity().getContentResolver().openInputStream(selectedImage);
                Bitmap bitmap1=BitmapFactory.decodeStream(inInputStream);
                contentIv.setImageBitmap(bitmap1);
            } catch (FileNotFoundException e) {
                Log.e(TAG, "onActivityResult: "+e.getMessage(),e );
            }

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
                AAA
                AAA
                AAA
                AAA
                AAA
             LOOK BELOW
               AAAAAA
                AAA
                 A
        */
        //My posts contents.
        //isDeleteVisible is true for only my post section
        //it is not ready because of backend there are no query url.
        //When ready please add.
        listContents = new ArrayList<>();
        isDataEmty = true; // this will modify when data provided.

    }

    public void activateButton(Button b)
    { b.setTextColor(getResources().getColor(R.color.black_text)); }
    public void deactivateButton(Button b)
    { b.setTextColor(getResources().getColor(R.color.grayText)); }

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
                if(isDataEmty){
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
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);

        }
    }

}
