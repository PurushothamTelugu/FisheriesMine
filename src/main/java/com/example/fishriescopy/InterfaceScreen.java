package com.example.fishriescopy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class InterfaceScreen extends AppCompatActivity {

    TextView mtxt_prawn_stock,mtxt_fish_stock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interface_screen);

        mtxt_fish_stock = findViewById(R.id.txt_fish_stock);
        mtxt_prawn_stock = findViewById(R.id.txt_prawn_stock);

        mtxt_fish_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailScreen();
            }
        });

        mtxt_prawn_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailScreen();
            }
        });
    }

    private void detailScreen(){
        startActivity(new Intent(InterfaceScreen.this,LocalService.class));
        finish();
    }

}