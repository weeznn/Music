package com.example.weezn.music;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private Visualizer visualizer;
    private MediaPlayer mediaPlayer;
    private VisualizerView visualizerView = null;
    int showType=9;
    boolean isshow=false;

    private Button btn_play;
    private Button btn_pause;
    private Button btn_stop;
    private Button btn_exit;
    private Button btn_columns;
    private Button btn_line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "oncreat start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mediaPlayer = MediaPlayer.create(this, R.raw.go);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        btn_play = (Button) findViewById(R.id.play);
        btn_pause = (Button) findViewById(R.id.pause);
        btn_stop = (Button) findViewById(R.id.stop);
        btn_exit = (Button) findViewById(R.id.exit);
        btn_columns = (Button) findViewById(R.id.columns);
        btn_line = (Button) findViewById(R.id.line);

        btn_play.setOnClickListener(this);
        btn_pause.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        btn_columns.setOnClickListener(this);
        btn_line.setOnClickListener(this);
        //初始化
        setupView();

        Log.i(TAG, "oncreat stop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mediaPlayer) {
            visualizer.setEnabled(false);
            mediaPlayer.release();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
        visualizer.setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mediaPlayer) {
            mediaPlayer.release();
            visualizer.setEnabled(false);
            visualizer.release();
        }

    }

    private void setupView() {
        Log.i(TAG, "setupView");

        visualizerView = (VisualizerView) findViewById(R.id.view);
        visualizer = new Visualizer(mediaPlayer.getAudioSessionId());
        visualizer.setCaptureSize(256);
        visualizer.setEnabled(false);
        visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            //采集波形数据
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
                Log.i(TAG, "波形数据采集");
                if (null != visualizer && Contanst.WAVE == showType&&isshow) {
                    visualizerView.updata(waveform);
                }
            }

            //采集快速傅里叶变换的数据

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
                Log.i(TAG, "快速傅里叶变化");

                if (null != visualizer && Contanst.FFT == showType&&isshow) {
                    visualizerView.updata(fft);
                }
            }
        }, Visualizer.getMaxCaptureRate() / 2, true, false);

        Log.i(TAG, "setupView over");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
                if(!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                }
                break;
            case R.id.pause:
                mediaPlayer.pause();
                break;
            case R.id.stop:
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                break;

            case R.id.columns:
                visualizer.setEnabled(true);
                showType = Contanst.WAVE;
                visualizerView.setDrawType(Contanst.COLUMNS);
                isshow=true;
                break;

            case R.id.line:
                visualizer.setEnabled(true);
                showType = Contanst.WAVE;
                visualizerView.setDrawType(Contanst.LINE);
                isshow=true;

            case R.id.exit:
                mediaPlayer.release();
                visualizer.setEnabled(false);
                visualizer.release();
                finish();
                break;
            default:
                break;
        }
    }
}
