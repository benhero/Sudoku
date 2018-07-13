package com.ben.sudoku;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;

/**
 * com.ben.sudoku
 *
 * @author chenbenbin
 * @date 2018/7/12
 */
public class MainActivity extends Activity {

    private LinearLayout mNumberChooseLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNumberChooseLayout = findViewById(R.id.number_choose_layout);
        initNumberChooseLayout();
    }

    private void initNumberChooseLayout() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1;
        layoutParams.leftMargin = 10;
        layoutParams.rightMargin = 10;
        for (int i = 0; i < 9; i++) {
            final TextView textView = new TextView(this);
            textView.setText(String.valueOf((i + 1)));
            textView.setTextSize(12);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.WHITE);
            textView.setBackgroundColor(Color.GRAY);
            textView.setPadding(0, 20, 0, 20);
            textView.setLayoutParams(layoutParams);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, textView.getText(), Toast.LENGTH_SHORT).show();
                }
            });
            mNumberChooseLayout.addView(textView, layoutParams);
        }
    }
}