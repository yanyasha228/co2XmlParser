package com.example.test.testproj;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.test.testproj.Utils.SearchUtils;
import com.example.test.testproj.adapters.DBAdapter;
import com.example.test.testproj.adapters.ShowsListAdapter;
import com.example.test.testproj.models.Offer;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment which manipulates the data of favorites show
 *
 * @author Ruslan Zosimov
 * @version 1.0
 */

public class FavoritesFragment extends Fragment implements ShowsListAdapter.OfferClickListener {

    private RecyclerView recyclerView;
    private EditText search;
    private static List<Offer> showFavoritesList;
    private ShowsListAdapter showListAdapter;
    private DBAdapter dbAdapter;
    private TextView noDataResults;
    private SearchUtils<Offer> offerSearchUtils = new SearchUtils<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        getAllFavorites();
        showListAdapter.setFilter(showFavoritesList);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_favorites, container, false);
        search = (EditText) layout.findViewById(R.id.searchFavorites);
        noDataResults = (TextView) layout.findViewById(R.id.noResultsFavorites);
        dbAdapter = new DBAdapter(getActivity());
        getAllFavorites();
        recyclerView = layout.findViewById(R.id.showListFavorites);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        showListAdapter = new ShowsListAdapter(getActivity(), showFavoritesList);
        showListAdapter.setOfferClickListener(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(showListAdapter);


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Checking internet connection
                //if (!connectivityHelper.isConnected())
                   // Toast.makeText(getActivity(), "Waiting for internet connection...", Toast.LENGTH_SHORT).show();
                //Searching in favorites show
                String searchText = (s.toString()).toLowerCase();
                List<Offer> searchShowList = offerSearchUtils.findSearchingItemByNonFullName(showFavoritesList , searchText);

//                for (Offer searchOffers : showFavoritesList) {
//                    String name = searchOffers.getName().toLowerCase();
//                    if (name.contains(searchText)) {
//                        searchShowList.add(searchOffers);
//                    }
//                }
                if (searchShowList.size() == 0 && s.length() != 0) {
                    recyclerView.setVisibility(View.GONE);
                    noDataResults.setVisibility(View.VISIBLE);
                } else {
                    noDataResults.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

                showFavoritesList = searchShowList;
                if (s.length() == 0) getAllFavorites();
                showListAdapter.setFilter(showFavoritesList);
            }
        });
        //If press ENTER -> hide keyboard
        search.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER:
                            KeyboardFavorites.hide(search);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        return layout;
    }

    //Class with hideKeyboard func
    public static class KeyboardFavorites {
        public static void hide(View view) {
            Context context = view.getContext();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //Get all favorites show from database
    private void getAllFavorites() {
        dbAdapter.open();
        showFavoritesList = dbAdapter.getOffers();
        dbAdapter.close();
    }

    //Deleting Show from database
    private void deleteFavoriteShow(int pos) {
        dbAdapter.open();
        dbAdapter.delete(showFavoritesList.get(pos).getId());
        dbAdapter.close();
        //getAllFavorites();
        showFavoritesList.remove(pos);
        showListAdapter.setFilter(showFavoritesList);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /*
*Implementing interface which is in the custom RecyclerView.Adapter   {@link ShowsListAdapter}
*/
    @Override
    public void showClicked(View view, int position) {
        switch (view.getId()) {
            case R.id.showItemLayout:
                Intent intent = new Intent(getActivity(), ChangeOfferActivity.class);
                intent.putExtra("offersId", showFavoritesList.get(position).getId());
                getActivity().startActivity(intent);
                break;
            case R.id.offerFavorite:
                deleteFavoriteShow(position);
                break;
            case R.id.offerImage:
                Intent imageIntent = new Intent(getActivity(), ImageActivity.class);
                imageIntent.putExtra("url", showFavoritesList.get(position).getImage());
                getActivity().startActivity(imageIntent);
                break;
        }

    }
}
