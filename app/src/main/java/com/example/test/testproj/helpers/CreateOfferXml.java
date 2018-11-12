package com.example.test.testproj.helpers;

import android.content.Context;
import android.os.Environment;

import com.example.test.testproj.models.Offer;
import com.example.test.testproj.models.OfferServerList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by yanyasha228 on 28.02.18.
 */

public class CreateOfferXml {
    private OfferServerList offerServerList;
    private List<Offer> favOfferList;
    private Document document;



    public CreateOfferXml(List<Offer> favOfferList) {
        this.favOfferList = favOfferList;

        offerServerList = OfferServerList.getInstance();
    }

    public String createXml() {

        document = offerServerList.getCopyOfChangedMainDoc();

        document.normalize();

        Element rootElement = document.getDocumentElement();
        NodeList categoriesGroups = rootElement.getElementsByTagName("categories");
        NodeList offersGroups = rootElement.getElementsByTagName("offers");
        Node yml_catalog = rootElement.getChildNodes().item(1);
        NodeList yml_catalogChild = yml_catalog.getChildNodes();
        Node categories = categoriesGroups.item(0);
        NodeList categoriesList = categories.getChildNodes();
        Node offers = offersGroups.item(0);
        NodeList offersList = offers.getChildNodes();
        Node offer = null;
        NodeList offerParamsList = null;
        Node currentOfferParam = null;
        Node category = null;
        Node id = null;
        NamedNodeMap attributes = null;
        Node shopTitle = null;

        //Deleting unnecessary ids
        deleting:
        for (int i = 1; i < categoriesList.getLength(); i++) {
            category = categoriesList.item(i);
            if (category.getTextContent().equalsIgnoreCase("\n")) continue;
            attributes = category.getAttributes();
            id = attributes.getNamedItem("id");
            for (Offer offerCat : favOfferList) {
                if (id.getTextContent().equalsIgnoreCase(String.valueOf(offerCat.getCategoryId())))
                    continue deleting;
                if (id.getTextContent().equalsIgnoreCase(String.valueOf(offerCat.getCategory_parentId())))
                    continue deleting;
            }
            categories.removeChild(category);
        }
//Deleting unnecessary offers and the replacement of nodes in existing offers

        for (int i = 0; i < offersList.getLength(); i++) {
            Offer imageOffer = new Offer();
            Boolean exist = false;
            offer = offersList.item(i);
            if (offer.getTextContent().equalsIgnoreCase("\n")) continue;
            offerParamsList = offer.getChildNodes();
            for (int j = 0; j < offerParamsList.getLength(); j++) {
                currentOfferParam = offerParamsList.item(j);
                if (currentOfferParam.getNodeName().equalsIgnoreCase("picture"))
                    offer.removeChild(currentOfferParam);
                if (currentOfferParam.getNodeName().equalsIgnoreCase("vendor"))
                    offer.removeChild(currentOfferParam);
                if (currentOfferParam.getNodeName().equalsIgnoreCase("name"))
                    offer.removeChild(currentOfferParam);
                if (currentOfferParam.getNodeName().equalsIgnoreCase("param"))
                    offer.removeChild(currentOfferParam);
                if (currentOfferParam.getNodeName().equalsIgnoreCase("description"))
                    offer.removeChild(currentOfferParam);
                if (currentOfferParam.getNodeName().equalsIgnoreCase("categoryid"))
                    offer.removeChild(currentOfferParam);
                if (currentOfferParam.getNodeName().equalsIgnoreCase("currencyid"))
                    currentOfferParam.setTextContent("UAH");
                if (currentOfferParam.getNodeName().equalsIgnoreCase("url")) {
                    for (Offer favOffer : favOfferList) {
                        if (currentOfferParam.getTextContent().equalsIgnoreCase(favOffer.getUrl())) {
                            exist = true;
                            imageOffer = favOffer;

                        }
                    }

                }

            }
            if (!exist) {
                offers.removeChild(offer);
            } else {
                Element picture = document.createElement("picture");
                picture.setTextContent(imageOffer.getImage());
                offer.appendChild(picture);
                Element stock_quantity = document.createElement("stock_quantity");
                stock_quantity.setTextContent(String.valueOf(imageOffer.getStock_quantity()));
                offer.appendChild(stock_quantity);
                Element price = (Element) offer;
                NodeList offersPrice = price.getElementsByTagName("price");
                Node checkPrice = offersPrice.item(0);
                checkPrice.setTextContent(String.valueOf(imageOffer.getPrice()));
                Element vendor = document.createElement("vendor");
                vendor.setTextContent(imageOffer.getVendor());
                offer.appendChild(vendor);
                Element name = document.createElement("name");
                name.setTextContent(imageOffer.getName());
                offer.appendChild(name);
                Element categoryId = document.createElement("categoryId");
                categoryId.setTextContent(String.valueOf(imageOffer.getCategoryId()));
                offer.appendChild(categoryId);
                Element description = document.createElement("description");
                description.setTextContent(imageOffer.getDescription());
                offer.appendChild(description);

                NamedNodeMap offersAttributesOf = offer.getAttributes();
                Node availableAttrOf = offersAttributesOf.getNamedItem("available");
                if (availableAttrOf != null) {
                    if (imageOffer.getOffer_available() == 1) {
                        availableAttrOf.setNodeValue("true");
                    } else availableAttrOf.setNodeValue("false");
                } else if (imageOffer.getOffer_available() == 1) {
                    ((Element) offer).setAttribute("available", "true");
                } else ((Element) offer).setAttribute("available", "false");

                addParams(offer, imageOffer);


            }
        }

//XmlHead
        for (int i = 0; i < yml_catalogChild.getLength(); i++) {
            shopTitle = yml_catalogChild.item(i);
            if (shopTitle.getNodeName().equalsIgnoreCase("name")) {
                shopTitle.setTextContent("CO2");
            }
            if (shopTitle.getNodeName().equalsIgnoreCase("company")) {
                shopTitle.setTextContent("ФОП Фролов А.В.");
            }
            if (shopTitle.getNodeName().equalsIgnoreCase("url")) {
                shopTitle.setTextContent("http://co2.biz.ua");
            }
            if (shopTitle.getNodeName().equalsIgnoreCase("platform")) {
                yml_catalog.removeChild(shopTitle);
            }
            if (shopTitle.getNodeName().equalsIgnoreCase("agency")) {
                yml_catalog.removeChild(shopTitle);
            }
            if (shopTitle.getNodeName().equalsIgnoreCase("email")) {
                yml_catalog.removeChild(shopTitle);
            }


        }

        return xmlToString(document);
    }

    //Adding param fields and delete without empty
    private void addParams(Node offerNode, Offer offerFav) {
        Document offerXmlParams = offerFav.getParams_xml();
        Element rootElement = offerXmlParams.getDocumentElement();
        NodeList paramList = rootElement.getElementsByTagName("param");
        Node currentParam = null;
        NamedNodeMap paramAttributes = null;
        Node attribute = null;
        if (paramList != null) {
            for (int i = 0; i < paramList.getLength(); i++) {
                currentParam = paramList.item(i);
                paramAttributes = currentParam.getAttributes();
                attribute = paramAttributes.getNamedItem("name");
                String curParamTextCont = currentParam.getTextContent();
                String curParamTextContRepl = curParamTextCont.replaceAll(" ", "");
                if (!curParamTextContRepl.equals("")) {
                    Element addParam = document.createElement("param");
                    addParam.setAttribute("name", attribute.getTextContent());
                    addParam.setTextContent(currentParam.getTextContent());
                    offerNode.appendChild(addParam);
                }


            }

        }
    }

    public static String xmlToString(Document doc) {
        try {
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            return sw.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error converting to String", ex);
        }
    }

    public static Document stringToXml(String strForMarshalling) {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        Document document = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(new InputSource(new StringReader(strForMarshalling)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return document;
    }

    public static File stringToFile(String stringXml, Context context) {
        String filename = "co2ShopPriceListForRozetka.xml";
        File file = new File(context.getFilesDir(), filename);
        FileOutputStream outputStream;

        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(stringXml.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return file;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }


}
