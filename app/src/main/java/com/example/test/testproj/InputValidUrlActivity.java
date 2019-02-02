package com.example.test.testproj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.test.testproj.helpers.ConnectivityHelper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Utf8;

/**
 * Created by yanyasha228 on 27.07.18.
 */

public class InputValidUrlActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText inputUrl;
    private ImageView iv;
    private Button validateUrlButton;
//    private Button goOffLineButon;
    private ConnectivityHelper connectivityHelper;
    private Intent intent;
    SharedPreferences sPref;



    final String SAVED_URL = "saved_url";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_valid_url);
        intent = new Intent(this, SplashScreen.class);
        connectivityHelper = new ConnectivityHelper(this);
        inputUrl = (EditText) findViewById(R.id.inputUrl);
        validateUrlButton = (Button) findViewById(R.id.validateUrlButton);
//        goOffLineButon = (Button) findViewById(R.id.goOffLineButton);
//        goOffLineButon.setOnClickListener(this);
        validateUrlButton.setOnClickListener(this);
        iv = (ImageView) findViewById(R.id.iSplashView);
        Animation splashAnim = AnimationUtils.loadAnimation(this, R.anim.splashtransition);
        inputUrl.startAnimation(splashAnim);
        iv.startAnimation(splashAnim);
        inputUrl.setText(loadUrl());


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.validateUrlButton:

                validateUrl(inputUrl.getText().toString());
                break;

//
//            case R.id.goOffLineButton:
//                intent.putExtra("urlXML", "");
//                startActivity(intent);
//                finish();
//                break;

        }
    }

    private void validateUrl(final String urlForValidating) {
        if (connectivityHelper.isConnected()) {
            Thread timer = new Thread() {
                public void run() {
                    try {
//                        OkHttpClient client;
//                        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
//                        okBuilder.connectTimeout(2, TimeUnit.MINUTES);
//                        okBuilder.readTimeout(2 , TimeUnit.MINUTES);
//                        client = okBuilder.build();
//                        Request request = new Request.Builder().url(urlForValidating).build();
//                        Response response = null;
//                        try {
//                            response = client.newCall(request).execute();
//                            int i =0;
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }

//                        if (response != null &&
//                                response.isSuccessful()
//                                && response.headers().get("Content-Type").equalsIgnoreCase("text/xml; charset=utf-8")) {
                        String xmlStrN;
                        String line;
                        InputStream in;
                        BufferedReader bufferedReader;
                        StringBuilder stringBuilder = new StringBuilder();
                        URL url = new URL(urlForValidating);
                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                        try {
                            in = new BufferedInputStream(urlConnection.getInputStream());

                            bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                            try{

                                while ((line = bufferedReader.readLine()) != null) {
                                    stringBuilder.append(line);
                                }

                                xmlStrN = stringBuilder.toString();

                            }finally {
                                bufferedReader.close();
                            }
                        } finally {
                            urlConnection.disconnect();
                        }


                        if(xmlStrN.contains("yml_catalog") && xmlStrN.contains("<url>https://co2.kh.ua</url>")){
                            saveUrl(urlForValidating);
                            intent.putExtra("urlXML", urlForValidating);
                            startActivity(intent);
                            finish();
                        } else InputValidUrlActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InputValidUrlActivity.this, "Просроченная ссылка!!!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            };
            timer.start();
        } else
            Toast.makeText(this, "Ожидание соединения...", Toast.LENGTH_SHORT).show();
    }

    private void saveUrl(String urlForSave) {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_URL, urlForSave);
        ed.commit();
    }

    private String loadUrl() {
        sPref = getPreferences(MODE_PRIVATE);
        return sPref.getString(SAVED_URL, "");
    }
}
