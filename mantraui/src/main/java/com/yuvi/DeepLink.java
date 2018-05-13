package com.yuvi;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import com.yuvi.mantraui.Pref;
import com.yuvi.mantraui.Utils;

public class DeepLink {
    AppCompatActivity appCompatActivity;
    Pref pref;

    public DeepLink(AppCompatActivity activityCompat) {
        this.appCompatActivity = activityCompat;
        pref = new Pref(appCompatActivity);
    }

    public void manageDeeplink(String deeplink) {
        String key = Uri.parse(deeplink).getQueryParameter("action");

        switch (key) {
            case "rateus":
                Utils.doRate(appCompatActivity);
                break;
            case "share":
                Utils.shareApp(appCompatActivity, pref.getPreferences(Pref.KEY_PACKAGE_NAME));
                return;
            case "disclaimer":
                String message = pref.getPreferences(Pref.KEY_DISCLAIMER);
                showDialog("Discalimer", message, "", "");
                break;
            case "setting":
                try {
                    appCompatActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(pref.getPreferences(Pref.KEY_SETTING_LINK)))
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case "aboutus":
                break;
            case "checkforupdate":
                String mesg = Uri.parse(deeplink).getQueryParameter("mesg");
                String link = "market://details?id=" + pref.getPreferences(Pref.KEY_PACKAGE_NAME);
                showDialog("New Updates available", mesg, "Check for update", link);
                break;
            default:
                appCompatActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(deeplink))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                return;
        }

    }

    private void showDialog(String title, String message, String actionName, final String link) {
        AlertDialog.Builder builder = new AlertDialog.Builder(appCompatActivity);
        builder.setTitle(title);
        builder.setMessage(Html.fromHtml(message));
        if (!TextUtils.isEmpty(link)) {
            builder.setPositiveButton(actionName, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    appCompatActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link))
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });

            builder.setNegativeButton("Cancel", null);
        }
        builder.create().show();

    }
}
