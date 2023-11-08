package com.example.dapm_food.SendNotification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAiaZHqcA:APA91bE4WLC3DDNvR--1uGLlri6QrN_0nUacqL6WEgjEk7eDiV5cjfbm58CJuGkoKGiryr140AWobNL4uq5l1DNGvWkzHWMXDEgcL7kU34TwVkoKv3Gt0jNSYx3WuekwyVrJv2c_T6kO"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body);
}
