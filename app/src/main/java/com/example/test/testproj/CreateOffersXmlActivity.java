package com.example.test.testproj;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.test.testproj.adapters.DBAdapter;
import com.example.test.testproj.helpers.CreateOfferXml;
import com.example.test.testproj.models.Offer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class CreateOffersXmlActivity extends AppCompatActivity implements View.OnClickListener {
    private Button createXmlButt;
    private DBAdapter dbAdapter;
    private List<Offer> favOffersList;
    //    private File fileToSend;
    private String xmlStrToSend;
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
//        xmlStrToSend = new CreateOfferXml(favOffersList).createXml();
//        Intent sendIntent = new Intent();
//        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_TEXT, xmlStrToSend);
//        sendIntent.setType("text/plain");
//        startActivity(sendIntent);
//        finish();

        File fileToSend = CreateOfferXml.stringToFile(new CreateOfferXml(favOffersList).createXml() , this);
        Intent sendIntent = new Intent();
        Uri path = FileProvider.getUriForFile(this,  "com.example.test.testproj.fileprovider", fileToSend);
        String fileStrtTest = null;
        try {
            fileStrtTest = getStringFromFile(fileToSend);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_STREAM, path);
        startActivity(sendIntent);
        finish();
//        /data/user/0/com.example.test.testproj/files/co2ShopPriceListForRozetka.xml
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

    public static String getStringFromFile (File file) throws Exception {
        FileInputStream fin = new FileInputStream(file);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }
}
