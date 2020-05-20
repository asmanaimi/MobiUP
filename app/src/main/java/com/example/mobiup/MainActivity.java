package com.example.mobiup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;

import com.example.mobiup.bluetooth;

public class MainActivity extends AppCompatActivity {
    GridLayout mainGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainGrid = (GridLayout) findViewById(R.id.mainGrid);


    }

    private void bluetooth(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        int i =0;

        if(mainGrid.getChildCount()==i)
        {
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(MainActivity.this, bluetooth.class);
                    startActivity(intent);

                }
            });
        }
    }


    public void blue(View view) {
        if(view.getId()==R.id.blue){
            Intent intent = new Intent(MainActivity.this,bluetooth.class);
            startActivity(intent);

        }
    }

    public void about(View view) {
        if(view.getId()==R.id.about){
            Intent intent = new Intent(MainActivity.this,aboutus.class);
            startActivity(intent);

        }
    }
}
