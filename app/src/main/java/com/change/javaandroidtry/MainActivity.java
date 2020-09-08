package com.change.javaandroidtry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.change.javaandroidtry.spinner.CustomSpinnerActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        btn_spinner = findViewById(R.id.btn_spinner);
        btn_spinner.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_spinner:
               Intent intent = new Intent(this, CustomSpinnerActivity.class);
               startActivity(intent);
                break;
        }
    }
}