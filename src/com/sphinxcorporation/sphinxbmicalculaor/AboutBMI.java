package com.sphinxcorporation.sphinxbmicalculaor;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

/**
 * Created by Karthik on 6/21/13.
 */
public class AboutBMI extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_bmi);

        TextView aboutBMI = (TextView)findViewById(R.id.AboutBMITextView);
        // aboutBMI.setText(Html.fromHtml(getResources().getString(R.string.aboutBMISource)));
        aboutBMI.setText(Html.fromHtml(getResources().getString(R.string.aboutBMISource)));
    }
}