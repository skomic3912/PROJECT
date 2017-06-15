package com.example.shotakomiyama.project;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


/**
 * Created by shotakomiyama on 2017/06/13.
 */

public class gameOverActivity extends Activity {

    public void onCreate(Bundle saveInstance){
        super.onCreate(saveInstance);
        setContentView(R.layout.gameover);

        Button continue_Button = (Button) findViewById(R.id.continue_button);
        Button end_Button = (Button) findViewById(R.id.endButton);

        continue_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), gameActivity.class);
                startActivity(intent);
            }
        });

        end_Button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
