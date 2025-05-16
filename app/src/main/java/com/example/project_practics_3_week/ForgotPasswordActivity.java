package com.example.project_practics_3_week;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etEmail;
    private TextView errorText;
    private View overlay;
    private Dialog notificationDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Инициализация элементов
        ImageButton btnBack = findViewById(R.id.btnBack);
        etEmail = findViewById(R.id.etEmail);
        errorText = findViewById(R.id.errorText);
        overlay = findViewById(R.id.overlay);
        androidx.appcompat.widget.AppCompatButton btnSubmit = findViewById(R.id.btnSubmit);

        notificationDialog = new Dialog(this);
        notificationDialog.setContentView(R.layout.dialog_notification);
        Window window = notificationDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.copyFrom(window.getAttributes());
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setDimAmount(0.75f);
        }
        notificationDialog.setCanceledOnTouchOutside(false);
        notificationDialog.setCancelable(false);

        btnBack.setOnClickListener(v -> finish());

        btnSubmit.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (validateEmail(email)) {
                errorText.setVisibility(View.GONE);
                overlay.setVisibility(View.VISIBLE);
                notificationDialog.show();
            } else {
                errorText.setVisibility(View.VISIBLE);
                overlay.setVisibility(View.GONE);
                notificationDialog.dismiss();
            }
        });

        notificationDialog.findViewById(android.R.id.content).setOnClickListener(v -> {
            notificationDialog.dismiss();
            overlay.setVisibility(View.GONE);
            Intent intent = new Intent(ForgotPasswordActivity.this, OtpVerificationActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private boolean validateEmail(String email) {
        if (email.isEmpty()) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (notificationDialog != null && notificationDialog.isShowing()) {
            notificationDialog.dismiss();
        }
    }
}
