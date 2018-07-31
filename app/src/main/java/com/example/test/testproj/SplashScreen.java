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

import com.example.test.testproj.adapters.DBAdapter;
import com.example.test.testproj.helpers.ConnectivityHelper;
import com.example.test.testproj.helpers.XmlOffersBuilder;
import com.example.test.testproj.models.Offer;
import com.example.test.testproj.models.OfferServerList;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
    private DBAdapter dbAdapter;
    private List<Offer> oldShowFavoritesList;
    private List<Offer> listForValidate;
    private String urlForXmlDownloading;

    private static final String OLD_FAVORITES_URL = "http://www.co2.biz.ua/wp-content/uploads/2018/03/co2ShopPriceListForRozetka.xml";


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
        offerServerList.createCategoriesParams();
        dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        oldShowFavoritesList = dbAdapter.getOffers();
        dbAdapter.close();
        urlForXmlDownloading = getIntent().getStringExtra("urlXML");
        final Intent intent = new Intent(this, MainActivity.class);

        loadingApp(connectivityHelper,intent);

    }



    private void insertOldFavOffersIntoDB(List<Offer> offersListToInsert) {
        dbAdapter.open();

        for (Offer offerToInsert : offersListToInsert) {
            dbAdapter.insert(offerToInsert);
        }

        dbAdapter.close();
    }

    private void loadingApp(ConnectivityHelper connectivityHelper, final Intent intent) {
        if (connectivityHelper.isConnected()) {
            Thread timer = new Thread() {
                public void run() {
                    try {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder().url(urlForXmlDownloading).build();
                        List<Offer> testOfList = oldShowFavoritesList;
                        if (oldShowFavoritesList.size() == 0) {
                            Request requestForOldFav = new Request.Builder().url(OLD_FAVORITES_URL).build();
                            try {
                                Response response = client.newCall(requestForOldFav).execute();
                                String xmlOldFav = response.body().string();
                                List<Offer> oldOffersList = new XmlOffersBuilder(xmlOldFav).getOffersFromValidXml();
                                insertOldFavOffersIntoDB(oldOffersList);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        try {

                            Response response = client.newCall(request).execute();
                            String xmlString = response.body().string();

                            offerServerList.setStringOffersXmlMain(xmlString);
                            offerServerList.setOfferServerMainList(new XmlOffersBuilder(xmlString).getOfferMainList());
                            validateFavoriteList(offerServerList.getOfferServerMainList());

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
        } else
            Toast.makeText(this, "Waiting for internet connection...", Toast.LENGTH_SHORT).show();
    }

    private void validateFavoriteList(List<Offer> listForValidate) {

        for (Offer oldImageOffer : oldShowFavoritesList) {
            for (Offer validImageOffer : listForValidate) {
                if (oldImageOffer.getUrl().equals(validImageOffer.getUrl())) {
                    oldImageOffer.setImage(validImageOffer.getImage());
                }
            }
        }

        for (Offer newFavoriteOffer : oldShowFavoritesList) {
            dbAdapter.open();
            dbAdapter.update(newFavoriteOffer);
            dbAdapter.close();
        }

    }
}
