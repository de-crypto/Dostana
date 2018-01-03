package com.example.decrypto.dostana;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Zoom_Profile extends AppCompatActivity {

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    DBhandler mydatabase;
    ImageView imageView ;
    private boolean zoomOut =  false;
    public static String userdata, passdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom__profile);

        mydatabase = new DBhandler(this,null,null,1);
          imageView = (ImageView)findViewById(R.id.imageView1);

        //Setting up my toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setDisplayHomeAsUpEnabled(true);


        String bmp = Zoom_Profile.this.getIntent().getStringExtra("Bitmap");
        userdata =  Zoom_Profile.this.getIntent().getStringExtra("user");
        passdata =  Zoom_Profile.this.getIntent().getStringExtra("pass");
        if(bmp==null)
        {
            //Do Nothing ;)
        }
        else
        {
            File imgFile = new File(bmp);
            if(imgFile.exists()){
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inScaled = false;
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),options);
              //  Bitmap newbmp = BITMAP_RESIZER(myBitmap,100,100);
                imageView.setImageBitmap(myBitmap);

            }
        }
    }
    public Bitmap BITMAP_RESIZER(Bitmap bitmap,int newWidth,int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float ratioX = newWidth / (float) bitmap.getWidth();
        float ratioY = newHeight / (float) bitmap.getHeight();
        float middleX = newWidth / 2.0f;
        float middleY = newHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;

    }
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent i = new Intent(this,test_2.class);
            i.putExtra("user",userdata);
            i.putExtra("pass",passdata);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            this.finish();
            return true;
        }
        return false;
    }

    public void camera_option(){
        boolean result= Utility.checkPermission(Zoom_Profile.this);
        if(result) {
            cameraIntent();
        } else {return ;}
    }

    //use gallery
    public void gallery_option(){
        boolean result=Utility.checkPermission(Zoom_Profile.this);
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

        imageView.setImageBitmap(thumbnail);
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
        imageView.setImageBitmap(bm);
    }

    private void call_dialogbox() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setTitle("Title...");

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.zoom_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)  {
        switch (item.getItemId()) {
            case R.id.edit_action:
                call_dialogbox();
                return true;
            case android.R.id.home:
                Intent i = new Intent(this,test_2.class);
                i.putExtra("user",userdata);
                i.putExtra("pass",passdata);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                this.finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

    }
}
