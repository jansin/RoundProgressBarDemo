package com.miao.roundprogressbar;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private RoundProgressBar mRoundProgressBar1;
    private CountDownProgress countDownProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRoundProgressBar1 = (RoundProgressBar) findViewById(R.id.roundProgressBar1);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        mRoundProgressBar1.setmRadius(width/4);
        ((Button)findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mRoundProgressBar1.startCountDownTime(new RoundProgressBar.OnProgressFinishListener() {
                    @Override
                    public void progressFinished() {

                    }
                }, 322.5, 100);
            }
        });

        countDownProgress = (CountDownProgress) findViewById(R.id.countdownProgress);
        countDownProgress.setCountdownTime(10*1000);
        countDownProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownProgress.startCountDownTime(new CountDownProgress.OnCountdownFinishListener() {
                    @Override
                    public void countdownFinished() {
                        Toast.makeText(MainActivity.this, "倒计时结束了--->该UI处理界面逻辑了", Toast.LENGTH_LONG).show();
                    }
                });
                /*Message message = Message.obtain();
                message.what = HANDLER_MESSAGE;
                handler.sendMessage(message);*/
            }
        });
    }
}
