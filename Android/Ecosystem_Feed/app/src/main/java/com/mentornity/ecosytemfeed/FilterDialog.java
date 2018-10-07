package com.mentornity.ecosytemfeed;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mentornity.ecosytemfeed.jsonConnection.FetchData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilterDialog extends Dialog implements View.OnClickListener, AdapterView.OnItemClickListener {
    public Activity activity;
    public Button okBTN;
    private TextView regionTV,ecosystemTV,categoryTV;
    private ImageButton regionIB,ecosystemIB,categoryIB;
    private ListView regionLV,ecosystemLV,categoryLV;
    private String TAG="FilterDialog",data="";
    public List<String> listRegions;
    public HashMap<String,String> mapRegion=new HashMap<>();
    public List<String> listEcosytems;
    public HashMap<String,String> mapEcosystems=new HashMap<>();
    public List<String> listCategories=new ArrayList<>();

    public FilterDialog(Activity activity) {
        super(activity);
        this.activity=activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.filter_dialog);
        //filter yap.
        init();
        //fetching regions.
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


    }

    private void init() {
        okBTN=findViewById(R.id.filter_filter_btn);

        regionTV=findViewById(R.id.filter_dialog_region_tv);
        ecosystemTV=findViewById(R.id.filter_dialog_ecosystem_tv);
        categoryTV=findViewById(R.id.filter_dialog_category_tv);

        regionIB=findViewById(R.id.filter_dialog_region_Ib);
        ecosystemIB=findViewById(R.id.filter_dialog_ecosystem_Ib);
        categoryIB=findViewById(R.id.filter_dialog_category_Ib);

        regionLV=findViewById(R.id.filter_dialog_region_lv);
        ecosystemLV=findViewById(R.id.filter_dialog_ecosystem_lv);
        categoryLV=findViewById(R.id.filter_dialog_category_lv);

        regionTV.setOnClickListener(this);
        ecosystemTV.setOnClickListener(this);
        categoryTV.setOnClickListener(this);
        regionLV.setOnItemClickListener(this);
        ecosystemLV.setOnItemClickListener(this);
        categoryLV.setOnItemClickListener(this);
        okBTN.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.filter_filter_btn:
                dismiss();
                break;
            case R.id.filter_dialog_region_tv:
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
            case R.id.filter_dialog_ecosystem_tv:
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
            case R.id.filter_dialog_category_tv:
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

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId())
        {

            case R.id.filter_dialog_region_lv:
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
                break;
            case R.id.filter_dialog_ecosystem_lv:
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
                break;
            case R.id.filter_dialog_category_lv:
                categoryTV.setText(categoryLV.getItemAtPosition(i).toString());
                categoryTV.performClick();
                break;
        }
    }
}
