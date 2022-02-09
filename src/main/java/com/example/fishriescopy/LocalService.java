package com.example.fishriescopy;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.ToIntFunction;

import static java.lang.Package.getPackages;
import static java.util.Collections.unmodifiableList;

public class LocalService extends AppCompatActivity {

    DatabaseStore db;
//    ProgressDialog progressDialog;
    private String selectedMandalCodeStr = "";
    private int mandal_spinPosition = 0;

//    MandalAdapter mandalAdapter;

    ArrayList<String> seasonalList,perinialList,reservoirList;

    LinearLayout mseasonal_layout,mmandal_layout,mintended_layout,mintended_body_layout
            ,mintended_supplier_layout,msupplier_layout,msupplier_seed_layout,mintended_leftover_layout,
            mprimary_mandal_layout,mprimary_village_layout,mfinancial_layout,mcultural_type_layout,
    mdate_layout;

    Spinner mculture_type_selection,mfin_year_selection,mseasonlity_selection,mprimary_mandal_selection,
            mprimary_village_selection,mintended_water_body_selection,mintended_suppier_selection,mspecies_selection;

    ImageView mimageView;
    final int REQUEST_CODE_GALLERY = 999;
    ArrayList<String> imageUris;
    ArrayList<String> cultureType_list,finYear_list,seasonalSelection_list,mandalSelection_list,villageSelection_list,
            waterBody_list,supplier_list,speciesSelection_list;

    ArrayList<String> finYearCode_list,cultureTypeCode_list,seasonalSelectionCode_list,districtSelectionCode_list,mandalSelectionCode_list,villageSelectionCode_list,
            waterBodyCode_list,supplierCode_list,speciesSelectionCode_list;

    Button malert_btn,mimage_picking_btn,mfinal_submit_button;
    EditText mfingerlings_no,mtotal_kgs,mvehicleno,mtotal_fingerlings_edit;

    RecyclerView rv;
    RecyclerAdapter recyclerAdapter;
    ArrayList<SetMethods> rvlist = new ArrayList<>();

    //    Service mapping values
    JSONObject finYearJsonObject,culturalTypeJsonObject,seasonalJsonObject,mandalJsonObject,villageJsonObject,
            waterBodyJsonObject,supplierJsonObject,j2,jsonObject;
    JSONObject finYearLoopJsonObject,culturalTypeLoopJsonObject,seasonalLoopJsonObject,mandalLoopJsonObject,villageLoopJsonObject,
            waterBodyLoopJsonObject,supplierLoopJsonObject;

    ArrayAdapter finYearAdap,cultureTypeAdap,seasonalAdap,mandalAdap,villageAdap,waterBodyAdap,supplierAdap,speciesAdap;

    String finYearService,cultureTypeService,seasonalService,mandalService,villageService,waterBodyService,supplierService;

    TextView mtotal_fingerlings,merror_text,merror_text_leftover,mset_date_tv,msupplier_seed,mleftover_seed,mlogout;
    ImageView mselect_date;

    PojoClass pojoClass;
    String discode = " ",mandCode = "",villCode = " ",season = " ", year_desc = " ",
            cultCode = " ", waterBodyCode = " ", supplierCode = " ", villagename =" ";
    String totalString;
    CardView mimage_card,mfetch_card;
    String supSeed,leftOver;

    SharedPreferences preferences;

    String totalSpeciesList = " ";

    int mYear, mMonth, mDay;
    ArrayAdapter speciesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modified_seed_stocking);

        preferences = getApplicationContext().getSharedPreferences("ParsingggggXm",0);
        discode = preferences.getString("DistCode_Login"," ");
        Log.d("discodeeeeeeeee",discode);

        inItViews();

        if (db.getCultureCount() == 0){
            cultureType();
        }else {
            assignCultureSpin();
        }
        if (db.getFinancialCount() == 0){
            finYearType();
        }else {
            assignFinYearSpin();
        }
        if (db.getSeasonCount() == 0){
            seasonalType();
        }else {
            assignSeasonalSpin();
        }
        if (db.getMandalCount() == 0 ){
            mandalType();
        }else {
            assignMandalSpin();
        }
        if (db.getVillageCount() == 0 ){
            villageType();
        }else {
            assignVillageSpin(mandCode);
        }

//        waterBodyType();
        seasonalList = new ArrayList<>();
        seasonalList.add("<--Select-->");
        seasonalList.add("Catla");
        seasonalList.add("Rohu");

        perinialList= new ArrayList<>();
        perinialList.add("<--Select-->");
        perinialList.add("Catla");
        perinialList.add("Rohu");
        perinialList.add("Mrigala");
        perinialList.add("Common Carp");
    }

    private  void imagePicking(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, REQUEST_CODE_GALLERY );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            mimageView.setImageURI(imageUri);
        }
    }

    @SuppressLint("ResourceAsColor")
    private void inItViews(){

        imageUris = new ArrayList<>();
        pojoClass = new PojoClass();

        mcultural_type_layout = findViewById(R.id.cultural_type_layout);
        mculture_type_selection = findViewById(R.id.culture_type_selection);
        mfin_year_selection = findViewById(R.id.fin_year_selection);
        mseasonlity_selection = findViewById(R.id.seasonlity_selection);
        mprimary_mandal_selection = findViewById(R.id.primary_mandal_selection);
        mprimary_village_selection = findViewById(R.id.primary_village_selection);
        mintended_water_body_selection = findViewById(R.id.intended_water_body_selection);
        mintended_suppier_selection = findViewById(R.id.intended_suppier_selection);
        mdate_layout = findViewById(R.id.date_layout);

        mfinancial_layout = findViewById(R.id.financial_layout);
        mseasonal_layout = findViewById(R.id.seasonal_layout);
        mmandal_layout = findViewById(R.id.mandal_layout);
        mintended_layout = findViewById(R.id.intended_layout);
        mintended_body_layout = findViewById(R.id.intended_body_layout);
        mintended_supplier_layout = findViewById(R.id.intended_supplier_layout);
        msupplier_layout = findViewById(R.id.supplier_layout);
        msupplier_seed_layout = findViewById(R.id.supplier_seed_layout);
        mintended_leftover_layout = findViewById(R.id.intended_leftover_layout);
        mprimary_mandal_layout = findViewById(R.id.primary_mandal_layout);
        mprimary_village_layout = findViewById(R.id.primary_village_layout);

        msupplier_seed = findViewById(R.id.supplier_seed);
        mleftover_seed = findViewById(R.id.leftover_seed);

        mfinal_submit_button = findViewById(R.id.final_submit_button);
        mimage_card = findViewById(R.id.image_card);
        mfetch_card = findViewById(R.id.fetch_card);

        mimage_picking_btn = findViewById(R.id.image_picking_btn);
        mimageView = findViewById(R.id.img);

        merror_text = findViewById(R.id.error_text);
        mset_date_tv = findViewById(R.id.set_date_tv);
        merror_text_leftover = findViewById(R.id.error_text_leftover);

        mselect_date = findViewById(R.id.select_date);

        merror_text.setVisibility(View.GONE);
        merror_text_leftover.setVisibility(View.GONE);

        mlogout = findViewById(R.id.logout);

        db = new DatabaseStore(LocalService.this);
//        mandCode = pojoClass.getMandalCode();

        mlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutAlert();
            }
        });
        malert_btn = findViewById(R.id.alert_btn);
        malert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert_popup();
            }
        });

        mimage_picking_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(LocalService.this,new String[]
                        {Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_GALLERY );

//                Will go to the gallery directly to pick the image
                Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery, REQUEST_CODE_GALLERY );

//         ImagePicking will go for all the gallery contents and will provide the option to get selected
                imagePicking();
//                pickImage();

                mimageView.setVisibility(View.VISIBLE);
                mfinal_submit_button.setVisibility(View.VISIBLE);

            }
        });

        rv = findViewById(R.id.recyclerview);
        rv.setHasFixedSize(true);

        callCalendar();


       /* mfingerlings_no = findViewById(R.id.fingerlings_no);
        mtotal_kgs = findViewById(R.id.total_kgs);
        mtotal_fingerlings = findViewById(R.id.total_fingerlings);

        mfingerlings_no.addTextChangedListener(textWatcher);
        mtotal_kgs.addTextChangedListener(textWatcher);*/

    }

    private void logoutAlert(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(LocalService.this);
        builder1.setTitle("Logout....");
        builder1.setMessage("Are you sure want to Logout?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(LocalService.this, ParsingggggXm.class));
                        finish();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if ((!TextUtils.isEmpty(mfingerlings_no.getText().toString().trim())
                    && !TextUtils.isEmpty(mtotal_kgs.getText().toString().trim())
            /*|| !TextUtils.isEmpty(mtotal_fingerlings_edit.getText().toString().trim())*/)
            ) {

                int answer = 0;
                try {
                    answer = Integer.parseInt(mfingerlings_no.getText().toString().trim()) *
                            Integer.parseInt(mtotal_kgs.getText().toString().trim()) /*+
                    Integer.parseInt(mtotal_fingerlings_edit.getText().toString().trim())*/;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                Log.e("RESULT", String.valueOf(answer));
                mtotal_fingerlings.setText(String.valueOf(answer));

            }/*else if (!TextUtils.isEmpty(mtotal_fingerlings_edit.getText().toString().trim())) {

                int answer = 0;
                try {
                    answer = Integer.parseInt(mtotal_fingerlings_edit.getText().toString().trim());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                Log.e("RESULT", String.valueOf(answer));
                mtotal_fingerlings.setText(String.valueOf(answer));
            }*/else {
                mtotal_fingerlings.setText("");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void prepareRCItems(){
        recyclerAdapter = new RecyclerAdapter(LocalService.this, rvlist);
        rv.setLayoutManager(new LinearLayoutManager(LocalService.this));
        rv.setAdapter(recyclerAdapter);
    }

    private void cultureType(){

        try {
            cultureTypeService = new CulturalType().execute().get();
            culturalTypeJsonObject = new JSONObject(cultureTypeService);

            Log.i("culturalTypeService",""+cultureTypeService);
            String totalString = culturalTypeJsonObject.getString("Data");
            JSONArray jsonArray = new JSONArray(totalString);

            if (jsonArray.length() > 0){
                for (int i = 0;i < jsonArray.length();i++){
                    culturalTypeLoopJsonObject =jsonArray.getJSONObject(i);

                    Log.d("jsonArray",""+jsonArray);
                    String Fishculture_cd = culturalTypeLoopJsonObject.getString("Fishculture_cd");
                    String Fishculture_Name = culturalTypeLoopJsonObject.getString("Fishculture_Name");

                    db.insertCultureType(""+jsonArray.getJSONObject(i).getString("Fishculture_cd"),
                            ""+jsonArray.getJSONObject(i).getString("Fishculture_Name"));

                    /*db.insertCultureType(""+Fishculture_cd,
                            ""+Fishculture_Name);*/

                }
                /*if (db.getFinancialCount() == 0){
                    finYearType();
                }*/
//                callingMst();

            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        callingMst();
//        finYearType();
    }
    private void assignCultureSpin(){
        cultureType_list = new ArrayList<>();
        cultureType_list.add("<--Select-->");

        cultureTypeCode_list = new ArrayList<>();
        cultureTypeCode_list.add("0");

        ArrayList<PojoClass> stringArrayList = db.getCultureData();

        for (int i=0;i<stringArrayList.size();i++){
            cultureTypeCode_list.add(stringArrayList.get(i).getFishCultureCode());
            cultureType_list.add(stringArrayList.get(i).getFishCultureName());
        }

        cultureTypeAdap = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line,cultureType_list);
        cultureTypeAdap.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        mculture_type_selection.setAdapter(cultureTypeAdap);
        mculture_type_selection.setSelection(cultureType_list.indexOf(1));

/*//        To change the Spinner box color
        mculture_type_selection.getBackground()
                .setColorFilter(ContextCompat.
                        getColor(this, R.color.button_color), PorterDuff.Mode.SRC_ATOP);*/

        mculture_type_selection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){

                    cultCode = cultureTypeCode_list.get(position).toString();
                    Log.d("cultCode",cultCode);
                    mmandal_layout.setVisibility(View.GONE);
                    mintended_layout.setVisibility(View.GONE);
                    msupplier_layout.setVisibility(View.GONE);
                    merror_text_leftover.setVisibility(View.GONE);
                    mdate_layout.setVisibility(View.GONE);
                    malert_btn.setVisibility(View.GONE);
                    mfetch_card.setVisibility(View.GONE);
                    mimage_card.setVisibility(View.GONE);
                    mfinal_submit_button.setVisibility(View.GONE);
                    merror_text.setVisibility(View.GONE);
                    mseasonal_layout.setVisibility(View.VISIBLE);

                }else {
                    mmandal_layout.setVisibility(View.GONE);
                    mintended_layout.setVisibility(View.GONE);
                    msupplier_layout.setVisibility(View.GONE);
                    merror_text_leftover.setVisibility(View.GONE);
                    mdate_layout.setVisibility(View.GONE);
                    malert_btn.setVisibility(View.GONE);
                    mfetch_card.setVisibility(View.GONE);
                    mimage_card.setVisibility(View.GONE);
                    mfinal_submit_button.setVisibility(View.GONE);
                    merror_text.setVisibility(View.GONE);
                }
               /* mprimary_mandal_layout.setVisibility(View.GONE);
                mprimary_village_layout.setVisibility(View.GONE);
                mintended_layout.setVisibility(View.GONE);
                msupplier_layout.setVisibility(View.GONE);*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        assignFinYearSpin();
    }

    private void finYearType(){

        try {
            finYearService = new FinancialYear().execute().get();
            finYearJsonObject = new JSONObject(finYearService);

            Log.i("finYearService",""+finYearService);
            String totalString = finYearJsonObject.getString("Data");
            JSONArray jsonArray = new JSONArray(totalString);

            if (jsonArray.length() > 0){
                for (int i = 0;i < jsonArray.length();i++){
                    finYearLoopJsonObject =jsonArray.getJSONObject(i);

                    Log.d("jsonArray",""+jsonArray);
                 //   String Fishculture_cd = culturalTypeLoopJsonObject.getString("Fishculture_cd");
                   // String Fishculture_Name = culturalTypeLoopJsonObject.getString("Fishculture_Name");

                    db.insertFinancialYear(""+jsonArray.getJSONObject(i).getString("Year_Code"),
                            ""+jsonArray.getJSONObject(i).getString("Year_Desc"),
                            ""+jsonArray.getJSONObject(i).getString("Fisilyear_Desc"));

                    /*db.insertCultureType(""+Fishculture_cd,
                            ""+Fishculture_Name);*/

                }
                /*if (db.getFinancialCount() == 0){
                    finYearType();
                }*/
//                callingMst();

            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        callingMst();
//        finYearType();
    }
    private void assignFinYearSpin() {
        finYear_list = new ArrayList<>();
        finYear_list.add("<--Select-->");

        finYearCode_list = new ArrayList<>();
        finYearCode_list.add("0");

        ArrayList<PojoClass> stringArrayList = db.getFinancialData();

        for (int i=0;i<stringArrayList.size();i++){
//            cultureTypeCode_list.add(stringArrayList.get(i).getFishCultureCode());
            finYear_list.add(stringArrayList.get(i).getFinancialYear());
        }

        finYearAdap = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line,finYear_list);
        finYearAdap.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        mfin_year_selection.setAdapter(finYearAdap);

        mfin_year_selection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0){
                    year_desc = finYear_list.get(position).toString();
                    mmandal_layout.setVisibility(View.GONE);
                    mintended_layout.setVisibility(View.GONE);
                    msupplier_layout.setVisibility(View.GONE);
                    merror_text_leftover.setVisibility(View.GONE);
                    mdate_layout.setVisibility(View.GONE);
                    malert_btn.setVisibility(View.GONE);
                    mfetch_card.setVisibility(View.GONE);
                    mimage_card.setVisibility(View.GONE);
                    mfinal_submit_button.setVisibility(View.GONE);
                    merror_text.setVisibility(View.GONE);
                    mseasonal_layout.setVisibility(View.VISIBLE);

                }else {
                    mmandal_layout.setVisibility(View.GONE);
                    mintended_layout.setVisibility(View.GONE);
                    msupplier_layout.setVisibility(View.GONE);
                    merror_text_leftover.setVisibility(View.GONE);
                    mdate_layout.setVisibility(View.GONE);
                    malert_btn.setVisibility(View.GONE);
                    mfetch_card.setVisibility(View.GONE);
                    mimage_card.setVisibility(View.GONE);
                    mfinal_submit_button.setVisibility(View.GONE);
                    merror_text.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*mculture_type_selection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){
                    mseasonal_layout.setVisibility(View.VISIBLE);
                    mprimary_mandal_layout.setVisibility(View.VISIBLE);
                    mprimary_mandal_layout.setVisibility(View.VISIBLE);
                    mprimary_village_layout.setVisibility(View.VISIBLE);
                    mintended_layout.setVisibility(View.VISIBLE);
                    msupplier_layout.setVisibility(View.VISIBLE);

                }else {
                    mseasonal_layout.setVisibility(View.GONE);
                    mprimary_mandal_layout.setVisibility(View.GONE);
                }
               *//* mprimary_mandal_layout.setVisibility(View.GONE);
                mprimary_village_layout.setVisibility(View.GONE);
                mintended_layout.setVisibility(View.GONE);
                msupplier_layout.setVisibility(View.GONE);*//*
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
//        assignFinYearSpin();
    }

    private void seasonalType(){

        try {
            seasonalService = new SeasonalType().execute().get();
            finYearJsonObject = new JSONObject(seasonalService);

            Log.i("seasonalService",""+seasonalService);
            String totalString = finYearJsonObject.getString("Data");
            JSONArray jsonArray = new JSONArray(totalString);

            if (jsonArray.length() > 0){
                for (int i = 0;i < jsonArray.length();i++){
                    finYearLoopJsonObject =jsonArray.getJSONObject(i);

                    Log.d("jsonArray",""+jsonArray);
                    //   String Fishculture_cd = culturalTypeLoopJsonObject.getString("Fishculture_cd");
                    // String Fishculture_Name = culturalTypeLoopJsonObject.getString("Fishculture_Name");

                    db.insertseasional(""+jsonArray.getJSONObject(i).getString("SNo"),
                            ""+jsonArray.getJSONObject(i).getString("Seasionality"));

                    /*db.insertCultureType(""+Fishculture_cd,
                            ""+Fishculture_Name);*/

                }
                /*if (db.getFinancialCount() == 0){
                    finYearType();
                }*/
//                callingMst();

            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        callingMst();
//        finYearType();
    }
    private void assignSeasonalSpin(){
        seasonalSelection_list = new ArrayList<>();
        seasonalSelection_list.add("<---Select--->");

        seasonalSelectionCode_list = new ArrayList<>();
        seasonalSelectionCode_list.add("0");

        ArrayList<PojoClass> stringArrayList = db.getSeasonalData();

        for (int i=0;i<stringArrayList.size();i++){
            seasonalSelection_list.add(stringArrayList.get(i).getSeasonality());
            seasonalSelectionCode_list.add(stringArrayList.get(i).getSeasonalitySNo());
        }

        seasonalAdap = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line,seasonalSelection_list);
        seasonalAdap.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        mseasonlity_selection.setAdapter(seasonalAdap);

        mseasonlity_selection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){
                    mmandal_layout.setVisibility(View.VISIBLE);
                    mprimary_mandal_layout.setVisibility(View.VISIBLE);
                    mprimary_village_layout.setVisibility(View.GONE);
                    mintended_layout.setVisibility(View.GONE);
                    msupplier_layout.setVisibility(View.GONE);
                    merror_text_leftover.setVisibility(View.GONE);
                    mdate_layout.setVisibility(View.GONE);
                    malert_btn.setVisibility(View.GONE);
                    mfetch_card.setVisibility(View.GONE);
                    mimage_card.setVisibility(View.GONE);
                    mfinal_submit_button.setVisibility(View.GONE);

                    season = seasonalSelectionCode_list.get(position).toString();
//                    totalSpeciesList = String.valueOf(seasonalSelection_list.get(position).equalsIgnoreCase("Seasonal"));
                    totalSpeciesList = mseasonlity_selection.getSelectedItem().toString();
//                    one = seasonalSelection_list.get(position).toString();

//                    assignSpeciesSpin(season);
                } else {
                    mmandal_layout.setVisibility(View.GONE);
                    mintended_layout.setVisibility(View.GONE);
                    msupplier_layout.setVisibility(View.GONE);
                    merror_text_leftover.setVisibility(View.GONE);
                    mdate_layout.setVisibility(View.GONE);
                    malert_btn.setVisibility(View.GONE);
                    mfetch_card.setVisibility(View.GONE);
                    mimage_card.setVisibility(View.GONE);
                    mfinal_submit_button.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*mculture_type_selection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){

                    mseasonlity_selection.setSelection(0);
                    mseasonal_layout.setVisibility(View.VISIBLE);
                    mprimary_mandal_layout.setVisibility(View.VISIBLE);
                    mprimary_mandal_layout.setVisibility(View.VISIBLE);
                    mprimary_village_layout.setVisibility(View.VISIBLE);
                    mintended_layout.setVisibility(View.VISIBLE);
                    msupplier_layout.setVisibility(View.VISIBLE);

                }else {
                    mseasonal_layout.setVisibility(View.GONE);
                    mprimary_mandal_layout.setVisibility(View.GONE);
                }
               *//* mprimary_mandal_layout.setVisibility(View.GONE);
                mprimary_village_layout.setVisibility(View.GONE);
                mintended_layout.setVisibility(View.GONE);
                msupplier_layout.setVisibility(View.GONE);*//*
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
//        assignFinYearSpin();
    }

    private void mandalType(){

        try {
            mandalService = new Mandal().execute().get();
            mandalJsonObject = new JSONObject(mandalService);

            Log.i("seasonalService",""+seasonalService);
            String totalString = mandalJsonObject.getString("Data");
            JSONArray jsonArray = new JSONArray(totalString);

            if (jsonArray.length() > 0){
                for (int i = 0;i < jsonArray.length();i++){
                    mandalLoopJsonObject =jsonArray.getJSONObject(i);

                    Log.d("jsonArray",""+jsonArray);
                    //   String Fishculture_cd = culturalTypeLoopJsonObject.getString("Fishculture_cd");
                    // String Fishculture_Name = culturalTypeLoopJsonObject.getString("Fishculture_Name");

                        db.insertMandal("" + jsonArray.getJSONObject(i).getString("DistCode"),
                                "" + jsonArray.getJSONObject(i).getString("MandCode"),
                                "" + jsonArray.getJSONObject(i).getString("MandName"));


                    /*db.insertCultureType(""+Fishculture_cd,
                            ""+Fishculture_Name);*/

                }
                /*if (db.getFinancialCount() == 0){
                    finYearType();
                }*/
//                callingMst();

            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        callingMst();
//        finYearType();
    }
    
    private void assignMandalSpin(){
        mandalSelection_list = new ArrayList<>();
        mandalSelection_list.add("<---Select--->");

        mandalSelectionCode_list = new ArrayList<>();
        mandalSelectionCode_list.add("0");

        ArrayList<PojoClass> stringArrayList = db.getMandData(discode);

        for (int i=0;i<stringArrayList.size();i++){
            mandalSelection_list.add(stringArrayList.get(i).getMandalName());
            mandalSelectionCode_list.add(stringArrayList.get(i).getMandalCode());
        }

        mandalAdap = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line,mandalSelection_list);
        mandalAdap.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        mprimary_mandal_selection.setAdapter(mandalAdap);

        mprimary_mandal_selection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){

                   /*if (mandalSelection_list.get(position).toString().equals("MandCode"))
                       str = mandalSelectionCode_list.get(position);
                       assignVillageSpin(str);*/

                    /*pojoClass = mandalAdapter.getItem(position);
                    new Village().execute(""+pojoClass.getVillCode());*/

                    mprimary_village_layout.setVisibility(View.VISIBLE);
                    mintended_layout.setVisibility(View.GONE);
                    msupplier_layout.setVisibility(View.GONE);
                    merror_text_leftover.setVisibility(View.GONE);
                    mdate_layout.setVisibility(View.GONE);
                    malert_btn.setVisibility(View.GONE);
                    mfetch_card.setVisibility(View.GONE);
                    mimage_card.setVisibility(View.GONE);
                    mfinal_submit_button.setVisibility(View.GONE);

                    mandCode = mandalSelectionCode_list.get(position).toString();
                    Log.d("mandCode",mandCode);
                    assignVillageSpin(mandCode);

//                        mandCode = mandalSelectionCode_list.get(position);

//                    str.toString();
//                    if (disCode == pojoClass.getDistCode())
//                    mandString = db.pojoClass.getMandalCode();

                    /*mandCode = parent.getSelectedItem().toString();
                    assignVillageSpin(mandCode);*/
                   /* for (int i=0;i<stringArrayList.size();i++){
                        mandCodee = mandalSelectionCode_list.get(i);
                        assignVillageSpin(mandCodee);
                    }*/

                   /* mandCodee = mandalSelectionCode_list.get(position);
                    assignVillageSpin();*/
                   /* mandCodee = parent.getSelectedItem().toString();
                    assignVillageSpin(mandCodee);*/
                       /* if (mandalSelectionCode_list.get(position).toString().equalsIgnoreCase("MandCode"))
                            assignVillageSpin(mandalSelection_list);*/
                }else {
                    mintended_layout.setVisibility(View.GONE);
                    msupplier_layout.setVisibility(View.GONE);
                    merror_text_leftover.setVisibility(View.GONE);
                    mdate_layout.setVisibility(View.GONE);
                    malert_btn.setVisibility(View.GONE);
                    mfetch_card.setVisibility(View.GONE);
                    mimage_card.setVisibility(View.GONE);
                    mfinal_submit_button.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void villageType(){

        try {
            villageService = new Village().execute().get();
            villageJsonObject = new JSONObject(villageService);

            Log.i("villageService",""+villageService);
            String totalString = villageJsonObject.getString("Data");
            JSONArray jsonArray = new JSONArray(totalString);

            if (jsonArray.length() > 0){
                for (int i = 0;i < jsonArray.length();i++){
                   /* mandalLoopJsonObject =jsonArray.getJSONObject(i);

                    Log.d("jsonArray",""+jsonArray);*/
                    //   String Fishculture_cd = culturalTypeLoopJsonObject.getString("Fishculture_cd");
                    // String Fishculture_Name = culturalTypeLoopJsonObject.getString("Fishculture_Name");

                    db.insertVillage(""+jsonArray.getJSONObject(i).getString("DistCode"),
                            ""+jsonArray.getJSONObject(i).getString("MandCode"),
                            ""+jsonArray.getJSONObject(i).getString("VillCode"),
                            ""+jsonArray.getJSONObject(i).getString("VillName"));

                    /*db.insertCultureType(""+Fishculture_cd,
                            ""+Fishculture_Name);*/

                }
                /*if (db.getFinancialCount() == 0){
                    finYearType();
                }*/
//                callingMst();

            }


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        callingMst();
//        finYearType();
    }
    private void assignVillageSpin(String mandCode){

        villageSelection_list = new ArrayList<String>();
        villageSelection_list.add("<---Select--->");

        villageSelectionCode_list = new ArrayList<>();
        villageSelectionCode_list.add("0");

//        ArrayList<PojoClass> stringArrayList = db.getVillData("01",mandCodee);
        ArrayList<PojoClass> stringArrayList = db.getVillData(discode,mandCode);
        Log.d("stringArrayList",""+stringArrayList);
        for (int i=0;i<stringArrayList.size();i++){
            villageSelection_list.add(stringArrayList.get(i).getVillName());
            Log.d("villageSelection_list",""+villageSelection_list);
            villageSelectionCode_list.add(stringArrayList.get(i).getVillCode());
        }

        villageAdap = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line,villageSelection_list);
        villageAdap.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        mprimary_village_selection.setAdapter(villageAdap);

        mprimary_village_selection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("purushotham_position","position - "+position);
                Log.v("purushotham","position - "+villageSelectionCode_list.get(position));
                if (position > 0){

                    mintended_layout.setVisibility(View.VISIBLE);
                    mintended_body_layout.setVisibility(View.VISIBLE);
                    mintended_supplier_layout.setVisibility(View.GONE);
                    msupplier_layout.setVisibility(View.GONE);
                    merror_text_leftover.setVisibility(View.GONE);
                    mdate_layout.setVisibility(View.GONE);
                    malert_btn.setVisibility(View.VISIBLE);
                    mfetch_card.setVisibility(View.GONE);
                    mimage_card.setVisibility(View.GONE);
                    mfinal_submit_button.setVisibility(View.GONE);

                    villCode = villageSelectionCode_list.get(position).toString();
                    Log.d("villCode",villCode);
                    new WaterBodyMST(LocalService.this,"VillageMaster")
                            .execute(""+villCode);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void parsingWaterBodyDataResp(String response){

        waterBody_list = new ArrayList<>();
        waterBody_list.add("<-- Select -->");

        waterBodyCode_list = new ArrayList<>();
        waterBodyCode_list.add("");



        if (Utility.showLogs == 0)
            Log.d("response: ", "parsingWaterBodyDataRespJSONResp response: " + response.toString());
//        progressDialog.dismiss();

        try {
            jsonObject = new JSONObject(response);

//                if (jsonObject.getInt("SuccessFlag") == 1){
            if (jsonObject.getString("SuccessFlag").equalsIgnoreCase("1")){
                totalString = jsonObject.getString("Data");
                JSONArray jsonArray = new JSONArray(totalString);

                for (int i = 0;i < jsonArray.length();i++){
                    waterBody_list.add(jsonArray.getJSONObject(i).getString("wb_name"));
                    waterBodyCode_list.add(jsonArray.getJSONObject(i).getString("wb_code"));

                }
                waterBodyAdap = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_dropdown_item_1line,waterBody_list);
                waterBodyAdap.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
                mintended_water_body_selection.setAdapter(waterBodyAdap);

                mintended_water_body_selection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position > 0){

                            mintended_layout.setVisibility(View.VISIBLE);
                            mintended_supplier_layout.setVisibility(View.VISIBLE);
                            msupplier_layout.setVisibility(View.GONE);
                            malert_btn.setVisibility(View.GONE);
                            mdate_layout.setVisibility(View.GONE);
                            mimage_card.setVisibility(View.GONE);
                            mfetch_card.setVisibility(View.GONE);
                            mfinal_submit_button.setVisibility(View.GONE);
                            merror_text_leftover.setVisibility(View.GONE);

                            waterBodyCode = waterBodyCode_list.get(position).toString();
                            new IntentedSupplierMST(LocalService.this,"GetWaterbodySupplierlist")
                                    .execute(""+waterBodyCode);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }else {
                malert_btn.setVisibility(View.GONE);
                mdate_layout.setVisibility(View.GONE);
                msupplier_layout.setVisibility(View.GONE);
                mintended_body_layout.setVisibility(View.GONE);
                mintended_supplier_layout.setVisibility(View.GONE);
                mintended_layout.setVisibility(View.GONE);
                mimage_card.setVisibility(View.GONE);
                mfetch_card.setVisibility(View.GONE);
                mfinal_submit_button.setVisibility(View.GONE);
                merror_text.setVisibility(View.GONE);
                merror_text_leftover.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void parsingSupplierDataResp(String response){

        supplier_list = new ArrayList<>();
        supplier_list.add("<-- Select -->");

        supplierCode_list = new ArrayList<>();
        supplierCode_list.add("");

        if (Utility.showLogs == 0)
            Log.d("response: ", "parsingSupplierDataRespJSONResp response: " + response.toString());
//        progressDialog.dismiss();

        try {
            jsonObject = new JSONObject(response);

                if (jsonObject.getInt("SuccessFlag") == 1){
//            if (jsonObject.getString("SuccessFlag").equalsIgnoreCase("1")){
                totalString = jsonObject.getString("Data");
                JSONArray jsonArray = new JSONArray(totalString);
                for (int i = 0;i < jsonArray.length();i++){
                    supplier_list.add(jsonArray.getJSONObject(i).getString("Supplier_name"));
                    supplierCode_list.add(jsonArray.getJSONObject(i).getString("Supplier_code"));
                }
                supplierAdap = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_dropdown_item_1line,supplier_list);
                supplierAdap.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
                mintended_suppier_selection.setAdapter(supplierAdap);

                mintended_suppier_selection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position > 0){

                            msupplier_layout.setVisibility(View.VISIBLE);
                            msupplier_seed_layout.setVisibility(View.VISIBLE);
                            mintended_leftover_layout.setVisibility(View.VISIBLE);
                            malert_btn.setVisibility(View.GONE);
                            mdate_layout.setVisibility(View.VISIBLE);
                            mimage_card.setVisibility(View.GONE);
                            mfetch_card.setVisibility(View.GONE);
                            mfinal_submit_button.setVisibility(View.GONE);
                            merror_text_leftover.setVisibility(View.GONE);

                            supplierCode = supplierCode_list.get(position).toString();
                           new GetWaterbodySupplierIndenting(LocalService.this,"parsingWaterBodySupplierDataResp")
                                   .execute(""+supplierCode);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }else {
                malert_btn.setVisibility(View.GONE);
                msupplier_layout.setVisibility(View.GONE);
                mimage_card.setVisibility(View.GONE);
                mfetch_card.setVisibility(View.GONE);
                mfinal_submit_button.setVisibility(View.GONE);
                mdate_layout.setVisibility(View.GONE);
                merror_text.setVisibility(View.GONE);
                merror_text_leftover.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void parsingWaterBodySupplierDataResp(String response){

        if (Utility.showLogs == 0)
            Log.d("response: ", "parsingWaterBodySupplierDataResp response: " + response.toString());
//        progressDialog.dismiss();

        try {
            jsonObject = new JSONObject(response);

//                if (jsonObject.getInt("SuccessFlag") == 1){
            if (jsonObject.getString("SuccessFlag").equalsIgnoreCase("1")){

                totalString = jsonObject.getString("Data");
                JSONArray jsonArray = new JSONArray(totalString);
                for (int i = 0;i < jsonArray.length();i++){

                    supSeed = jsonArray.getJSONObject(i).getString("FishSeedIndented");
                    leftOver = jsonArray.getJSONObject(i).getString("Stocking_Left");

                }
                msupplier_seed.setText(supSeed);
                mleftover_seed.setText(leftOver);
            }else {
                malert_btn.setVisibility(View.GONE);
                msupplier_layout.setVisibility(View.GONE);
                mimage_card.setVisibility(View.GONE);
                mfetch_card.setVisibility(View.GONE);
                mfinal_submit_button.setVisibility(View.GONE);
                mdate_layout.setVisibility(View.GONE);
                merror_text.setVisibility(View.GONE);
                merror_text_leftover.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void callCalendar(){

        mselect_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date date = new Date();
                final Calendar c = Calendar.getInstance();
                c.setTime(date);

                c.add(Calendar.DAY_OF_MONTH,0);
                long maxDate = c.getTime().getTime();

                        /*c.add(Calendar.DAY_OF_MONTH,-1);
                        long minDate = c.getTime().getTime();*/

                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                c.add(Calendar.DAY_OF_MONTH,-1);
                long minDate = c.getTime().getTime();

                DatePickerDialog datePickerDialog = new DatePickerDialog(LocalService.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

//                                mset_date_tv.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                mset_date_tv.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                if (!mset_date_tv.getText().toString().isEmpty()){
                                    malert_btn.setVisibility(View.VISIBLE);
                                }else {
                                    malert_btn.setVisibility(View.GONE);
                                }

                            }
                        }
                        , mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(maxDate);
                datePickerDialog.getDatePicker().setMinDate(minDate);
                datePickerDialog.show();

            }
        });
    }

    private void speciesSelection(ArrayList<String> totalSpeciesList){

        speciesAdap = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line,totalSpeciesList);
        speciesAdap.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        mspecies_selection.setAdapter(speciesAdap);
    }

    public void alert_popup(){

        Button mclose_btn,msubmit_btn;
//        TextView mtxt_no_of_fingerlings,mtxt_total_kgs,mtxt_total_no_kgs;

        final  AlertDialog.Builder alert_dialog = new AlertDialog.Builder(LocalService.this);
        alert_dialog.setCancelable(false);
        LayoutInflater inflater = LocalService.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_popup,null);
        alert_dialog.setView(view);

        final AlertDialog alertDialog = alert_dialog.create();

        mclose_btn = view.findViewById(R.id.close_btn);
        msubmit_btn = view.findViewById(R.id.submit_btn);
        mfingerlings_no = view.findViewById(R.id.fingerlings_no);
        mtotal_kgs = view.findViewById(R.id.total_kgs);
        mvehicleno = view.findViewById(R.id.vehicleno);
        mspecies_selection = view.findViewById(R.id.species_selection);
        mtotal_fingerlings = view.findViewById(R.id.total_fingerlings);
        mtotal_fingerlings_edit = view.findViewById(R.id.total_fingerlings_edit);
        /*mtxt_no_of_fingerlings = view.findViewById(R.id.txt_no_of_fingerlings);
        mtxt_total_kgs = view.findViewById(R.id.txt_total_kgs);
        mtxt_total_no_kgs = view.findViewById(R.id.txt_total_no_kgs);*/
//       LinearLayout mtotal_kg_layout = view.findViewById(R.id.total_kg_layout);
       LinearLayout m_other_catla = view.findViewById(R.id.other_catla);
//       LinearLayout vehic = view.findViewById(R.id.list_modulee);
//        mseasonlity_selection = view.findViewById(R.id.seasonlity_selection);

        mfingerlings_no.addTextChangedListener(textWatcher);
        mtotal_kgs.addTextChangedListener(textWatcher);
        mtotal_fingerlings_edit.addTextChangedListener(textWatcher);

        /*mfingerlings_no.addTextChangedListener(textWatcher);
        mtotal_kgs.addTextChangedListener(textWatcher);*/

        /*if (mtotal_fingerlings_edit.length() > 0){
            mtotal_fingerlings.setText(mtotal_fingerlings_edit.getText().toString());
        }else{
            mtotal_fingerlings.setText("");
        }*/

        if (totalSpeciesList.equals("Seasonal")){

            speciesSelection(seasonalList);
            mtotal_fingerlings_edit.setVisibility(View.VISIBLE);
            mfingerlings_no.setVisibility(View.VISIBLE);
            mtotal_fingerlings.setVisibility(View.VISIBLE);
            mtotal_kgs.setVisibility(View.VISIBLE);
            mvehicleno.setVisibility(View.VISIBLE);
            msubmit_btn.setVisibility(View.VISIBLE);
            mclose_btn.setVisibility(View.VISIBLE);
            /*mtxt_no_of_fingerlings.setVisibility(View.GONE);
            mtxt_total_kgs.setVisibility(View.GONE);
            mtxt_total_no_kgs.setVisibility(View.GONE);
            vehic.setVisibility(View.GONE);*/
            /*mfingerlings_no.setText("0");
            mtotal_kgs.setText("0");*/
//            mtotal_kg_layout.setVisibility(View.GONE);
        }else {
//            m_other_catla.setVisibility(View.GONE);
            speciesSelection(perinialList);
            mtotal_fingerlings_edit.setVisibility(View.GONE);
            mfingerlings_no.setVisibility(View.VISIBLE);
            mtotal_fingerlings.setVisibility(View.VISIBLE);
            mtotal_kgs.setVisibility(View.VISIBLE);
            mvehicleno.setVisibility(View.GONE);
            msubmit_btn.setVisibility(View.VISIBLE);
            mclose_btn.setVisibility(View.VISIBLE);
            mtotal_fingerlings_edit.setText("0");
        }

       /* mspecies_selection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0){

                    if (totalSpeciesList.equals("Seasonal")){

                        *//*speciesSelection(seasonalList);

                        mtotal_fingerlings_edit.setVisibility(View.VISIBLE);
                        mfingerlings_no.setVisibility(View.GONE);
                        mtotal_fingerlings.setVisibility(View.GONE);
                        mtotal_kgs.setVisibility(View.GONE);
                        mvehicleno.setVisibility(View.GONE);
                        msubmit_btn.setVisibility(View.VISIBLE);
                        mclose_btn.setVisibility(View.VISIBLE);
                        mtxt_no_of_fingerlings.setVisibility(View.GONE);
                        mtxt_total_kgs.setVisibility(View.GONE);
                        mtxt_total_no_kgs.setVisibility(View.GONE);
                        vehic.setVisibility(View.GONE);
                        mfingerlings_no.setText("0");
                        mtotal_kgs.setText("0");*//*

                    }else {
//            m_other_catla.setVisibility(View.GONE);
                      *//*  speciesSelection(perinialList);
                        mtotal_fingerlings_edit.setVisibility(View.GONE);
                        mfingerlings_no.setVisibility(View.VISIBLE);
                        mtotal_fingerlings.setVisibility(View.VISIBLE);
                        mtotal_kgs.setVisibility(View.VISIBLE);
                        mvehicleno.setVisibility(View.GONE);
                        msubmit_btn.setVisibility(View.VISIBLE);
                        mclose_btn.setVisibility(View.VISIBLE);
                        mtotal_fingerlings_edit.setText("0");*//*
                    }
                    *//*if (totalSpeciesList.equalsIgnoreCase("Seasonal")) {
                        speciesSelection(seasonalList);
//                        mtotal_kg_layout.setVisibility(View.GONE);
                    }else {
                        speciesSelection(perinialList);
//                        mtotal_fingerlings_edit.setVisibility(View.GONE);
                    }*//*

                }*//*else {
                    mtotal_fingerlings_edit.setVisibility(View.GONE);
                    mfingerlings_no.setVisibility(View.GONE);
                    mtotal_fingerlings.setVisibility(View.GONE);
                    mtotal_kgs.setVisibility(View.GONE);
                    mtotal_kgs.setVisibility(View.GONE);
                    mvehicleno.setVisibility(View.GONE);
                    msubmit_btn.setVisibility(View.GONE);
                    mclose_btn.setVisibility(View.GONE);
                }*//*
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        msubmit_btn.setOnClickListener(new View.OnClickListener() {
            SetMethods setMethods;
            @Override
            public void onClick(View v) {
                if (mfingerlings_no.getText().toString().isEmpty()){
                    mfingerlings_no.setError("Please enter the fingerlings");
                }else if (mtotal_kgs.getText().toString().isEmpty()){
                    mtotal_kgs.setError("Please enter the Number of kgs");
                }else if (mvehicleno.getText().toString().isEmpty()){
                    mvehicleno.setError("Please enter the Vehicle Number");

                }else if (rvlist.size() > 0) {

                    /*Optional< SetMethods > maxValue = rvlist
                            .stream().reduce((partialResult, currentValue) -> Math.max(partialResult, currentValue));
                    List<SetMethods> somList = new SetMethods();
                    BigDecimal result= somList.stream().map(SetMethods::getTotal_fingerlings).reduce(
                            BigDecimal.ZERO, BigDecimal::);
                    for(int i = 0; i < rvlist.size(); i++){
                        if (!(rvlist.get(i).getTotal_kgs() == null)){
                            SetMethods myObject = rvlist.get(i);
                            total += myObject.getTotal_kgs(); // create getter in your MyObject class
                            Log.i("total_kgs",""+total);
                        }

                    }*/

//          comparing the Vehicle number with existing vehicle number if it equals then break the values(list) there itself

    for (int i = 0; i < rvlist.size(); i++) {
       if (rvlist.get(i).getFishtype().equalsIgnoreCase(mspecies_selection.getSelectedItem().toString().trim()) &&
          rvlist.get(i).getVehicle().equalsIgnoreCase(mvehicleno.getText().toString().trim())) {

           int totalSum = 0;
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
               totalSum = rvlist.stream().mapToInt(new ToIntFunction<SetMethods>() {
                   @Override
                   public int applyAsInt(SetMethods setMethods) {
                       return Integer.parseInt(setMethods.getTotal_kgs());
                   }
               }).sum();
           }
           if (totalSum > Integer.parseInt(leftOver)){
                 Log.i("totalSum : ",""+totalSum);
              Toast.makeText(LocalService.this,
                      "You don't have enough stock, Please check the details.",
                      Toast.LENGTH_LONG).show();

                            }
     Toast.makeText(getApplicationContext(),
             "The Vehicle Number "+mvehicleno.getText().toString()+" Already Exists",
             Toast.LENGTH_LONG).show();
              break;
                        }
                    }

      mfetch_card.setVisibility(View.VISIBLE);
      mimage_card.setVisibility(View.VISIBLE);
      mimageView.setVisibility(View.GONE);

//    mtotal_fingerlings.setText(mtotal_fingerlings_edit.getText().toString());
      setMethods = new SetMethods(

      mspecies_selection.getSelectedItem().toString().trim(),
      mfingerlings_no.getText().toString().trim(),
      mtotal_kgs.getText().toString().trim(),
      mtotal_fingerlings.getText().toString().trim(),
      mvehicleno.getText().toString().trim()
      );

      rvlist.add(setMethods);
      Log.i("size",""+rvlist.size());
      if (rvlist.size() > 1){

          Log.i("size",""+rvlist.size());
          recyclerAdapter.notifyItemChanged(rvlist.size()-1);
      }else{
          prepareRCItems();
      }

        Log.i("sssssssss",""+rvlist.size());

//      recyclerAdapter.notifyItemInserted(rvlist.size() - 1);
      } else {
       Log.i("ssssssssssssssss",""+rvlist.size());
       mfetch_card.setVisibility(View.VISIBLE);
       mimage_card.setVisibility(View.VISIBLE);
       mimageView.setVisibility(View.GONE);

//    mtotal_fingerlings.setText(mtotal_fingerlings_edit.getText().toString());
       setMethods = new SetMethods(
        mspecies_selection.getSelectedItem().toString().trim(),
        mfingerlings_no.getText().toString().trim(),
        mtotal_kgs.getText().toString().trim(),
        mtotal_fingerlings.getText().toString().trim(),
        mvehicleno.getText().toString().trim()
        );

        rvlist.add(setMethods);
        Log.i("size",""+rvlist.size());
        if (rvlist.size() > 1){
          Log.i("size",""+rvlist.size());
            recyclerAdapter.notifyItemChanged(rvlist.size()-1);
              }else{
            prepareRCItems();
                    }

//           recyclerAdapter.notifyItemInserted(rvlist.size() - 1);

                }

                /*Iterator<SetMethods> itr = rlist.iterator();
                while (itr.hasNext()) {
                    SetMethods element = itr.next();

                       *//* if (element.equals(mvehicleno.getText().toString())){
                            mvehicleno.setError("Please check the vehicle number");
                        }*//*

                    Log.i("element",""+element);
                }*/
                alertDialog.cancel();
            }



           /* public  ArrayList<SetMethods> removeDuplicates(ArrayList<SetMethods> list) {

                // Create a new ArrayList
                ArrayList<SetMethods> newList = new ArrayList<SetMethods>();

                // Traverse through the first list
                for (SetMethods element : list) {

                    // If this element is not present in newList
                    // then add it
                    if (!newList.contains(element)) {

                        newList.add(element);
                    }
                }

                // return the new list
                return newList;
            }*/

           /* List<SetMethods> one = new ArrayList<>(rvlist);

            String item = rvlist.toString();

            String selected = rvlist.get(one.indexOf(item)).getVehicle();
                if (!one.contains(selected)) {

                //add item in second ListView

                arr2.add(selected);

            } else {
                show popup
            }*/


        });

        mclose_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        alertDialog.show();
//        assignSpeciesSpin(season);
    }

    private void assignSpeciesSpin(String season){
        seasonalSelection_list = new ArrayList<String>();
        seasonalSelection_list.add("<---Select--->");

        seasonalSelectionCode_list = new ArrayList<>();
        seasonalSelectionCode_list.add("0");

//        ArrayList<PojoClass> stringArrayList = db.getVillData("01",mandCodee);
        ArrayList<PojoClass> stringArrayList = db.getVillData(discode,mandCode);
        Log.d("stringArrayList",""+stringArrayList);
        for (int i=0;i<stringArrayList.size();i++){
            seasonalSelection_list.add(stringArrayList.get(i).getVillName());
            Log.d("villageSelection_list",""+seasonalSelection_list);
            seasonalSelection_list.add(stringArrayList.get(i).getVillCode());
        }

        seasonalAdap = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line,seasonalSelection_list);
        seasonalAdap.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        mprimary_village_selection.setAdapter(seasonalAdap);

        mprimary_village_selection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("purushotham","position - "+position);
                Log.v("purushotham","position - "+villageSelectionCode_list.get(position));
                if (position > 0){
                    mintended_water_body_selection.setSelection(0);
                    villCode = villageSelectionCode_list.get(position).toString();
                    new WaterBodyMST(LocalService.this,"GetIndentedWaterbody").execute(""+villCode);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /*private void waterBodyType(){
        waterBody_list = new ArrayList<>();
        waterBody_list.add("<-- Select -->");

        waterBodyCode_list = new ArrayList<>();
        waterBodyCode_list.add("<-- Select -->");

        try {
            waterBodyService = new WaterBody().execute().get();
            Log.d("waterBodyService",""+waterBodyService);
            waterBodyJsonObject = new JSONObject(waterBodyService);

//            Log.i("waterBodyService",""+waterBodyService);
            String totalString = waterBodyJsonObject.getString("Data");
            JSONArray jsonArray = new JSONArray(totalString);

            for (int i = 0;i < jsonArray.length();i++){

                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                waterBody_list.add(jsonObject1.getString(""));
                waterBodyAdap = new ArrayAdapter<String>
                        (LocalService.this, android.R.layout.simple_spinner_item, waterBody_list);
                mintended_water_body_selection.setAdapter(waterBodyAdap);

                if (jsonObject1.getInt("SuccessFlag") == 0){
                    mintended_layout.setVisibility(View.GONE);
                    mimage_card.setVisibility(View.GONE);
                    mfetch_card.setVisibility(View.GONE);
                    mfinal_submit_button.setVisibility(View.GONE);

                  Toast.makeText(LocalService.this,"You don't have any Water Bodies in this Village",
                            Toast.LENGTH_LONG).show();
                }else {
                    mintended_layout.setVisibility(View.VISIBLE);
                    mimage_card.setVisibility(View.VISIBLE);
                    mfetch_card.setVisibility(View.VISIBLE);
                    mfinal_submit_button.setVisibility(View.VISIBLE);
                }

            }
            *//*if (jsonArray.length() > 0){
                for (int i = 0;i < jsonArray.length();i++){
                    mandalLoopJsonObject =jsonArray.getJSONObject(i);

                    *//**//*Log.d("jsonArray",""+jsonArray);*//**//*
                    //   String Fishculture_cd = culturalTypeLoopJsonObject.getString("Fishculture_cd");
                    // String Fishculture_Name = culturalTypeLoopJsonObject.getString("Fishculture_Name");

                    *//**//*db.insertVillage(""+jsonArray.getJSONObject(i).getString("DistCode"),
                            ""+jsonArray.getJSONObject(i).getString("MandCode"),
                            ""+jsonArray.getJSONObject(i).getString("VillCode"),
                            ""+jsonArray.getJSONObject(i).getString("VillName"));*//**//*

                    *//**//*db.insertCultureType(""+Fishculture_cd,
                            ""+Fishculture_Name);*//**//*

                }
                *//**//*if (db.getFinancialCount() == 0){
                    finYearType();
                }*//**//*
//                callingMst();

            }*//*

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        callingMst();
//        finYearType();
    }*/

    public class CulturalType extends AsyncTask<String,String,String> {

        @SuppressLint("LongLogTag")
        @Override
        protected String doInBackground(String... strings) {

            SoapObject request = new SoapObject(Utility.NAMESPACE, Utility.METHODNAME_CultureTypeMST);
            request.addProperty("WS_UserName",Utility.wsusername);
            request.addProperty("WS_Password",Utility.wspassword);
            request.addProperty("MobileVersion",Utility.getVersionNameCode(LocalService.this));
            Log.d("aredqw","aftfedr");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
            Log.d("requestCulture",""+request);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            Log.d("call","aftfedr");
            HttpTransportSE transport = new HttpTransportSE(Utility.URL);

            Log.d("call",""+transport);
            try {
                Log.d("Bedfolred","aftfedr");
                transport.call(Utility.SOAPaCTION_CultureTypeMST,envelope);

                Log.d("aftfedr","aftfedr");
            } catch (IOException e) {
                Log.d("eddd",""+e);
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            SoapObject response = (SoapObject) envelope.bodyIn;
            Log.d("response",""+response);
            SoapPrimitive result = (SoapPrimitive) response.getProperty("CultureTypeMstResult");
            Log.d("result_culture",""+result);
            cultureTypeService = result.toString();
            Log.d("cultureTypeServiceReturn",cultureTypeService);
            return result.toString();
        }
    }

    public class FinancialYear extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(Utility.NAMESPACE, Utility.METHODNAME_FinYearMST);

            request.addProperty("WS_UserName",Utility.wsusername);
            request.addProperty("WS_Password",Utility.wspassword);
            request.addProperty("MobileVersion",Utility.getVersionNameCode(LocalService.this));

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(Utility.URL);
            try {
                transport.call(Utility.SOAPaCTION_FinYearMST,envelope);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            SoapObject response = (SoapObject) envelope.bodyIn;
            SoapPrimitive result = (SoapPrimitive) response.getProperty("FinYearMSTResult");
            Log.d("result_financial",""+result);
            finYearService = result.toString();
            return result.toString();
        }
    }

    public class SeasonalType extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(Utility.NAMESPACE, Utility.METHODNAME_SeasonalTypeMST);

            request.addProperty("WS_UserName",Utility.wsusername);
            request.addProperty("WS_Password",Utility.wspassword);
            request.addProperty("MobileVersion",Utility.getVersionNameCode(LocalService.this));

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(Utility.URL);
            try {
                transport.call(Utility.SOAPaCTION_SeasonalTypeMST,envelope);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            SoapObject response = (SoapObject) envelope.bodyIn;
            Log.d("response",""+response);
            SoapPrimitive result = (SoapPrimitive) response.getProperty("SeasonalityMstResult");
            Log.d("result_Seasonal",""+result);
            seasonalService = result.toString();
            return result.toString();
        }
    }

    public class Mandal extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(Utility.NAMESPACE, Utility.METHODNAME_MandalMST);

            request.addProperty("WS_UserName",Utility.wsusername);
            request.addProperty("WS_Password",Utility.wspassword);
            request.addProperty("MobileVersion",Utility.getVersionNameCode(LocalService.this));

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(Utility.URL);
            try {
                transport.call(Utility.SOAPaCTION_MandalMST,envelope);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            SoapObject response = (SoapObject) envelope.bodyIn;
            Log.d("response",""+response);
            SoapPrimitive result = (SoapPrimitive) response.getProperty("MandalMasterResult");
            Log.d("result_Mandal",""+result);
            seasonalService = result.toString();
            return result.toString();
        }
    }

    public class Village extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(Utility.NAMESPACE, Utility.METHODNAME_VillageMST);
            request.addProperty("WS_UserName",Utility.wsusername);
            request.addProperty("WS_Password",Utility.wspassword);
            request.addProperty("MobileVersion",Utility.getVersionNameCode(LocalService.this));

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(Utility.URL);
            try {
                transport.call(Utility.SOAPaCTION_VillageMST,envelope);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            SoapObject response = (SoapObject) envelope.bodyIn;
            SoapPrimitive result = (SoapPrimitive) response.getProperty("VillageMasterResult");
            Log.d("result_Village",""+result);
            villageService = result.toString();
            Log.d("VillageMasterResult",villageService);
            return result.toString();
        }
    }

    public class WaterBodyMST extends AsyncTask<String, Void, String> {

        private LocalService mainActivity;
        private String activityVal = "0";
        private final String TAG = WaterBodyMST.class.getSimpleName();

        public WaterBodyMST(LocalService activity, String val) {
            this.mainActivity = activity;
            activityVal = val;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

       /* if (activityVal.equalsIgnoreCase(Utility.distStr) ||
                activityVal.equalsIgnoreCase(Utility.mandStr)) {*/
            // Check network available.
            if (!Utility.isNetworkAvailable(mainActivity)) {
                mainActivity.onError("Network error");
            }
//         }

        }

        @Override
        protected String doInBackground(String... strings) {

            Log.d("brequest","request");
            SoapObject request = null;
            String returnSoapObj = null;

            //if (activityVal.equalsIgnoreCase(Utility.distStr))
            request = new SoapObject(Utility.NAMESPACE, Utility.METHODNAME_WaterbodyMst);

            Log.d("request",""+request);

        /*if (activityVal.equalsIgnoreCase(Utility.mandStr))
            request = new SoapObject(Utility.WSDL_TARGET_NAMESPACE, Utility.OPERATION_NAME_GetMandMst_Data);*/

            request.addProperty("Distcode",discode);
            request.addProperty("Mandalcd",mandCode);
            request.addProperty("villcode",villCode);
            request.addProperty("Seasonnalitycd",season);
            request.addProperty("year",year_desc);
            request.addProperty("CultureType",cultCode);
            request.addProperty("WS_UserName",Utility.wsusername);
            request.addProperty("WS_Password",Utility.wspassword);
            request.addProperty("MobileVersion",Utility.getVersionNameCode(LocalService.this));
//        request.addProperty("GetSheepGroundState", Utility.OPERATION_NAME_GetDistMst_Data);

            String Url = Utility.URL;

//        if (activityVal.equalsIgnoreCase("GetDistMst_Data"))
//        if (activityVal.equalsIgnoreCase("District") )
//            Url = Utility.Url;

//            if (Utility.showLogs == 0)
            Log.d(TAG, "requestService: " + request);

//         if (activityVal.equalsIgnoreCase(Utility.distStr))
//        if (activityVal.equalsIgnoreCase("District"))
            returnSoapObj = getXMLResult(request, Url,
                    "" + Utility.SOAPaCTION_WaterbodyMst);
        /*else if (activityVal.equalsIgnoreCase(Utility.mandStr))
            returnSoapObj = getXMLResult(request, Url,
                    "" + Utility.SOAP_ACTION_GetMandMst_Data);*/
            return returnSoapObj;
        }

        /**
         * get service result and catching exceptions
         *
         * @param request
         * @param url
         * @param soapAction
         * @return
         */
        protected String getXMLResult(SoapObject request, String url, String soapAction) {
            try {
                return getServiceResult(url, soapAction, request);
            } catch (SoapFault e) {
                if (Utility.showLogs == 0)
                    e.printStackTrace();
                return null;
            } catch (XmlPullParserException e) {
                if (Utility.showLogs == 0)
                    e.printStackTrace();
                return null;
            } catch (IOException e) {
                if (Utility.showLogs == 0)
                    e.printStackTrace();
                return null;
            } catch (Exception e) {
                if (Utility.showLogs == 0)
                    e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            j2 = new JSONObject();
            Log.d("purushotham_response", "" + response);
            if (response == null) {
                Log.d("purushotham_response", "null - " + response);
                mainActivity.onError("Getting Data Error");
            } /*else if (activityVal.equalsIgnoreCase(Utility.distStr) *//*||
                activityVal.equalsIgnoreCase(Utility.mandStr)*//*) {
//        } else if (activityVal.equalsIgnoreCase("District") ) {

            mainActivity.parsingDistDataResp(response);

        }*/ else {
                Log.d("purushotham_response", "not null - " + response);
                /*try {
                 *//*if (j2.getInt("SuccessFlag") == 0){
                        mintended_layout.setVisibility(View.GONE);
                        mimage_card.setVisibility(View.GONE);
                        mfetch_card.setVisibility(View.GONE);
                        mfinal_submit_button.setVisibility(View.GONE);

                        Toast.makeText(LocalService.this,"You don't have any Water Bodies in this Village",
                                Toast.LENGTH_LONG).show();
                    }else {
                        mintended_layout.setVisibility(View.VISIBLE);
                        mimage_card.setVisibility(View.VISIBLE);
                        mfetch_card.setVisibility(View.VISIBLE);
                        mfinal_submit_button.setVisibility(View.VISIBLE);

                    }*//*
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

//             mainActivity.onError("Getting Data Error");
                mainActivity.parsingWaterBodyDataResp(response);
            }
        }

        /**
         * Get result (list of soap objects) from web service
         *
         * @param strURL
         * @param strSoapAction
         * @param request
         * @return
         * @throws XmlPullParserException
         * @throws IOException
         */
        public String getServiceResult(String strURL, String strSoapAction, SoapObject request)
                throws XmlPullParserException, IOException {
            // Create envelope
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            // Set output SOAP object
            envelope.setOutputSoapObject(request);

            // Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(strURL);
            androidHttpTransport.debug = true;

            SoapObject response;

            String response1;

            // StringBuffer result = null;

            System.setProperty("http.keepAlive", "false");

            try {
                // Invoke web service
                androidHttpTransport.call(strSoapAction, envelope);

                // Get the response
                response1 = envelope.getResponse().toString();
           /*response = (SoapObject) envelope.getResponse();
           StringBuffer result;
           result = new StringBuffer(response.toString());*/

                if (Utility.showLogs == 0)
                    Log.d(TAG, response1);

            } catch (SoapFault e) {
                if (Utility.showLogs == 0)
                    Log.e(TAG, "SoapFaultException");
                throw e;
            } catch (XmlPullParserException e) {
                if (Utility.showLogs == 0)
                    Log.e(TAG, "XmlPullParserException");
                throw e;
            } catch (IOException e) {
                if (Utility.showLogs == 0)
                    Log.e(TAG, "IOException");
                throw e;
            } catch (Exception e) {
                if (Utility.showLogs == 0)
                    Log.e(TAG, "HttpResponseException");
                throw e;
            }
            return response1;
        }

    }

    public class IntentedSupplierMST extends AsyncTask<String, Void, String> {

        private LocalService mainActivity;
        private String activityVal = "0";
        private final String TAG = IntentedSupplierMST.class.getSimpleName();

        public IntentedSupplierMST(LocalService activity, String val) {
            this.mainActivity = activity;
            activityVal = val;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

       /* if (activityVal.equalsIgnoreCase(Utility.distStr) ||
                activityVal.equalsIgnoreCase(Utility.mandStr)) {*/
            // Check network available.
            if (!Utility.isNetworkAvailable(mainActivity)) {
                mainActivity.onError("Network error");
            }
//         }

        }

        @Override
        protected String doInBackground(String... strings) {

            SoapObject request = null;
            String returnSoapObj = null;

            //if (activityVal.equalsIgnoreCase(Utility.distStr))
            request = new SoapObject(Utility.NAMESPACE, Utility.METHODNAME_Supplierlist);

        /*if (activityVal.equalsIgnoreCase(Utility.mandStr))
            request = new SoapObject(Utility.WSDL_TARGET_NAMESPACE, Utility.OPERATION_NAME_GetMandMst_Data);*/

            request.addProperty("waterCode",waterBodyCode);
            request.addProperty("year",year_desc);
            request.addProperty("CultureType",cultCode);
            request.addProperty("WS_UserName",Utility.wsusername);
            request.addProperty("WS_Password",Utility.wspassword);
            request.addProperty("MobileVersion",Utility.getVersionNameCode(LocalService.this));
//        request.addProperty("GetSheepGroundState", Utility.OPERATION_NAME_GetDistMst_Data);

            String Url = Utility.URL;

//        if (activityVal.equalsIgnoreCase("GetDistMst_Data"))
//        if (activityVal.equalsIgnoreCase("District") )
//            Url = Utility.Url;

//            if (Utility.showLogs == 0)
            Log.d(TAG, "request: " + request);

//         if (activityVal.equalsIgnoreCase(Utility.distStr))
//        if (activityVal.equalsIgnoreCase("District"))
            returnSoapObj = getXMLResult(request, Url,
                    "" + Utility.SOAPaCTION_Supplierlist);
        /*else if (activityVal.equalsIgnoreCase(Utility.mandStr))
            returnSoapObj = getXMLResult(request, Url,
                    "" + Utility.SOAP_ACTION_GetMandMst_Data);*/
            return returnSoapObj;
        }

        /**
         * get service result and catching exceptions
         *
         * @param request
         * @param url
         * @param soapAction
         * @return
         */
        protected String getXMLResult(SoapObject request, String url, String soapAction) {
            try {
                return getServiceResult(url, soapAction, request);
            } catch (SoapFault e) {
                if (Utility.showLogs == 0)
                    e.printStackTrace();
                return null;
            } catch (XmlPullParserException e) {
                if (Utility.showLogs == 0)
                    e.printStackTrace();
                return null;
            } catch (IOException e) {
                if (Utility.showLogs == 0)
                    e.printStackTrace();
                return null;
            } catch (Exception e) {
                if (Utility.showLogs == 0)
                    e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            j2 = new JSONObject();
            Log.d("purushothamss", "" + response);
            if (response == null) {
                Log.d("purushotham", "null - " + response);
                mainActivity.onError("Getting Data Error");
            } /*else if (activityVal.equalsIgnoreCase(Utility.distStr) *//*||
                activityVal.equalsIgnoreCase(Utility.mandStr)*//*) {
//        } else if (activityVal.equalsIgnoreCase("District") ) {

            mainActivity.parsingDistDataResp(response);

        }*/ else {
                Log.d("purushothamss", "not null - " + response);

                mainActivity.parsingSupplierDataResp(response);
            }
        }

        /**
         * Get result (list of soap objects) from web service
         *
         * @param strURL
         * @param strSoapAction
         * @param request
         * @return
         * @throws XmlPullParserException
         * @throws IOException
         */
        public String getServiceResult(String strURL, String strSoapAction, SoapObject request)
                throws XmlPullParserException, IOException {
            // Create envelope
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            // Set output SOAP object
            envelope.setOutputSoapObject(request);

            // Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(strURL);
            androidHttpTransport.debug = true;

            SoapObject response;

            String response1;

            // StringBuffer result = null;

            System.setProperty("http.keepAlive", "false");

            try {
                // Invoke web service
                androidHttpTransport.call(strSoapAction, envelope);

                // Get the response
                response1 = envelope.getResponse().toString();
           /*response = (SoapObject) envelope.getResponse();
           StringBuffer result;
           result = new StringBuffer(response.toString());*/

                if (Utility.showLogs == 0)
                    Log.d(TAG, response1);

            } catch (SoapFault e) {
                if (Utility.showLogs == 0)
                    Log.e(TAG, "SoapFaultException");
                throw e;
            } catch (XmlPullParserException e) {
                if (Utility.showLogs == 0)
                    Log.e(TAG, "XmlPullParserException");
                throw e;
            } catch (IOException e) {
                if (Utility.showLogs == 0)
                    Log.e(TAG, "IOException");
                throw e;
            } catch (Exception e) {
                if (Utility.showLogs == 0)
                    Log.e(TAG, "HttpResponseException");
                throw e;
            }
            return response1;
        }

    }

    public class GetWaterbodySupplierIndenting extends AsyncTask<String, Void, String> {

        private LocalService mainActivity;
        private String activityVal = "0";
        private final String TAG = IntentedSupplierMST.class.getSimpleName();

        public GetWaterbodySupplierIndenting(LocalService activity, String val) {
            this.mainActivity = activity;
            activityVal = val;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

       /* if (activityVal.equalsIgnoreCase(Utility.distStr) ||
                activityVal.equalsIgnoreCase(Utility.mandStr)) {*/
            // Check network available.
            if (!Utility.isNetworkAvailable(mainActivity)) {
                mainActivity.onError("Network error");
            }
//         }

        }

        @Override
        protected String doInBackground(String... strings) {

            SoapObject request = null;
            String returnSoapObj = null;

            //if (activityVal.equalsIgnoreCase(Utility.distStr))
            request = new SoapObject(Utility.NAMESPACE, Utility.METHODNAME_GetWaterbodySupplierIndenting);

        /*if (activityVal.equalsIgnoreCase(Utility.mandStr))
            request = new SoapObject(Utility.WSDL_TARGET_NAMESPACE, Utility.OPERATION_NAME_GetMandMst_Data);*/

            request.addProperty("waterCode",waterBodyCode);
            request.addProperty("suppliercd",supplierCode);
            request.addProperty("year",year_desc);
            request.addProperty("CultureType",cultCode);
            request.addProperty("WS_UserName",Utility.wsusername);
            request.addProperty("WS_Password",Utility.wspassword);
            request.addProperty("MobileVersion",Utility.getVersionNameCode(LocalService.this));
//        request.addProperty("GetSheepGroundState", Utility.OPERATION_NAME_GetDistMst_Data);

            String Url = Utility.URL;

//        if (activityVal.equalsIgnoreCase("GetDistMst_Data"))
//        if (activityVal.equalsIgnoreCase("District") )
//            Url = Utility.Url;

//            if (Utility.showLogs == 0)
            Log.d(TAG, "request: " + request);

//         if (activityVal.equalsIgnoreCase(Utility.distStr))
//        if (activityVal.equalsIgnoreCase("District"))
            returnSoapObj = getXMLResult(request, Url,
                    "" + Utility.SOAPaCTION_GetWaterbodySupplierIndenting);
        /*else if (activityVal.equalsIgnoreCase(Utility.mandStr))
            returnSoapObj = getXMLResult(request, Url,
                    "" + Utility.SOAP_ACTION_GetMandMst_Data);*/
            return returnSoapObj;
        }

        /**
         * get service result and catching exceptions
         *
         * @param request
         * @param url
         * @param soapAction
         * @return
         */
        protected String getXMLResult(SoapObject request, String url, String soapAction) {
            try {
                return getServiceResult(url, soapAction, request);
            } catch (SoapFault e) {
                if (Utility.showLogs == 0)
                    e.printStackTrace();
                return null;
            } catch (XmlPullParserException e) {
                if (Utility.showLogs == 0)
                    e.printStackTrace();
                return null;
            } catch (IOException e) {
                if (Utility.showLogs == 0)
                    e.printStackTrace();
                return null;
            } catch (Exception e) {
                if (Utility.showLogs == 0)
                    e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            j2 = new JSONObject();
            Log.d("purushothamss", "" + response);
            if (response == null) {
                Log.d("purushotham", "null - " + response);
                mainActivity.onError("Getting Data Error");
            } /*else if (activityVal.equalsIgnoreCase(Utility.distStr) *//*||
                activityVal.equalsIgnoreCase(Utility.mandStr)*//*) {
//        } else if (activityVal.equalsIgnoreCase("District") ) {

            mainActivity.parsingDistDataResp(response);

        }*/ else {
                Log.d("purushothamss", "not null - " + response);

                mainActivity.parsingWaterBodySupplierDataResp(response);
            }
        }

        /**
         * Get result (list of soap objects) from web service
         *
         * @param strURL
         * @param strSoapAction
         * @param request
         * @return
         * @throws XmlPullParserException
         * @throws IOException
         */
        public String getServiceResult(String strURL, String strSoapAction, SoapObject request)
                throws XmlPullParserException, IOException {
            // Create envelope
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            // Set output SOAP object
            envelope.setOutputSoapObject(request);

            // Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(strURL);
            androidHttpTransport.debug = true;

            SoapObject response;

            String response1;

            // StringBuffer result = null;

            System.setProperty("http.keepAlive", "false");

            try {
                // Invoke web service
                androidHttpTransport.call(strSoapAction, envelope);

                // Get the response
                response1 = envelope.getResponse().toString();
           /*response = (SoapObject) envelope.getResponse();
           StringBuffer result;
           result = new StringBuffer(response.toString());*/

                if (Utility.showLogs == 0)
                    Log.d(TAG, response1);

            } catch (SoapFault e) {
                if (Utility.showLogs == 0)
                    Log.e(TAG, "SoapFaultException");
                throw e;
            } catch (XmlPullParserException e) {
                if (Utility.showLogs == 0)
                    Log.e(TAG, "XmlPullParserException");
                throw e;
            } catch (IOException e) {
                if (Utility.showLogs == 0)
                    Log.e(TAG, "IOException");
                throw e;
            } catch (Exception e) {
                if (Utility.showLogs == 0)
                    Log.e(TAG, "HttpResponseException");
                throw e;
            }
            return response1;
        }

    }

    /*public class WaterBody extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            Log.d("brequest","brequest");
            SoapObject request = new SoapObject(Utility.NAMESPACE, Utility.METHODNAME_WaterbodyMst);

            request.addProperty("Distcode",discode);
            request.addProperty("Mandalcd",mandCode);
            request.addProperty("villcode",villCode);
            request.addProperty("Seasonnalitycd",season);
            request.addProperty("year",year_desc);
            request.addProperty("CultureType",cultCode);
            request.addProperty("WS_UserName",Utility.wsusername);
            request.addProperty("WS_Password",Utility.wspassword);
            request.addProperty("MobileVersion",Utility.getVersionNameCode(LocalService.this));

            Log.d("request","request");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(Utility.URL);
            try {
                transport.call(Utility.SOAPaCTION_WaterbodyMst,envelope);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            SoapObject response = (SoapObject) envelope.bodyIn;
            SoapPrimitive result = (SoapPrimitive) response.getProperty("GetIndentedWaterbodyResult");
            Log.d("result_WaterBody",""+result);
            waterBodyService = result.toString();
            Log.d("GetIndentedWaterbodyResult",waterBodyService);
            return result.toString();
        }
    }*/

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = getAssets().open("country.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public class MandalAdapter extends ArrayAdapter<PojoClass> {

        // Your sent context
        private Context context;
        // Your custom values for the spinner (User)
        private List<PojoClass> arrayList;
        // invoke the suitable constructor of the ArrayAdapter class
        public MandalAdapter(@NonNull Context context, ArrayList<PojoClass> arrayList) {

            // pass the context and arrayList for the super
            // constructor of the ArrayAdapter class
            super(context, 0, arrayList);
            this.context = context;
            this.arrayList = arrayList;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Nullable
        @Override
        public PojoClass getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            TextView tv = (TextView) super.getView(position, convertView, parent);
            tv.setTextColor(Color.BLACK);
            tv.setText(arrayList.get(position).getMandalCode());
            return tv;

       /* // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list_view, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        NumbersView currentNumberPosition = getItem(position);

        // then according to the position of the view assign the desired image for the same
        ImageView numbersImage = currentItemView.findViewById(R.id.imageView);
        assert currentNumberPosition != null;
        numbersImage.setImageResource(currentNumberPosition.getNumbersImageId());

        // then according to the position of the view assign the desired TextView 1 for the same
        TextView textView1 = currentItemView.findViewById(R.id.textView1);
        textView1.setText(currentNumberPosition.getNumberInDigit());

        // then according to the position of the view assign the desired TextView 2 for the same
        TextView textView2 = currentItemView.findViewById(R.id.textView2);
        textView2.setText(currentNumberPosition.getNumbersInText());

        // then return the recyclable view
        return currentItemView;
    }*/
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView tvv = (TextView) super.getDropDownView(position,convertView,parent);
            tvv.setTextColor(Color.BLACK);
            tvv.setText(arrayList.get(position).getMandalCode());
            return tvv;
        }
    }

    /*public class RecyclerAdapter extends RecyclerView.Adapter {
        public RecyclerAdapter(LocalService seedStockingService, ArrayList<SetMethods> rlist) {
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }*/

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{
        Context context;
        LayoutInflater inflater;
        ArrayList<SetMethods> rlist = new ArrayList();

        public RecyclerAdapter(Context context,ArrayList<SetMethods> rlist) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.rlist = rlist;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            inflater = LayoutInflater.from(context);
            View view =inflater.inflate(R.layout.view_layout,parent,false);
//            View view =inflater.inflate(R.layout.alert_popup,parent,false);
            if (viewType % 2 == 1){
                view.setBackgroundColor(Color.BLACK);
            }else {
                view.setBackgroundColor(Color.WHITE);
            }
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

            holder.tv1.setText(rlist.get(position).getFishtype());
            holder.tv2.setText(rlist.get(position).getFingerling_no());
            holder.tv3.setText(rlist.get(position).getTotal_kgs());
            holder.tv4.setText(rlist.get(position).getTotal_fingerlings());
            holder.tv5.setText(rlist.get(position).getVehicle());

            Log.d("holder",rlist.get(position).getFishtype());

        }

        /*public void vehicleError(){
            if (holder.tv5.getText().equals(mvehicleno)){
                mvehicleno.setError("Please Enter Correct Vehicle Number");
                mvehicleno.hasFocus();
            }
        }*/
        @Override
        public int getItemCount() {
            return rlist.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{

            TextView tv1,tv2,tv3,tv4,tv5;
            public MyViewHolder(View itemView) {
                super(itemView);

                /*tv1 = itemView.findViewById(R.id.fishname);
                tv2 = itemView.findViewById(R.id.no_fingerlings);
                tv3 = itemView.findViewById(R.id.kgs);
                tv4 = itemView.findViewById(R.id.kgs_total);
                tv5 = itemView.findViewById(R.id.vehicle_no);*/

                tv1 = itemView.findViewById(R.id.fishname);
                tv2 = itemView.findViewById(R.id.no_fingerlings);
                tv3 = itemView.findViewById(R.id.kgs);
                tv4 = itemView.findViewById(R.id.kgs_total);
                tv5 = itemView.findViewById(R.id.vehicle_no);
            }

        }

    }

    public void onError(String error) {
//        progressDialog.dismiss();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
