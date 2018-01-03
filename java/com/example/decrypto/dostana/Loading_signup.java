package com.example.decrypto.dostana;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class Loading_signup extends AppCompatActivity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 800;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_loading_signup);
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        // create a ProgressDialog instance, with a specified theme:
        final ProgressDialog dialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_DARK);
// set indeterminate style
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
// set title and message
        dialog.setTitle("Please wait");
        dialog.setMessage("Loading Signup page...");
// and show it
        dialog.show();
        dialog.setCancelable(false);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(Loading_signup.this,Signup.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(mainIntent);
                //startActivity(new Intent(MainActivity.this, Test_1.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                Loading_signup.this.finish();
                dialog.dismiss();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
