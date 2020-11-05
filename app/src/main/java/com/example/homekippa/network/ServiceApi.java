package com.example.homekippa.network;

import com.example.homekippa.data.CreateGroupData;
import com.example.homekippa.data.CreateGroupResponse;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.SignUpData;
import com.example.homekippa.data.SignUpResponse;
import com.example.homekippa.data.UidData;
import com.example.homekippa.data.UidRespense;
import com.example.homekippa.data.UserData;
import com.example.homekippa.ui.group.SingleItemPet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServiceApi {
//    @POST("/user/login")
//    Call<LoginResponse> userLogin(@Body LoginData data);

    @POST("/user/add")
    Call<SignUpResponse> userSignUp(@Body SignUpData data);

    @POST("/group/add")
    Call<CreateGroupResponse> groupCreate(@Body CreateGroupData data);

    @GET("/user")
    Call<UserData> getUserData(@Query("userId") String userId);

    @GET("/group")
    Call<GroupData> getGroupData(@Query("groupId") int groupId);

    @GET("/pets")
    Call<List<SingleItemPet>> getPetsData(@Query("groupId") int groupId);

//    @POST("/pet/reports/add")
//    Call<CreateGroupResponse> createDailyWork(@Body CreateGroupData data);
}
