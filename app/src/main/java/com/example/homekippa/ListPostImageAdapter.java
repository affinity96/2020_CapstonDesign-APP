package com.example.homekippa;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.homekippa.network.RetrofitClient;
import com.example.homekippa.network.ServiceApi;
import com.example.homekippa.ui.group.YesGroup;

import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListPostImageAdapter extends RecyclerView.Adapter<ListPostImageAdapter.MyViewHolder> {
    private ArrayList<SingleItemPostImage> postImage_Items;

    private ServiceApi service;
    private Cache cache;

    private Context context;

    public ListPostImageAdapter(ArrayList<SingleItemPostImage> postImageItems) {
        this.postImage_Items = postImageItems;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        service = RetrofitClient.getClient().create(ServiceApi.class);
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_postimage, parent, false);
        cache = new Cache(parent.getContext());
        context = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListPostImageAdapter.MyViewHolder holder, int position) {
        setPostImageData(holder, position);
    }

    private void setPostImageData(MyViewHolder holder, int position) {
        SingleItemPostImage postImage = postImage_Items.get(position);
//        if(postImage.getPostImage() == null){
//            holder.postImage.setVisibility(View.GONE);
//        }
//        else{
        getPostImage(holder, postImage.getPostImage());
//        }
    }

    private void getPostImage(MyViewHolder holder, String url) {
        Log.d("url", url);
        String[] w = url.split("/");
        String key = w[w.length - 1];

        Bitmap bit = cache.getBitmapFromCacheDir(key);
        if (bit != null) {
            holder.postImage.setImageBitmap(bit);
        } else {
//            ImageLoadTask task = new ImageLoadTask(url, holder.postImage, context, true);
//            task.execute();
            ImageTask task = new ImageTask(url, holder.postImage, context, false);
            task.getImage();
//            service.getProfileImage(url).enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    String TAG = "ListPostImageAdapter";
//                    if (response.isSuccessful()) {
//                        Log.d(TAG, "server contacted and has file");
//                        InputStream is = response.body().byteStream();
//                        Bitmap bitmap = BitmapFactory.decodeStream(is);
//                        if (bitmap != null) {
//                            holder.postImage.setImageBitmap(bitmap);
//                            cache.saveBitmapToJpeg(bitmap, key);
//                        }
//                    } else {
//                        Log.d(TAG, "server contact failed");
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
////                Toast.makeText(YesGroup.this, "그룹생성 에러 발생", Toast.LENGTH_SHORT).show();
////              Log.e("createGroup error",t.getMessage());
//                    t.printStackTrace();
//                }
//            });
        }

    }

    @Override
    public int getItemCount() {
        return postImage_Items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView postImage;

        MyViewHolder(View view) {
            super(view);
            postImage = (ImageView) view.findViewById(R.id.listitem_PostImage);
        }
    }
}