package com.sw.cocomong.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sw.cocomong.R;
import com.sw.cocomong.dto.FoodListItemDto;
import com.sw.cocomong.view.adapter.FoodAdapter;
import com.sw.cocomong.view.activity.FoodAddActivity;

import java.util.List;

public class UserActivity extends AppCompatActivity {

    ListView list;
    Button refridge, foodAdd, favorite, mypage, sort;
    FoodAdapter foodAdapter;

    public static List<FoodListItemDto> foodListItemDtos = FoodListItemDto.getFoodListItems();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_list);

        list = findViewById(R.id.list_food);
        foodAdd = findViewById(R.id.btn_foodAdd);
        favorite = findViewById(R.id.btn_listFavorite);
        mypage = findViewById(R.id.btn_mypage);
        refridge=findViewById(R.id.btn_refback);
        sort=findViewById(R.id.btn_sort);

        foodAdapter = new FoodAdapter(UserActivity.this, foodListItemDtos);
        list.setAdapter(foodAdapter);

        list.setOnItemClickListener((parent, view, position, id) -> {

            Intent intent = new Intent(UserActivity.this, FoodInfoActivity.class);
            intent.putExtra("position",position);
            startActivity(intent);
        });

        foodAdd.setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, FoodAddSelectActivity.class);
            startActivity(intent);
        });

        favorite.setOnClickListener(v->{
            Intent intent = new Intent(UserActivity.this, FavoriteActivity.class);
            startActivity(intent);
        });

        mypage.setOnClickListener(v->{
            Intent intent = new Intent(UserActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        refridge.setOnClickListener(v->{
            Intent intent = new Intent(UserActivity.this, RefridgeActivity.class);
            startActivity(intent);
        });

        sort.setOnClickListener(v->{
            final PopupMenu sortMenu = new PopupMenu(getApplicationContext(),v);
            getMenuInflater().inflate(R.menu.sort_menu, sortMenu.getMenu());
            sortMenu.setOnMenuItemClickListener(p->{
                if(p.getItemId()==R.id.sort_name){
                    Toast.makeText(this, "이름순 정렬", Toast.LENGTH_SHORT).show();
                } else if (p.getItemId()==R.id.sort_expire) {
                    Toast.makeText(this, "유통기한 정렬", Toast.LENGTH_SHORT).show();
                }
                return false;
            });
            sortMenu.show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        foodAdapter.notifyDataSetChanged();
    }

}
