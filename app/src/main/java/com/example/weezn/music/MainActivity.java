package com.example.weezn.music;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private final int VOICE_RECOGNITION_REQUST_CODE=1234;
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
    private Button btn_ring;
    private Button btn_circle;
    private Button btn_speack;



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
        btn_ring= (Button) findViewById(R.id.ring);
        btn_circle= (Button) findViewById(R.id.cicle);
        btn_speack= (Button) findViewById(R.id.speak);

        btn_play.setOnClickListener(this);
        btn_pause.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        btn_columns.setOnClickListener(this);
        btn_line.setOnClickListener(this);
        btn_ring.setOnClickListener(this);
        btn_circle.setOnClickListener(this);
        btn_speack.setOnClickListener(this);
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
                    visualizerView.updata(getTheGamePoint(fft));
                }
            }
        }, Visualizer.getMaxCaptureRate() / 2, false, true);

        Log.i(TAG, "setupView over");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
                //透明动画
                AlphaAnimation aa=new AlphaAnimation(0,1);
                aa.setDuration(1000);
                v.startAnimation(aa);

                if(!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                }
                break;
            case R.id.pause:
                //围绕（100，50）旋转动画
                RotateAnimation ro=new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                ro.setDuration(1000);
                v.startAnimation(ro);


                mediaPlayer.pause();
                break;
            case R.id.stop:

                //缩放动画效果
                ScaleAnimation sa=new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                sa.setDuration(1000);
                sa.start();

                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                break;

            case R.id.columns:
                //混合效果
                AnimationSet as=new AnimationSet(true);
                as.setDuration(1000);

                AlphaAnimation aa1=new AlphaAnimation(0,1);
                aa1.setDuration(1000);
                as.addAnimation(aa1);

                TranslateAnimation ta1=new TranslateAnimation(0,1,0,1);
                ta1.setDuration(1000);
                as.addAnimation(ta1);
                visualizer.setEnabled(true);
                showType = Contanst.FFT;
                visualizerView.setDrawType(Contanst.COLUMNS);
                isshow=true;
                break;

            case R.id.line:
                visualizer.setEnabled(true);
                showType = Contanst.FFT;
                visualizerView.setDrawType(Contanst.LINE);
                isshow=true;
                break;
            case R.id.cicle:
                visualizer.setEnabled(true);
                showType = Contanst.FFT;
                visualizerView.setDrawType(Contanst.Cicle);
                isshow=true;
                break;
            case R.id.ring:
                visualizer.setEnabled(true);
                showType = Contanst.FFT;
                visualizerView.setDrawType(Contanst.RING);
                isshow=true;
                break;
            case R.id.exit:
                mediaPlayer.release();
                visualizer.setEnabled(false);
                visualizer.release();
                finish();
                break;
            case R.id.speak:
                PackageManager pm=getPackageManager();
                //通过包查找是否有语音识别的服务
                List<ResolveInfo> activities =pm.queryIntentActivities(new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH),0);
                if(0!=activities.size()){
                    startVoiceRecogntionActivity();
                }else{
                    btn_speack.setEnabled(false);
                    btn_speack.setText("无法使用语音服务");
                }

                break;
            default:
                break;
        }
    }

    /**
     * 生成驱动因子
     * @param bytes_copy
     * @return
     */
    public byte[] getTheGamePoint(byte []bytes_copy){
        double p=0.00002;
        double spl=0;
        for(int i=0;i<bytes_copy.length-1;i++){
            bytes_copy[i]= (byte) (20*Math.log(bytes_copy[i]/p));
            spl+=bytes_copy[i];
        }
        bytes_copy[0]= (byte) (spl/bytes_copy.length);
        return  bytes_copy;
    }


    private void startVoiceRecogntionActivity(){
        //设置语音识别参数
        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                        "Speech recognition demo");
        startActivityForResult(intent,VOICE_RECOGNITION_REQUST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(VOICE_RECOGNITION_REQUST_CODE==requestCode&&RESULT_OK==resultCode){
            ArrayList<String> matches =data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String resultString="";
            for(int i=0;i<matches.size();i++){
                resultString+=matches.get(i);
            }
            if(0!=resultString.length()){
                if(0==resultString.compareTo("播放")){
                    if(!mediaPlayer.isPlaying()){
                        mediaPlayer.start();
                    }
                }else if(0==resultString.compareTo("停止")){
                    if(!mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                    }
                }
                Toast.makeText(this,resultString,Toast.LENGTH_SHORT);
            }else{
                Toast.makeText(this,"获取信息错误请重试",Toast.LENGTH_LONG);
            }

        }
    }
}
