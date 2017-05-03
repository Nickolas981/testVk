package com.example.nickolas.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKList;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener {

    private String[] scope = new String[]{VKScope.MESSAGES, VKScope.FRIENDS, VKScope.WALL};
    private ListView listView;
    private Button showMessage;
//    private VKList list;
    private EditText etMessage;
    private String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Messages.dialogListView = (ListView) findViewById(R.id.dialogListView);
        etMessage = (EditText) findViewById(R.id.editText);
        VKSdk.login(this, scope);
        msg = etMessage.getText().toString();
        showMessage = (Button) findViewById(R.id.sendMessage);
        showMessage.setOnClickListener(this);
        VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "photo_50", "order", "hints"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Friends.list = (VKList) response.parsedModel;
            }
        });
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        refreshMessages(MainActivity.this, etMessage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Friends.setList();

        Intent intent = new Intent(MainActivity.this, FriendList.class);
        intent.putExtra("requestCode", requestCode);
        intent.putExtra("resultCode", resultCode);
        intent.putExtra("data", data);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.sendMessage) {
            refreshMessages(MainActivity.this, etMessage);
        }
    }

    public static void refreshMessages(final Context context, final EditText editText){
        final VKRequest request = VKApi.messages().getDialogs(VKParameters.from(VKApiConst.COUNT, 10));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);

                VKApiGetDialogResponse getDialogResponse = (VKApiGetDialogResponse) response.parsedModel;

                final VKList<VKApiDialog> list = getDialogResponse.items;

                ArrayList<String> messages = new ArrayList<>();
                final ArrayList<String> users = new ArrayList<>();


                for (final VKApiDialog msg : list) {
                    users.add(String.valueOf(Friends.list.getById(msg.message.user_id)));
                    messages.add(msg.message.body);
                }

                Messages.dialogListView.setAdapter(new CustomDialogAdapter(context, users, messages, list, editText));
            }
        });
    }
}

