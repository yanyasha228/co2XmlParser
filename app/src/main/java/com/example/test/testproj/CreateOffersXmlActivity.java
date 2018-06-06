package com.example.test.testproj;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.test.testproj.adapters.DBAdapter;
import com.example.test.testproj.helpers.CreateOfferXml;
import com.example.test.testproj.models.Offer;

import java.io.File;
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
        xmlStrToSend = new CreateOfferXml(favOffersList).createXml();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, xmlStrToSend);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
        finish();
//        fileToSend = new CreateOfferXml(favOffersList,this).createXml();
//        Intent sendIntent = new Intent();
//        Uri path = Uri.fromFile(fileToSend);
//        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.setType("vnd.android.cursor.dir/email");
//        sendIntent.putExtra(Intent.EXTRA_STREAM, path);
//        startActivity(sendIntent);
//        finish();
    }

    private void getAllFavorites() {
        dbAdapter.open();
        favOffersList = dbAdapter.getOffers();
        dbAdapter.close();
    }
}
