package com.garret.chimera;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.garret.chimera.DataObjects.ScreenDataObject;
import com.garret.chimera.Database.ChimeraDatabase;
import com.garret.chimera.ViewObjects.ScreenFragment;

import java.util.ArrayList;

public class BottomNavigationActivity extends AppCompatActivity {

    ChimeraDatabase db;
    Context context;
    ArrayList<ScreenDataObject> allScreens;
    private static final int MENU_ID = Menu.FIRST;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

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
                    screenFragment.setArguments(bundle);
                    Log.d("Navbar frag arg UUID: ", uuid);

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

            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        db = new ChimeraDatabase(this);
        context = getApplicationContext();
        allScreens = db.Get_All_Screens_Metadata();

        String startUUID = null;

        final Menu menu = navigation.getMenu();
        menu.clear();
        for (int i = 0; i < allScreens.size(); i++) {
            // todo: set icon:
            // menu.add(Menu.NONE, (MENU_ID + i), Menu.NONE, allScreens.get(i).getName()).setIcon(R.drawable.icon);
            if (startUUID == null) {
                startUUID = allScreens.get(i).getUuid();
            }

            menu.add(Menu.NONE, (MENU_ID + i), Menu.NONE, allScreens.get(i).getName());
        }
        if (allScreens.size() > 3) {
            BottomNavigationViewHelper.disableShiftMode(navigation);
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

}
