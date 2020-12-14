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
import com.example.homekippa.data.DeletePostResponse;
import com.example.homekippa.data.DoneReportsResponse;
import com.example.homekippa.data.EditDailyWorkData;
import com.example.homekippa.data.EditDailyWorkResponse;
import com.example.homekippa.data.EditUserData;
import com.example.homekippa.data.EditUserResponse;
import com.example.homekippa.data.ExitResponse;
import com.example.homekippa.data.FollowData;
import com.example.homekippa.data.FollowResponse;
import com.example.homekippa.data.GetFollowData;
import com.example.homekippa.data.GroupData;
import com.example.homekippa.data.GroupInviteData;
import com.example.homekippa.data.GroupPostResponse;
import com.example.homekippa.data.GroupSelectResponse;
import com.example.homekippa.data.LikeData;
import com.example.homekippa.data.LikeResponse;
import com.example.homekippa.data.ModifyGroupResponse;
import com.example.homekippa.data.ModifyPetResponse;
import com.example.homekippa.data.NotiData;
import com.example.homekippa.data.PostResponse;
import com.example.homekippa.data.SignUpData;
import com.example.homekippa.data.SignUpResponse;
import com.example.homekippa.data.UidRespense;
import com.example.homekippa.data.UserData;

import com.example.homekippa.data.WeatherLocationData;
import com.example.homekippa.data.WeatheLocationResponse;
import com.example.homekippa.ui.group.SingleItemDailyWork;
import com.example.homekippa.ui.group.SingleItemPet;

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
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface ServiceApi {
//    @POST("/user/login")
//    Call<LoginResponse> userLogin(@Body LoginData data);

    @POST("/user/add")
    Call<SignUpResponse> userSignUp(@Body SignUpData data);

    @PUT("/user/delete")
    Call<ExitResponse> userExit(@Query("userId") String userId);

    @POST("/user/update")
    Call<EditUserResponse> userEdit(@Body EditUserData data);

    @POST("/group/add")
    Call<CreateGroupResponse> groupCreate(@Body CreateGroupData data);

    @GET("/group/image")
    Call<ResponseBody> getProfileImage(@Query("apiName") String apiName);

    @POST("/group/reset/photo")
    Call<ModifyGroupResponse> setGroupProfileImageDefault(@Query("groupId") int groupId);

    @POST("/group/reset/cover")
    Call<ModifyGroupResponse> setGroupCoverDefault(@Query("groupId") int groupId);

    @Multipart
    @POST("/group/modify/photo")
    Call<ModifyGroupResponse> modifyGroupProfileImage(@Part("groupId") RequestBody id, @Part MultipartBody.Part image);

    @POST("/group/modify/name")
    Call<ModifyGroupResponse> modifyGroupName(@Query("groupId") int groupId, @Query("Name") String data);

    @POST("/group/modify/intro")
    Call<ModifyGroupResponse> modifyGroupIntro(@Query("groupId") int groupId ,@Query("Intro") String data);

    @POST("/group/modify/Address")
    Call<ModifyGroupResponse> modifyGroupAddress(@Query("groupId") int groupId, @Query("Address") String address, @Query("Area") String area);

    @Multipart
    @POST("/group/add/cover")
    Call<ModifyGroupResponse> uploadGroupCover(@Part("groupId") RequestBody id, @Part MultipartBody.Part image);

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

    @PUT("/post/delete")
    Call<DeletePostResponse> deletePost(@Query("postId") int postId);


    @POST("/pet/add")
    Call<AddPetResponse> addPetReg(@Body AddPetData data);

    @GET("/user")
    Call<UserData> getUserData(@Query("userId") String userId, @Query("token") String token);

    @GET("/group")
    Call<GroupData> getGroupData(@Query("groupId") int groupId);

    @GET("/pet")
    Call<List<SingleItemPet>> getPetsData(@Query("groupId") int groupId);

    @Multipart
    @POST("/pet/modify/photo")
    Call<ModifyPetResponse> modifyPetImage(@Part("petId") RequestBody id, @Part MultipartBody.Part image);

    @POST("/pet/reset/photo")
    Call<ModifyPetResponse> resetPetImage(@Query("petId") int petId);

    @POST("/pet/modify/name")
    Call<ModifyPetResponse> modifyPetName(@Query("petId") int petId, @Query("name") String data);

    @POST("/pet/modify/species")
    Call<ModifyPetResponse> modifyPetSpecies(@Query("petId") int petId, @Query("species") String data);

    @POST("/pet/modify/gender")
    Call<ModifyPetResponse> modifyPetGender(@Query("petId") int petId, @Query("gender") String data);

    @POST("/pet/modify/neutering")
    Call<ModifyPetResponse> modifyPetNeutering(@Query("petId") int petId, @Query("neutering") String data);

    @POST("/pet/modify/birth")
    Call<ModifyPetResponse> modifyPetBirth(@Query("petId") int petId, @Query("birth") String data);

    @GET("/pet/reports")
    Call<List<SingleItemDailyWork>> getDailyWorkData(@Query("petId") int petId);

    @PUT("/pet/reports/done")
    Call<DoneReportsResponse> doneDailyWork(@Query("id") int id, @Query("done_user_id") String userId, @Query("done_user_image") String userImage);

    @POST("/pet/reports/update")
    Call<EditDailyWorkResponse> editDailyWork(@Body EditDailyWorkData data);

//    @POST("/pet/add")
//    Call<AddPetResponse> addPetReg(@Body AddPetData data);


    @Multipart
    @POST("/pet/add/des/photo")
    Call<AddpetDesResponse> addPetDesWithPhoto(@PartMap HashMap<String, RequestBody> data, @Part MultipartBody.Part image);

    @POST("/pet/add/des")
    Call<AddpetDesResponse> addPetDes(@Body AddPetDesData data);

    @GET("/post/group")
    Call<GroupPostResponse> getGroupPost(@Query("groupId") int groupId);

    @GET("/post/home")
    Call<PostResponse> getHomePost(@Query("groupId") int groupid, @Query("tab_") String tab_, @Query("area") String area);

    @POST("/post/setlike")
    Call<LikeResponse> setLike(@Body LikeData data);

    @POST("/comment/setComment")
    Call<CommentResponse> setComment(@Body CommentData data);

    @GET("/comment/getComment")
    Call<CommentGetResponse> getComment(@Query("postId") int postId);

    @GET("/comment/deleteComment")
    Call<CommentResponse> deleteComment(@Query("commentId") int commentId);

//    @POST("/pet/reports/add")
//    Call<CreateGroupResponse> createDailyWork(@Body CreateGroupData data);

    @GET("/user/group")
    Call<List<UserData>> getUsersInGroup(@Query("groupId") int groupId);

    @GET("/user/list/filter")
    Call<List<UserData>> getUserSearchResult(@Query("searchFilter") String filter);

    @GET("/group/list/filter")
    Call<List<GroupData>> getGroupSearchResult(@Query("searchFilter") String filter);

    @POST("/group/invite")
    Call<UidRespense> sendGroupInvite(@Body GroupInviteData data);

    @GET("/user/getNoti")
    Call<List<NotiData>> getNotiData(@Query("userId") String userId);

    @POST("/weather/infor")
    Call<WeatheLocationResponse> getWeatehrData(@Body WeatherLocationData data);

    @POST("/group/invite/accept")
    Call<UserData> acceptInvite(@Body GroupInviteData data);

    @POST("/group/selectInvite")
    Call<GroupSelectResponse> checkInvite(@Body GroupInviteData data);

    @POST("/follow/follow")
    Call<FollowResponse> followGroup(@Body FollowData data);

    @POST("/follow/unfollow")
    Call<FollowResponse> unfollowGroup(@Body FollowData data);

    @GET("/follow/getFollow")
    Call<GetFollowData> getFollow(@Query("groupId") int groupId);

    @GET("/group/member")
    Call<List<UserData>> getMemberData(@Query("groupId") int groupId);
}
