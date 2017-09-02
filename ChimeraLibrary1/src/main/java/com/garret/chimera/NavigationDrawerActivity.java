package com.garret.chimera;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garret.chimera.DataObjects.IDataObject;
import com.garret.chimera.DataObjects.ScreenDataObject;
import com.garret.chimera.DataObjects.TextfieldDataObject;
import com.garret.chimera.Database.ChimeraDatabase;
import com.garret.chimera.ViewObjects.Screen;
import com.garret.chimera.ViewObjects.ScreenFragment;

import java.util.ArrayList;

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ChimeraDatabase db;
    Context context;
    private int number_of_screens;
    ScreenFragment screenFragment;
    private static final int MENU_ID = Menu.FIRST;
    ArrayList<ScreenDataObject> allScreens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new ChimeraDatabase(this);
        context = getApplicationContext();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        LinearLayout headerView = (LinearLayout) navigationView.getHeaderView(0);
        //todo: set up to dynamically change title in header - also the image too! And of the activity!
        TextView tv = (TextView) headerView.findViewById(R.id.textView1);
        TextView tv2 = (TextView) headerView.findViewById(R.id.textView);
        tv.setText("Chimera");
        tv2.setText("I'll adjust this stuff later");

        final Menu menu = navigationView.getMenu();
        menu.clear();
        //todo: use Get_Screens_Metadata() instead?

        allScreens = db.Get_All_Screens_Metadata();

        String startUUID = null;

        for (int i = 0; i < allScreens.size(); i++) {
            // todo: set icon:
            // menu.add(Menu.NONE, (MENU_ID + i), Menu.NONE, allScreens.get(i).getName()).setIcon(R.drawable.icon);
            if (startUUID == null) {
                startUUID = allScreens.get(i).getUuid();
            }

            menu.add(Menu.NONE, (MENU_ID + i), Menu.NONE, allScreens.get(i).getName());
        }

        //Initial Fragment
        ScreenFragment screenFragment = new ScreenFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("uuid", startUUID);
        Log.d("startUUID: ", startUUID);
        screenFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_container, screenFragment);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        for (int i = 0; i < allScreens.size(); i++) {
            if (id == (MENU_ID + i)) {
                Log.i("Nav Item Selected: ", "Menu ID: " + MENU_ID + " and i: " + i);
                /*ArrayList<IDataObject> interfaceDataObjectListFromDb;
                interfaceDataObjectListFromDb = db.Get_Screen_Content_By_Screen(i);
*/
                ScreenDataObject sdo = (ScreenDataObject) allScreens.get(i);
                String name = sdo.getName();
                Log.i("Menu Click Selection: ", name);

                ScreenFragment screenFragment = new ScreenFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                String uuid = sdo.getUuid();
                bundle.putString("uuid", uuid);
                //bundle.putString("screen_name", name);
                Log.d("Navbar frag arg UUID: ", uuid);

                screenFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container, screenFragment);
                fragmentTransaction.commit();

/*                try {
                    for (int j = 0; j < interfaceDataObjectListFromDb.size(); j++) {
                        if (interfaceDataObjectListFromDb.get(j).getClass() == ScreenDataObject.class) {
                            ScreenDataObject sdo = (ScreenDataObject) interfaceDataObjectListFromDb.get(j);
                            String name = sdo.getName();
                            Log.i("Menu Click Selection: ", name);
                        }
                    }

                } catch (Exception e) {
                    Log.e("menu click error: ", e.toString());
                }*/

            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void PopulateUI() {
        Log.i(Constants.TAG, "PopulateUI() Constructor");

        number_of_screens = db.Get_Number_Of_Screens();

        Log.d("number_of_screens = ", String.valueOf(number_of_screens));

        // index in db starts at 1, not 0, so use i
        for (int i = 0; i < number_of_screens + 1; i++) {
            Log.d("PopulateUI()", "number of i: " + i);
            if (i == 0){
                //No Screens to Add
            }
            if (i > 0){
                screenFragment = new ScreenFragment();
            }
        }
    }
}
