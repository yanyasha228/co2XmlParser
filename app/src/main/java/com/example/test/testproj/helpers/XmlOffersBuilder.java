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
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by yanyasha228 on 27.02.18.
 */

public class XmlOffersBuilder {
    private List<Offer> offerMainList = new ArrayList<Offer>();
    private String xmlString;
    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;
    private Document cDoc;
    private OfferServerList offerServerList;
    private static int AIR_RIFLES_CATEGORY = 100000;
    private static int FLAUBERT_PISTOLS_CATEGORY = 100001;
    private static int AIR_PISTOLS_CATEGORY = 100002;
    private static int STARTING_PISTOLS_CATEGORY = 100003;
    private static int AIMS_CATEGORY = 100004;
    private static int CASES_CATEGORY = 100005;
    private static int HOLSTERS_CATEGORY = 100006;
    private static int BAGS_CATEGORY = 100007;
    private static int EMPTY_CATEGORY = 200000;
    private static int SPARE_PARTS_CATEGORY = 100008;

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
            cDoc = documentBuilder.parse(new InputSource(new StringReader(xmlString)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Offer> getOffersFromValidXml() {
        stringToXml();
        List<Offer> offerListFromValidXml = new ArrayList<Offer>();
        Element rootElement = cDoc.getDocumentElement();
        NodeList xmlOfferList = rootElement.getElementsByTagName("offer");

        for (int i = 0; i < xmlOfferList.getLength(); i++) {
            Offer offer = new Offer();
            offer.setFav(1);
            offer.setOffer_changed(1);
            Node currentXmlOffer = xmlOfferList.item(i);
            NodeList xmlOffer = currentXmlOffer.getChildNodes();
            NamedNodeMap offerAttributeList = currentXmlOffer.getAttributes();
            Node availableAttribute = offerAttributeList.getNamedItem("available");
            if (availableAttribute != null) {
                if (availableAttribute.getNodeValue().equalsIgnoreCase("true")) {
                    offer.setOffer_available(1);
                } else offer.setOffer_available(0);
            } else offer.setOffer_available(0);

            Document docParams = null;
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setNamespaceAware(true);
            Element params = null;

            try {
                docParams = docFactory.newDocumentBuilder().newDocument();
                params = docParams.createElement("params");
            } catch (Exception ex) {
                throw new RuntimeException("Error in creating xml", ex);
            }

            for (int j = 0; j < xmlOffer.getLength(); j++) {
                Node currentXmlOfferParam = xmlOffer.item(j);
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
                        offer.setImage(currentXmlOfferParam.getTextContent());

                        break;

                    case "stock_quantity":
                        offer.setStock_quantity(Integer.valueOf(currentXmlOfferParam.getTextContent()));
                        break;

                    case "vendor":
                        offer.setVendor(currentXmlOfferParam.getTextContent());

                        break;

                    case "name":
                        offer.setName(currentXmlOfferParam.getTextContent());
                        break;

                    case "categoryid":
                        offer.setCategoryId(Integer.valueOf(currentXmlOfferParam.getTextContent()));
                        break;

                    case "description":
                        offer.setDescription(currentXmlOfferParam.getTextContent());
                        break;

                    case "param":
                        Element newParam = docParams.createElement("param");
                        newParam.setAttribute("name",
                                currentXmlOfferParam.
                                        getAttributes().
                                        getNamedItem("name").
                                        getNodeValue());
                        newParam.setTextContent(currentXmlOfferParam.getTextContent());
                        params.appendChild(newParam);
                        break;

                }
            }
            docParams.appendChild(params);
            offer.setParams_xml(docParams);
            String testDoc = CreateOfferXml.xmlToString(docParams);
            offerListFromValidXml.add(offer);
        }

        return offerListFromValidXml;
    }

    public List<Offer> getOfferMainList() {
        validateCategoriesInXml();
        Element rootElement = cDoc.getDocumentElement();
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

            NamedNodeMap offerAttributeList = currentXmlOffer.getAttributes();
            Node availableAttribute = offerAttributeList.getNamedItem("available");
            if (availableAttribute != null) {
                if (availableAttribute.getNodeValue().equalsIgnoreCase("true")) {
                    offer.setOffer_available(1);
                } else offer.setOffer_available(0);
            } else offer.setOffer_available(0);

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
                    case "description":
                        offer.setDescription(currentXmlOfferParam.getTextContent());
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
        offerServerList.setChangedMainDoc(cDoc);
        return offerMainList;
    }

    //Adding categories and renaming old
    private void validateCategoriesInXml() {
        stringToXml();
        Element rootElement = cDoc.getDocumentElement();
        Node yml_catalog = rootElement.getChildNodes().item(1);
        NodeList yml_catalogChild = yml_catalog.getChildNodes();
        Node categories = yml_catalogChild.item(15);
        NodeList categoriesList = categories.getChildNodes();
        Node category = null;
        Node id = null;
        NamedNodeMap attributes = null;

        Element vintovkiCategory = cDoc.createElement("category");
        vintovkiCategory.setAttribute("id", String.valueOf(AIR_RIFLES_CATEGORY));
        vintovkiCategory.setTextContent("Пневматические винтовки");
        categories.appendChild(vintovkiCategory);

        Element pistoletyFloberaCategory = cDoc.createElement("category");
        pistoletyFloberaCategory.setAttribute("id", String.valueOf(FLAUBERT_PISTOLS_CATEGORY));
        pistoletyFloberaCategory.setTextContent("Оружие под патрон Флобера");
        categories.appendChild(pistoletyFloberaCategory);

        Element pistoletyPnevmatCategory = cDoc.createElement("category");
        pistoletyPnevmatCategory.setAttribute("id", String.valueOf(AIR_PISTOLS_CATEGORY));
        pistoletyPnevmatCategory.setTextContent("Пневматические пистолеты");
        categories.appendChild(pistoletyPnevmatCategory);

        Element pistoletyStartovieCategory = cDoc.createElement("category");
        pistoletyStartovieCategory.setAttribute("id", String.valueOf(STARTING_PISTOLS_CATEGORY));
        pistoletyStartovieCategory.setTextContent("Стартовые пистолеты");
        categories.appendChild(pistoletyStartovieCategory);

        Element pricelyCategory = cDoc.createElement("category");
        pricelyCategory.setAttribute("id", String.valueOf(AIMS_CATEGORY));
        pricelyCategory.setTextContent("Прицелы");
        categories.appendChild(pricelyCategory);

        Element chechliIKeisyCategory = cDoc.createElement("category");
        chechliIKeisyCategory.setAttribute("id", String.valueOf(CASES_CATEGORY));
        chechliIKeisyCategory.setTextContent("Чехлы и кейсы");
        categories.appendChild(chechliIKeisyCategory);

        Element koburyCategory = cDoc.createElement("category");
        koburyCategory.setAttribute("id", String.valueOf(HOLSTERS_CATEGORY));
        koburyCategory.setTextContent("Кобуры");
        categories.appendChild(koburyCategory);

        Element sumkiIPodsumkiCategory = cDoc.createElement("category");
        sumkiIPodsumkiCategory.setAttribute("id", String.valueOf(BAGS_CATEGORY));
        sumkiIPodsumkiCategory.setTextContent("Сумки и подсумки");
        categories.appendChild(sumkiIPodsumkiCategory);

        Element zapchastiCategory = cDoc.createElement("category");
        zapchastiCategory.setAttribute("id", String.valueOf(SPARE_PARTS_CATEGORY));
        zapchastiCategory.setTextContent("Аксессуары для пневматики");
        categories.appendChild(zapchastiCategory);
    }

    private void validateXmlOffersList() {

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
                cOffer.setCategoryId(AIR_RIFLES_CATEGORY);
            }

            if (cOffer.getCategoryId() == 295988) {
                cOffer.setCategoryId(FLAUBERT_PISTOLS_CATEGORY);
            }

            if (cOffer.getCategoryId() == 295987 || cOffer.getCategoryId() == 295989 || cOffer.getCategoryId() == 295990) {
                cOffer.setCategoryId(AIR_PISTOLS_CATEGORY);
            }

            if (cOffer.getCategoryId() == 646982) {
                cOffer.setCategoryId(STARTING_PISTOLS_CATEGORY);
            }

            if (cOffer.getCategoryId() == 295993 || cOffer.getCategoryId() == 646969 || cOffer.getCategoryId() == 646981) {
                cOffer.setCategoryId(AIMS_CATEGORY);
            }
            if (cOffer.getCategoryId() == 295998) {
                if (cOffer.getName().toLowerCase().contains("чехол") || cOffer.getName().toLowerCase().contains("кейс"))
                    cOffer.setCategoryId(CASES_CATEGORY);
                if (cOffer.getName().toLowerCase().contains("кобура"))
                    cOffer.setCategoryId(HOLSTERS_CATEGORY);
                if (cOffer.getName().toLowerCase().contains("сумка") || cOffer.getName().toLowerCase().contains("подсумок"))
                    cOffer.setCategoryId(BAGS_CATEGORY);
            }

            if (cOffer.getCategoryId() == 295997 ||
                    cOffer.getCategory_parentId() == 295997 ||
                    cOffer.getCategoryId() == 6500407 ||
                    cOffer.getCategoryId() == 22590652) {
                cOffer.setCategoryId(SPARE_PARTS_CATEGORY);
            }


        }

        for (Offer nonValidOffer : offerMainList) {
            if (nonValidOffer.getImage() == null)
                nonValidOffer.setImage("https://static.tvmaze.com/images/no-img/no-img-portrait-text.png");
            if (nonValidOffer.getCurrencyId() == null)
                nonValidOffer.setCurrencyId("?");
            if (nonValidOffer.getName() == null)
                nonValidOffer.setName("No name");
            if (nonValidOffer.getUrl() == null)
                nonValidOffer.setUrl("http://co2.prom.ua/");
            if (nonValidOffer.getDescription() == null)
                nonValidOffer.setDescription("No Description");
            if (nonValidOffer.getParams_xml() == null)
                nonValidOffer.setParams_xml(OfferServerList.EMPTY_PARAMS);
            nonValidOffer.setOffer_changed(0);

            if (nonValidOffer.getCategoryId() == AIR_RIFLES_CATEGORY)
                nonValidOffer.setParams_xml(OfferServerList.AIR_RIFLES_PARAMS);
            if (nonValidOffer.getCategoryId() == FLAUBERT_PISTOLS_CATEGORY)
                nonValidOffer.setParams_xml(OfferServerList.FLAUBERT_PISTOLS_PARAMS);
            if (nonValidOffer.getCategoryId() == AIR_PISTOLS_CATEGORY)
                nonValidOffer.setParams_xml(OfferServerList.AIR_PISTOLS_PARAMS);
            if (nonValidOffer.getCategoryId() == STARTING_PISTOLS_CATEGORY)
                nonValidOffer.setParams_xml(OfferServerList.STARTING_PISTOLS_PARAMS);
            if (nonValidOffer.getCategoryId() == AIMS_CATEGORY)
                nonValidOffer.setParams_xml(OfferServerList.AIMS_PARAMS);
            if (nonValidOffer.getCategoryId() == CASES_CATEGORY)
                nonValidOffer.setParams_xml(OfferServerList.CASES_PARAMS);
            if (nonValidOffer.getCategoryId() == HOLSTERS_CATEGORY)
                nonValidOffer.setParams_xml(OfferServerList.HOLSTERS_PARAMS);
            if (nonValidOffer.getCategoryId() == BAGS_CATEGORY)
                nonValidOffer.setParams_xml(OfferServerList.BAGS_PARAMS);
            if (nonValidOffer.getCategoryId() == SPARE_PARTS_CATEGORY)
                nonValidOffer.setParams_xml(OfferServerList.SPARE_PARTS);


        }

    }

}


