package com.example.my_last;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IPredictAPI {
    @POST("/ai_api/detect/")
    Call<JsonObject> predict_eyes(@Body String res);
}
