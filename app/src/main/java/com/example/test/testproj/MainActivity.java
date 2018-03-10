package com.example.test.testproj;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.test.testproj.adapters.DBAdapter;
import com.example.test.testproj.helpers.ConnectivityHelper;
import com.example.test.testproj.helpers.DBHelper;
import com.example.test.testproj.helpers.ShowBuilder;

import com.example.test.testproj.models.Offer;
import com.example.test.testproj.models.OfferServerList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Main appActivity
 *
 * @author Ruslan Zosimov
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final static String TOOLBAR_TITLES = "TOOLBAR_TITLES";
    private final static String VISIBLE = "VISIBLE";
    //private boolean STACK_BOOL = true;
    private String titels;
    Toolbar toolbar;
    private MainFragment mainFragment;
    private FavoritesFragment favoritesFragment;
    private DBAdapter dbAdapter;
    public List<Offer> showList;
    private ConnectivityHelper connectivityHelper;
    private OfferServerList offerServerList;
    private List<Offer> offersList;
    private String offname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        offerServerList = OfferServerList.getInstance();
        connectivityHelper = new ConnectivityHelper(this);
        offersList = offerServerList.getOfferServerMainList();
        setSupportActionBar(toolbar);

        if (savedInstanceState != null) titels = savedInstanceState.getString(TOOLBAR_TITLES);
        else {
            replaceFragment(mainFragment = new MainFragment(), true);
            titels = "Main";
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        //Fragments backStackChangedListener
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                FragmentManager fragmentManager = getSupportFragmentManager();

                Fragment fragment = fragmentManager.findFragmentByTag(VISIBLE);

                if (fragment instanceof MainFragment) {
                    titels = "Main";
                    navigationView.setCheckedItem(R.id.nav_main);
                } else {
                    titels = "Favorites";
                    navigationView.setCheckedItem(R.id.nav_favorites);
                }
                toolbar.setTitle(titels);

            }

        });
        toolbar.setTitle(titels);

        if (!connectivityHelper.isConnected())
            Toast.makeText(this, "Waiting for internet connection...", Toast.LENGTH_SHORT).show();

    }

    //Replacing fragment func
    private void replaceFragment(Fragment fragment, boolean addToStack) {
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContentContainer, fragment, VISIBLE);
        if (addToStack) fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    // Clearing fragments "backStack"
    private void clearBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 1) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(1);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(VISIBLE);
        if (id == R.id.nav_main) {
            if (fragment instanceof MainFragment) drawer.closeDrawer(GravityCompat.START);
            else {
                if (mainFragment == null)
                    replaceFragment(mainFragment = new MainFragment(), true);
                else replaceFragment(mainFragment, true);
                titels = "Main";
                toolbar.setTitle(R.string.nav_mains);
                drawer.closeDrawer(GravityCompat.START);
                mainFragment.getOfferListWithFavoritesValidation();
                //If drawerLayout -> click "Main" -> in fragments backStack remains only MainFragment
                //This helps to avoid an infinite set of fragments
                clearBackStack();
            }
        } else if (id == R.id.nav_favorites) {
            if (fragment instanceof FavoritesFragment) drawer.closeDrawer(GravityCompat.START);

            else {
                if (favoritesFragment == null)
                    replaceFragment(favoritesFragment = new FavoritesFragment(), true);
                else replaceFragment(favoritesFragment, true);
                titels = "Favorites";
                toolbar.setTitle(R.string.nav_favoritess);
                drawer.closeDrawer(GravityCompat.START);
            }
        } else if (id == R.id.nav_createXml) {
            Intent intent = new Intent(this, CreateOffersXmlActivity.class);
            drawer.closeDrawer(GravityCompat.START);

            startActivity(intent);
        }


        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TOOLBAR_TITLES, titels);
    }


}
