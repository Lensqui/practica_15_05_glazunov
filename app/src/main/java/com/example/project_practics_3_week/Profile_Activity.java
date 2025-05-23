package com.example.project_practics_3_week;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile_Activity extends AppCompatActivity {

    private TextView profileFullname, nameText, surnameText, phoneText, addressText, paymentText;
    private ImageView profileImage, nameCheck, familiaCheck, telephoneNumberCheck, addressCheck, cardCheck;
    private SessionManager sessionManager;
    private DrawerLayout drawerLayout;
    private TextView navUserFullname;
    private ImageView navUserAvatar;
    private User currentUser;

    private static final String API_KEY = BuildConfig.SUPABASE_API_KEY;
    private static final String AUTH_TOKEN = BuildConfig.SUPABASE_AUTH_TOKEN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sessionManager = new SessionManager(this);

        profileFullname = findViewById(R.id.profile_fullname);
        profileImage = findViewById(R.id.profile_image);

        CardView cardName = findViewById(R.id.card_name);
        LinearLayout nameLayout = (LinearLayout) cardName.getChildAt(0);
        nameText = nameLayout.findViewById(R.id.name_text);
        nameCheck = nameLayout.findViewById(R.id.name_check);

        CardView cardSurname = findViewById(R.id.card_surname);
        LinearLayout surnameLayout = (LinearLayout) cardSurname.getChildAt(0);
        surnameText = surnameLayout.findViewById(R.id.surname_text);
        familiaCheck = surnameLayout.findViewById(R.id.familia_check);

        CardView cardPhone = findViewById(R.id.card_phone);
        LinearLayout phoneLayout = (LinearLayout) cardPhone.getChildAt(0);
        phoneText = phoneLayout.findViewById(R.id.phone_text);
        telephoneNumberCheck = phoneLayout.findViewById(R.id.telephoneNumber_check);

        CardView cardAddress = findViewById(R.id.card_address);
        LinearLayout addressLayout = (LinearLayout) cardAddress.getChildAt(0);
        addressText = addressLayout.findViewById(R.id.address_text);
        addressCheck = addressLayout.findViewById(R.id.adress_check);

        CardView cardPayment = findViewById(R.id.card_payment);
        LinearLayout paymentLayout = (LinearLayout) cardPayment.getChildAt(0);
        paymentText = paymentLayout.findViewById(R.id.payment_text);
        cardCheck = paymentLayout.findViewById(R.id.card_check);

        drawerLayout = findViewById(R.id.drawer_layout);

        View drawerView = findViewById(R.id.drawer_menu);
        navUserFullname = drawerView.findViewById(R.id.user_fullname);
        navUserAvatar = drawerView.findViewById(R.id.user_avatar);

        if (drawerView == null) {
            Toast.makeText(this, "DrawerView is null!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "DrawerView loaded successfully", Toast.LENGTH_LONG).show();
        }

        currentUser = (User) getIntent().getSerializableExtra("user");
        if (currentUser == null) {
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
            }
        } else {
            sessionManager.saveUser(currentUser);
            updateUI(currentUser);
            updateNavigationDrawer(currentUser);
        }
    }

    public void onNavigationItemClicked(View view) {
        int id = view.getId();
        drawerLayout.closeDrawer(GravityCompat.START);

        if (id == R.id.nav_profile) {
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
            Intent intent = new Intent(Profile_Activity.this, Activity_Sign.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void updateUI(User user) {
        String name = user.getName() != null ? user.getName() : "";
        String firstname = user.getFirstName() != null ? user.getFirstName() : "";
        profileFullname.setText((!name.isEmpty() ? name : "") + " " + (!firstname.isEmpty() ? firstname : "").trim());
        nameText.setText(name);
        surnameText.setText(firstname);
        phoneText.setText(user.getTelephone_number() != null ? user.getTelephone_number() : "");
        addressText.setText(user.getAddres_home() != null ? user.getAddres_home() : "");

        if (user.getCart_oplats() == null || user.getCart_oplats().isEmpty()) {
            paymentText.setText("Не добавлена");
        } else {
            String cardNumber = user.getCart_oplats().replaceAll("\\s", "");
            if (cardNumber.length() >= 4) {
                String lastFour = cardNumber.substring(cardNumber.length() - 4);
                String maskedPart = "•••• •••• ••••";
                paymentText.setText(maskedPart + " " + lastFour);
            } else {
                paymentText.setText(cardNumber);
            }
        }

        nameCheck.setVisibility(!name.isEmpty() ? View.VISIBLE : View.GONE);
        familiaCheck.setVisibility(!firstname.isEmpty() ? View.VISIBLE : View.GONE);
        telephoneNumberCheck.setVisibility(user.getTelephone_number() != null && !user.getTelephone_number().isEmpty() ? View.VISIBLE : View.GONE);
        addressCheck.setVisibility(user.getAddres_home() != null && !user.getAddres_home().isEmpty() ? View.VISIBLE : View.GONE);
        cardCheck.setVisibility(user.getCart_oplats() != null && !user.getCart_oplats().isEmpty() ? View.VISIBLE : View.GONE);

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
                    .into(profileImage);
        } else {
            profileImage.setImageResource(R.drawable.default_avatar);
        }
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

        Network.getApiService().getUsers(
                API_KEY,
                AUTH_TOKEN
        ).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<User> users = response.body();
                    boolean found = false;
                    User updatedUser = null;

                    for (User user : users) {
                        if (user.getEmail() != null && user.getEmail().equals(email)) {
                            found = true;
                            updatedUser = user;
                            break;
                        }
                    }

                    if (found && updatedUser != null) {
                        currentUser = updatedUser;
                        sessionManager.saveUser(updatedUser);
                        updateUI(updatedUser);
                        updateNavigationDrawer(updatedUser);
                        Toast.makeText(Profile_Activity.this, "Данные успешно синхронизированы", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Profile_Activity.this, "Пользователь не найден на сервере", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Profile_Activity.this, "Ошибка сервера: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(Profile_Activity.this, "Ошибка синхронизации: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sessionManager.setToken(null);
    }
}