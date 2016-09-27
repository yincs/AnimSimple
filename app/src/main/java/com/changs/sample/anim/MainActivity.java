package com.changs.sample.anim;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.changs.sample.anim.anim.AnimGroup;

public class MainActivity extends AppCompatActivity {


    private AnimGroup animGroup;
    private String nickname = "伟大的松哥";
    private String taskName = "获取到一大波美女";
    private Bitmap defaultBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        animGroup = (AnimGroup) findViewById(R.id.animGroup);
        defaultBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.user);

        findViewById(R.id.btn_anim_enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animGroup.startEnterAnim(nickname, defaultBitmap);
            }
        });


        findViewById(R.id.btn_anim_like).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animGroup.startLikeAnim(nickname, defaultBitmap);
            }
        });

        findViewById(R.id.btn_anim_win).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animGroup.startWinAnim(nickname, defaultBitmap);
            }
        });

        findViewById(R.id.btn_anim_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animGroup.startTaskDoneAnim(nickname, taskName);
            }
        });
    }


}
