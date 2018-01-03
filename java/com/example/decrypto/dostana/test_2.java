package com.example.decrypto.dostana;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class test_2 extends AppCompatActivity {

    public static String profile_destination,new_profile_destination;
    public static String userdata,passdata;
    private boolean zoomOut =  false;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Button btnSelect;
    private ImageView ivImage;
    private String userChoosenTask;
    DBhandler mydatabase;
   // FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_2);
        // Obtain the FirebaseAnalytics instance.
      //  mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mydatabase = new DBhandler(this,null,null,1);
        //Image Selection Button
        ivImage = (ImageView) findViewById(R.id.chat_img);

        //Receiving the data from Intent
         userdata =  test_2.this.getIntent().getStringExtra("user");
         passdata =  test_2.this.getIntent().getStringExtra("pass");
        if(userdata==null || passdata==null)
        {
            userdata = "Did not receive any data from intent";
        }
        final TextView text = (TextView) findViewById(R.id.word1);
        //String number = mydatabase.get_phonenum(userdata);
        text.setText(" Welcome ! " + userdata);
        profile_destination = mydatabase.check_image(userdata);

        /* Firebase analytics */
       /* Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, userdata);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, passdata);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);*/

        if(profile_destination==null)
        {
            //Do Nothing ;)
        }
        else
        {
            File imgFile = new File(profile_destination);
            if(imgFile.exists()){
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inScaled = false;
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),options);
                ivImage.setImageBitmap(myBitmap);

            }
        }
    }

    public void click_profile(View view){
        new_profile_destination = mydatabase.check_image(userdata);
        Intent intent = new Intent(this,Zoom_Profile.class);
        intent.putExtra("Bitmap",new_profile_destination);
        intent.putExtra("user",userdata);
        intent.putExtra("pass",passdata);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    //Open Edit options
    public void show_edit_options(View view){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setTitle("Choose Profile photo");

        // set the custom dialog components - text, image and button
        Button dialogButton1 = (Button) dialog.findViewById(R.id.dbtn1);
        Button dialogButton2 = (Button) dialog.findViewById(R.id.dbtn2);
        // if button is clicked, close the custom dialog
        dialogButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gallery_option();
                dialog.dismiss();
            }
        });
        dialogButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera_option();
                dialog.dismiss();
            }
        });
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM | Gravity.CENTER;
        wmlp.width=900;
        wmlp.height=800;
        wmlp.y = -400;   //y position
        dialog.show();
    }

    //Log out from this activity
    private void logout() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(test_2.this,R.style.DialogStyle);
        builder1.setMessage("Are you sure you want to Log out?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent j = new Intent(test_2.this, Test_1.class);
                        startActivity(j);
                        test_2.this.finish();
                        overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
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
    }

    //handling back key button
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            return false;
        }
        return false;
    }

    public void camera_option(){
        boolean result= Utility.checkPermission(test_2.this);
        if(result) {
            cameraIntent();
        } else {return ;}
    }

    //use gallery
    public void gallery_option(){
        boolean result=Utility.checkPermission(test_2.this);
        if(result) {
            galleryIntent();
        } else {return ;}
    }
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            }
            else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        //add image path in the user's database
        String path = destination.toString();
        mydatabase.addimage(userdata,path);

        //setting image
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ivImage.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Uri selectedImageURI = data.getData();
        File imageFile = new File(getRealPathFromURI(selectedImageURI));
        //adding image path from gallery intent in the database
        String path = imageFile.toString();
        mydatabase.addimage(userdata,path);
        ivImage.setImageBitmap(bm);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuuser,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)  {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.aboutus:
                return true;
            case R.id.setting:
                return true;
            case R.id.chat:
                Intent i = new Intent(this,Chat_activity.class);
                i.putExtra("user",userdata);
                startActivity(i);
                return true;
            case R.id.logout:
                logout();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
