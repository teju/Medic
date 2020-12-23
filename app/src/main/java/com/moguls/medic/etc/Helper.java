package com.moguls.medic.etc;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.moguls.medic.model.Response;
import com.moguls.medic.model.SpannString;
import com.moguls.medic.webservices.settings.HTTPAsyncTask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class Helper {
    public static class GenericHttpAsyncTask extends HTTPAsyncTask {

        private final TaskListener taskListener;

        public GenericHttpAsyncTask(TaskListener taskListener) {
            this.taskListener = taskListener;
        }
        public  interface TaskListener {
            public void onPreExecute();
            public void onPostExecute( Response response);
        }

        @Override
        protected void onPreExecute() {
            if (taskListener != null) taskListener.onPreExecute();

        }

        @Override
        protected void onPostExecute(Response response) {

            if (response == null) {

                String apiDetails = "";

                try {
                    apiDetails = apiDetails + this.url.getPath() + "\n";
                } catch (Exception e) {
                }

                try {
                    apiDetails = apiDetails + this.params.toString() + "\n";
                } catch (Exception e) {
                }

                String rawResponse = "";
                try {
                    rawResponse = rawResponse + this.rawResponseString;
                } catch (Exception e) {
                }

            }

            if (taskListener != null) taskListener.onPostExecute(response);
        }
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public static boolean isEmpty(String string) {
        if (string == null || string.trim().length() == 0) {
            return true;
        }
        return false;
    }
    public static String dateFormat(String format, Date date) {
        String pattern = format;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String returnDate = simpleDateFormat.format(date);
        return returnDate;
    }
    public static Date convertStringToDate(String format, String date) {
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Date parse;
        try {
             parse = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return parse;
    }

    public static long findDifferenceBetweenDates(Date startDate,Date endDate) {
        long duration  = endDate.getTime() - startDate.getTime();
        long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);
        return diffInDays;
    }
    public static Boolean isNetworkAvailable(Context ctx)  {
        try {
            ConnectivityManager manager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);;
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();

            Boolean isAvailable = false;
            if (networkInfo != null && networkInfo.isConnected()) {
                isAvailable = true;
            }
            if (!isAvailable) {
            }
            return isAvailable;
        } catch (Exception e) {
            return true;
        }

    }

    public static SpannableStringBuilder spanString(List<SpannString> spannableStringList){
        SpannableStringBuilder builder = new SpannableStringBuilder();
        for(int i=0;i<spannableStringList.size();i++) {
            SpannableString str= new SpannableString(spannableStringList.get(i).getString());
            str.setSpan(new ForegroundColorSpan(spannableStringList.get(i).getColour()), 0, str.length(), 0);
            builder.append(str);
        }
        return builder;
    }
    public static ArrayList<String> listFragmentsMainTab(){
        ArrayList<String> list =new ArrayList<>();

        list.add("FIRST_TAB");
        list.add("SECOND_TAB");
        list.add("THIRD_TAB");
        return list;

    }
    public static boolean isValidMobile(String phone) {
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() == 10;
        }
        return false;
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    public static void applyHeader(Context context, HTTPAsyncTask async) {
        if (async == null)
            return;

        async.setCache(false);
        async.setHeader(BaseKeys.ContentType, "application/json");
        if(!Helper.isEmpty(SharedPreference.getString(context, BaseKeys.Authorization))) {
            async.setHeader(BaseKeys.Authorization, "Bearer " + SharedPreference.getString(context, BaseKeys.Authorization));
        }
    }
    public static void visibleView(View v) {
        if (v != null) {
            v.setVisibility(View.VISIBLE);
        }
    }
    public static void goneView(View v) {
        if (v != null) {
            v.setVisibility(View.GONE);
        }
    }

}
