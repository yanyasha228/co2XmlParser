package com.example.test.testproj;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.testproj.helpers.ConnectivityHelper;
import com.example.test.testproj.helpers.XmlOffersBuilder;
import com.example.test.testproj.models.Offer;
import com.example.test.testproj.models.OfferServerList;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yanyasha228 on 22.01.18.
 */

public class SplashScreen extends AppCompatActivity {

private TextView tv;
private ImageView iv;
private ConnectivityHelper connectivityHelper;
private OfferServerList offerServerList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        connectivityHelper = new ConnectivityHelper(this);
        tv = (TextView) findViewById(R.id.tSplashView);
        iv = (ImageView) findViewById(R.id.iSplashView);
        Animation splashAnim = AnimationUtils.loadAnimation(this, R.anim.splashtransition);
        tv.startAnimation(splashAnim);
        iv.startAnimation(splashAnim);
        offerServerList = OfferServerList.getInstance();
        final Intent intent = new Intent(this, MainActivity.class);
        if (connectivityHelper.isConnected()) {
            Thread timer = new Thread() {
                public void run() {
                    try {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder().url("http://co2.kh.ua/files/temp/e178092a8ffd65f2639c199c54ceef8b.xml").build();
                        try {
                            Response response = client.newCall(request).execute();
                            String xmlString = response.body().string();
                            offerServerList.setStringOffersXmlMain(xmlString);
                            offerServerList.setOfferServerMainList(new XmlOffersBuilder(xmlString).getOfferMainList());
                            //List<Offer> offersxml = offerServerList.getOfferServerMainList();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } finally {
                        startActivity(intent);
                        finish();
                    }
                }
            };
            timer.start();
        } else Toast.makeText(this, "Waiting for internet connection...", Toast.LENGTH_SHORT).show();
    }
}
