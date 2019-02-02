package com.example.test.testproj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.test.testproj.adapters.DBAdapter;
import com.example.test.testproj.helpers.CreateOfferXml;
import com.example.test.testproj.helpers.XmlFtpUploader;
import com.example.test.testproj.helpers.XmlOffersBuilder;
import com.example.test.testproj.models.Offer;
import com.example.test.testproj.models.OfferServerList;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CreateOffersXmlActivity extends AppCompatActivity implements View.OnClickListener {
    private Button createXmlButt;
    private DBAdapter dbAdapter;
    private List<Offer> favOffersList;
    private TextView offersQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_offers_xml);
        createXmlButt = (Button) findViewById(R.id.createFavXml);
        offersQuantity = (TextView) findViewById(R.id.checkOffersQuantityXml);
        createXmlButt.setOnClickListener(this);
        dbAdapter = new DBAdapter(this);
        getAllFavorites();
        offersQuantity.setText(String.valueOf(favOffersList.size()));

    }

    @Override
    public void onClick(View view) {
//        String xmlStrToSend = new CreateOfferXml(favOffersList).createXml();
//        Intent sendIntent = new Intent();
//        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_TEXT, xmlStrToSend);
//        sendIntent.setType("text/plain");
//        startActivity(sendIntent);
//        finish();

        File fileToSend = CreateOfferXml.stringToFile(new CreateOfferXml(favOffersList).createXml(), this);
        String str = new CreateOfferXml(favOffersList).createXml();
        new XmlFtpUploader(this,
                fileToSend).execute();
        finish();
    }

    private void getAllFavorites() {
        dbAdapter.open();
        favOffersList = dbAdapter.getOffers();
        dbAdapter.close();
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }



}
