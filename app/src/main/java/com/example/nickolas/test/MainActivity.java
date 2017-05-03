package com.example.nickolas.test;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKApiGetDialogResponse;
import com.vk.sdk.api.model.VKList;

import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private String[] scope = new String[]{VKScope.MESSAGES, VKScope.FRIENDS, VKScope.WALL};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Messages.dialogListView = (ListView) findViewById(R.id.dialogListView);
        VKSdk.login(this, scope);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dialog_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actionRefreshDialogs){
            refreshDialogs(MainActivity.this);
        }
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refreshDialogs(MainActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent intent = new Intent(MainActivity.this, FriendList.class);
        intent.putExtra("requestCode", requestCode);
        intent.putExtra("resultCode", resultCode);
        intent.putExtra("data", data);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }

    public static void refreshDialogs(final Context context){
        final VKRequest request = VKApi.messages().getDialogs(VKParameters.from(VKApiConst.COUNT, 15));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);

                VKApiGetDialogResponse getDialogResponse = (VKApiGetDialogResponse) response.parsedModel;

                final VKList<VKApiDialog> list = getDialogResponse.items;

                ArrayList<String> messages = new ArrayList<>();
                final ArrayList<String> users = new ArrayList<>();
                ArrayList<String> photo = new ArrayList<String>();

                for (final VKApiDialog msg : list) {
                    users.add(String.valueOf(Friends.list.getById(msg.message.user_id)));
                    messages.add(msg.message.body);
                    try {
                        photo.add(Friends.list.getById(msg.message.user_id).photo_50);
                    } catch (Exception e) {
                        photo.add("1");
                    }
                }

                Messages.dialogListView.setAdapter(new CustomDialogAdapter(context, users, messages, list, photo));
            }
        });
    }
}

