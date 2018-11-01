package com.example.test.testproj;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.test.testproj.Utils.SearchUtils;
import com.example.test.testproj.adapters.DBAdapter;
import com.example.test.testproj.adapters.ShowsListAdapter;
import com.example.test.testproj.models.Offer;
import com.example.test.testproj.models.OfferServerList;

import java.util.List;

/**
 *
 *
 * @author Ruslan Zosimov
 * @version 1.0
 */

public class MainFragment extends Fragment implements ShowsListAdapter.OfferClickListener {

    private RecyclerView recyclerView;
    private EditText search;
    private DBAdapter dbAdapter;
    private ShowsListAdapter showListAdapter;
    private static List<Offer> offersMainList;
    private static List<Offer> offersFavoriteList;
    private static List<Offer> offersSearchList;
    private TextView noDataResults;
    private OfferServerList offerServerList;
    private SearchUtils<Offer> offerSearchUtils = new SearchUtils<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_main, container, false);

        search = (EditText) layout.findViewById(R.id.searchMain);

        noDataResults = (TextView) layout.findViewById(R.id.noResultsMain);

        dbAdapter = new DBAdapter(getActivity());

        offerServerList = OfferServerList.getInstance();


        getAllFavorites();

        if(offerServerList.getOfferServerMainList().size()==0){
            offersMainList = offersFavoriteList;
        } else offersMainList = offerServerList.getOfferServerMainList();

        offersSearchList = offersFavoriteList;


        recyclerView = layout.findViewById(R.id.showListMain);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        showListAdapter = new ShowsListAdapter(getActivity(), offersSearchList);

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
                //If checking connection is successful - > lets search
                //Searching offers
                String searchText = (s.toString()).toLowerCase();
                List<Offer> searchOfferList =  offerSearchUtils.findSearchingItemByNonFullName(offersMainList , searchText);


                if (searchOfferList.size() == 0 && s.length() != 0) {
                    recyclerView.setVisibility(View.GONE);
                    noDataResults.setVisibility(View.VISIBLE);
                } else {
                    noDataResults.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

                offersSearchList = searchOfferList;
                if (s.length() == 0) {
                    getAllFavorites();
                    offersSearchList = offersFavoriteList;
                }

                getOfferSearchListWithFavoritesValidation();
                showListAdapter.setFilter(offersSearchList);

            }
        });
//If press ENTER -> hide keyboard
        search.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER:
                            KeyboardMain.hide(search);
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
    public static class KeyboardMain {
        public static void hide(View view) {
            Context context = view.getContext();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

//If Show doesn't exist put Show into database
    private void addFavoriteShow(int position) {
        dbAdapter.open();
        Offer addToFavoritesOffer = offersSearchList.get(position);
        if (!dbAdapter.offerAlreadyExists(addToFavoritesOffer)) {
            addToFavoritesOffer.setFav(1);
            dbAdapter.insert(addToFavoritesOffer);
            addToFavoritesOffer.setId(dbAdapter.getOfferByName(addToFavoritesOffer.getName()).getId());
        }
        dbAdapter.close();
        getAllFavorites();

    }

    //Deleting Show
    private void deleteFavoriteShow(Offer offer) {
        dbAdapter.open();
        dbAdapter.delete(offer.getId());
        dbAdapter.close();
        getAllFavorites();
    }

    public void getOfferListWithFavoritesValidation() {
        getAllFavorites();
        for (Offer favOffer : offersFavoriteList) {
            for (Offer nOffer : offersMainList) {
                if (nOffer.getUrl().equals(favOffer.getUrl())) {
                    nOffer.setFav(1);
                    nOffer.setId(favOffer.getId());
                    nOffer.setStock_quantity(favOffer.getStock_quantity());
                    nOffer.setPrice(favOffer.getPrice());
                    nOffer.setVendor(favOffer.getVendor());
                    nOffer.setName(favOffer.getName());
                    nOffer.setOffer_changed(favOffer.getOffer_changed());
                } else {
                    nOffer.setFav(0);
                    nOffer.setId(0);
                    nOffer.setOffer_changed(0);

                }
            }
            for (Offer nsOffer : offersSearchList) {
                if (nsOffer.getUrl().equals(favOffer.getUrl())) {
                    nsOffer.setFav(1);
                    nsOffer.setOffer_changed(favOffer.getOffer_changed());
                    nsOffer.setId(favOffer.getId());
                    nsOffer.setStock_quantity(favOffer.getStock_quantity());
                    nsOffer.setPrice(favOffer.getPrice());
                    nsOffer.setVendor(favOffer.getVendor());
                    nsOffer.setName(favOffer.getName());
                } else {
                    nsOffer.setFav(0);
                    nsOffer.setId(0);
                    nsOffer.setOffer_changed(0);
                }

            }

        }
    }

    public void getOfferSearchListWithFavoritesValidation() {
        getAllFavorites();
        for (Offer favOffer : offersFavoriteList)
            for (Offer nOffer : offersSearchList) {
                if (nOffer.getUrl().equals(favOffer.getUrl())) {
                    nOffer.setFav(1);
                    nOffer.setOffer_changed(favOffer.getOffer_changed());
                    nOffer.setId(favOffer.getId());
                    nOffer.setStock_quantity(favOffer.getStock_quantity());
                    nOffer.setPrice(favOffer.getPrice());
                    nOffer.setVendor(favOffer.getVendor());
                    nOffer.setName(favOffer.getName());
                }

            }

    }
    /*
*Implementing interface which is in the custom RecyclerView.Adapter   {@link ShowsListAdapter}
*/
    @Override
    public void showClicked(View view, int position) {
        switch (view.getId()) {
            case R.id.showItemLayout:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse((offersSearchList.get(position)).getUrl()));
                getActivity().startActivity(intent);
                break;
            case R.id.offerFavorite:
                if (offersSearchList.get(position).getFav() == 0) {
                    addFavoriteShow(position);
                    ((ImageButton) view).setImageResource(R.drawable.heart_like);
                    offersSearchList.get(position).setFav(1);
                } else {
                    deleteFavoriteShow(offersSearchList.get(position));
                    ((ImageButton) view).setImageResource(R.drawable.heart_unlike);
                    offersSearchList.get(position).setFav(0);
                }
                break;
            case R.id.offerImage:
                Intent imageIntent = new Intent(getActivity(), ImageActivity.class);
                imageIntent.putExtra("url", offersSearchList.get(position).getImage());
                getActivity().startActivity(imageIntent);
                break;
        }
    }


    //Get favorites list
    public void getAllFavorites() {
        dbAdapter.open();
        offersFavoriteList = dbAdapter.getOffers();
        dbAdapter.close();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
