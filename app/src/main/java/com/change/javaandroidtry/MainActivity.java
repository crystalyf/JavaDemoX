package com.change.javaandroidtry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.change.javaandroidtry.dialog.DialogActivity;
import com.change.javaandroidtry.spinner.CustomSpinnerActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_spinner;
    Button btn_dialog_fullscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        btn_spinner = findViewById(R.id.btn_spinner);
        btn_spinner.setOnClickListener(this);
        btn_dialog_fullscreen = findViewById(R.id.btn_dialog_fullscreen);
        btn_dialog_fullscreen.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_spinner:
               Intent intent = new Intent(this, CustomSpinnerActivity.class);
               startActivity(intent);
                break;

            case R.id.btn_dialog_fullscreen:
                Intent intent2 = new Intent(this, DialogActivity.class);
                startActivity(intent2);
                break;

        }
    }
}