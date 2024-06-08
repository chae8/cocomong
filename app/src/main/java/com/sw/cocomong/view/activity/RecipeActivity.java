package com.sw.cocomong.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sw.cocomong.Object.FoodResObj;
import com.sw.cocomong.Object.RecipeObj;
import com.sw.cocomong.R;
import com.sw.cocomong.task.foodtask.FoodListGetTask;
import com.sw.cocomong.view.adapter.FoodAdapter;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RecipeActivity extends AppCompatActivity implements FoodListGetTask.FoodListGetTaskListener {

    ListView list;
    TextView refName_tv;
    Button refridge, foodAdd, favorite, recipe, sort, category;
    FoodAdapter foodAdapter, categoryAdapter,favAdapter;
    List<FoodResObj> foodResObjs = new ArrayList<>();
    public static List<FoodResObj> favoriteList=new ArrayList<>();
    List<FoodResObj> categoryList=new ArrayList<>();

    boolean isFavorite=false;
    boolean isCategory=false;
    String username,refname,refnum;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_list);
        Intent intent=getIntent();
        Bundle extras=intent.getExtras();
        refname=extras.getString("refname");  // 냉장고 위치 받아옴
        username=extras.getString("username");  // username 받아옴
        try {
            new FoodListGetTask(username, refname,this);
            foodResObjs.forEach(foodResObj -> {
                if(foodResObj.getFavorite().equals("true")) favoriteList.add(foodResObj);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        refName_tv=findViewById(R.id.ref_name);
        refName_tv.setText(refname);

        list = findViewById(R.id.list_food);
        foodAdd = findViewById(R.id.btn_foodAdd);
        favorite = findViewById(R.id.btn_listFavorite);
        recipe = findViewById(R.id.btn_recipe);
        refridge=findViewById(R.id.btn_refback);
        sort=findViewById(R.id.btn_sort);
        category=findViewById(R.id.btn_list_category);

        //foodAdapter = new FoodAdapter(UserActivity.this, foodListItemDtos, refListItemDto);
        foodAdapter = new FoodAdapter(RecipeActivity.this, foodResObjs);
        favAdapter = new FoodAdapter(RecipeActivity.this, favoriteList);
        categoryAdapter = new FoodAdapter(RecipeActivity.this, categoryList);

        list.setAdapter(foodAdapter);

        list.setOnItemClickListener((parent, view, position, id) -> {

            String foodname;
            String foodid;
            if(isCategory){
                foodname=categoryList.get(position).getFoodname();
                foodid = categoryList.get(position).getIdx().toString();
            }else if(isFavorite){
                foodname=favoriteList.get(position).getFoodname();
                foodid = favoriteList.get(position).getIdx().toString();
            }else {
                foodname = foodResObjs.get(position).getFoodname();
                foodid = foodResObjs.get(position).getIdx().toString();
            }

            Intent foodIntent = new Intent(RecipeActivity.this, FoodInfoActivity.class);
            foodIntent.putExtra("username",username);
            foodIntent.putExtra("foodname", foodname);
            foodIntent.putExtra("refname",refname);
            foodIntent.putExtra("foodid",foodid);
            startActivity(foodIntent);
        });

        foodAdd.setOnClickListener(v -> {
            if(isCategory) {
                isCategory=false;
                list.setAdapter(foodAdapter);
                category.setText("카테고리");
                category.setBackgroundColor(getResources().getColor(R.color.purple_100));
                category.setTextColor(getResources().getColor(R.color.black));
            }else if(isFavorite){
                isFavorite=false;
                list.setAdapter(foodAdapter);
                favorite.setText("즐겨찾기");
                favorite.setBackgroundColor(getResources().getColor(R.color.purple_100));
                favorite.setTextColor(getResources().getColor(R.color.black));
            }
            list.setAdapter(foodAdapter);
            Intent foodAddIntent = new Intent(RecipeActivity.this, FoodAddSelectActivity.class);
            foodAddIntent.putExtra("username",username);
            foodAddIntent.putExtra("refname",refname);
            foodAddIntent.putExtra("refnum",refnum);
            startActivity(foodAddIntent);
        });


        favorite.setOnClickListener(v->{
            favoriteList.clear();
            for(FoodResObj food : foodResObjs){
                if(food.getFavorite().equals("true"))
                    favoriteList.add(food);
            }
            if(!isFavorite) {
                if(isCategory) {
                    isCategory=false;
                    list.setAdapter(foodAdapter);
                    category.setText("카테고리");
                    category.setBackgroundColor(getResources().getColor(R.color.purple_100));
                    category.setTextColor(getResources().getColor(R.color.black));
                }
                isFavorite = true;
                list.setAdapter(favAdapter);
                favorite.setText("돌아가기");
                favorite.setBackgroundColor(getResources().getColor(R.color.purple_200));
                favorite.setTextColor(getResources().getColor(R.color.white));
            }else {
                isFavorite=false;
                list.setAdapter(foodAdapter);
                favorite.setText("즐겨찾기");
                favorite.setBackgroundColor(getResources().getColor(R.color.purple_100));
                favorite.setTextColor(getResources().getColor(R.color.black));
            }
        });

        category.setOnClickListener(v->{
            if(!isCategory){
                Intent categoryIntent = new Intent(RecipeActivity.this, CategorySelectActivity.class);
                startActivityForResult(categoryIntent,1300);
            }else{
                isCategory=false;
                list.setAdapter(foodAdapter);
                category.setText("카테고리");
                category.setBackgroundColor(getResources().getColor(R.color.purple_100));
                category.setTextColor(getResources().getColor(R.color.black));
            }
        });


        recipe.setOnClickListener(v->{
            list.setAdapter(foodAdapter);
            Intent mypageIntent = new Intent(RecipeActivity.this, LoginActivity.class);
            startActivity(mypageIntent);
        });

        refridge.setOnClickListener(v->{
            Intent refIntent = new Intent(RecipeActivity.this, RefridgeActivity.class);
            refIntent.putExtra("username",username);
            startActivity(refIntent);
        });

        sort.setOnClickListener(v->{
            final PopupMenu sortMenu = new PopupMenu(getApplicationContext(),v);
            getMenuInflater().inflate(R.menu.sort_menu, sortMenu.getMenu());
            sortMenu.setOnMenuItemClickListener(p->{
                if(p.getItemId()==R.id.sort_name){
                    if(isFavorite){
                        Collections.sort(favoriteList, Comparator.comparing(FoodResObj::getFoodname));
                        list.setAdapter(favAdapter);
                    }else if(isCategory){
                        Collections.sort(categoryList, Comparator.comparing(FoodResObj::getFoodname));
                        list.setAdapter(categoryAdapter);
                    }else {
                        Collections.sort(foodResObjs, Comparator.comparing(FoodResObj::getFoodname));
                        list.setAdapter(foodAdapter);
                    }
                } else if (p.getItemId()==R.id.sort_expire) {
                    if(isFavorite){
                        Collections.sort(favoriteList, Comparator.comparing(FoodResObj::getExpiredate));
                        list.setAdapter(favAdapter);
                    }else if(isCategory){
                        Collections.sort(categoryList, Comparator.comparing(FoodResObj::getExpiredate));
                        list.setAdapter(categoryAdapter);
                    }else {
                        Collections.sort(foodResObjs, Comparator.comparing(FoodResObj::getExpiredate));
                        list.setAdapter(foodAdapter);
                    }

                }
                return false;
            });
            sortMenu.show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            new FoodListGetTask(username, refname,this);  // foodlist 요청
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        foodAdapter.notifyDataSetChanged();
        favAdapter.notifyDataSetChanged();
        categoryAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        categoryList.clear();
        if (requestCode==1300&&resultCode==RESULT_OK){
            if(data!=null){
                String selectedCategory = data.getStringExtra("category");
                for(FoodResObj foodResObj : foodResObjs){
                    if(foodResObj.getCategory().equals(selectedCategory)) categoryList.add(foodResObj);
                }
                isCategory=true;
                if(isFavorite){
                    isFavorite=false;
                    list.setAdapter(foodAdapter);
                    favorite.setText("즐겨찾기");
                    favorite.setBackgroundColor(getResources().getColor(R.color.purple_100));
                    favorite.setTextColor(getResources().getColor(R.color.black));
                }
                Log.i("tag",categoryList.toString());
                categoryAdapter = new FoodAdapter(RecipeActivity.this, categoryList);
                list.setAdapter(categoryAdapter);
                category.setText(selectedCategory);
                category.setBackgroundColor(getResources().getColor(R.color.purple_200));
                category.setTextColor(getResources().getColor(R.color.white));
            }
        }
    }

    @Override
    public void onFoodListReceived(List<FoodResObj> foodResObjs) {
        this.foodResObjs = foodResObjs;
        // Update UI with the received food list
        updateUI();
    }
    private void updateUI() {
        foodAdapter = new FoodAdapter(this, foodResObjs);
        favAdapter = new FoodAdapter(RecipeActivity.this, favoriteList);
        list.setAdapter(foodAdapter);
        if(isCategory){
            list.setAdapter(categoryAdapter);
        }
    }

}