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
import com.example.homekippa.data.GetGroupImageResponse;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.GroupInviteData;
import com.example.homekippa.data.LikeData;
import com.example.homekippa.data.LikeResponse;
import com.example.homekippa.data.PostResponse;
import com.example.homekippa.data.SignUpData;
import com.example.homekippa.data.SignUpResponse;
import com.example.homekippa.data.UidRespense;
import com.example.homekippa.data.UserData;
import com.example.homekippa.ui.group.SingleItemPet;
import com.example.homekippa.ui.group.SingleItemPost;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceApi {
//    @POST("/user/login")
//    Call<LoginResponse> userLogin(@Body LoginData data);

    @POST("/user/add")
    Call<SignUpResponse> userSignUp(@Body SignUpData data);

    @POST("/group/add")
    Call<CreateGroupResponse> groupCreate(@Body CreateGroupData data);

    @GET("images/{apiName}")
    Call<ResponseBody> getProfileImage(@Path("apiName") String apiName);

    @Multipart
    @POST("/group/add/photo")
    Call<CreateGroupResponse> groupCreateWithPhoto(@PartMap HashMap<String, RequestBody> data, @Part MultipartBody.Part image);

    @POST("/pet/reports/add")
    Call<CreateDailyWorkResponse> createDailyWork(@Body CreateDailyWorkData data);

    @Multipart
    @POST("/post/add/photo")
    Call<AddPostResponse> addPostWithPhoto(@PartMap HashMap<String, RequestBody> data, @Part MultipartBody.Part image);

    @POST("/post/add")
    Call<AddPostResponse> addPost(@Body AddPostData data);

    @POST("/pet/add")
    Call<AddPetResponse> addPetReg(@Body AddPetData data);

    @GET("/user")
    Call<UserData> getUserData(@Query("userId") String userId);

    @GET("/group")
    Call<GroupData> getGroupData(@Query("groupId") int groupId);

    @GET("/pet")
    Call<List<SingleItemPet>> getPetsData(@Query("groupId") int groupId);

//    @POST("/pet/add")
//    Call<AddPetResponse> addPetReg(@Body AddPetData data);

    @Multipart
    @POST("/pet/add/des/photo")
    Call<AddpetDesResponse> addPetDesWithPhoto(@PartMap HashMap<String, RequestBody> data, @Part MultipartBody.Part image);

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
}
