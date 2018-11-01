package com.example.test.testproj.Utils;

import com.example.test.testproj.models.SearchingItem;


import java.util.ArrayList;
import java.util.List;

public final class SearchUtils<T extends SearchingItem> {


    public List<T> findSearchingItemByNonFullName(List<T> listForSearching,
                                                                     String nonFullSearchItemName) {

        String[] searchingWords = nonFullSearchItemName.split("\\s");

        List<T> firstWordSearch = new ArrayList<>();

        List<T> searchingItemsThatMatch = new ArrayList<>();

        if (searchingWords.length != 0 && !searchingWords[0].equalsIgnoreCase("")) {
            for(T searchingItem : listForSearching){
                if (searchingItem.getName().toLowerCase().contains(searchingWords[0])){
                    firstWordSearch.add(searchingItem);
                }
            }
        } else return searchingItemsThatMatch;


        if (firstWordSearch.size() != 0) {
            out:
            for (T searchingItemForSearch : firstWordSearch) {
                for (int i = 1; i < searchingWords.length; i++) {
                    if (!searchingItemForSearch.getName().toLowerCase().contains(searchingWords[i])) continue out;
                }
                searchingItemsThatMatch.add(searchingItemForSearch);
            }
        }


        return searchingItemsThatMatch;
    }



}
