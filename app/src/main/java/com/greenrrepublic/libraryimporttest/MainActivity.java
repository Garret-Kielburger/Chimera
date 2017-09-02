package com.greenrrepublic.libraryimporttest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.garret.chimera.ChimeraMainActivity;
import com.garret.chimera.Constants;
import com.garret.chimera.SplashActivity;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    String website;
    Button button;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        website = Constants.API_SERVER_URL;

        button = (Button) findViewById(R.id.button);
        button.setText(website);

        //todo: Check for Google Play services / level
        // see https://firebase.google.com/docs/cloud-messaging/android/client#sample-play

    }

    public void RunLibrary(View v){
        Intent i = new Intent(this, ChimeraMainActivity.class);
        startActivity(i);

        finish();

    }

}
