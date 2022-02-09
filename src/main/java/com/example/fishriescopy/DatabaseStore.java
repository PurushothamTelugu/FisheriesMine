package com.example.fishriescopy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseStore {

   public static MyDBHelper myDBHelper;
   PojoClass pojoClass;

    public DatabaseStore(Context context) {
        myDBHelper = new MyDBHelper(context);

    }

//                              Inserting the data into tables

    public long insertCultureType(String culture_code,String culture_name) {

        SQLiteDatabase databaseInsert = myDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyDBHelper.FISH_CULTURE_CODE,culture_code);
        contentValues.put(MyDBHelper.FISH_CULTURE_NAME, culture_name);
        long data = databaseInsert.insert(MyDBHelper.CULTURE_TABLE, null, contentValues);
        return data;

    }

    public long insertFinancialYear(String year_code,String year_name,String year_desc) {
        SQLiteDatabase databaseInsert = myDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(MyDBHelper.FINANCIAL_CODE,year_code);
        contentValues.put(MyDBHelper.FINANCIAL_YEAR, year_name);
        contentValues.put(MyDBHelper.FINANCIAL_DESCRIPTION, year_desc);
        long data = databaseInsert.insert(MyDBHelper.FINANCIAL_TABLE, null, contentValues);
        return data;
    }

    public long insertseasional(String seasonal_sno,String seasonality) {
        SQLiteDatabase databaseInsert = myDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(MyDBHelper.SEASONALITY_SNo,seasonal_sno);
        contentValues.put(MyDBHelper.SEASONALITY, seasonality);
        long data = databaseInsert.insert(MyDBHelper.SEASONALITY_TABLE, null, contentValues);
        return data;
    }

    public long insertDistrict(String state_code,String distname, String distcode, String distnametel) {
        SQLiteDatabase databaseInsert = myDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(MyDBHelper.STATE_CODE,state_code);
        contentValues.put(MyDBHelper.DISTRICT_NAME, distname);
        contentValues.put(MyDBHelper.DISTRICT_CODE, distcode);
        contentValues.put(MyDBHelper.DISTRICT_NAME_TEL, distnametel);
        long data = databaseInsert.insert(MyDBHelper.DISTRICT_TABLE, null, contentValues);
        return data;
    }

    public long insertMandal(String district_code,String mandcode, String mandname) {
        SQLiteDatabase databaseInsert = myDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyDBHelper.MANDAL_DISTRICT_CODE, district_code);
        contentValues.put(MyDBHelper.MANDAL_CODE, mandcode);
        contentValues.put(MyDBHelper.MANDAL_NAME, mandname);

        long data = databaseInsert.insert(MyDBHelper.MANDAL_TABLE, null, contentValues);
        Log.d("mandal_data",""+data);
        return data;
    }

    public long insertVillage(String vdist_code, String vmand_code, String village_code, String village_name) {
        SQLiteDatabase databaseInsert = myDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyDBHelper.VILLAGE_DISTRICT_CODE, vdist_code);
        contentValues.put(MyDBHelper.VILLAGE_MANDAL_CODE, vmand_code);
        contentValues.put(MyDBHelper.VILLAGE_CODE, village_code);
        contentValues.put(MyDBHelper.VILLAGE_NAME, village_name);


        long data = databaseInsert.insert(MyDBHelper.VILLAGE_TABLE, null, contentValues);
        Log.d("village_data",""+data);
        return data;
    }

    //        Getting Count of the Respected Database

    public int getCultureCount(){
        int count = 0;
        SQLiteDatabase getDB = myDBHelper.getWritableDatabase();

        String query = " select * from "+myDBHelper.CULTURE_TABLE;

        Cursor cursor = getDB.rawQuery(query,null);

        if(cursor != null && !cursor.isClosed()){
            count = cursor.getCount();
            cursor.close();

            Log.d("get_count",""+count);
        }
        return count;
    }

    public int getFinancialCount(){
        int count = 0;
        SQLiteDatabase getDB = myDBHelper.getWritableDatabase();

        String query = " select * from "+myDBHelper.FINANCIAL_TABLE;

        Cursor cursor = getDB.rawQuery(query,null);

        if(cursor != null && !cursor.isClosed()){
            count = cursor.getCount();
            cursor.close();

            Log.d("get_count",""+count);
        }
        return count;
    }


    public int getSeasonCount(){
        int count = 0;
        SQLiteDatabase getDB = myDBHelper.getWritableDatabase();

        String query = " select * from "+myDBHelper.SEASONALITY_TABLE;

        Cursor cursor = getDB.rawQuery(query,null);

        if(cursor != null && !cursor.isClosed()){
            count = cursor.getCount();
            cursor.close();

            Log.d("get_count",""+count);
        }
        return count;
    }


    public int getDistCount(){
        int count = 0;
        SQLiteDatabase getDB = myDBHelper.getWritableDatabase();

        String query = " select * from "+myDBHelper.DISTRICT_TABLE;

        Cursor cursor = getDB.rawQuery(query,null);

        if(cursor != null && !cursor.isClosed()){
            count = cursor.getCount();
            cursor.close();

            Log.d("get_count",""+count);
        }
        return count;
    }

    public int getMandalCount(){
        int count = 0;
        SQLiteDatabase getDB = myDBHelper.getWritableDatabase();

        String query = " select * from "+myDBHelper.MANDAL_TABLE;

        Cursor cursor = getDB.rawQuery(query,null);

        if(cursor != null && !cursor.isClosed()){
            count = cursor.getCount();
            cursor.close();

            Log.d("get_count",""+count);
        }
        return count;
    }

    public int getVillageCount(){
        int count = 0;
        SQLiteDatabase getDB = myDBHelper.getWritableDatabase();

        String query = " select * from "+myDBHelper.VILLAGE_TABLE;

        Cursor cursor = getDB.rawQuery(query,null);

        if(cursor != null && !cursor.isClosed()){
            count = cursor.getCount();
            cursor.close();

            Log.d("get_count",""+count);
        }
        return count;
    }

    public ArrayList<PojoClass> getCultureData() {
        ArrayList getList = new ArrayList();

        SQLiteDatabase getDB = myDBHelper.getWritableDatabase();

        String query = "select * from "+myDBHelper.CULTURE_TABLE;

        Cursor cursor = getDB.rawQuery(query,null);

        String fish_culture_code,fish_culture_name;

        if (cursor.moveToNext()) {

            do {
                pojoClass = new PojoClass();
              //  fish_culture_code = cursor.getString(cursor.getColumnIndex(MyDBHelper.FISH_CULTURE_CODE));
              //  fish_culture_name = cursor.getString(cursor.getColumnIndex(MyDBHelper.FISH_CULTURE_NAME));

               // pojoClass.setFishCultureCode(fish_culture_code);
                //pojoClass.setFishCultureName(fish_culture_name);
                pojoClass.setFishCultureCode(cursor.getString(cursor.getColumnIndex(MyDBHelper.FISH_CULTURE_CODE)));
                pojoClass.setFishCultureName(cursor.getString(cursor.getColumnIndex(MyDBHelper.FISH_CULTURE_NAME)));

                Log.d("fish_culture_name",""+cursor.getString(cursor.getColumnIndex(MyDBHelper.FISH_CULTURE_NAME)));

                getList.add(pojoClass);
            }while (cursor.moveToNext());
        }

        return getList;
    }

    public ArrayList<PojoClass> getFinancialData() {
        ArrayList getList = new ArrayList();

        SQLiteDatabase getDB = myDBHelper.getWritableDatabase();

        String query = "select * from "+myDBHelper.FINANCIAL_TABLE;

        Cursor cursor = getDB.rawQuery(query,null);

        String financial_code,financial_Year,financial_Desc;

        if (cursor.moveToNext()) {

            do {
                pojoClass = new PojoClass();
                financial_code = cursor.getString(cursor.getColumnIndex(MyDBHelper.FINANCIAL_CODE));
                financial_Year = cursor.getString(cursor.getColumnIndex(MyDBHelper.FINANCIAL_YEAR));
                financial_Desc = cursor.getString(cursor.getColumnIndex(MyDBHelper.FINANCIAL_DESCRIPTION));

                pojoClass.setFinancialCode(financial_code);
                pojoClass.setFinancialYear(financial_Year);
                pojoClass.setFinancialDesc(financial_Desc);

                Log.d("financial_code",""+financial_code);
                Log.d("financial_Year",""+financial_Year);
                Log.d("financial_Desc",""+financial_Desc);

                getList.add(pojoClass);
            }while (cursor.moveToNext());
        }


        return getList;
    }

    public ArrayList<PojoClass> getSeasonalData() {
        ArrayList getList = new ArrayList();

        SQLiteDatabase getDB = myDBHelper.getWritableDatabase();

        String query = "select * from "+myDBHelper.SEASONALITY_TABLE;

        Cursor cursor = getDB.rawQuery(query,null);

        String seasonality_sno,seasonality;

        if (cursor.moveToNext()) {

            do {
                pojoClass = new PojoClass();
                seasonality_sno = cursor.getString(cursor.getColumnIndex(MyDBHelper.SEASONALITY_SNo));
                seasonality = cursor.getString(cursor.getColumnIndex(MyDBHelper.SEASONALITY));

                Log.d("seasonality",seasonality);

                pojoClass.setSeasonalitySNo(seasonality_sno);
                pojoClass.setSeasonality(seasonality);


                getList.add(pojoClass);
            }while (cursor.moveToNext());
        }


        return getList;
    }

    public ArrayList<PojoClass> getDistData() {
        ArrayList getList = new ArrayList();

        SQLiteDatabase getDB = myDBHelper.getWritableDatabase();

        String query = "select * from "+myDBHelper.DISTRICT_TABLE;

        Cursor cursor = getDB.rawQuery(query,null);

        String state_code,dist_name,dist_code,dist_name_tel;

        if (cursor.moveToNext()) {

            do {
                pojoClass = new PojoClass();
                state_code = cursor.getString(cursor.getColumnIndex(MyDBHelper.STATE_CODE));
                dist_name = cursor.getString(cursor.getColumnIndex(MyDBHelper.DISTRICT_NAME));
                dist_code = cursor.getString(cursor.getColumnIndex(MyDBHelper.DISTRICT_CODE));
                dist_name_tel = cursor.getString(cursor.getColumnIndex(MyDBHelper.DISTRICT_NAME_TEL));

                pojoClass.setStateCode(state_code);
                pojoClass.setDistName(dist_name);
                pojoClass.setDistCode(dist_code);
                pojoClass.setDistNameTel(dist_name_tel);

                getList.add(pojoClass);
            }while (cursor.moveToNext());
        }

        return getList;
    }

    public ArrayList<PojoClass> getMandData(String discCode) {
        ArrayList getList = new ArrayList();

        SQLiteDatabase getDB = myDBHelper.getWritableDatabase();
        Log.i("discCode",discCode);

        String query = "select * from "+myDBHelper.MANDAL_TABLE+" where DistCode='"+discCode+"'";
        Log.d("mandal","query"+query);

        Cursor cursor = getDB.rawQuery(query,null);

        String mandal_dist_code,mandal_name,mandal_code;
        if (cursor.moveToNext()) {

            do {
                pojoClass = new PojoClass();
                mandal_dist_code = cursor.getString(cursor.getColumnIndex(MyDBHelper.MANDAL_DISTRICT_CODE));
                mandal_name = cursor.getString(cursor.getColumnIndex(MyDBHelper.MANDAL_NAME));
                mandal_code = cursor.getString(cursor.getColumnIndex(MyDBHelper.MANDAL_CODE));

                pojoClass.setMandalName(mandal_name);
                pojoClass.setMandalCode(mandal_code);
                pojoClass.setMandalNameTel(mandal_dist_code);

                Log.d("mandal_name",""+mandal_name);
                Log.d("mandal_code",""+mandal_code);
                Log.d("mandal_dist_code",""+mandal_dist_code);

                getList.add(pojoClass);
            }while (cursor.moveToNext());
        }

        return getList;
    }

    public ArrayList<PojoClass> getVillData(String discCode,String mandCode) {
        ArrayList getList = new ArrayList();

//        SQLiteDatabase getDB = myDBHelper.getWritableDatabase();
        SQLiteDatabase getDB = myDBHelper.getReadableDatabase();

        String query = "select * from "+myDBHelper.VILLAGE_TABLE+" where DistCode='"+discCode+"' AND MandCode='"+mandCode+"'";
//        String query = "select * from "+myDBHelper.VILLAGE_TABLE+" where (DistCode='discCode' AND  MandCode='mandCode')";

        Log.i("mandCode",mandCode);
        Cursor cursor = getDB.rawQuery(query,null);

        String vill_dist_code,vill_mandal_code,vill_code,vill_name;

        if (cursor.moveToNext()) {

            do {
                pojoClass = new PojoClass();

                vill_dist_code = cursor.getString(cursor.getColumnIndex(MyDBHelper.VILLAGE_DISTRICT_CODE));
                vill_mandal_code = cursor.getString(cursor.getColumnIndex(MyDBHelper.VILLAGE_MANDAL_CODE));
                vill_code = cursor.getString(cursor.getColumnIndex(MyDBHelper.VILLAGE_CODE));
                vill_name = cursor.getString(cursor.getColumnIndex(MyDBHelper.VILLAGE_NAME));

                pojoClass.setVillDistCode(vill_dist_code);
                pojoClass.setVillMandalCode(vill_mandal_code);
                pojoClass.setVillCode(vill_code);
                pojoClass.setVillName(vill_name);

                Log.d("vill_code",""+vill_code);
                Log.d("vill_name",""+vill_name);

                getList.add(pojoClass);
            }while (cursor.moveToNext());
        }

        return getList;
    }

    public static class MyDBHelper extends SQLiteOpenHelper {

        private static final String DATA_BASE = "Mydatabase";
        private static final String TABLE_NAME = "My_table";

//        Start of Culture datatable
        private static final String CULTURE_TABLE = "CultureTypeMst";
        private static final String FISH_CULTURE_CODE = "Fishculture_cd";
        private static final String FISH_CULTURE_NAME = "Fishculture_Name";

//        Start of FinancialYear datatable
        private static final String FINANCIAL_TABLE = "FinYearMST";
        private static final String FINANCIAL_CODE = "Year_Code";
        private static final String FINANCIAL_YEAR = "Year_Desc";
        private static final String FINANCIAL_DESCRIPTION = "Fisilyear_Desc";

//        Start of Seasonality datatable
        private static final String SEASONALITY_TABLE = "SeasonalityMst";
        private static final String SEASONALITY_SNo = "SNo";
        private static final String SEASONALITY = "Seasionality";

//        Start of district datatable
        private static final String DISTRICT_TABLE = "DistrictMaster";
        private static final String STATE_CODE = "StateCode";
        private static final String DISTRICT_NAME = "DistName";
        private static final String DISTRICT_CODE = "DistCode";
        private static final String DISTRICT_NAME_TEL = "DistName_Tel";

//          Start of mandal datatable
        private static final String MANDAL_TABLE = "MandalMaster";
        private static final String MANDAL_DISTRICT_CODE = "DistCode";
        private static final String MANDAL_NAME = "MandName";
        private static final String MANDAL_CODE = "MandCode";

//     Start of village datatable
        private static final String VILLAGE_TABLE = "VillageMaster";
        private static final String VILLAGE_DISTRICT_CODE = "DistCode";
        private static final String VILLAGE_MANDAL_CODE = "MandCode";
        private static final String VILLAGE_CODE = "VillCode";
        private static final String VILLAGE_NAME = "VillName";

//      Start of WaterBodyIntending datatable
        private static final String WATERBODY_TABLE = "GetIndentedWaterbody";
        private static final String WATERBODY_DISTRICT_CODE = "DistCode";
        private static final String WATERBODY_MANDAL_CODE = "MandCode";
        private static final String WATERBODY_VILLAGE_CODE = "VillCode";
        private static final String WATERBODY_SEASONALITY_SNo = "SNo";
        private static final String WATERBODY_FINANCIAL_YEAR = "Year_Desc";
        private static final String WATERBODY_FISH_CULTURE_CODE = "Fishculture_cd";


        private static final String CREATE_CULTURE_TABLE = " CREATE TABLE " + CULTURE_TABLE + "("
                + FISH_CULTURE_CODE + " TEXT,"
                + FISH_CULTURE_NAME + " TEXT)";

        private static final String CREATE_FINANCIAL_TABLE = " CREATE TABLE " + FINANCIAL_TABLE + "("
                + FINANCIAL_CODE + " TEXT,"
                + FINANCIAL_YEAR + " TEXT," +
                FINANCIAL_DESCRIPTION + " TEXT)";

        private static final String CREATE_SEASONALITY_TABLE = " CREATE TABLE " + SEASONALITY_TABLE + "("
                + SEASONALITY_SNo + " TEXT,"
                + SEASONALITY + " TEXT)";

        private static final String CREATE_DISTRICT_TABLE = " CREATE TABLE " + DISTRICT_TABLE + "("
                + STATE_CODE + " TEXT,"
                + DISTRICT_NAME + " TEXT,"
                + DISTRICT_CODE + " TEXT," +
                DISTRICT_NAME_TEL + " TEXT)";

        private static final String CREATE_MANDAL_TABLE = " CREATE TABLE " + MANDAL_TABLE + "("
                + DISTRICT_CODE + " TEXT,"
                + MANDAL_CODE + " TEXT,"
                + MANDAL_NAME + " TEXT)";

        private static final String CREATE_VILLAGE_TABLE = " CREATE TABLE " + VILLAGE_TABLE + "("
                + VILLAGE_DISTRICT_CODE + " TEXT,"
                + VILLAGE_MANDAL_CODE + " TEXT," +
                VILLAGE_CODE + " TEXT," +
                VILLAGE_NAME + " TEXT)";

        private static final String CREATE_WATERBODY_TABLE = " CREATE TABLE " + WATERBODY_TABLE + "("
                + WATERBODY_DISTRICT_CODE + " TEXT,"
                + WATERBODY_MANDAL_CODE + " TEXT," +
                WATERBODY_VILLAGE_CODE + " TEXT," +
                WATERBODY_SEASONALITY_SNo + " TEXT," +
                WATERBODY_FINANCIAL_YEAR + " TEXT," +
                WATERBODY_FISH_CULTURE_CODE + " TEXT)";

        private static final String DISTRICT = "district";
        private static final String MANDAL = "mandal";

        private static final int DATABASE_VERSION = 1;
        private Context context;
        public MyDBHelper(@Nullable Context context) {
            super(context, DATA_BASE, null, DATABASE_VERSION);
            this.context = context;

        }

//        Executing the Tables
        @Override
    public void onCreate(SQLiteDatabase db) {

            try {
//                db.execSQL(CREATE_TABLE);
                db.execSQL(CREATE_CULTURE_TABLE);
                db.execSQL(CREATE_FINANCIAL_TABLE);
                db.execSQL(CREATE_SEASONALITY_TABLE);
                db.execSQL(CREATE_DISTRICT_TABLE);
                db.execSQL(CREATE_MANDAL_TABLE);
                db.execSQL(CREATE_VILLAGE_TABLE);
                db.execSQL(CREATE_WATERBODY_TABLE);

            } catch (Exception e) {

            }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    }

}
