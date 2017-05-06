package com.example.nickolas.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiMessage;

import org.json.JSONArray;
import org.json.JSONException;

public class Messages extends ActionBarActivity implements View.OnClickListener {

    public static ListView dialogListView;
    public ListView messageListView;
    Button sendButton;
    EditText etSendingMessage;
    VKApiMessage[] msg;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        messageListView = (ListView) findViewById(R.id.messageListView);
        sendButton = (Button) findViewById(R.id.sendMessage);
        etSendingMessage = (EditText) findViewById(R.id.editTextMessage);

        sendButton.setOnClickListener(this);

        Intent intent = getIntent();

        id = intent.getIntExtra("user_id", 1);
        getSupportActionBar().setTitle(intent.getStringExtra("user_name"));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        refreshMessages();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.messages_menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actionRefreshMessages){
            refreshMessages();
        } else if (item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }


    public void refreshMessages() {
        VKRequest vkRequest = new VKRequest("messages.getHistory", VKParameters.from(VKApiConst.USER_ID, id));
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
                CustomMessageAdapter customMessageAdapter = new CustomMessageAdapter(Messages.this, msg);
                messageListView.setAdapter(new CustomMessageAdapter(Messages.this, msg));
            messageListView.setSelection(customMessageAdapter.getCount() - 1);
            }

        });
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sendMessage) {
            VKRequest vkRequest = new VKRequest("messages.send", VKParameters.from(VKApiConst.USER_ID, id, VKApiConst.MESSAGE, etSendingMessage.getText().toString()));
            etSendingMessage.setText("");
            vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response) {
                    super.onComplete(response);
                    refreshMessages();
                }
            });
        }
    }
}

