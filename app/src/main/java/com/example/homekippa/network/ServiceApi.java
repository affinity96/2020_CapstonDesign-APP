package com.example.homekippa.network;

import com.example.homekippa.data.AddPetData;
import com.example.homekippa.data.AddPetDesData;
import com.example.homekippa.data.AddPetResponse;
import com.example.homekippa.data.AddPostData;
import com.example.homekippa.data.AddPostResponse;
import com.example.homekippa.data.AddpetDesResponse;
import com.example.homekippa.data.CommentData;
import com.example.homekippa.data.CommentGetResponse;
import com.example.homekippa.data.CommentResponse;
import com.example.homekippa.data.CreateDailyWorkData;
import com.example.homekippa.data.CreateDailyWorkResponse;
import com.example.homekippa.data.CreateGroupData;
import com.example.homekippa.data.CreateGroupResponse;
import com.example.homekippa.data.CreateGroupUploadResponse;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.GroupInviteData;
import com.example.homekippa.data.LikeData;
import com.example.homekippa.data.LikeResponse;
import com.example.homekippa.data.NotiData;
import com.example.homekippa.data.PostResponse;
import com.example.homekippa.data.SignUpData;
import com.example.homekippa.data.SignUpResponse;
import com.example.homekippa.data.UidData;
import com.example.homekippa.data.UidRespense;
import com.example.homekippa.data.UserData;
import com.example.homekippa.ui.group.SingleItemComment;
import com.example.homekippa.ui.group.SingleItemPet;
import com.example.homekippa.ui.group.SingleItemPost;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ServiceApi {
//    @POST("/user/login")
//    Call<LoginResponse> userLogin(@Body LoginData data);

    @POST("/user/add")
    Call<SignUpResponse> userSignUp(@Body SignUpData data);

    @Multipart
    @POST("/group/add")
    Call<CreateGroupResponse> groupCreate(@PartMap HashMap<String, RequestBody> data, @Part MultipartBody.Part image);

    @POST("/pet/reports/add")
    Call<CreateDailyWorkResponse> createDailyWork(@Body CreateDailyWorkData data);

    @POST("/post/add")
    Call<AddPostResponse> addPost(@Body AddPostData data);

    @POST("/pet/add")
    Call<AddPetResponse> addPetReg(@Body AddPetData data);

    @GET("/user")
    Call<UserData> getUserData(@Query("userId") String userId, @Query("token") String token);

    @GET("/group")
    Call<GroupData> getGroupData(@Query("groupId") int groupId);

    @GET("/pet")
    Call<List<SingleItemPet>> getPetsData(@Query("groupId") int groupId);

//    @POST("/pet/add")
//    Call<AddPetResponse> addPetReg(@Body AddPetData data);

    @POST("/pet/add/des")
    Call<AddpetDesResponse> addPetDes(@Body AddPetDesData data);

//    @POST("/pet/reports/add")
//    Call<CreateDailyWorkResponse> createDailyWork(@Body CreateDailyWorkData data);


    @GET("/post/group")
    Call<List<SingleItemPost>> getGroupPost(@Query("groupId") int groupId);

    @GET("/post/location")
    Call<PostResponse> getLocationPost();

    @POST("/post/setlike")
    Call<LikeResponse> setLike(@Body LikeData data);

    @POST("/post/setComment")
    Call<CommentResponse> setComment(@Body CommentData data);

    @GET("/post/getComment")
    Call<CommentGetResponse> getComment(@Query("postId") int postId);

//    @POST("/pet/reports/add")
//    Call<CreateGroupResponse> createDailyWork(@Body CreateGroupData data);

    @GET("/user/group")
    Call<List<UserData>> getUsersInGroup(@Query("groupId") int groupId);

    @GET("/user/list/filter")
    Call<List<UserData>> getUserSearchResult(@Query("searchFilter") String filter);

    @POST("/group/invite")
    Call<UidRespense> sendGroupInvite(@Body GroupInviteData data);

    @GET("/user/getNoti")
    Call<List<NotiData>> getNotiData(@Query("userId") String userId);
}
