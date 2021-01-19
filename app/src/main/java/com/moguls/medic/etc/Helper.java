package com.moguls.medic.etc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.moguls.medic.R;
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
    public static void loadImage(Context context, String url, int placeholder, ImageView imageView) {
        try {
            GlideUrl glideurl = new GlideUrl(APIs.PhotoBaseUrl.concat(url), new LazyHeaders.Builder()
                    .addHeader(BaseKeys.ContentType, "application/json")
                    .addHeader(BaseKeys.Authorization, "Bearer " + SharedPreference.getString(context, BaseKeys.Authorization))
                    .build());
            Glide.with(context)
                    .load(glideurl)
                    .placeholder(placeholder)
                    .into(imageView);
        }catch (Exception e) {
            imageView.setImageResource(placeholder);
            e.printStackTrace();
        }
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
    public static Intent getPickIntent(Uri cameraOutputUri, Context context) {
        final List<Intent> intents = new ArrayList<Intent>();

        if (true) {
            intents.add(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
        }

        if (true) {
            setCameraIntents(intents, cameraOutputUri,context);
        }

        if (intents.isEmpty()) return null;
        Intent result = Intent.createChooser(intents.remove(0), null);
        if (!intents.isEmpty()) {
            result.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents.toArray(new Parcelable[] {}));
        }
        return result;
    }
    private static void setCameraIntents(List<Intent> cameraIntents, Uri output, Context context) {
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = context.getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, output);
                cameraIntents.add(intent);
            }
        }
    }
    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null,
                    null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


}
