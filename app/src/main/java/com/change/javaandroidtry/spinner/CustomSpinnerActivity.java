package com.change.javaandroidtry.spinner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.change.javaandroidtry.R;
import com.change.javaandroidtry.spinner.widget.SpinnerTextView;

import java.util.ArrayList;
import java.util.List;

public class CustomSpinnerActivity extends AppCompatActivity {

    private SpinnerTextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_spinner);
        textView = findViewById(R.id.custom_text);
        //设置数据源
        textView.setDataList(getSpinnerLeftData());
        textView.setOnItemSelectListener(new SpinnerTextView.OnItemSelectListener() {
            @Override
            public void OnItemSelect(int position, String text) {
               //选中回调TODO

            }
        });
    }

    private List<String> getSpinnerLeftData(){
        List listLeft = new ArrayList<String>();
        listLeft.add("W");
        listLeft.add("99");
        return listLeft;
    }
}