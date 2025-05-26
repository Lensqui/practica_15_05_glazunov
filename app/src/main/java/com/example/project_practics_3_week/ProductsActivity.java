package com.example.project_practics_3_week;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsActivity extends AppCompatActivity {
    private RecyclerView productRecyclerView;
    private ProductAdapter productAdapter;
    private DrawerLayout drawerLayout;
    private ImageView navUserAvatar;
    private TextView navUserFullname;
    private SessionManager sessionManager;
    private User currentUser;
    private static final String API_KEY = BuildConfig.SUPABASE_API_KEY;
    private static final String AUTH_TOKEN = BuildConfig.SUPABASE_AUTH_TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_categories);

        sessionManager = new SessionManager(this);
        drawerLayout = findViewById(R.id.drawer_layout);
        View drawerView = findViewById(R.id.drawer_menu);
        navUserFullname = drawerView.findViewById(R.id.user_fullname);
        navUserAvatar = drawerView.findViewById(R.id.user_avatar);

        productRecyclerView = findViewById(R.id.popularity);
        productRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productAdapter = new ProductAdapter(this, new ArrayList<>(), false);
        productRecyclerView.setAdapter(productAdapter);

        TextView title = findViewById(R.id.categories);
        title.setText(getIntent().getStringExtra("category_name"));

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());

        RecyclerView categoryRecyclerView = findViewById(R.id.buttons_recycler_view);
        categoryRecyclerView.setVisibility(View.GONE);

        currentUser = sessionManager.getUser();
        if (currentUser == null) {
            String email = sessionManager.getEmail();
            if (email != null) {
                checkDataSync();
            } else {
                Toast.makeText(this, "Сессия истекла, войдите снова", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, Activity_Sign.class));
                finish();
            }
        } else {
            updateNavigationDrawer(currentUser);
        }

        String categoryId = getIntent().getStringExtra("category_id");
        fetchProducts(categoryId);
    }

    private void fetchProducts(String categoryId) {
        Network.getApiService().getProducts(API_KEY, AUTH_TOKEN, "eq." + categoryId).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productAdapter.updateProducts(response.body());
                } else {
                    Toast.makeText(ProductsActivity.this, "Не удалось загрузить товары", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(ProductsActivity.this, "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateNavigationDrawer(User user) {
        String name = user.getName() != null ? user.getName() : "";
        String firstname = user.getFirstName() != null ? user.getFirstName() : "";
        navUserFullname.setText((!name.isEmpty() ? name : "") + " " + (!firstname.isEmpty() ? firstname : "").trim());

        if (user.getAvatar_url() != null && !user.getAvatar_url().isEmpty()) {
            String imageUrl = "https://bhvkcqjjlfvxiwbntdpy.supabase.co/storage/v1/object/public/avatar/" + user.getAvatar_url();
            GlideUrl glideUrl = new GlideUrl(imageUrl, new LazyHeaders.Builder()
                    .addHeader("apikey", API_KEY)
                    .addHeader("Authorization", AUTH_TOKEN)
                    .build());
            Glide.with(this)
                    .load(glideUrl)
                    .placeholder(R.drawable.default_avatar)
                    .error(R.drawable.default_avatar)
                    .into(navUserAvatar);
        } else {
            navUserAvatar.setImageResource(R.drawable.default_avatar);
        }
    }

    private void checkDataSync() {
        String email = sessionManager.getEmail();
        if (email == null) {
            Toast.makeText(this, "Сессия истекла, войдите снова", Toast.LENGTH_SHORT).show();
            return;
        }

        Network.getApiService().getUsers(API_KEY, AUTH_TOKEN).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<User> users = response.body();
                    User updatedUser = null;
                    for (User user : users) {
                        if (user.getEmail() != null && user.getEmail().equals(email)) {
                            updatedUser = user;
                            break;
                        }
                    }
                    if (updatedUser != null) {
                        currentUser = updatedUser;
                        sessionManager.saveUser(updatedUser);
                        updateNavigationDrawer(updatedUser);
                        Toast.makeText(ProductsActivity.this, "Данные синхронизированы", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProductsActivity.this, "Пользователь не найден", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProductsActivity.this, "Ошибка сервера: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(ProductsActivity.this, "Ошибка синхронизации: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void onNavigationItemClicked(View view) {
        int id = view.getId();
        drawerLayout.closeDrawer(GravityCompat.START);

        if (id == R.id.nav_profile) {
            startActivity(new Intent(this, Profile_Activity.class));
        } else if (id == R.id.nav_cart) {
            Toast.makeText(this, "Переход в Корзину", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_favorites) {
            Toast.makeText(this, "Переход в Избранное", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_orders) {
            Toast.makeText(this, "Переход в Заказы", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_notifications) {
            Toast.makeText(this, "Переход в Уведомления", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_settings) {
            Toast.makeText(this, "Переход в Настройки", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_support) {
            checkDataSync();
        } else if (id == R.id.nav_logout) {
            sessionManager.clearSession();
            Intent intent = new Intent(this, Activity_Sign.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}
