package com.example.nickolas.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKApiGetDialogResponse;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private String[] scope = new String[]{VKScope.MESSAGES, VKScope.FRIENDS, VKScope.WALL};
    private ListView listView;
    private Button showMessage;
    private VKList list;
    private EditText etMessage;
    private String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        etMessage = (EditText) findViewById(R.id.editText) ;
        VKSdk.login(this, scope);
        msg = etMessage.getText().toString();
        showMessage = (Button) findViewById(R.id.showMassege);
        showMessage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final VKRequest request = VKApi.messages().getDialogs(VKParameters.from(VKApiConst.COUNT, 10));
                request.executeWithListener(new VKRequest.VKRequestListener(){
                    @Override
                    public void onComplete(VKResponse response){
                        super.onComplete(response);

                        VKApiGetDialogResponse getDialogResponse = (VKApiGetDialogResponse) response.parsedModel;

                        final VKList<VKApiDialog> list = getDialogResponse.items;

                        ArrayList<String> messages = new ArrayList<>();
                        ArrayList<String> users = new ArrayList<>();


                        for (VKApiDialog msg:list) {
                            users.add(String.valueOf(MainActivity.this.list.getById(msg.message.user_id)));
                            messages.add(msg.message.body);
                        }

                        listView.setAdapter(new CustomMessageAdapter(MainActivity.this, users, messages, list, (EditText) findViewById(R.id.editText)));
                    }
                });
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "photo_50","order", "hints"));
                request.executeWithListener(new VKRequest.VKRequestListener(){
                    @Override
                    public void onComplete(VKResponse response){
                        super.onComplete(response);
                        list = (VKList) response.parsedModel;
                        ArrayList<String> names = new ArrayList<String>();
                        ArrayList<String> avatars = new ArrayList<String>();


                        VKList<VKApiUser> userVKList = (VKList<VKApiUser>)response.parsedModel;


                        for (VKApiUser user:userVKList) {
                            names.add(user.first_name + " " + user.last_name);
                            avatars.add(user.photo_50);
                        }

                        listView.setAdapter(new CustomFriendAdapter(MainActivity.this, names, avatars));
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
