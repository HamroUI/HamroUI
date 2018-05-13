package com.yuvi.mantraui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;

public class DeeplinkActivity extends AppCompatActivity {
    Pref pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = new Pref(this);
        Uri fromDeeplink = getIntent().getData();
        String key = fromDeeplink.getQueryParameter("action");

        switch (key) {
            case "rateus":
                Utils.doRate(this);
                finish();
                break;
            case "share":
                Utils.shareApp(this);
                finish();
                break;
            case "disclaimer":
                String message = pref.getPreferences(Pref.KEY_DISCLAIMER);
                showDialog("Discalimer", message, "", "");
                break;
            case "setting":
                break;
            case "aboutus":
                break;
            case "checkforupdate":
                String mesg = fromDeeplink.getQueryParameter("mesg");
                String link = "market://details?id=" + pref.getPreferences(Pref.KEY_PACKAGE_NAME);
                showDialog("New Updates available", mesg, "Check for update", link);
                break;
        }

    }

    private void showDialog(String title, String message, String actionName, final String link) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(Html.fromHtml(message));
        if (!TextUtils.isEmpty(link)) {
            builder.setPositiveButton(actionName, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
                    finish();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DeeplinkActivity.this.finish();
                }
            });
        }
        builder.create().show();
    }
}
