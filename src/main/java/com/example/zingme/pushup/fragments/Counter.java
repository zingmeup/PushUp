package com.example.zingme.pushup.fragments;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zingme.pushup.R;
import com.example.zingme.pushup.appdata.SP;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Counter extends Fragment implements SensorEventListener {

    /*    int upDelay=1000;*/
    public long time;
    private static final int SENSOR_SENSITIVITY = 2;
    int goal, goalCompleted, setSize, currentRep, gains, series1Index, currentSeriesIndex;
    boolean counting = false, longWait = false;
    double cals = 0.0;
    View rootView;
    TextView timeView, calsView, headingView, subheadView, countView, targerView, gainsView, targetIncrementerView, goalCompletedView, goalPercView;
    Button startBtn, stopBtn;
    DecoView arcView;
    SensorManager sensorManager;
    Sensor sensor;
    TextToSpeech t1;MediaPlayer m;
    public final String colors[] = {"#f44336", "#e91e63", "#9c27b0", "#673ab7", "#3f51b5", "#2196f3", "#03a9f4", "#00bcd4",
            "#009688", "#4caf50", "#8bc34a", "#cddc39", "#ffeb3b", "#ffc107", "#ff9800", "#ff5722", "#4e342e", "#607d8b",
            "#b71c1c", "#880e4f", "#4a148c", "#311b92", "#283593", "#00695c", "#558b2f", "#f57f17", "#ffc400", "#f57c00",
            "#ff3d00", "#263238"};
    View.OnClickListener startBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            m=MediaPlayer.create(getContext(),R.raw.ting);
            m.start();
            counting = true;
            arcView.deleteAll();
            headingView.setText(String.valueOf(goal));
            subheadView.setText("No Pain No Gain");
            setcountViewText(String.valueOf(currentRep));
            arcView.addSeries(new SeriesItem.Builder(Color.argb(160, 218, 218, 218))
                    .setRange(0, 100, 100)
                    .setInitialVisibility(false)
                    .setLineWidth(15f)
                    .build());

            final SeriesItem seriesItem2 = new SeriesItem.Builder(Color.argb(255, 255, 255, 255))
                    .setRange(0, 100, 0).setInitialVisibility(false)
                    .setLineWidth(40f)
                    .build();

            currentSeriesIndex = arcView.addSeries(seriesItem2);
            arcView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                    .setDelay(1000)
                    .setDuration(2000)
                    .build());
            arcView.addEvent(new DecoEvent.Builder(((currentRep / setSize) * 100)).setIndex(currentSeriesIndex).setDelay(1000).build());
            startBtn.setVisibility(View.INVISIBLE);

            stopBtn.setVisibility(View.VISIBLE);
            stopBtn.setOnClickListener(stopBtnListener);
            final Handler mHandler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (counting) {
                        setTimeView();
                        time++;
                        mHandler.postDelayed(this, 1000);
                    }
                }
            };

            mHandler.post(runnable);

        }
    };

    private void setTimeView() {
        int h = (int) (time / 3600);
        int m = (int) (time % 3600) / 60;
        int s = (int) ((time % 3600) % 60);
        Log.e("Timer-", "TIME: " + time + " h:" + h + " m:" + m + " s:" + s);
        timeView.setText(h + ":" + m + ":" + s);
    }

    View.OnClickListener stopBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            counting = false;
            arcView.deleteAll();
            SP.ed.putInt("dailyCompleted", goalCompleted);
            SP.ed.putString("cals", String.valueOf((int) cals));
            SP.ed.putInt("setSize", (setSize + gains));
            SP.ed.putInt("time", (int) time);
            SP.ed.commit();
            getActivity().recreate();
            SimpleDateFormat df = new SimpleDateFormat("dd");
            Date c = Calendar.getInstance().getTime();
            String formattedDate = df.format(c);
            if (!SP.sp.contains("date")) {
                SP.ed.putString("date", formattedDate).commit();
            }else {
                if(!SP.sp.getString("date","0").equals(formattedDate)){
                    Toast.makeText(getContext(), "It's a new Day", Toast.LENGTH_SHORT).show();
                    SP.ed.putInt("dailyCompleted", 0).commit();
                    SP.ed.putString("cals", "0").commit();
                    SP.ed.putInt("time", 0).commit();
                    SP.ed.putString("date", formattedDate).commit();

                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_counter, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setValues();
        setViews();
    }

    private void setViews() {
        timeView = rootView.findViewById(R.id.counter_time);
        calsView = rootView.findViewById(R.id.counter_cals);
        goalCompletedView = rootView.findViewById(R.id.counter_goal);
        goalPercView = rootView.findViewById(R.id.counter_goalperc);
        headingView = rootView.findViewById(R.id.counter_head);
        subheadView = rootView.findViewById(R.id.counter_subhead);
        countView = rootView.findViewById(R.id.counter_count);
        targerView = rootView.findViewById(R.id.counter_targetView);
        gainsView = rootView.findViewById(R.id.counter_gains);
        startBtn = rootView.findViewById(R.id.counter_start);
        stopBtn = rootView.findViewById(R.id.counter_stop);
        arcView = rootView.findViewById(R.id.counter_decoView);
        targerView.setText(String.valueOf(setSize));
        gainsView.setText("0");
        calsView.setText(String.valueOf((int) (cals)) + " Cals");
        setTimeView();
        setcountViewText(String.valueOf(goalCompleted));
        t1 = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
        subheadView.setText(String.valueOf(goal));
        goalCompletedView.setText(String.valueOf(goalCompleted));
        goalPercView.setText(String.valueOf(((goalCompleted * 100) / goal)) + "%");

        arcView.addSeries(new SeriesItem.Builder(Color.argb(160, 218, 218, 218))
                .setRange(0, 100, 100)
                .setInitialVisibility(false)
                .setLineWidth(15f)
                .build());

        Log.e("Counter-completed", String.valueOf(goalCompleted));
        Log.e("Counter-goal", String.valueOf(goal));
        final SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 64, 196, 0))
                .setRange(0, 100, 0).setInitialVisibility(false)
                .setLineWidth(32f)
                .build();
        series1Index = arcView.addSeries(seriesItem1);
        arcView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                .setDelay(1000)
                .setDuration(2000)
                .build());
        arcView.addEvent(new DecoEvent.Builder(((goalCompleted * 100 / goal))).setIndex(series1Index).setDelay(1000).build());


        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        startBtn.setOnClickListener(startBtnListener);

        if (!SP.sp.getBoolean("dispTime", true)) {
            timeView.setVisibility(View.GONE);
        }
        if (!SP.sp.getBoolean("dispCals", true)) {
            calsView.setVisibility(View.GONE);
        }
        if (!SP.sp.getBoolean("dispPerc", true)) {
            goalPercView.setVisibility(View.GONE);
        }
        if (!SP.sp.getBoolean("dispComp", true)) {
            goalCompletedView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        setValues();
    }

    private void setValues() {
        goal = SP.sp.getInt("dailyGoal", 50);
        goalCompleted = SP.sp.getInt("dailyCompleted", 0);
        setSize = SP.sp.getInt("setSize", 0);
        currentRep = 0;
        gains = 0;
        cals = Double.valueOf(SP.sp.getString("cals", "0"));
        time = SP.sp.getInt("time", 0);
    }

    @Override
    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (counting) {
            if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                longWait = false;
                if (event.values[0] <= SENSOR_SENSITIVITY) {

                    //Toast.makeText(getContext(), "near", Toast.LENGTH_SHORT).show();
                    if (SP.sp.getBoolean("color", true)) {
                        rootView.setBackgroundColor(Color.parseColor(colors[(int) (Math.random() * (colors.length))]));
                    }
                    if (SP.sp.getBoolean("ttsudu", true)) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                t1.speak("Up", TextToSpeech.QUEUE_FLUSH, null, null);
                            }
                        }, 1000);
                    }
                    if (SP.sp.getBoolean("ting", true)) {
                        m=MediaPlayer.create(getContext(),R.raw.ting);
                        m.start();
                    }
/*                    upDelay=upDelay+(((currentRep*upDelay)/(setSize*10)));
                    Log.e("Delay-test-up",String.valueOf(upDelay));*/
                } else {

                    if (SP.sp.getBoolean("ting", true)) {
                        m=MediaPlayer.create(getContext(),R.raw.ting);
                        m.start();
                    }
                    goalCompleted++;
                    if (goalCompleted > goal) {
                        goal+=5;
                        headingView.setText(String.valueOf(goal));
                        SP.ed.putInt("dailyGoal", goalCompleted);
                    }
                    currentRep++;
                    cals += currentRep * (0.07);
                    calsView.setText(String.valueOf((int) cals) + " Cals");
                    goalCompletedView.setText(String.valueOf(goalCompleted));
                    goalPercView.setText(String.valueOf((int) ((goalCompleted * 100) / goal)) + "%");
                    if (currentRep > setSize) {
                        gains++;
                        gainsView.setText(String.valueOf(gains));
                    }
                    Log.e("Counting-currentRep", String.valueOf(currentRep));
                    if (SP.sp.getBoolean("color", true)) {
                        rootView.setBackgroundColor(Color.parseColor(colors[(int) (Math.random() * (colors.length))]));
                    }
                    //Toast.makeText(getContext(), "far", Toast.LENGTH_SHORT).show();
                    if (SP.sp.getBoolean("ttsudu", true)) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                t1.speak("Down", TextToSpeech.QUEUE_FLUSH, null, null);
                            }
                        }, 1500);
                    }
                    if (SP.sp.getBoolean("tts123", true)) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                t1.speak(String.valueOf(currentRep), TextToSpeech.QUEUE_FLUSH, null, null);
                            }
                        }, 500);
                    }
                    setcountViewText(String.valueOf(currentRep));

                    Log.e("Counting-currentMove", String.valueOf(((currentRep * 100 / setSize))));
                    arcView.moveTo(currentSeriesIndex, ((currentRep * 100 / setSize)), 500);
                }

                SP.ed.putInt("dailyCompleted", goalCompleted);
                SP.ed.putString("cals", String.valueOf(cals));
                SP.ed.putInt("setSize", setSize + gains);
                SP.ed.putInt("time", (int) time);
                SP.ed.commit();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.e("Counting-accuracy", String.valueOf(accuracy));

    }

    private void setcountViewText(String s) {
        if (Integer.parseInt(s) > 99) {
            countView.setTextSize(150f);
            countView.setText(s);
        } else {
            countView.setTextSize(200f);
            countView.setText(s);

        }
    }


}