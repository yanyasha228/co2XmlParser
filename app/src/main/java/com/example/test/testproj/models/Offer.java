package com.example.test.testproj.models;

import org.w3c.dom.Document;

/**
 * Created by yanyasha228 on 27.02.18.
 */

public class Offer implements SearchingItem {
    private long id;
    private String name;
    private String image;
    private String url;
    private double price;
    private int fav;
    private String currencyId;
    private int stock_quantity;
    private int categoryId;
    private int category_parentId;
    private String vendor;
    private String description;
    private Document params_xml;
    private int offer_changed;
    private int offer_available;
    private boolean selectedForChangingPrice;

    public Offer() {
    }

    public Offer(long id, String name,
                 String image,
                 String url,
                 double price,
                 int fav,
                 String currencyId,
                 int stock_quantity,
                 int categoryId,
                 int category_parentId,
                 String vendor,
                 String description,
                 Document params_xml,
                 int offer_changed,
                 int offer_available) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.url = url;
        this.price = price;
        this.fav = fav;
        this.currencyId = currencyId;
        this.stock_quantity = stock_quantity;
        this.categoryId = categoryId;
        this.category_parentId = category_parentId;
        this.vendor = vendor;
        this.description = description;
        this.params_xml = params_xml;
        this.offer_changed = offer_changed;
        this.offer_available = offer_available;
    }

    public int getOffer_available() {
        return offer_available;
    }

    public void setOffer_available(int offer_available) {
        this.offer_available = offer_available;
    }

    public long getId() {
        return id;
    }

    public void setId(long _id) {
        this.id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getFav() {
        return fav;
    }

    public void setFav(int favorite) {
        this.fav = favorite;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public int getStock_quantity() {
        return stock_quantity;
    }

    public void setStock_quantity(int stock_quantity) {
        this.stock_quantity = stock_quantity;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCategory_parentId() {
        return category_parentId;
    }

    public void setCategory_parentId(int category_parentId) {
        this.category_parentId = category_parentId;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Document getParams_xml() {
        return params_xml;
    }

    public void setParams_xml(Document params_xml) {
        this.params_xml = params_xml;
    }

    public int getOffer_changed() {
        return offer_changed;
    }

    public void setOffer_changed(int offer_changed) {
        this.offer_changed = offer_changed;
    }

    public boolean isSelectedForChangingPrice() {
        return selectedForChangingPrice;
    }

    public void setSelectedForChangingPrice(boolean selectedForChangingPrice) {
        this.selectedForChangingPrice = selectedForChangingPrice;
    }
}
