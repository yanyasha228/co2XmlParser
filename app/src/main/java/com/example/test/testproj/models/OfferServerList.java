package com.example.test.testproj.models;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
    public static Document AIR_RIFLES_PARAMS;
    public static Document FLAUBERT_PISTOLS_PARAMS;
    public static Document AIR_PISTOLS_PARAMS;
    public static Document STARTING_PISTOLS_PARAMS;
    public static Document AIMS_PARAMS;
    public static Document CASES_PARAMS;
    public static Document HOLSTERS_PARAMS;
    public static Document BAGS_PARAMS;
    public static Document EMPTY_PARAMS;
    public static Document SPARE_PARTS;

    public static OfferServerList getInstance() {
        if (instance == null) instance = new OfferServerList();
        return instance;
    }

    private OfferServerList() {
    }

    public void createCategoriesParams() {
        createAirRiflesParams();
        createFlaubertPistolsParams();
        createAirPistolsParams();
        createStartingPistolsParams();
        createAimsParams();
        createCasesParams();
        createHolstersParams();
        createBagsParams();
        createEmptyParams();
        createSparePartsParams();
    }
private void createEmptyParams(){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            Document doc = factory.newDocumentBuilder().newDocument();
            Element params = doc.createElement("params");
            doc.appendChild(params);

            EMPTY_PARAMS = doc;
        } catch (Exception ex) {
            throw new RuntimeException("Error in creating xml", ex);
        }
}

    private void createSparePartsParams(){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            Document doc = factory.newDocumentBuilder().newDocument();
            Element params = doc.createElement("params");
            doc.appendChild(params);


            Element param5 = doc.createElement("param");
            param5.setAttribute("name", "Вид");
            params.appendChild(param5);

            Element param = doc.createElement("param");
            param.setAttribute("name", "Совместимость");
            params.appendChild(param);

            Element param1 = doc.createElement("param");
            param1.setAttribute("name", "Масса");
            params.appendChild(param1);

            Element param2 = doc.createElement("param");
            param2.setAttribute("name", "Дополнительные характеристики");
            params.appendChild(param2);

            Element param3 = doc.createElement("param");
            param3.setAttribute("name", "Гарантия");
            params.appendChild(param3);

            SPARE_PARTS = doc;
        } catch (Exception ex) {
            throw new RuntimeException("Error in creating xml", ex);
        }
    }

private void createHolstersParams(){
    try {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document doc = factory.newDocumentBuilder().newDocument();
        Element params = doc.createElement("params");
        doc.appendChild(params);


        Element param5 = doc.createElement("param");
        param5.setAttribute("name", "Материал");
        params.appendChild(param5);

        Element param = doc.createElement("param");
        param.setAttribute("name", "Тип");
        params.appendChild(param);

        Element param1 = doc.createElement("param");
        param1.setAttribute("name", "Совместимость");
        params.appendChild(param1);

        Element param2 = doc.createElement("param");
        param2.setAttribute("name", "Дополнительные характеристики");
        params.appendChild(param2);

        Element param3 = doc.createElement("param");
        param3.setAttribute("name", "Цвет");
        params.appendChild(param3);

        Element param4 = doc.createElement("param");
        param4.setAttribute("name", "Гарантия");
        params.appendChild(param4);

        Element param7 = doc.createElement("param");
        param7.setAttribute("name", "Теги");
        params.appendChild(param7);

        HOLSTERS_PARAMS = doc;
    } catch (Exception ex) {
        throw new RuntimeException("Error in creating xml", ex);
    }
}
private void createCasesParams(){
    try {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document doc = factory.newDocumentBuilder().newDocument();
        Element params = doc.createElement("params");
        doc.appendChild(params);

        Element param = doc.createElement("param");
        param.setAttribute("name", "Тип");
        params.appendChild(param);

        Element param5 = doc.createElement("param");
        param5.setAttribute("name", "Материал");
        params.appendChild(param5);

        Element param1 = doc.createElement("param");
        param1.setAttribute("name", "Совместимость");
        params.appendChild(param1);

        Element param13 = doc.createElement("param");
        param13.setAttribute("name", "Размеры");
        params.appendChild(param13);

        Element param2 = doc.createElement("param");
        param2.setAttribute("name", "Дополнительные характеристики");
        params.appendChild(param2);

        Element param3 = doc.createElement("param");
        param3.setAttribute("name", "Цвет");
        params.appendChild(param3);

        Element param4 = doc.createElement("param");
        param4.setAttribute("name", "Гарантия");
        params.appendChild(param4);

        CASES_PARAMS = doc;
    } catch (Exception ex) {
        throw new RuntimeException("Error in creating xml", ex);
    }


}
private void createBagsParams(){
    try {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document doc = factory.newDocumentBuilder().newDocument();
        Element params = doc.createElement("params");
        doc.appendChild(params);

        Element param = doc.createElement("param");
        param.setAttribute("name", "Назначение");
        params.appendChild(param);

        Element param1 = doc.createElement("param");
        param1.setAttribute("name", "Тип");
        params.appendChild(param1);

        Element param13 = doc.createElement("param");
        param13.setAttribute("name", "Система крепления");
        params.appendChild(param13);

        Element param2 = doc.createElement("param");
        param2.setAttribute("name", "Материал");
        params.appendChild(param2);

        Element param3 = doc.createElement("param");
        param3.setAttribute("name", "Дополнительные характеристики");
        params.appendChild(param3);

        Element param4 = doc.createElement("param");
        param4.setAttribute("name", "Размеры");
        params.appendChild(param4);

        Element param5 = doc.createElement("param");
        param5.setAttribute("name", "Цвет");
        params.appendChild(param5);

        Element param6 = doc.createElement("param");
        param6.setAttribute("name", "Страна регистрации бренда");
        params.appendChild(param6);

        Element param7 = doc.createElement("param");
        param7.setAttribute("name", "Страна-производитель товара");
        params.appendChild(param7);

        Element param8 = doc.createElement("param");
        param8.setAttribute("name", "Гарантия");
        params.appendChild(param8);

        BAGS_PARAMS = doc;
    } catch (Exception ex) {
        throw new RuntimeException("Error in creating xml", ex);
    }


}
    private void createAimsParams() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            Document doc = factory.newDocumentBuilder().newDocument();
            Element params = doc.createElement("params");
            doc.appendChild(params);

            Element param = doc.createElement("param");
            param.setAttribute("name", "Тип прицела");
            params.appendChild(param);

            Element param1 = doc.createElement("param");
            param1.setAttribute("name", "База крепления");
            params.appendChild(param1);

            Element param13 = doc.createElement("param");
            param13.setAttribute("name", "Максимальная кратность, х");
            params.appendChild(param13);

            Element param2 = doc.createElement("param");
            param2.setAttribute("name", "Подсветка прицельной марки");
            params.appendChild(param2);

            Element param3 = doc.createElement("param");
            param3.setAttribute("name", "Диаметр трубы прицела");
            params.appendChild(param3);

            Element param4 = doc.createElement("param");
            param4.setAttribute("name", "Отстройка от параллакса");
            params.appendChild(param4);

            Element param5 = doc.createElement("param");
            param5.setAttribute("name", "Минимальная кратность, х");
            params.appendChild(param5);

            Element param6 = doc.createElement("param");
            param6.setAttribute("name", "Крепление в комплекте");
            params.appendChild(param6);

            Element param7 = doc.createElement("param");
            param7.setAttribute("name", "Крепление");
            params.appendChild(param7);

            Element param8 = doc.createElement("param");
            param8.setAttribute("name", "Диаметр передней линзы");
            params.appendChild(param8);

            Element param9 = doc.createElement("param");
            param9.setAttribute("name", "Барабанчики поправок, цена деления");
            params.appendChild(param9);

            Element param10 = doc.createElement("param");
            param10.setAttribute("name", "Тип сетки");
            params.appendChild(param10);

            Element paramDopol = doc.createElement("param");
            paramDopol.setAttribute("name", "Дополнительные характеристики");
            params.appendChild(paramDopol);

            Element param11 = doc.createElement("param");
            param11.setAttribute("name", "Длина прицела");
            params.appendChild(param11);

            Element param12 = doc.createElement("param");
            param12.setAttribute("name", "Вес");
            params.appendChild(param12);

            Element param14 = doc.createElement("param");
            param14.setAttribute("name", "Цвет");
            params.appendChild(param14);

            Element param15 = doc.createElement("param");
            param15.setAttribute("name", "Гарантия");
            params.appendChild(param15);


            AIMS_PARAMS = doc;
        } catch (Exception ex) {
            throw new RuntimeException("Error in creating xml", ex);
        }

    }

    private void createStartingPistolsParams() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            Document doc = factory.newDocumentBuilder().newDocument();
            Element params = doc.createElement("params");
            doc.appendChild(params);

            Element param = doc.createElement("param");
            param.setAttribute("name", "Тип");
            params.appendChild(param);

            Element param1 = doc.createElement("param");
            param1.setAttribute("name", "Калибр");
            params.appendChild(param1);

            Element param13 = doc.createElement("param");
            param13.setAttribute("name", "Общая длина");
            params.appendChild(param13);

            Element param2 = doc.createElement("param");
            param2.setAttribute("name", "Обойма");
            params.appendChild(param2);

            Element param3 = doc.createElement("param");
            param3.setAttribute("name", "Вес");
            params.appendChild(param3);

            Element paramDopol = doc.createElement("param");
            paramDopol.setAttribute("name", "Дополнительные характеристики");
            params.appendChild(paramDopol);

            Element param4 = doc.createElement("param");
            param4.setAttribute("name", "Цвет");
            params.appendChild(param4);

            Element param12 = doc.createElement("param");
            param12.setAttribute("name", "Гарантия");
            params.appendChild(param12);


            STARTING_PISTOLS_PARAMS = doc;
        } catch (Exception ex) {
            throw new RuntimeException("Error in creating xml", ex);
        }

    }


    private void createAirPistolsParams() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            Document doc = factory.newDocumentBuilder().newDocument();
            Element params = doc.createElement("params");
            doc.appendChild(params);

            Element param = doc.createElement("param");
            param.setAttribute("name", "Начальная скорость пули");
            params.appendChild(param);

            Element param1 = doc.createElement("param");
            param1.setAttribute("name", "Источник энергии");
            params.appendChild(param1);

            Element param13 = doc.createElement("param");
            param13.setAttribute("name", "Материал");
            params.appendChild(param13);

            Element param2 = doc.createElement("param");
            param2.setAttribute("name", "Тип боеприпасов");
            params.appendChild(param2);

            Element param3 = doc.createElement("param");
            param3.setAttribute("name", "Емкость магазина (барабана)");
            params.appendChild(param3);

            Element param4 = doc.createElement("param");
            param4.setAttribute("name", "Имитация отдачи (blowback)");
            params.appendChild(param4);

            Element param5 = doc.createElement("param");
            param5.setAttribute("name", "Калибр");
            params.appendChild(param5);

            Element param6 = doc.createElement("param");
            param6.setAttribute("name", "Общая длина");
            params.appendChild(param6);

            Element param7 = doc.createElement("param");
            param7.setAttribute("name", "Тип прицела");
            params.appendChild(param7);

            Element param8 = doc.createElement("param");
            param8.setAttribute("name", "Вес");
            params.appendChild(param8);

            Element paramDopol = doc.createElement("param");
            paramDopol.setAttribute("name", "Дополнительно");
            params.appendChild(paramDopol);

            Element param12 = doc.createElement("param");
            param12.setAttribute("name", "Гарантия");
            params.appendChild(param12);

            AIR_PISTOLS_PARAMS = doc;
        } catch (Exception ex) {
            throw new RuntimeException("Error in creating xml", ex);
        }

    }

    private void createFlaubertPistolsParams() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            Document doc = factory.newDocumentBuilder().newDocument();
            Element params = doc.createElement("params");
            doc.appendChild(params);

            Element param = doc.createElement("param");
            param.setAttribute("name", "Калибр");
            params.appendChild(param);

            Element param1 = doc.createElement("param");
            param1.setAttribute("name", "Материал накладки на рукояти");
            params.appendChild(param1);

            Element param13 = doc.createElement("param");
            param13.setAttribute("name", "Вид ствола");
            params.appendChild(param13);

            Element param2 = doc.createElement("param");
            param2.setAttribute("name", "Длина ствола");
            params.appendChild(param2);

            Element param3 = doc.createElement("param");
            param3.setAttribute("name", "Вместимость барабана");
            params.appendChild(param3);

            Element param4 = doc.createElement("param");
            param4.setAttribute("name", "Начальная скорость пули");
            params.appendChild(param4);

            Element param5 = doc.createElement("param");
            param5.setAttribute("name", "Общая длина");
            params.appendChild(param5);

            Element param6 = doc.createElement("param");
            param6.setAttribute("name", "Материал барабана");
            params.appendChild(param6);

            Element param7 = doc.createElement("param");
            param7.setAttribute("name", "Тип прицела");
            params.appendChild(param7);

            Element param8 = doc.createElement("param");
            param8.setAttribute("name", "Тип взвода");
            params.appendChild(param8);

            Element param9 = doc.createElement("param");
            param9.setAttribute("name", "Покрытие");
            params.appendChild(param9);

            Element param10 = doc.createElement("param");
            param10.setAttribute("name", "Цвет рукояти");
            params.appendChild(param10);

            Element param11 = doc.createElement("param");
            param11.setAttribute("name", "Вес");
            params.appendChild(param11);

            Element param14 = doc.createElement("param");
            param14.setAttribute("name", "Комплектация");
            params.appendChild(param14);

            Element param12 = doc.createElement("param");
            param12.setAttribute("name", "Гарантия");
            params.appendChild(param12);


            FLAUBERT_PISTOLS_PARAMS = doc;
        } catch (Exception ex) {
            throw new RuntimeException("Error in creating xml", ex);
        }

    }

    private void createAirRiflesParams() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            Document doc = factory.newDocumentBuilder().newDocument();
            Element params = doc.createElement("params");
            doc.appendChild(params);

            Element param = doc.createElement("param");
            param.setAttribute("name", "Калибр");
            params.appendChild(param);

            Element param1 = doc.createElement("param");
            param1.setAttribute("name", "Начальная скорость пули");
            params.appendChild(param1);

            Element param13 = doc.createElement("param");
            param13.setAttribute("name", "Общая длина");
            params.appendChild(param13);

            Element param2 = doc.createElement("param");
            param2.setAttribute("name", "Источник энергии");
            params.appendChild(param2);

            Element param3 = doc.createElement("param");
            param3.setAttribute("name", "Тип прицела");
            params.appendChild(param3);

            Element param4 = doc.createElement("param");
            param4.setAttribute("name", "Характеристика прицела");
            params.appendChild(param4);

            Element param5 = doc.createElement("param");
            param5.setAttribute("name", "Тип крепления оптики");
            params.appendChild(param5);

            Element param6 = doc.createElement("param");
            param6.setAttribute("name", "Емкость магазина");
            params.appendChild(param6);

            Element param7 = doc.createElement("param");
            param7.setAttribute("name", "Материал приклада");
            params.appendChild(param7);

            Element param8 = doc.createElement("param");
            param8.setAttribute("name", "Тип взвода");
            params.appendChild(param8);

            Element param9 = doc.createElement("param");
            param9.setAttribute("name", "Вес");
            params.appendChild(param9);

            Element param10 = doc.createElement("param");
            param10.setAttribute("name", "Тип боеприпасов");
            params.appendChild(param10);

            Element param11 = doc.createElement("param");
            param11.setAttribute("name", "Комплект поставки");
            params.appendChild(param11);

            Element param12 = doc.createElement("param");
            param12.setAttribute("name", "Гарантия");
            params.appendChild(param12);

            AIR_RIFLES_PARAMS = doc;
        } catch (Exception ex) {
            throw new RuntimeException("Error in creating xml", ex);
        }

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
