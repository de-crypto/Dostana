package com.example.decrypto.dostana;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class Signup extends AppCompatActivity {

    private static final long SPLASH_DISPLAY_TIME = 100;
    DBhandler mydatabase ;
    public static String newuser,newpass,repass,number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mydatabase = new DBhandler(this,null,null,1);
    }

    //on click of the button
    public void create_account(View view){
        //fetching the data from signup activity
        EditText user = (EditText) findViewById(R.id.new_username);
        EditText pass = (EditText) findViewById(R.id.new_password);
        EditText re = (EditText) findViewById(R.id.re_password);
        EditText phone = (EditText) findViewById(R.id.edittext_phone);

        //converting the data to string
        newuser = user.getText().toString();
        newpass = pass.getText().toString();
        repass = re.getText().toString();
        number = phone.getText().toString();
        checkpassword(newuser,newpass,repass);
    }

    public void checkpassword(String newuser, String newpass, String repass){
           if(newuser.matches("")){
               Toast.makeText(this,"You did not enter the Username !",Toast.LENGTH_LONG).show();
               return ;
           }
           else {
               if ((newpass.compareTo(repass) != 0)) {
                   AlertDialog.Builder builder1 = new AlertDialog.Builder(this, R.style.DialogStyle);
                   builder1.setMessage("Password does not match !");
                   builder1.setCancelable(true);

                   builder1.setPositiveButton(
                           "Okay",
                           new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int id) {
                                   dialog.cancel();
                               }
                           });
                   AlertDialog alert11 = builder1.create();
                   alert11.show();
               } else {
                   Boolean check = mydatabase.CheckIsDataAlreadyInDBorNot(newuser);
                   if (check) {
                       AlertDialog.Builder builder1 = new AlertDialog.Builder(this, R.style.DialogStyle);
                       builder1.setMessage(" Username Already Exists !");
                       builder1.setCancelable(true);

                       builder1.setPositiveButton(
                               "Okay",
                               new DialogInterface.OnClickListener() {
                                   public void onClick(DialogInterface dialog, int id) {
                                       dialog.cancel();
                                   }
                               });
                       AlertDialog alert11 = builder1.create();
                       alert11.show();

                   } else {
                       mydatabase.addNewuser(newuser, newpass, number);
                       Toast.makeText(Signup.this, "User created Successfully ", Toast.LENGTH_LONG).show();
                       Intent i = new Intent(this, Test_1.class);
                       startActivity(i);
                       Signup.this.finish();
                   }
               }
           }
    }
    //check credential ends here

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(Signup.this, Test_1.class);
            startActivity(i);
            Signup.this.finish();
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        return true;
        }
        return false;
    }
}
