package com.example.project_practics_3_week;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EnterPinActivity extends AppCompatActivity {

    private EditText pinInput;
    private View[] pinDots;
    private String enteredPin = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pin);

        pinInput = findViewById(R.id.pinInput);
        pinDots = new View[]{
                findViewById(R.id.dot1),
                findViewById(R.id.dot2),
                findViewById(R.id.dot3),
                findViewById(R.id.dot4)
        };

        setupKeyboard();

        pinInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                enteredPin = s.toString();
                updatePinDots();
                if (enteredPin.length() == 4) {
                    Intent intent = new Intent(EnterPinActivity.this, ConfirmPinActivity.class);
                    intent.putExtra("PIN", enteredPin);
                    startActivity(intent);
                }
            }
        });


    /*    TextView forgotPin = findViewById(R.id.forgotPin);
        forgotPin.setOnClickListener(v -> Toast.makeText(this, "Функция восстановления PIN-кода", Toast.LENGTH_SHORT).show());

     */
        TextView skipB = findViewById(R.id.skippB);
        skipB.setOnClickListener(v -> {
            Toast.makeText(this, "Пропущено", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupKeyboard() {
        Button key0 = findViewById(R.id.key0);
        Button key1 = findViewById(R.id.key1);
        Button key2 = findViewById(R.id.key2);
        Button key3 = findViewById(R.id.key3);
        Button key4 = findViewById(R.id.key4);
        Button key5 = findViewById(R.id.key5);
        Button key6 = findViewById(R.id.key6);
        Button key7 = findViewById(R.id.key7);
        Button key8 = findViewById(R.id.key8);
        Button key9 = findViewById(R.id.key9);
        ImageButton keyDelete = findViewById(R.id.keyDelete);

        key0.setOnClickListener(v -> appendDigit("0"));
        key1.setOnClickListener(v -> appendDigit("1"));
        key2.setOnClickListener(v -> appendDigit("2"));
        key3.setOnClickListener(v -> appendDigit("3"));
        key4.setOnClickListener(v -> appendDigit("4"));
        key5.setOnClickListener(v -> appendDigit("5"));
        key6.setOnClickListener(v -> appendDigit("6"));
        key7.setOnClickListener(v -> appendDigit("7"));
        key8.setOnClickListener(v -> appendDigit("8"));
        key9.setOnClickListener(v -> appendDigit("9"));
        keyDelete.setOnClickListener(v -> deleteDigit());
    }

    private void appendDigit(String digit) {
        if (enteredPin.length() < 4) {
            enteredPin += digit;
            pinInput.setText(enteredPin);
        }
    }

    private void deleteDigit() {
        if (enteredPin.length() > 0) {
            enteredPin = enteredPin.substring(0, enteredPin.length() - 1);
            pinInput.setText(enteredPin);
        }
    }
    private void updatePinDots() {
        for (int i = 0; i < pinDots.length; i++) {
            pinDots[i].setSelected(i < enteredPin.length());
        }
    }
}
