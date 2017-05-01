package com.example.nickolas.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKApiGetDialogResponse;
import com.vk.sdk.api.model.VKList;

import java.util.ArrayList;

public class FriendList extends AppCompatActivity {

    private ListView listView;
    private Button showMessage;
    private VKList list;
    private EditText etMessage;
    private String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
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
                            users.add(String.valueOf(FriendList.this.list.getById(msg.message.user_id)));
                            messages.add(msg.message.body);
                        }

                        listView.setAdapter(new CustomMessageAdapter(FriendList.this, users, messages, list, (EditText) findViewById(R.id.editText)));
                    }
                });
            }
        });
    }
}
