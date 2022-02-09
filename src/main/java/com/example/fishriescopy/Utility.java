package com.example.fishriescopy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.UUID;

public class Utility {

    ProgressDialog progressDialog;
    public static final int showLogs = 0;
    public static final String wsusername = "IFDS";
    public static final String wspassword = "IFDS$Admin@123";

    public static final String URL = "https://tmatsya.telangana.gov.in/HarvestingDetails.asmx?WSDL";
    public static final String LOGIN_URL = "https://tmatsya.telangana.gov.in/ifds_service.asmx";
    public static final String NAMESPACE = "http://tempuri.org/";

//    Start of Service Action(METHODNAME and SOAPACTION)
    public static final String METHODNAME_FinYearMST = "FinYearMST";
    public static final String SOAPaCTION_FinYearMST = NAMESPACE + METHODNAME_FinYearMST;

    public static final String METHODNAME_CultureTypeMST = "CultureTypeMst";
    public static final String SOAPaCTION_CultureTypeMST = NAMESPACE + METHODNAME_CultureTypeMST;

    public static final String METHODNAME_SeasonalTypeMST = "SeasonalityMst";
    public static final String SOAPaCTION_SeasonalTypeMST = NAMESPACE + METHODNAME_SeasonalTypeMST;

    public static final String METHODNAME_MandalMST = "MandalMaster";
    public static final String SOAPaCTION_MandalMST = NAMESPACE + METHODNAME_MandalMST;

    public static final String METHODNAME_VillageMST = "VillageMaster";
    public static final String SOAPaCTION_VillageMST = NAMESPACE + METHODNAME_VillageMST;

    public static final String METHODNAME_WaterbodyMst = "GetIndentedWaterbody";
    public static final String SOAPaCTION_WaterbodyMst = NAMESPACE + METHODNAME_WaterbodyMst;

    public static final String METHODNAME_Supplierlist = "GetWaterbodySupplierlist";
    public static final String SOAPaCTION_Supplierlist = NAMESPACE + METHODNAME_Supplierlist;

    public static final String METHODNAME_GetWaterbodySupplierIndenting = "GetWaterbodySupplierIndenting";
    public static final String SOAPaCTION_GetWaterbodySupplierIndenting = NAMESPACE + METHODNAME_GetWaterbodySupplierIndenting;

    public static final String METHODNAME_ValidateLogin = "ValidateLogin";
    public static final String SOAPaCTION_ValidateLogin = NAMESPACE + METHODNAME_ValidateLogin;

    static String versionNameOrCodeStr = "";
    private Object Context;

    public static String getVersionNameCode(Context _context) {

        try {
            PackageInfo pinfo = _context.getPackageManager().getPackageInfo(
                    _context.getPackageName(), 0);

            if (Utility.showLogs == 0) {
                Log.d("pinfoCode", "" + pinfo.versionCode);
                Log.d("pinfoName", pinfo.versionName);
            }

            // versionCodeStr=String.valueOf(pinfo.versionCode);
            versionNameOrCodeStr = pinfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return versionNameOrCodeStr;
    }

    public static void showAlertDialog(Context context, String title,
                                       String message, Boolean status) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);

        if (status == true) {
            builder.setIcon(R.drawable.success_48x48);
        } else {
            builder.setIcon(R.drawable.fail_48x48);
        }

        builder.setMessage(message).setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                    }
                });
        final AlertDialog alert = builder.create();

        alert.show();

    }
    public void onError(String error) {
        progressDialog.dismiss();
//        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        Snackbar.make((View) Context, error, Snackbar.LENGTH_LONG)
                /*.setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))*/
                .show();
    }

    public static final SharedPreferences getSharedPreferences(Context context) {

        return context.getApplicationContext().getSharedPreferences("prefMI", 0);

    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }
;
        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();

    }

    @SuppressLint("MissingPermission")
    public static String GetIMEINO(Context context) {
        String IMEI = null;
        TelephonyManager manager = (TelephonyManager) context
                .getSystemService(context.TELEPHONY_SERVICE);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                IMEI = UUID.randomUUID().toString();
            else
                IMEI = manager.getDeviceId();

            if (showLogs == 0)
                Log.d("Utility","UUID: "+IMEI);

        }catch (SecurityException se){

        }

        return IMEI;
    }

    public static void callHideKeyBoard(Context context) {
        // Check if no view has focus:
        View view = ((Activity) context).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) ((Activity) context)
                    .getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
            
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showAlertDialogToCallPlayStore(final Context context, String title,
                                                      String message, Boolean status) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Specify the alert dialog title
//        String titleText = "Say Hello!";

        // Initialize a new foreground color span instance
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.BLUE);

        // Initialize a new spannable string builder instance
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(title);

        // Apply the text color span
        ssBuilder.setSpan(
                foregroundColorSpan,
                0,
                title.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        // Set the alert dialog title using spannable string builder
        builder.setTitle(ssBuilder);

//        builder.setTitle(title);

        if (status == true) {
            builder.setIcon(R.drawable.success_48x48);
        } else {
            builder.setIcon(R.drawable.fail_48x48);
        }

        builder.setMessage(message).setCancelable(false)
                .setPositiveButton("Goto Google Play Store", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                        final String my_package_name = context.getPackageName(); // getPackageName() from Context or Activity object
                        String url = "";

                        try {
                            //Check whether Google Play store is installed or not:
                            context.getPackageManager().getPackageInfo("com.android.vending", 0);

                            url = "market://details?id=" + my_package_name;
                        } catch (final Exception e) {
                            url = "https://play.google.com/store/apps/details?id=" + my_package_name;
                        }


//Open the app page in Google Play store:
                        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                        ((Activity) context).startActivity(intent);
//                        ((Activity) context).startActivity(new Intentent(context,Menulist.class));
                        ((Activity) context).finish();


                    }
                });

        /*.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        })*/
        final AlertDialog alert = builder.create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);

            }
        });

        alert.show();
    }

    public static void callSignOutAlert(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Confirmation");
        builder.setIcon(R.drawable.confirm_48x48);

        builder.setMessage("Are you sure you want to quit Application?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                ((Activity) context).finish();

                                dialog.cancel();

                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);

            }
        });

        alert.show();

    }

}
