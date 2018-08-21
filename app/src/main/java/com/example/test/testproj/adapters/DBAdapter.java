package com.example.test.testproj.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.example.test.testproj.helpers.CreateOfferXml;
import com.example.test.testproj.helpers.DBHelper;
import com.example.test.testproj.models.Offer;


/**
 * An adapter that helps to manipulate data in database
 *
 * @author Ruslan Zosimov
 * @version 1.0
 */


public class DBAdapter {

    private DBHelper dbHelper;
    private SQLiteDatabase database;

    public DBAdapter(Context context) {
        dbHelper = new DBHelper(context.getApplicationContext());
        dbHelper.create_db();
    }

    public DBAdapter open() {
        database = dbHelper.open();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public Cursor getAllEntries() {
        String[] columns = new String[]{DBHelper.COLUMN_ID,
                DBHelper.COLUMN_NAME,
                DBHelper.COLUMN_IMAGE,
                DBHelper.COLUMN_URL,
                DBHelper.COLUMN_PRICE,
                DBHelper.COLUMN_FAVORITE,
                DBHelper.COLUMN_CURRENCYID,
                DBHelper.COLUMN_STOC_QUANTITY,
                DBHelper.COLUMN_CATEGORY_ID,
                DBHelper.COLUMN_CATEGORY_PARENT_ID,
                DBHelper.COLUMN_VENDOR,
                DBHelper.COLUMN_DESCRIPTION,
                DBHelper.COLUMN_PARAMS_XML,
                DBHelper.COLUMN_OFFER_CHANGED,
                DBHelper.COLUMN_OFFER_AVAILABLE};
        return database.query(DBHelper.TABLE, columns, null, null, null, null, null);
    }

    public List<Offer> getOffers() {
        ArrayList<Offer> offers = new ArrayList<>();
        Cursor cursor = getAllEntries();
        if (cursor.moveToLast()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME));
                String image = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IMAGE));
                String url = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_URL));
                double price = cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUMN_PRICE));
                int favorite = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_FAVORITE));
                String currencyId = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CURRENCYID));
                int stock_quantity = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_STOC_QUANTITY));
                int categoryId = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_CATEGORY_ID));
                int category_parentId = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_CATEGORY_PARENT_ID));
                String vendor = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_VENDOR));
                String description = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DESCRIPTION));
                String params_xml = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PARAMS_XML));
                int offer_changed = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_OFFER_CHANGED));
                int offer_available = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_OFFER_AVAILABLE));
                offers.add(new Offer(id,
                        name,
                        image,
                        url,
                        price,
                        favorite,
                        currencyId,
                        stock_quantity,
                        categoryId,
                        category_parentId,
                        vendor,
                        description,
                        CreateOfferXml.stringToXml(params_xml),
                        offer_changed,
                        offer_available));
            }
            while (cursor.moveToPrevious());
        }
        cursor.close();
        return offers;
    }

    public boolean offerAlreadyExists(Offer offer) {
        boolean bool = false;
        List<Offer> allFavoritesShow = getOffers();
        if (allFavoritesShow != null) {
            for (Offer checkShow : allFavoritesShow) {
                if (offer.getUrl().equals(checkShow.getUrl()))
                    bool = true;
            }
        }
        return bool;
    }

    public long getCount() {
        return DatabaseUtils.queryNumEntries(database, DBHelper.TABLE);
    }

    public List<Offer> getOffersByNonFullName(String showNonFullName) {
        ArrayList<Offer> offers = new ArrayList<>();
        String query = String.format("SELECT * FROM %s WHERE %s LIKE ?", DBHelper.TABLE, DBHelper.COLUMN_NAME);
        Cursor cursor = database.rawQuery(query, new String[]{"%" + showNonFullName + "%"});
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME));
                String image = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IMAGE));
                String url = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_URL));
                double price = cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUMN_PRICE));
                int favorite = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_FAVORITE));
                String currencyId = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CURRENCYID));
                int stock_quantity = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_STOC_QUANTITY));
                int categoryId = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_CATEGORY_ID));
                int category_parentId = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_CATEGORY_PARENT_ID));
                String vendor = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_VENDOR));
                String description = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DESCRIPTION));
                String params_xml = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PARAMS_XML));
                int offer_changed = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_OFFER_CHANGED));
                int offer_available = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_OFFER_AVAILABLE));
                offers.add(new Offer(id,
                        name,
                        image,
                        url,
                        price,
                        favorite,
                        currencyId,
                        stock_quantity,
                        categoryId,
                        category_parentId,
                        vendor,
                        description,
                        CreateOfferXml.stringToXml(params_xml),
                        offer_changed,
                        offer_available));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return offers;
    }

    public Offer getOfferByName(String showName) {
        Offer offer = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?", DBHelper.TABLE, DBHelper.COLUMN_NAME);
        Cursor cursor = database.rawQuery(query, new String[]{showName});
        if (cursor.moveToFirst()) {
            long id = cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME));
            String image = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IMAGE));
            String url = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_URL));
            double price = cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUMN_PRICE));
            int favorite = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_FAVORITE));
            String currencyId = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CURRENCYID));
            int stock_quantity = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_STOC_QUANTITY));
            int categoryId = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_CATEGORY_ID));
            int category_parentId = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_CATEGORY_PARENT_ID));
            String vendor = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_VENDOR));
            String description = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DESCRIPTION));
            String params_xml = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PARAMS_XML));
            int offer_changed = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_OFFER_CHANGED));
            int offer_available = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_OFFER_AVAILABLE));
            offer = new Offer(id,
                    name,
                    image,
                    url,
                    price,
                    favorite,
                    currencyId,
                    stock_quantity,
                    categoryId,
                    category_parentId,
                    vendor,
                    description,
                    CreateOfferXml.stringToXml(params_xml),
                    offer_changed,
                    offer_available);


        }
        cursor.close();
        return offer;
    }

    public Offer getOfferById(long id) {
        Offer offer = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?", DBHelper.TABLE, DBHelper.COLUMN_ID);
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME));
            String image = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IMAGE));
            String url = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_URL));
            double price = cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUMN_PRICE));
            int favorite = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_FAVORITE));
            String currencyId = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CURRENCYID));
            int stock_quantity = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_STOC_QUANTITY));
            int categoryId = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_CATEGORY_ID));
            int category_parentId = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_CATEGORY_PARENT_ID));
            String vendor = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_VENDOR));
            String description = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DESCRIPTION));
            String params_xml = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PARAMS_XML));
            int offer_changed = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_OFFER_CHANGED));
            int offer_available = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_OFFER_AVAILABLE));
            offer = new Offer(id,
                    name,
                    image,
                    url,
                    price,
                    favorite,
                    currencyId,
                    stock_quantity,
                    categoryId,
                    category_parentId,
                    vendor,
                    description,
                    CreateOfferXml.stringToXml(params_xml),
                    offer_changed,
                    offer_available);
        }
        cursor.close();
        return offer;
    }

    public long insert(Offer offer) {

        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUMN_NAME, offer.getName());
        cv.put(DBHelper.COLUMN_IMAGE, offer.getImage());
        cv.put(DBHelper.COLUMN_URL, offer.getUrl());
        cv.put(DBHelper.COLUMN_PRICE, offer.getPrice());
        cv.put(DBHelper.COLUMN_FAVORITE, offer.getFav());
        cv.put(DBHelper.COLUMN_CURRENCYID, offer.getCurrencyId());
        cv.put(DBHelper.COLUMN_STOC_QUANTITY, offer.getStock_quantity());
        cv.put(DBHelper.COLUMN_CATEGORY_ID, offer.getCategoryId());
        cv.put(DBHelper.COLUMN_CATEGORY_PARENT_ID, offer.getCategory_parentId());
        cv.put(DBHelper.COLUMN_VENDOR, offer.getVendor());
        cv.put(DBHelper.COLUMN_DESCRIPTION, offer.getDescription());
        cv.put(DBHelper.COLUMN_PARAMS_XML, CreateOfferXml.xmlToString(offer.getParams_xml()));
        cv.put(DBHelper.COLUMN_OFFER_CHANGED, offer.getOffer_changed());
        cv.put(DBHelper.COLUMN_OFFER_AVAILABLE, offer.getOffer_available());

        return database.insert(DBHelper.TABLE, null, cv);
    }

    public long delete(long showId) {

        String whereClause = "_id = ?";
        String[] whereArgs = new String[]{String.valueOf(showId)};
        return database.delete(DBHelper.TABLE, whereClause, whereArgs);
    }

    public long update(Offer offer) {

        String whereClause = DBHelper.COLUMN_ID + "=" + String.valueOf(offer.getId());
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUMN_NAME, offer.getName());
        cv.put(DBHelper.COLUMN_IMAGE, offer.getImage());
        cv.put(DBHelper.COLUMN_URL, offer.getUrl());
        cv.put(DBHelper.COLUMN_PRICE, offer.getPrice());
        cv.put(DBHelper.COLUMN_FAVORITE, offer.getFav());
        cv.put(DBHelper.COLUMN_CURRENCYID, offer.getCurrencyId());
        cv.put(DBHelper.COLUMN_STOC_QUANTITY, offer.getStock_quantity());
        cv.put(DBHelper.COLUMN_CATEGORY_ID, offer.getCategoryId());
        cv.put(DBHelper.COLUMN_CATEGORY_PARENT_ID, offer.getCategory_parentId());
        cv.put(DBHelper.COLUMN_VENDOR, offer.getVendor());
        cv.put(DBHelper.COLUMN_DESCRIPTION, offer.getDescription());
        cv.put(DBHelper.COLUMN_PARAMS_XML, CreateOfferXml.xmlToString(offer.getParams_xml()));
        cv.put(DBHelper.COLUMN_OFFER_CHANGED, offer.getOffer_changed());
        cv.put(DBHelper.COLUMN_OFFER_AVAILABLE, offer.getOffer_available());
        return database.update(DBHelper.TABLE, cv, whereClause, null);
    }


}