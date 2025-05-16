package com.example.project_practics_3_week;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class OtpVerificationActivity extends AppCompatActivity {

    private EditText[] otpCells = new EditText[6];
    private TextView timerText, resendCodeText;
    private CountDownTimer countDownTimer;
    private Dialog resendDialog;
    private boolean isTimerRunning = false;
    private static final long TIMER_DURATION = 30000;
    private static final String CORRECT_OTP = "123456";  //проверка

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        ImageButton btnBack = findViewById(R.id.btnBack);
        otpCells[0] = findViewById(R.id.otp_cell_1);
        otpCells[1] = findViewById(R.id.otp_cell_2);
        otpCells[2] = findViewById(R.id.otp_cell_3);
        otpCells[3] = findViewById(R.id.otp_cell_4);
        otpCells[4] = findViewById(R.id.otp_cell_5);
        otpCells[5] = findViewById(R.id.otp_cell_6);
        timerText = findViewById(R.id.timerText);
        resendCodeText = findViewById(R.id.resendCodeText);

        resendDialog = new Dialog(this);
        resendDialog.setContentView(R.layout.dialog_resend_otp);
        Window window = resendDialog.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setDimAmount(0.75f);
        }
        resendDialog.setCanceledOnTouchOutside(false);
        resendDialog.setCancelable(false);

        setupOtpInput();
        startTimer();

        btnBack.setOnClickListener(v -> finish());

        resendCodeText.setOnClickListener(v -> {
            resendCodeText.setVisibility(View.GONE);
            resendDialog.show();
            startTimer();
            clearOtpCells();
        });

        resendDialog.findViewById(android.R.id.content).setOnClickListener(v -> resendDialog.dismiss());
    }

    private void setupOtpInput() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                EditText currentCell = null;
                int currentIndex = -1;

                for (int i = 0; i < otpCells.length; i++) {
                    if (otpCells[i].getText() == s) {
                        currentCell = otpCells[i];
                        currentIndex = i;
                        break;
                    }
                }

                if (currentCell != null) {
                    if (s.length() < currentCell.getText().toString().length() || !areAllCellsFilled()) {
                        setNeutralBorder();
                    }

                    if (s.length() == 1) {
                        if (currentIndex < otpCells.length - 1) {
                            otpCells[currentIndex + 1].requestFocus();
                        }
                    } else if (s.length() == 0 && currentIndex > 0) {
                        otpCells[currentIndex - 1].requestFocus();
                    }

                    if (areAllCellsFilled()) {
                        validateOtp();
                    }
                }
            }
        };

        for (EditText cell : otpCells) {
            cell.addTextChangedListener(textWatcher);
        }
    }

    private boolean areAllCellsFilled() {
        for (EditText cell : otpCells) {
            if (cell.getText().toString().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void validateOtp() {
        StringBuilder otp = new StringBuilder();
        for (EditText cell : otpCells) {
            otp.append(cell.getText().toString().trim());
        }
        if (otp.toString().equals(CORRECT_OTP)) {
            setNeutralBorder();
            Intent intent = new Intent(OtpVerificationActivity.this, Activity_register.class);
            startActivity(intent);
            finish();
        } else {
            setErrorBorder();
        }
    }

    private void setNeutralBorder() {
        for (EditText cell : otpCells) {
            cell.setBackgroundResource(R.drawable.neutral_border);
        }
    }

    private void setErrorBorder() {
        for (EditText cell : otpCells) {
            cell.setBackgroundResource(R.drawable.error_border);
        }
    }

    private void clearOtpCells() {
        for (EditText cell : otpCells) {
            cell.setText("");
        }
        setNeutralBorder();
        otpCells[0].requestFocus();
    }

    private void startTimer() {
        if (isTimerRunning) {
            countDownTimer.cancel();
        }
        isTimerRunning = true;
        resendCodeText.setVisibility(View.GONE);
        countDownTimer = new CountDownTimer(TIMER_DURATION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                timerText.setText(String.format("00:%02d", seconds));
            }

            @Override
            public void onFinish() {
                timerText.setText("00:00");
                resendCodeText.setVisibility(View.VISIBLE);
                isTimerRunning = false;
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (resendDialog != null && resendDialog.isShowing()) {
            resendDialog.dismiss();
        }
    }
}