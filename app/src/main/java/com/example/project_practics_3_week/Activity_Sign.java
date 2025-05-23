package com.example.project_practics_3_week;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Sign extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private AppCompatButton btnRegister;
    private AppCompatImageButton btnBack;
    private SessionManager sessionManager;
    private static final String API_KEY = BuildConfig.SUPABASE_API_KEY;
    private static final String AUTHORIZATION = BuildConfig.SUPABASE_AUTH_TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        btnBack = findViewById(R.id.btnBack);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        sessionManager = new SessionManager(this);

        String lastEmail = sessionManager.getLastEmail();
        if (lastEmail != null) {
            etEmail.setText(lastEmail);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Activity_Sign.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                    return;
                }

                Network.getApiService().getUsers(API_KEY, AUTHORIZATION).enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<User> users = response.body();
                            boolean isAuthenticated = false;
                            User authenticatedUser = null;

                            for (User user : users) {
                                if (user.getEmail() != null && user.getPassword() != null &&
                                        user.getEmail().equals(email) && user.getPassword().equals(password)) {
                                    isAuthenticated = true;
                                    authenticatedUser = user;
                                    break;
                                }
                            }

                            if (isAuthenticated) {
                                sessionManager.saveUserData(email, password);
                                sessionManager.saveUser(authenticatedUser);
                                sessionManager.setToken(AUTHORIZATION);
                                sessionManager.saveLastEmail(email);
                                Toast.makeText(Activity_Sign.this, "Успешный вход!", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(Activity_Sign.this, Profile_Activity.class);
                                intent.putExtra("user", authenticatedUser);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(Activity_Sign.this, "Неверный email или пароль", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Activity_Sign.this, "Ошибка сервера: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        Toast.makeText(Activity_Sign.this, "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
