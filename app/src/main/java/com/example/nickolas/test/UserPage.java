package com.example.nickolas.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;

public class UserPage extends AppCompatActivity {

    int id;
    String userName, avatarURL;
    ImageView avatar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_user_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        avatar = (ImageView) findViewById(R.id.header_logo);
//        avatar.setImageResource(R.drawable.ic_ab_app);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        Intent in = getIntent();

        userName = in.getStringExtra("user_name");
        collapsingToolbarLayout.setTitle(userName);

        id = in.getIntExtra("user_id", 1);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(UserPage.this, Messages.class);
                intent.putExtra("user_id", id);
                intent.putExtra("user_name", userName);
                startActivity(intent);

            }
        });
        gettingResponse();
        collapsingToolbarLayout.setTitle(userName);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }


    private void gettingResponse(){
        VKRequest vkRequest = VKApi.users().get(VKParameters.from(VKApiConst.USER_ID, id, VKApiConst.FIELDS, "photo_400_orig"));
        vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKApiUser user = ((VKList<VKApiUser>) response.parsedModel).get(0);
                avatarURL = user.photo_400_orig;
                new DownloadImageTask(avatar).execute(avatarURL);
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            }
        });
    }


}
