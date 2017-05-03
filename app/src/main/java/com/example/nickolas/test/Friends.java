package com.example.nickolas.test;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;

/**
 * Created by Nickolas on 02.05.2017.
 */

public final class Friends {

    public static VKList<VKApiUser> list;

    public static void setList() {
        VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "photo_50", "order", "hints")); //Error please check;
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                list = (VKList) response.parsedModel;
            }
        });
    }

}
