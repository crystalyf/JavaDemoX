package com.change.javaandroidtry.view.dragchoose;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.change.javaandroidtry.R;
import com.change.javaandroidtry.view.dragchoose.adapter.CustomAdapter;
import com.change.javaandroidtry.view.dragchoose.widget.DragChessView;

import java.util.ArrayList;

/**
 * 拖拽选择的item到指定位置
 */
public class DragChooseActivity extends AppCompatActivity {

    DragChessView dragChessView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_choose);
        initView();
    }

    void initView() {
        dragChessView = findViewById(R.id.drag_main);
        dragChessView.setDragModel(DragChessView.DRAG_BY_LONG_CLICK);
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            arrayList.add("" + i);
        }

        for (int j = 0; j < 30; j++) {
            list.add(('A' + j) + "");
        }
//        dragChessView.setBottomAdapter(new CustomAdapter(list));
        dragChessView.setTopAdapter(new CustomAdapter(arrayList));
    }
}