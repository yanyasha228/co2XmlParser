package com.example.test.testproj.helpers;

import android.util.Log;



import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 *
 *
 *
 *
 * @author Ruslan Zosimov
 * @version 1.0
 */

public class ShowBuilder {
/*
    private String jsonShowArray;
    private List<JsonShow> showsDataList = new ArrayList<JsonShow>();


    public ShowBuilder(String jsonShowArray) {
        this.jsonShowArray = jsonShowArray;
        getDataFromJson();
    }
//Clearing Show description of HTML tags
    private String descriptionClearing(String des) {
        String clearDes = des.replaceAll("<p>", "");
        clearDes = clearDes.replaceAll("</p>", "");
        clearDes = clearDes.replaceAll("<b>", "");
        clearDes = clearDes.replaceAll("</b>", "");
        clearDes = clearDes.replaceAll("\"", "");
        return clearDes;
    }
//Deserializing
    private void getDataFromJson() {
        Type jsonShowListType = new TypeToken<ArrayList<JsonShow>>() {
        }.getType();
        showsDataList = new Gson().fromJson(jsonShowArray, jsonShowListType);
    }
//If object in list has null fields -> set Defaults
    private void validaJsonShowList() {
        for (JsonShow jsonNonValideShow : showsDataList) {
            if (jsonNonValideShow.getShow().getImage() == null)
                jsonNonValideShow.getShow().setImage(new JsonShow.Image("", "https://static.tvmaze.com/images/no-img/no-img-portrait-text.png"));
            if (jsonNonValideShow.getShow().getRating() == null)
                jsonNonValideShow.getShow().setRating(new JsonShow.Rating(0.0));
            if(jsonNonValideShow.getShow().getSummary()==null)
                jsonNonValideShow.getShow().setSummary("No description");
        }
    }

//Return list which contains Favorites show
    public List<Show> getShowListWithFavoritesValidation(List<Show> showFavoriteList) {
        List<Show> showListWithFavoritesValidation = getShowList();
        for (Show favShow : showFavoriteList)
            for (Show nVshow : showListWithFavoritesValidation) {
                if (nVshow.getName().equals(favShow.getName()) && nVshow.getUrl().equals(favShow.getUrl())) {
                    nVshow.setFav(1);
                    nVshow.setId(favShow.getId());
                }

            }
        return showListWithFavoritesValidation;
    }
//Get "original" Show_list
    public List<Show> getShowList() {
        validaJsonShowList();
        List<Show> mainShowList = new ArrayList<Show>();
        int i = 0;
        for (JsonShow jshow : showsDataList) {

            Show show = new Show();
            show.setName(jshow.getShow().getName());
            show.setDescription(descriptionClearing(jshow.getShow().getSummary()));
            show.setImage(jshow.getShow().getImage().getOriginal());
            show.setRate(jshow.getShow().getRating().getAverage());
            show.setUrl(jshow.getShow().getUrl());
            show.setFav(0);
            mainShowList.add(show);
        }
        return mainShowList;
    }
    */
}
