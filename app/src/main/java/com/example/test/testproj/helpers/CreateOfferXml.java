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
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by yanyasha228 on 28.02.18.
 */

public class CreateOfferXml {
    private OfferServerList offerServerList;
    private List<Offer> favOfferList;
    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;
    private Document document;
    private String xmlNewStr;
//    private Context context;

    public CreateOfferXml(List<Offer> favOfferList) {
        this.favOfferList = favOfferList;
//        this.context = context;
        offerServerList = OfferServerList.getInstance();
    }

    public String createXml() {
        //stringToXml();
        document = offerServerList.getChangedMainDoc();
        document.normalize();

        Element rootElement = document.getDocumentElement();
        Node yml_catalog = rootElement.getChildNodes().item(1);
        NodeList yml_catalogChild = yml_catalog.getChildNodes();
        Node categories = yml_catalogChild.item(15);
        NodeList categoriesList = categories.getChildNodes();
        Node offers = yml_catalogChild.item(17);
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
                 String curParamTextCont = currentParam.getTextContent() ;
                 String curParamTextContRepl = curParamTextCont.replaceAll(" ","");
                 if(!curParamTextContRepl.equals("")) {
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

//    public static File documentToFile(Document documentToFile, Context ActContext)  {
//        DOMSource source = new DOMSource(documentToFile);
//        File xmlPriceList = new File(ActContext.getCacheDir(),"co2ShopPriceListForRozetka.xml");
//        try {
//            FileWriter writer = new FileWriter(xmlPriceList);
//            StreamResult result = new StreamResult(writer);
//
//            TransformerFactory transformerFactory = TransformerFactory.newInstance();
//            Transformer transformer = transformerFactory.newTransformer();
//            transformer.transform(source, result);
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//return xmlPriceList;
//    }


}
