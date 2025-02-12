package com.example.whtsappstatussaver;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class NextActivity extends AppCompatActivity {

    private ImageView next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        BindView();
        Click();

    }

    private void BindView() {
        next = (ImageView) findViewById(R.id.next);

    }

    private void Click() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(NextActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }
}