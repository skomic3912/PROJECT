package com.example.shotakomiyama.project;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;


import java.util.ArrayList;

/**
 * Created by shotakomiyama on 2017/06/13.
 */

public class gameActivity extends Activity {
    //info
    private ArrayList<String> info;


    @Override
    protected void onCreate(Bundle saveInstance){
        super.onCreate(saveInstance);
        View gView = new gameView(getApplication());
        setContentView(gView);

       /* //Button maybe it isnt used
        Button end=(Button) findViewById(R.id.endButton);
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
    }

}

