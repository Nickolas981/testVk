package com.example.nickolas.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;

import java.util.ArrayList;

public class FriendList extends ActionBarActivity {

    private VKList list;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        Intent intent = getIntent();
        listView = (ListView) findViewById(R.id.friends_list);
        int requestCode = intent.getIntExtra("requestCode", 0);
        int resultCode = intent.getIntExtra("resultCode", 0);
        Intent data = (Intent) intent.getParcelableExtra("data");

        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "photo_50", "order", "hints"));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        list = (VKList) response.parsedModel;
                        ArrayList<String> names = new ArrayList<String>();
                        ArrayList<String> avatars = new ArrayList<String>();
                        ArrayList<Integer> ids = new ArrayList<Integer>();
                        VKList<VKApiUser> userVKList = (VKList<VKApiUser>) response.parsedModel;
                        for (VKApiUser user : userVKList) {
                            names.add(user.first_name + " " + user.last_name);
                            avatars.add(user.photo_50);
                            ids.add(user.id);
                        }
                        listView.setAdapter(new CustomFriendAdapter(FriendList.this, names, avatars, ids));
                    }
                });
            }
            @Override
            public void onError(VKError error) {
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
