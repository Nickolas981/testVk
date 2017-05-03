package com.example.nickolas.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nickolas on 01.05.2017.
 */

public class CustomFriendAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> name, photo;

    @Override
    public int getCount() {
        return name.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public CustomFriendAdapter(Context context, ArrayList<String> name, ArrayList<String> photo) {
        this.context = context;
        this.name = name;
        this.photo = photo;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Bitmap[] photo_50 = new Bitmap[1];
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_friends_view, null);
        SetData setData = new SetData();

        setData.imageView = (ImageView) view.findViewById(R.id.friendAvatarImage);
        setData.textView = (TextView) view.findViewById(R.id.friendNameTextView);

        new DownloadImageTask(setData.imageView).execute(photo.get(position));
        setData.textView.setText(name.get(position));

        return view;
    }

     private class SetData {
        ImageView imageView;
        TextView textView;
    }
}

