package com.example.nickolas.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKList;

import java.util.ArrayList;

/**
 * Created by Nickolas on 30.04.2017.
 */

class CustomMessageAdapter extends BaseAdapter {


    private ArrayList<String> users, messages;
    private Context context;
    private VKList<VKApiDialog> list;
    private EditText etMsg;

    public CustomMessageAdapter(Context context, ArrayList<String> users, ArrayList<String> messages, VKList<VKApiDialog> list, EditText etMsg) {
        this.users = users;
        this.messages = messages;
        this.context = context;
        this.list = list;
        this.etMsg = etMsg;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        SetData setData = new SetData();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.customlistview, null);
        setData.userName = (TextView) view.findViewById(R.id.user_name);
        setData.msg = (TextView) view.findViewById(R.id.msg);
        setData.imageView = (ImageView) view.findViewById(R.id.imageView2);
//        VKParameters parameters = new VKParameters();
//        parameters.put("users_ids", list.get(position).message.user_id);
//        VKRequest request = new VKRequest("users.get", parameters);
//        request.executeWithListener(new VKRequest.VKRequestListener() {
//            @Override
//            public void onComplete(VKResponse response) {
//                System.out.println(list.get(position).message.user_id);
//                System.out.println(response);
//
//                super.onComplete(response);
//            }
//        });
        setData.userName.setText(users.get(position));
        setData.msg.setText(messages.get(position));
        int id = view.getResources().getIdentifier("com.example.nickolas.test:drawable/vk_icon", null, null);
        setData.imageView.setImageResource(id);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKRequest vkRequest = new VKRequest("messages.send", VKParameters.from(VKApiConst.USER_ID, list.get(position).message.user_id, VKApiConst.MESSAGE, etMsg.getText().toString()));
                etMsg.setText("");
                vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        System.out.println("Отправлено");
                    }
                });
            }
        });

        return view;
    }

    public class SetData {
        TextView userName, msg;
        ImageView imageView;
    }
}
