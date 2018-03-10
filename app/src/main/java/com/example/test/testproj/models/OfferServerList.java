package com.example.test.testproj.models;

import org.w3c.dom.Document;

import java.util.List;

/**
 * Created by yanyasha228 on 27.02.18.
 */

public class OfferServerList {
    private static OfferServerList instance;
    private static List<Offer> OfferServerMainList;
    private static String stringOffersXmlMain;
    private static Document changedMainDoc;
    private static double USD_CURRENCY;
    private static double EUR_CURRENCY;
    public static OfferServerList getInstance() {
        if (instance == null) instance = new OfferServerList();
        return instance;
    }
    private OfferServerList() {
    }

    public static List<Offer> getOfferServerMainList() {
        return OfferServerMainList;
    }

    public static void setOfferServerMainList(List<Offer> offerServerMainList) {
        OfferServerMainList = offerServerMainList;
    }

    public static String getStringOffersXmlMain() {
        return stringOffersXmlMain;
    }

    public static void setStringOffersXmlMain(String stringOffersXmlMain) {
        OfferServerList.stringOffersXmlMain = stringOffersXmlMain;
    }

    public static double getUsdCurrency() {
        return USD_CURRENCY;
    }

    public static void setUsdCurrency(double usdCurrency) {
        USD_CURRENCY = usdCurrency;
    }

    public static double getEurCurrency() {
        return EUR_CURRENCY;
    }

    public static void setEurCurrency(double eurCurrency) {
        EUR_CURRENCY = eurCurrency;
    }

    public static Document getChangedMainDoc() {
        return changedMainDoc;
    }

    public static void setChangedMainDoc(Document changedMainDoc) {
        OfferServerList.changedMainDoc = changedMainDoc;
    }
}
