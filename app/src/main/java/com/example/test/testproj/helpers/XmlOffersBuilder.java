package com.example.test.testproj.helpers;

import com.example.test.testproj.models.Offer;
import com.example.test.testproj.models.OfferServerList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by yanyasha228 on 27.02.18.
 */

public class XmlOffersBuilder {
    private List<Offer> offerMainList = new ArrayList<Offer>();
    private String xmlString;
    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;
    private Document document;
    private OfferServerList offerServerList;
    //private static double USD_CURRENCY = 27.7;
    //private static double EUR_CURRENCY = 35;


    public XmlOffersBuilder(String xmlString) {
        this.xmlString = xmlString;
        offerServerList = OfferServerList.getInstance();

    }

    private void stringToXml() {
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(new InputSource(new StringReader(xmlString)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Offer> getOfferMainList() {
        validateCategoriesInXml();
        Element rootElement = document.getDocumentElement();
        NodeList categoriesList = rootElement.getElementsByTagName("category");
        NodeList xmlOfferList = rootElement.getElementsByTagName("offer");
        NodeList currenciesList = rootElement.getElementsByTagName("currency");
        Node currency = null;
        NamedNodeMap currenciesAttributesList = null;
        Node currencyAttributeId = null;
        Node currencyAttributeRate = null;
        Node category = null;
        NamedNodeMap categoriesAttributes = null;
        Node categoriesId = null;
        Node categoriesParentId = null;
        NodeList xmlOffer = null;
        Node currentXmlOffer = null;
        Node currentXmlOfferParam = null;

        for (int i = 0; i < currenciesList.getLength(); i++) {
            currency = currenciesList.item(i);
            currenciesAttributesList = currency.getAttributes();
            currencyAttributeId = currenciesAttributesList.getNamedItem("id");
            currencyAttributeRate = currenciesAttributesList.getNamedItem("rate");
            if (currencyAttributeId.getNodeValue().equalsIgnoreCase("usd"))
                offerServerList.setUsdCurrency(Double.valueOf(currencyAttributeRate.getNodeValue()));
            if (currencyAttributeId.getNodeValue().equalsIgnoreCase("eur"))
                offerServerList.setEurCurrency(Double.valueOf(currencyAttributeRate.getNodeValue()));

        }
        double usd = offerServerList.getUsdCurrency();
        double eur = offerServerList.getEurCurrency();
        for (int i = 0; i < xmlOfferList.getLength(); i++) {
            Offer offer = new Offer();
            offer.setFav(0);
            offer.setId(0);
            offer.setCategory_parentId(0);
            offer.setVendor("No Vendor");
            offer.setStock_quantity((5 + (int) (Math.random() * 20)));
            currentXmlOffer = xmlOfferList.item(i);
            xmlOffer = currentXmlOffer.getChildNodes();
            for (int j = 0; j < xmlOffer.getLength(); j++) {
                currentXmlOfferParam = xmlOffer.item(j);
                switch (currentXmlOfferParam.getNodeName().toLowerCase()) {
                    case "url":
                        offer.setUrl(currentXmlOfferParam.getTextContent());
                        break;
                    case "price":
                        offer.setPrice(Double.valueOf(currentXmlOfferParam.getTextContent()));
                        break;
                    case "currencyid":
                        offer.setCurrencyId(currentXmlOfferParam.getTextContent());
                        break;
                    case "picture":
                        if (offer.getImage() == null)
                            offer.setImage(currentXmlOfferParam.getTextContent());
                        else break;
                        break;
                    case "name":
                        offer.setName(currentXmlOfferParam.getTextContent());
                        break;
                    case "vendor":
                        offer.setVendor(currentXmlOfferParam.getTextContent());
                        break;
                    case "categoryid":
                        offer.setCategoryId(Integer.valueOf(currentXmlOfferParam.getTextContent()));
                        for (int c = 0; c < categoriesList.getLength(); c++) {
                            category = categoriesList.item(c);
                            categoriesAttributes = category.getAttributes();
                            categoriesId = categoriesAttributes.getNamedItem("id");
                            if (categoriesId.getNodeValue().equalsIgnoreCase(currentXmlOfferParam.getTextContent())) {
                                categoriesParentId = categoriesAttributes.getNamedItem("parentId");
                                if (categoriesParentId != null) {
                                    offer.setCategory_parentId(Integer.valueOf(categoriesParentId.getNodeValue()));
                                    // parentCategoryId become a mainCategoryId
                                    //offer.setCategoryId(Integer.valueOf(categoriesParentId.getNodeValue()));
                                }
                                categoriesParentId = null;
                            }
                        }
                        break;
                }

            }
            offerMainList.add(offer);

        }
        validateXmlOffersList();
        offerServerList.setChangedMainDoc(document);
        return offerMainList;
    }

    //Adding categories and renaming old
    private void validateCategoriesInXml() {
        stringToXml();
        Element rootElement = document.getDocumentElement();
        Node yml_catalog = rootElement.getChildNodes().item(1);
        NodeList yml_catalogChild = yml_catalog.getChildNodes();
        Node categories = yml_catalogChild.item(15);
        NodeList categoriesList = categories.getChildNodes();
        Node category = null;
        Node id = null;
        NamedNodeMap attributes = null;

        Element vintovkiCategory = document.createElement("category");
        vintovkiCategory.setAttribute("id", "100000");
        vintovkiCategory.setTextContent("Пневматические винтовки");
        categories.appendChild(vintovkiCategory);

        Element pistoletyFloberaCategory = document.createElement("category");
        pistoletyFloberaCategory.setAttribute("id", "100001");
        pistoletyFloberaCategory.setTextContent("Оружие под патрон Флобера");
        categories.appendChild(pistoletyFloberaCategory);

        Element pistoletyPnevmatCategory = document.createElement("category");
        pistoletyPnevmatCategory.setAttribute("id", "100002");
        pistoletyPnevmatCategory.setTextContent("Пневматические пистолеты");
        categories.appendChild(pistoletyPnevmatCategory);

        Element pistoletyStartovieCategory = document.createElement("category");
        pistoletyStartovieCategory.setAttribute("id", "100003");
        pistoletyStartovieCategory.setTextContent("Стартовые пистолеты");
        categories.appendChild(pistoletyStartovieCategory);

        Element pricelyCategory = document.createElement("category");
        pricelyCategory.setAttribute("id", "100004");
        pricelyCategory.setTextContent("Прицелы");
        categories.appendChild(pricelyCategory);

        Element chechliIKeisyCategory = document.createElement("category");
        chechliIKeisyCategory.setAttribute("id", "100005");
        chechliIKeisyCategory.setTextContent("Чехлы и кейсы");
        categories.appendChild(chechliIKeisyCategory);

        Element koburyCategory = document.createElement("category");
        koburyCategory.setAttribute("id", "100006");
        koburyCategory.setTextContent("Кобуры");
        categories.appendChild(koburyCategory);

        Element sumkiIPodsumkiCategory = document.createElement("category");
        sumkiIPodsumkiCategory.setAttribute("id", "100007");
        sumkiIPodsumkiCategory.setTextContent("Сумки и подсумки");
        categories.appendChild(sumkiIPodsumkiCategory);

    }

    private void validateXmlOffersList() {
        for (Offer nonValideOffer : offerMainList) {
            if (nonValideOffer.getImage() == null)
                nonValideOffer.setImage("https://static.tvmaze.com/images/no-img/no-img-portrait-text.png");
            if (nonValideOffer.getCurrencyId() == null)
                nonValideOffer.setCurrencyId("?");
            if (nonValideOffer.getName() == null)
                nonValideOffer.setName("No name");
            if (nonValideOffer.getUrl() == null)
                nonValideOffer.setUrl("http://co2.prom.ua/");

        }
        for (Offer offer : offerMainList) {
            if (offer.getCurrencyId().equalsIgnoreCase("eur")) {
                offer.setPrice(((double) Math.round((offer.getPrice() * offerServerList.getEurCurrency()) * 100) / 100));
                offer.setCurrencyId("UAH");
            }
            if (offer.getCurrencyId().equalsIgnoreCase("usd")) {
                offer.setPrice(((double) Math.round((offer.getPrice() * offerServerList.getUsdCurrency()) * 100) / 100));
                offer.setCurrencyId("UAH");
            }
        }

        for (Offer cOffer : offerMainList) {

            if (cOffer.getCategoryId() == 295983 || cOffer.getCategoryId() == 295984 || cOffer.getCategoryId() == 295985 || cOffer.getCategoryId() == 295986) {
                cOffer.setCategoryId(100000);
            }

            if (cOffer.getCategoryId() == 295988) {
                cOffer.setCategoryId(100001);
            }

            if (cOffer.getCategoryId() == 295987 || cOffer.getCategoryId() == 295989 || cOffer.getCategoryId() == 295990) {
                cOffer.setCategoryId(100002);
            }

            if (cOffer.getCategoryId() == 646982) {
                cOffer.setCategoryId(100003);
            }

            if (cOffer.getCategoryId() == 295993|| cOffer.getCategoryId() == 646969|| cOffer.getCategoryId() == 646981) {
                cOffer.setCategoryId(100004);
            }
            if (cOffer.getCategoryId()==295998){
              if(cOffer.getName().toLowerCase().contains("чехол")||cOffer.getName().toLowerCase().contains("кейс")) cOffer.setCategoryId(100005);
              if(cOffer.getName().toLowerCase().contains("кобура")) cOffer.setCategoryId(100006);
              if(cOffer.getName().toLowerCase().contains("сумка")||cOffer.getName().toLowerCase().contains("подсумок")) cOffer.setCategoryId(100007);
            }

        }

    }

}


