package com.example.fishriescopy;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class ParsingggggXm extends AppCompatActivity {

     static EditText mlogin_email;
     static EditText mlogin_pwd;
     Button mlogin_button;

    TelephonyManager telephonyManager;
    public static String imeiNo = "";
    private static final int REQUEST_PERMISSION_SETTING = 101;

    SharedPreferences preferences;

    private final static String TAG = GetLoginData.class.getSimpleName();

//    ProgressDialog progressDialog;
    private Context context;

    String mlogin_email_str,mlogin_pwd_str;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mlogin_email = findViewById(R.id.login_email);
        mlogin_pwd = findViewById(R.id.login_password);
        mlogin_button = findViewById(R.id.login_button);

        mlogin_email.setText("dfo_adi");
        mlogin_pwd.setText("Sa@12345");

       /* mlogin_email.setText("dfo_adi");
        mlogin_pwd.setText("Sa@12345");*/

        int imeiI = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
//  <-------------------------------------------- New Started ---------------------------------->
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
            if (telephonyManager != null) {
                try {
                    imeiNo = telephonyManager.getImei();
                } catch (Exception e) {
                    e.printStackTrace();
                    imeiNo = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
                }
            }else {
                ActivityCompat.requestPermissions(ParsingggggXm.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1010);
            }} else {
            if (ActivityCompat.checkSelfPermission(ParsingggggXm.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                if (telephonyManager != null) {
                    imeiNo = telephonyManager.getDeviceId();
                }
            } else {
                ActivityCompat.requestPermissions(ParsingggggXm.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1010);
            }
        }

//        <--------------------------------------- New Ended ------------------------------->
        /*
        if (imeiI == PackageManager.PERMISSION_GRANTED){
            telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            imeiNo = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            imeiNo = telephonyManager.getDeviceId();
        }else {

            ActivityCompat.requestPermissions(this,new String[]
                                        {Manifest.permission.READ_PHONE_STATE},REQUEST_PERMISSION_SETTING);
        }*/

        mlogin_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                callMastersData();
                loginCheck();
            }
        });

        preferences = getApplicationContext().getSharedPreferences("ParsingggggXm", 0);

    }

    private void callMastersData() {

       /* progressDialog = new ProgressDialog(ParsingggggXm.this, R.style.Theme_AlertAddDelete);
        progressDialog.setMessage("Please wait while downloading data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();*/

        new GetLoginData(ParsingggggXm.this, "" ).execute();

    }

    private void loginCheck(){
        mlogin_email_str = mlogin_email.getText().toString();
        mlogin_pwd_str = mlogin_pwd.getText().toString();
        String preference_mail_str;
        String password = "Sa@12345";

        preferences = getApplicationContext().getSharedPreferences("ParsingggggXm",0);
        preference_mail_str = preferences.getString("UsrName"," ");

        Log.i("preference_mail_str",preference_mail_str);

         if (!mlogin_email_str.equals(preference_mail_str) &&
                !mlogin_pwd_str.equals(password)){

             Toast.makeText(ParsingggggXm.this,"InValid Login user",Toast.LENGTH_LONG).show();

         }  else {
             Intent intent = new Intent(ParsingggggXm.this, InterfaceScreen.class);
             startActivity(intent);
             finish();

         }

    }

    public void onError(String error) {
//        progressDialog.dismiss();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    public void parsingGetXMLMstDataResp(SoapObject response) {

        int i = 0;
        if (Utility.showLogs == 0)
            Log.d("responseParse: ", "response: " + response.toString());

        PropertyInfo pi = new PropertyInfo();
        response.getPropertyInfo(i, pi);
        Object property = response.getProperty(i);
        
        Log.d("property","property: "+property);

        if (pi.name.equals("LoginVasData") && property instanceof SoapObject/* ||
                pi.name.equals("MandMstData") && property instanceof SoapObject ||
                pi.name.equals("VillMstData") && property instanceof SoapObject*/) {

            SoapObject soapObject = (SoapObject) property;

            Log.d("ssssss",""+soapObject);

            if (Utility.showLogs == 0) {
                Log.d("response: ", "SucessFlag: " + soapObject.getProperty("SucessFlag").toString().trim());
                Log.d("response: ", "SucessMsg: " + soapObject.getProperty("SucessMsg").toString().trim());
            }

            if (soapObject.getProperty("SucessFlag").toString().trim().equalsIgnoreCase("1")) {

//              String totalString = s.getProperty("LoginVasData").toString();

//                for (int j = 0; j < response.getPropertyCount(); j++) {
//                for (int j = 0; j < totalString.length(); j++) {

                    /*PropertyInfo pi1 = new PropertyInfo();
                    response.getPropertyInfo(j, pi1);
                    Object property1 = response.getProperty(j);
                    Log.i("property1",""+property1);*/

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("UsrName", soapObject.getProperty("UsrName").toString());
                    editor.putString("DistCode_Login", soapObject.getProperty("DistCode").toString());
                    editor.putString("UserId", soapObject.getProperty("UserId").toString());
                    editor.commit();

                   /* if (pi1.name.equals("LoginVasData") && property1 instanceof SoapObject) {
                        SoapObject transDetail = (SoapObject) property1;

                        Log.i("transDetail",""+transDetail);


//              String PPBNoPrefixStr = transDetail.getProperty("PPBNO_Prefix").toString().trim();
                        String PPBNoPrefixStr = transDetail.getProperty("DistCode").toString().trim();
                        Log.i("PPBNoPrefixStr",PPBNoPrefixStr);
                        if (PPBNoPrefixStr.equalsIgnoreCase("") ||
                                PPBNoPrefixStr == "" ||
                                PPBNoPrefixStr.isEmpty() ||
                                PPBNoPrefixStr.equalsIgnoreCase("anyType{}"))
                            PPBNoPrefixStr = "";

                       *//* db.createVillageData(
                                "" + transDetail.getProperty("DistCode").toString().trim(),
                                "" + transDetail.getProperty("MandCode").toString().trim(),
                                "" + transDetail.getProperty("VillCode").toString().trim(),
                                "" + transDetail.getProperty("VillName").toString().trim(),
                                "" + transDetail.getProperty("VillName_Tel").toString().trim(),
                                "" + transDetail.getProperty("ClusterCode").toString().trim(),
                                "" + transDetail.getProperty("ClusterName").toString().trim(),
                                "" + transDetail.getProperty("V_LgCode").toString().trim()
                        );*//*

                    }*/
//                }

//                progressDialog.dismiss();

                new CountDownTimer(500, 500) {
                    //   getFarmerDetailsCropSurvey();


                    @Override
                    public void onTick(long millisUntilFinished) {

//                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFinish() {

//                        progressDialog.dismiss();
                    }
                }.start();

                Toast.makeText(ParsingggggXm.this,soapObject.getProperty("SucessMsg").toString(),
                        Toast.LENGTH_LONG).show();

            }
        }
        finish();

        }

    public class GetLoginData extends AsyncTask<String, Void, SoapObject> {

        private ParsingggggXm activityRegistration;
        private String activityVal = "0";
        private final String TAG = GetLoginData.class.getSimpleName();

        public GetLoginData(ParsingggggXm activity, String val) {
            this.activityRegistration = activity;
            activityVal = val;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (activityVal.equalsIgnoreCase("LoginVasData")/*||
                    activityVal.equalsIgnoreCase("Mandal")||activityVal.equalsIgnoreCase("Village")*/) {
                // Check network available.
                if (!Utility.isNetworkAvailable(activityRegistration)) {
                    activityRegistration.onError("Network error");
                }
            }
        /*else if (activityVal==1) {
            // Check network available.
            if (!Utility.isNetworkAvailable(activityRegistration)) {
                activityRegistration.onError("Network error");
            }
        }else if (activityVal==4) {
            // Check network available.
            if (!Utility.isNetworkAvailable(activityCropSurveyNavigationMenu)) {
                activityCropSurveyNavigationMenu.onError("Network error");
            }
        }else{
            // Check network available.
            if (!Utility.isNetworkAvailable(activityGiveItUp)) {
                activityGiveItUp.onError("Network error");
            }
        }*/
        }

        @Override
        protected SoapObject doInBackground(String... strings) {

            SoapObject request = null;
            SoapObject returnSoapObj = null;

       /* if (activityVal.equalsIgnoreCase("District"))
            request = new SoapObject(Utility.WSDL_TARGET_NAMESPACE, Utility.OPERATION_NAME_GetDistMst_Data);
        else if (activityVal.equalsIgnoreCase("Mandal"))
            request = new SoapObject(Utility.WSDL_TARGET_NAMESPACE, Utility.OPERATION_NAME_GetMandMst_Data);
//        else *//*if (activityVal.equalsIgnoreCase("Village"))*/
//                    request = new SoapObject(Utility.NAMESPACE, Utility.METHODNAME_ValidateLogin);

                /*if (activityVal.equalsIgnoreCase("LoginVasData")||
                        activityVal.equalsIgnoreCase("Mandal")||
                        activityVal.equalsIgnoreCase("Village")){*/

                    request = new SoapObject(Utility.NAMESPACE, Utility.METHODNAME_ValidateLogin);
                    request.addProperty("UserName",mlogin_email_str);
                    request.addProperty("Pwd",mlogin_pwd_str);
                    request.addProperty("IMEI_No",Utility.GetIMEINO(ParsingggggXm.this));
                    request.addProperty("WS_UserName",Utility.wsusername);
                    request.addProperty("WS_Password",Utility.wspassword);
                    request.addProperty("MobileVersion",Utility.getVersionNameCode(ParsingggggXm.this));
//                }
            Log.d(TAG, "requestAA: " + request);

            String Url;

//            if (activityVal.equalsIgnoreCase("ValidateLogin") )
            Url = Utility.LOGIN_URL;
            Log.d("Url",Url);

            if (Utility.showLogs == 0)
                Log.d(TAG, "requestAAAAAA: "+ request);
            Log.d(TAG, "dummyyyyyyyy: " +"dummy"+ activityVal);
            activityVal = "Districtname";
            Log.d(TAG, "dummy: " +"dummy"+ activityVal);
//            if (activityVal.equalsIgnoreCase("LoginVasData"))
//            if (activityVal.equalsIgnoreCase("ValidateLogin"))
//                Log.d(TAG, "dummy: " +"dummy"+ activityVal);
                Log.d(TAG, "dummy: " +"dummy"+ activityVal);
                returnSoapObj = getXMLResult(request, Url,
                        "" + Utility.SOAPaCTION_ValidateLogin);
            Log.d("returnSoapObj",""+returnSoapObj);
                /*else if (activityVal.equalsIgnoreCase("Mandal"))
                    returnSoapObj = getXMLResult(request, Url,
                            "" + Utility.SOAP_ACTION_GetMandMst_Data);
                else if (activityVal.equalsIgnoreCase("Village"))
                    returnSoapObj = getXMLResult(request, Url,
                            "" + Utility.SOAP_ACTION_GetVillMst_Data);*/

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
        protected SoapObject getXMLResult(SoapObject request, String url, String soapAction) {
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
        protected void onPostExecute(SoapObject response) {
            super.onPostExecute(response);

            if (response == null) {
                if (activityVal.equalsIgnoreCase(""))

                /*if (activityVal.equalsIgnoreCase("") || activityVal.equalsIgnoreCase("Mandal")
                        || activityVal.equalsIgnoreCase("Village") || activityVal.equalsIgnoreCase("OSSDS_Crop")
                        || activityVal.equalsIgnoreCase("FinYearMst"))*/
                Log.d("response","null : "+response);
                activityRegistration.onError("Getting Data Error");

            /*else  if (activityVal==1)
                activityRegistration.onError("Getting Data Error");

            else  if (activityVal==4)
                activityCropSurveyNavigationMenu.onError("Getting Data Error");
            else
                activityGiveItUp.onError("Getting Data Error");*/
            } else if (response.hasProperty("LoginVasData")) {

                activityRegistration.parsingGetXMLMstDataResp(response);
            }  else {
                if (activityVal.equalsIgnoreCase("") /*|| activityVal.equalsIgnoreCase("Mandal")
                        || activityVal.equalsIgnoreCase("Village"))*/)
                activityRegistration.onError("Data Not Found");
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
        public SoapObject getServiceResult(String strURL, String strSoapAction, SoapObject request)
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
                response = (SoapObject) envelope.getResponse();
                Log.d("responseeeeed",""+response);
                StringBuffer result;
                result = new StringBuffer(response.toString());

                if (Utility.showLogs == 0)
                    Log.d(TAG, result.toString());

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
            return response;
        }
    }
    }
