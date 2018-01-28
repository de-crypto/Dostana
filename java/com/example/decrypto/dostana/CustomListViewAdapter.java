package com.example.decrypto.dostana;

/**
 * Created by decrypto on 27/3/17.
 */

import java.io.File;
import java.util.List;

import com.bumptech.glide.Glide;
import com.example.decrypto.dostana.RowItem;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListViewAdapter extends ArrayAdapter<RowItem> {

    Context context;

    public CustomListViewAdapter(Context context, int resourceId,
                                 List<RowItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView txtDesc;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_list, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.chat_name);
            holder.imageView = (ImageView) convertView.findViewById(R.id.chat_img);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

       // holder.txtDesc.setText(rowItem.getDesc());
        String profile_destination = rowItem.getImageId();
        holder.txtTitle.setText(rowItem.getTitle());
        if(profile_destination==null)
        {
            //Do Nothing ;)
        }
        else
        {
            File imgFile = new File(profile_destination);
            if(imgFile.exists()){
               /* BitmapFactory.Options options = new BitmapFactory.Options();
                options.inScaled = false;
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),options);*/
               // holder.imageView.setImageBitmap(myBitmap);
                Glide.with(convertView).load(imgFile).into(holder.imageView);
            }
        }
        return convertView;
    }
}