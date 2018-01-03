package com.example.decrypto.dostana;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

public class Test_1 extends AppCompatActivity {

    DBhandler mydbhandler;
    Button mybutton,signup;
    // FirebaseAnalytics mFirebaseAnalytics;
   // private final String pass_ans="1234", name_ans="athar";
    private static String name_inp, pass_inp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtain the FirebaseAnalytics instance.
      //  mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        setContentView(R.layout.activity_test_1);
        mybutton = (Button) findViewById(R.id.mybutton);
        signup = (Button) findViewById(R.id.signup);
        mydbhandler =  new DBhandler(this,null,null,1);
        TextView wrong  = (TextView) findViewById(R.id.wrong);
        wrong.setVisibility(View.GONE);
        //On click event
        mybutton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText name= (EditText) findViewById(R.id.name);
                        EditText pass= (EditText) findViewById(R.id.password);
                        //convert the text to string in order to verify the user
                        name_inp= name.getText().toString();
                        pass_inp=pass.getText().toString();
                        check(name_inp,pass_inp);
                    }
                }
        );
    }


    //This function checks the login credentials
    protected void check(String name, String pass){

        boolean flag= mydbhandler.check_database(name,pass);
        if(flag){
          /* Firebase analytics */
           /* Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, name);
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, pass);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);*/
            Intent i = new Intent(Test_1.this, test_2.class);
            i.putExtra("user",name);
            i.putExtra("pass",pass);
            startActivity(i);
        }
        else {
            /*AlertDialog.Builder builder1 = new AlertDialog.Builder(this,R.style.DialogStyle);
            builder1.setMessage("Invalid Username or Password");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Okay",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();*/
            TextView wrong  = (TextView) findViewById(R.id.wrong);
            wrong.setVisibility(View.VISIBLE);
        }
        return ;
    }

    // New user tried to login
    // Start Signup Activity
    public void sign_up(View view) {
        Intent intent = new Intent(Test_1.this, Loading_signup.class);
        startActivity(intent);
        Test_1.this.finish();
    }
    int check =0;
    //See Password
    public void pass_visible(View view){

        EditText password = (EditText) findViewById(R.id.password);
        if(check == 0) {
            password.setTransformationMethod(null);
            check=1;
        }
        else {
            password.setTransformationMethod(new PasswordTransformationMethod());
            check=0;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(Test_1.this,R.style.DialogStyle);
            builder1.setMessage("Are you sure you want to Exit?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            //For calling Home screen
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                    });
            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }

                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
}
