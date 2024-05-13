package com.sw.cocomong.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sw.cocomong.R;
import com.sw.cocomong.dto.FoodListItemDto;
import com.sw.cocomong.view.activity.CameraCapture;

public class FoodAddActivity extends AppCompatActivity {

    ImageView foodimage;
    TextView title, category;
    ImageButton back, edit;
    Button save, delete, btnCategory;
    EditText foodName, expire, memo;
    FoodListItemDto foodListItemDto;
    Bitmap foodImageBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_info);

        title = findViewById(R.id.tv_infoTitle);
        back = findViewById(R.id.btn_back);
        edit = findViewById(R.id.btn_edit);
        save = findViewById(R.id.btn_save);
        delete = findViewById(R.id.btn_delete);
        foodName = findViewById(R.id.et_infoFoodName);
        btnCategory = findViewById(R.id.btn_infoCategory);
        category = findViewById(R.id.tv_category);
        expire = findViewById(R.id.et_infoExpire);
        memo = findViewById(R.id.et_memo);

        foodimage = findViewById(R.id.food_image);


        save.setVisibility(View.VISIBLE);
        delete.setVisibility(View.GONE);
        edit.setVisibility(View.GONE);

        foodName.setEnabled(true);
        btnCategory.setEnabled(true);
        expire.setEnabled(true);
        memo.setEnabled(true);

        foodImageBitmap=CameraCapture.moveBitmap();
        foodimage.setImageBitmap(foodImageBitmap);

        btnCategory.setOnClickListener(v->{
            Intent intentCategory = new Intent(FoodAddActivity.this, CategorySelectActivity.class);
            startActivityForResult(intentCategory,1212);
        });

        back.setOnClickListener(v -> {
            finish();
        });

        save.setOnClickListener(v -> {

            foodName.setEnabled(false);
            category.setEnabled(false);
            btnCategory.setEnabled(false);
            expire.setEnabled(false);
            memo.setEnabled(false);

            foodListItemDto = new FoodListItemDto(foodImageBitmap,foodName.getText().toString(), category.getText().toString(), expire.getText().toString(), memo.getText().toString());
            UserActivity.foodListItemDtos.add(foodListItemDto);

            save.setVisibility(View.GONE);
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1212&&resultCode==RESULT_OK){
            if(data!=null){
                String selectedCategory = data.getStringExtra("category");
                category.findViewById(R.id.tv_category);
                category.setText(selectedCategory);
            }
        }
    }

    // TODO: 2024-05-13 바코드 인식 후 카테고리 설정해주는 로직
    public String setCategory(String categoryName){

        return "";
    }
}
