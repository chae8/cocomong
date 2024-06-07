package com.sw.cocomong.task.reftask;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sw.cocomong.Object.RefObj;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RefDeleteTask {
    public RefDeleteTask(String id) throws JSONException {
        String url = "http://58.224.91.191:8080/ref/" + id;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).delete().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(!response.isSuccessful()){
                    //응답실패
                    Log.i("tag", "응답실패");
                }else{
                    //응답성공
                    final String responseData = response.body().string();
                    Log.i("tag", "응답성공"+responseData);
                }
            }
        });
    }
}
