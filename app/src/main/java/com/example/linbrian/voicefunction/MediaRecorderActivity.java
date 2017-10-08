package com.example.linbrian.voicefunction;

/**
 * Created by linbrian on 2017/10/7.
 */

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.io.File;
import java.io.IOException;
public class MediaRecorderActivity extends Activity {

    Context context;

    // 錄音按鈕
    private ToggleButton recordBt;
    // 撥放按鈕
    private Button playBt;
    // 音檔站存資料夾
    File RF;
    // 錄音器
    MediaRecorder MR = null;
    // 撥放器
    MediaPlayer MP = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_recorder);
        //R.layout.activity_media_recorder
        context = this;

        // 抓取錄音按鈕
        recordBt = (ToggleButton) findViewById(R.id.toggleButton);
        // 抓取撥放按鈕
        playBt = (Button) findViewById(R.id.Button2);

        // 設定一開始不能按的按鈕
        playBt.setEnabled(false);

        // 設定監聽
        recordBt.setOnClickListener(clickLT);
        playBt.setOnClickListener(clickLT);
    }

    // 監聽方法
    private View.OnClickListener clickLT = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.toggleButton:
                    record();
                    break;
                case R.id.Button2:
                    play();
                    break;
            }
        }
    };

    // 錄音
    private void record() {
        if (recordBt.isChecked()) {
            // 錄音流程
            try {
                MR = new MediaRecorder();
                MR.setAudioSource(MediaRecorder.AudioSource.MIC);
                MR.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                MR.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                // 設定檔案位置，可以在手機上的檔案管理找到剛剛錄下的聲音
                RF = File.createTempFile("raw", ".amr", Environment.getExternalStorageDirectory());
                MR.setOutputFile(RF.getAbsolutePath());
                MR.prepare();
                MR.start();
                Toast.makeText(context,RF.getAbsolutePath().toString(),Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(context,"FileIOException", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            // 停止錄音
            if (MR != null) {
                MR.stop();
                MR.release();
                MR = null;
                // 開啟不能按的按鈕
                playBt.setEnabled(true);
            }
        }
    }

    // 播放
    private void play() {
        // 播放流程
        Uri uri = Uri.fromFile(RF.getAbsoluteFile());
        MP = MediaPlayer.create(context, uri);
        MP.start();
        MP.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer MP) {
                MP.release();
            }
        });
    }


    @Override
    protected void onPause() {
        Toast.makeText(this, "Pause", Toast.LENGTH_LONG).show();
        super.onPause();
    }

}
