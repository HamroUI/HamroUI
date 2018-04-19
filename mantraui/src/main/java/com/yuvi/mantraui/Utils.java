package com.yuvi.mantraui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by yubaraj on 12/21/17.
 */

public class Utils {

    public static HashMap<String, Integer> layoutMap = new HashMap<String, Integer>(){{
        put("news_type_1", R.layout.layout_type_news_1);
        put("news_type_2", R.layout.layout_type_news_2);
        put("videos_type_1", R.layout.layout_type_videos_1);
        put("videos_type_2", R.layout.layout_type_videos_2);
    }};


    public static void log(Class<?> mClass, String message) {
        if (BuildConfig.DEBUG) {
            Log.d(mClass.getSimpleName(), message);
        }
    }

    public static int dpFromPx(Context context, float px) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }

    public static int pxFromDp(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    public static int pxFromSp(Context context, int sp) {
        return (int) (sp * context.getResources().getDisplayMetrics().scaledDensity);
    }

    public static JSONArray concatJSONArray(JSONArray... arrays) {
        JSONArray jsonArray = new JSONArray();
        try {
            for (JSONArray arr : arrays) {
                for (int i = 0; i < arr.length(); i++) {
                    jsonArray.put(arr.optJSONObject(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;

    }

    public static void listAllActivities(Context context) throws PackageManager.NameNotFoundException
    {
        // [^.]\w+$  -> regex to get only activity name
//        List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
//        for(PackageInfo pack : packages)
//        {
        ActivityInfo[] activityInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES).activities;
        Log.i("BaseMain", context.getPackageName()+ " has total " + ((activityInfo==null)?0:activityInfo.length) + " activities");
        if(activityInfo!=null)
        {
            for(int i=0; i<activityInfo.length; i++)
            {
                Log.i("PC", context.getPackageName() + " ::: " + activityInfo[i].name);
            }
        }
//        }
    }

    public static JSONArray remove(JSONArray jsonArray, int position) {
        try {
            if (jsonArray == null) {
                throw new NullPointerException("Utilities :: remove null pointer excpetion");
            }

            if (jsonArray.length() == 0 || position < 0 || position > jsonArray.length() - 1) {
                return jsonArray;
            } else {
                JSONArray tempJSon = new JSONArray();
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (i != position) {
                        tempJSon.put(jsonArray.optJSONObject(i));
                    }
                }
                Utils.log(Utils.class, "remove :: jsonArraylength = " + jsonArray.length() + " tempjson = " + tempJSon.length() + " position = " + position);
                return tempJSon;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void shareApp(Context context) {
        context.startActivity(Intent.createChooser(getDefaultShareIntent("Practical Answers Android App",
                "http://play.google.com/store/apps/details?id="
                        + context.getPackageName()), "Share this App"));
    }

    public static Intent getDefaultShareIntent(String title, String content) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
        intent.putExtra(android.content.Intent.EXTRA_TEXT, content);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;

    }


    public static void doRate(Context context) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                    .parse("market://details?id=" + context.getPackageName()))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                    .parse("http://play.google.com/store/apps/details?id="
                            + context.getPackageName()))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }

    }

    public static String getUrlFromYoutubeKey(String youtubeKey) {
        if (!TextUtils.isEmpty(youtubeKey)) {
            String url = "https://i.ytimg.com/vi/" + youtubeKey + "/hqdefault.jpg";
            return url;
        }
        return null;
    }

    public static String getRelativeTime(String date, String dateFormat) {
        //"yyyy-MM-dd"
        Log.d("utilities", "date = " + date);
        if (TextUtils.isEmpty(date)) {
            return "Just Now";
        }
        if (date == null || date.isEmpty())
            return "Just Now";
        try {
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
//            format.setTimeZone(TimeZone.getTimeZone("GMT+5:45"));
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date dt = format.parse(date);
            return (String) DateUtils.getRelativeTimeSpanString(dt.getTime(), System.currentTimeMillis(), 0L, DateUtils.FORMAT_ABBREV_ALL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int getVersioncode(Context context) {
        int version = 1;
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    @SuppressLint("RestrictedApi")
    public static void removeShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
        }
    }

    public static void loadImageWithGlide(Context context, String url, ImageView iv_thumbnail, final ProgressBar progressBar) {
        Glide.with(context).
                load(url)
                .centerCrop()
                .placeholder(android.R.drawable.ic_menu_close_clear_cancel)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                        return false;
                    }
                })
                .crossFade(1000)
                .into(iv_thumbnail);
    }

    public static String readFileFromInputStream(InputStream is) {
        try {
            int size = is.available();
            byte buffer[] = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static void copyClipboard(Context context, String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            @SuppressWarnings("deprecation")
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }

    public static String getInputStreamFromFile(Context context, String filename) {
        Log.d("Utilities", "filname = " + filename);
        try {
            InputStream is = context.getAssets().open(filename);
            return readFileFromInputStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONArray getSampleJSONArray(){
        JSONArray jsonArray = new JSONArray();
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", "test title");
            jsonObject.put("description", "testDescription");
            jsonObject.put("url", "https://img.youtube.com/vi/P2Dac91J0DU/mqdefault.jpg");
            jsonArray.put(jsonObject);
            jsonArray.put(jsonArray.optJSONObject(0));
            jsonArray.put(jsonArray.optJSONObject(0));
            jsonArray.put(jsonArray.optJSONObject(0));
        }catch (Exception e){}
        return jsonArray;
    }

}
