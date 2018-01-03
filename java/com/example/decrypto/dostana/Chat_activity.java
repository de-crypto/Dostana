package com.example.decrypto.dostana;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Chat_activity extends AppCompatActivity {

    DBhandler mydatabase;
    List<RowItem> rowItems;
    String userdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_activity);

        //Getting the Logged in User's name
        userdata =  Chat_activity.this.getIntent().getStringExtra("user");

        //Using the Database
        mydatabase = new DBhandler(this,null,null,1);

        //Setting up Custom Action Bar
        setTitle("Select Contact");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         String[] uname = mydatabase.get_username();
        String[] image_arr = mydatabase.get_imagepath(uname);

        rowItems = new ArrayList<RowItem>();
        for (int i = 0; i < uname.length; i++) {
            if(uname[i].compareTo(userdata)!=0){
            RowItem item = new RowItem(image_arr[i], uname[i]);
            rowItems.add(item);
            }
        }

        ListView mylist = (ListView) findViewById(R.id.mylist);
        CustomListViewAdapter adapter = new CustomListViewAdapter(this,
                R.layout.custom_list, rowItems);
        mylist.setAdapter(adapter);

        mylist.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        //Start ChatView
                        String user = String.valueOf(adapterView.getItemAtPosition(i));
                        Intent intent = new Intent(Chat_activity.this,Messaging.class);
                        intent.putExtra("user",user);
                        startActivity(intent);
                        //Toast.makeText(Chat_activity.this,food,Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
