package com.example.nickolas.test;

import android.content.Context;
import android.content.Intent;
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
    private ArrayList<Integer> ids;

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

    public CustomFriendAdapter(Context context, ArrayList<String> name, ArrayList<String> photo, ArrayList<Integer> ids) {
        this.context = context;
        this.name = name;
        this.photo = photo;
        this.ids = ids;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        Bitmap[] photo_50 = new Bitmap[1];
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_friends_view, null);
        SetData setData = new SetData();

        setData.imageView = (ImageView) view.findViewById(R.id.friendAvatarImage);
        setData.textView = (TextView) view.findViewById(R.id.friendNameTextView);

        new DownloadImageTask(setData.imageView).execute(photo.get(position));
        setData.textView.setText(name.get(position));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Messages.class);
                intent.putExtra("user_id", ids.get(position));
                context.startActivity(intent);

            }
        });
        return view;
    }

     private class SetData {
        ImageView imageView;
        TextView textView;
    }
}

