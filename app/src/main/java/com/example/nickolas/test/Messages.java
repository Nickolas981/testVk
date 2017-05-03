package com.example.nickolas.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiMessage;

import org.json.JSONArray;
import org.json.JSONException;

public class Messages extends Activity {

    public static ListView dialogListView;
    public ListView messageListView;
    VKApiMessage[] msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        messageListView = (ListView) findViewById(R.id.messageListView);

        Intent intent = getIntent();

        int id = intent.getIntExtra("user_id", 1);

        VKRequest vkRequest = new VKRequest("messages.getHistory", VKParameters.from(VKApiConst.USER_ID, id));
        System.out.println("tyt");
        vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);


                try {
                    JSONArray array = response.json.getJSONObject("response").getJSONArray("items");
                    msg = new VKApiMessage[array.length()];
                    for (int i = 0; i < array.length(); i++) {
                        VKApiMessage mes = new VKApiMessage(array.getJSONObject(i));
                        msg[array.length() - 1 - i] = mes;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                System.out.println("tam");
                messageListView.setAdapter(new CustomMessageAdapter(Messages.this, msg));
            }

        });

    }
}

