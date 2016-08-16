package com.changs.kelly.math;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.changs.kelly.math.anim_enter.EnterView;
import com.changs.kelly.math.anim_like.LikeView;
import com.changs.kelly.math.anim_task.TaskDoneView;
import com.changs.kelly.math.anim_win.WinView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        final EnterView enterView = (EnterView) findViewById(R.id.enter_view);
        final LikeView likeView = (LikeView) findViewById(R.id.like_view);
        final WinView winView = (WinView) findViewById(R.id.win_view);
        final TaskDoneView taskDoneView = (TaskDoneView) findViewById(R.id.task_done_view);

        findViewById(R.id.btn_anim_enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterView.start();
            }
        });


        findViewById(R.id.btn_anim_like).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeView.start();
            }
        });

        findViewById(R.id.btn_anim_win).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                winView.start();
            }
        });

        findViewById(R.id.btn_anim_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskDoneView.start();
            }
        });
    }


}
