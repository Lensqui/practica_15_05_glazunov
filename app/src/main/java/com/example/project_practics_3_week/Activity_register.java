package com.example.project_practics_3_week;

import android.os.Bundle;
import android.text.Html;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CheckBox cbAgreement = findViewById(R.id.cbAgreement);
        cbAgreement.setText(Html.fromHtml("Даю согласие на <u>обработку персональных данных</u>")); //может будет какое-то типо соглашение чисто тест//

    }
}
