package com.example.zingme.pushup;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zingme.pushup.appdata.SP;


public class MainActivity extends AppCompatActivity {
    View newPlayerView;
    SP sp;
    ProgressBar progressBar;
    EditText newPlayerEdit;
    TextView playerNameText;
    Button newPlayerBtn;
    ImageView img;
    View.OnClickListener newPlayerBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!newPlayerEdit.getText().toString().equals("")) {

                playerNameText = findViewById(R.id.splash_playernameText);
                playerNameText.setText(newPlayerEdit.getText().toString());
                sp.ed.putBoolean("firstTime", false);
                sp.ed.putString("playerName", newPlayerEdit.getText().toString());
                SP.ed.commit();
                playerNameText.setVisibility(View.VISIBLE);
                newPlayerView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                launchActivity();
            } else {
                Toast.makeText(getApplicationContext(), "You Got to have a Name", Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = new SP(getApplicationContext());
        setContentView(R.layout.fragment_splash);
        progressBar = findViewById(R.id.splash_progressbar);
        newPlayerView = findViewById(R.id.new_playerView);
        newPlayerBtn = findViewById(R.id.new_playerButton);
        playerNameText = findViewById(R.id.splash_playernameText);
        img = findViewById(R.id.splash_img);
        newPlayerEdit = findViewById(R.id.new_playerEdit);

        if (SP.sp.getBoolean("firstTime", true)) {
            progressBar.setVisibility(View.INVISIBLE);
            newPlayerView.setVisibility(View.VISIBLE);
            newPlayerBtn.setOnClickListener(newPlayerBtnListener);
        } else {
            launchActivity();
        }
        final Handler mHandler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int[] imgIds = {R.drawable.ic_down, R.drawable.ic_up};
                img.setImageDrawable(getDrawable(imgIds[(int) (Math.random() * 19) / 10]));
                if (true) {
                    mHandler.postDelayed(this, 500);
                }

            }
        };

        mHandler.post(runnable);


    }

    private void launchActivity() {
        playerNameText.setVisibility(View.VISIBLE);
        playerNameText.setText(SP.sp.getString("playerName", "John Wick"));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplication(), Main2Activity.class));
            }
        }, 3000);
    }

}
