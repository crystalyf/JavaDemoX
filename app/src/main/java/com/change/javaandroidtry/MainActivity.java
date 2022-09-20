package com.change.javaandroidtry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.change.javaandroidtry.dialog.DialogActivity;
import com.change.javaandroidtry.okhttpthree.Okhttp3Activity;
import com.change.javaandroidtry.okhttpthree.eiken.ENRequestBase;
import com.change.javaandroidtry.okhttpthree.eiken.ENRequestGetReceipt;
import com.change.javaandroidtry.okhttpthree.eiken.SGLogHelper;
import com.change.javaandroidtry.spinner.CustomSpinnerActivity;
import com.change.javaandroidtry.spinner.spinnerday.SpinnerDayActivity;
import com.change.javaandroidtry.view.DatePickerDialogActivity;
import com.google.gson.GsonBuilder;

import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Request;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_spinner;
    Button btn_spinner_day;
    Button btn_dialog_fullscreen;
    Button btn_okhttp;
    Button btn_text;
    Button btn_date_picker_dialog;

    ConcurrentHashMap<String, Long> lastBillingOkTimeMap = new ConcurrentHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        btn_spinner = findViewById(R.id.btn_spinner);
        btn_spinner.setOnClickListener(this);
        btn_spinner_day = findViewById(R.id.btn_spinner_day);
        btn_spinner_day.setOnClickListener(this);
        btn_dialog_fullscreen = findViewById(R.id.btn_dialog_fullscreen);
        btn_dialog_fullscreen.setOnClickListener(this);
        btn_okhttp = findViewById(R.id.btn_okhttp);
        btn_okhttp.setOnClickListener(this);
        btn_text = findViewById(R.id.btn_text);
        btn_text.setOnClickListener(this);
        btn_date_picker_dialog = findViewById(R.id.btn_date_picker_dialog);
        btn_date_picker_dialog.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_spinner:
               Intent intent = new Intent(this, CustomSpinnerActivity.class);
               startActivity(intent);
                break;

            case R.id.btn_spinner_day:
                Intent intent3 = new Intent(this, SpinnerDayActivity.class);
                startActivity(intent3);
                break;

            case R.id.btn_dialog_fullscreen:
                Intent intent2 = new Intent(this, DialogActivity.class);
                startActivity(intent2);
                break;

            case R.id.btn_okhttp:
                Intent intent4 = new Intent(this, Okhttp3Activity.class);
                startActivity(intent4);
                break;

            case R.id.btn_text:
                String str = "{\"orderId\":\"GPA.3395-3558-0075-86340\",\"packageName\":\"com.change.googleplaydemo\",\"productId\":\"com.change.googleplaydemo.product2\",\"purchaseTime\":1655272585857,\"purchaseState\":0,\"purchaseToken\":\"focdpdnlkfdijlpnkjcfefln.AO-J1OzIJ8tJqCDFhcb4edA_LzvWiPk2y888OU6lY-tCuuuB0pzg6WHVe92IN28aKNET5WSLMT-FD7Wi68FkEzvomVJA7QZhPqCzgdhScuhBF-Sn4xgJweo\",\"quantity\":1,\"acknowledged\":true}";

                ENRequestGetReceipt innerReq = new ENRequestGetReceipt.Builder()
                        .receiptData(str)
                        .personalId("3000040645")
                        .build();
                ENRequestBase baseReq = new ENRequestBase.Builder()
                        .c(ENRequestBase.API.GET_IN_APP_PURCHASE_RECEIPT)
                        .bkeapi(innerReq)
                        .build();

                logAtRequest(baseReq.getJson());

           //    String result = new GsonBuilder().setPrettyPrinting().create().toJson(str, str.getClass());
            //    Log.v("logStr:", "finish：");
                break;

            case R.id.btn_date_picker_dialog:
                Intent intent5 = new Intent(this, DatePickerDialogActivity.class);
                startActivity(intent5);
                break;

        }
    }

    /**
     * HTTPリクエスト時のログ出力
     *
     * @param json
     */
    private void logAtRequest(String json) {
        String result = json.replaceAll("\\\\", " ");
        SGLogHelper.api("BODY: " + result);
    }
}