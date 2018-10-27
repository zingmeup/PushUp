package com.example.zingme.pushup.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.zingme.pushup.R;
import com.example.zingme.pushup.appdata.SP;

public class Setup extends Fragment {
    View rootView;
    int[] dailyalt = {R.id.daily_dec, R.id.daily_inc, R.id.daily_minus5, R.id.daily_minus10, R.id.daily_minus20, R.id.daily_minus20, R.id.daily_plus5, R.id.daily_plus10, R.id.daily_plus20};
    int[] setalt = {R.id.set_inc, R.id.set_dec, R.id.set_minus5, R.id.set_minus10, R.id.set_minus20, R.id.set_minus20, R.id.set_plus5, R.id.set_plus10, R.id.set_plus20};
    TextView dailyView, SetView;
    Switch tts123, ttsudu, color, dispTime, dispCals, ting, dispperc, dispcomp;
    Button saveBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_setup, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dailyView = rootView.findViewById(R.id.daily_count);
        SetView = rootView.findViewById(R.id.set_count);
        dailyView.setText(String.valueOf(SP.sp.getInt("dailyGoal", 50)));
        SetView.setText(String.valueOf(SP.sp.getInt("setSize", 10)));
        for (int i = 0; i < dailyalt.length; i++) {
            TextView temp1 = rootView.findViewById(dailyalt[i]);
            temp1.setOnClickListener(dailyClickListener);
        }
        for (int i = 0; i < setalt.length; i++) {
            TextView temp1 = rootView.findViewById(setalt[i]);
            temp1.setOnClickListener(setClickListener);
        }
        tts123 = rootView.findViewById(R.id.pref_tts123_switch);
        ttsudu = rootView.findViewById(R.id.pref_ttsudu_switch);
        color = rootView.findViewById(R.id.pref_color_switch);
        dispTime = rootView.findViewById(R.id.pref_disptm_switch);
        dispCals = rootView.findViewById(R.id.pref_dispcals_switch);
        dispperc = rootView.findViewById(R.id.pref_perc_switch);
        dispcomp = rootView.findViewById(R.id.pref_comp_switch);
        ting = rootView.findViewById(R.id.pref_ting_switch);
        tts123.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (tts123.isChecked()) {
                    ttsudu.setChecked(false);
                    ting.setChecked(false);
                }

            }
        });
        ttsudu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ttsudu.isChecked()) {
                    tts123.setChecked(false);
                    ting.setChecked(false);
                }
            }
        });

        ting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ting.isChecked()) {
                    tts123.setChecked(false);
                    ttsudu.setChecked(false);
                }
            }
        });
        tts123.setChecked(SP.sp.getBoolean("tts123", true));
        ttsudu.setChecked(SP.sp.getBoolean("ttsudu", true));
        color.setChecked(SP.sp.getBoolean("color", true));
        dispTime.setChecked(SP.sp.getBoolean("dispTime", true));
        dispCals.setChecked(SP.sp.getBoolean("dispCals", true));
        dispCals.setChecked(SP.sp.getBoolean("dispPerc", true));
        dispCals.setChecked(SP.sp.getBoolean("dispComp", true));
        ting.setChecked(SP.sp.getBoolean("ting", true));

        saveBtn = rootView.findViewById(R.id.setup_btn);
        saveBtn.setOnClickListener(saveBtnListener);
        savePreferences();
    }

    View.OnClickListener saveBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            savePreferences();
            getActivity().recreate();
        }
    };

    private void savePreferences() {
        SP.ed.putInt("dailyGoal", Integer.valueOf(dailyView.getText().toString()));
        SP.ed.putInt("setSize", Integer.valueOf(SetView.getText().toString()));
        SP.ed.putBoolean("tts123", tts123.isChecked());
        SP.ed.putBoolean("ttsudu", ttsudu.isChecked());
        SP.ed.putBoolean("ting", ting.isChecked());
        SP.ed.putBoolean("color", color.isChecked());
        SP.ed.putBoolean("dispTime", dispTime.isChecked());
        SP.ed.putBoolean("dispCals", dispCals.isChecked());
        SP.ed.putBoolean("dispPerc", dispperc.isChecked());
        SP.ed.putBoolean("dispComp", dispcomp.isChecked());
        SP.ed.putBoolean("setup", true);
        SP.ed.commit();
    }

    View.OnClickListener dailyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int curent = Integer.parseInt(dailyView.getText().toString());
            int a = 0;
            String thing = ((TextView) v).getText().toString();
            if (thing.charAt(0) == '-') {
                if (thing.length() > 1) {
                    a = curent - Integer.parseInt(thing.substring(1, 3));
                } else {
                    a = curent - 1;
                }
            } else if (thing.charAt(0) == '+') {
                if (thing.length() > 1) {
                    a = curent + Integer.parseInt(thing.substring(1, 3));
                } else {
                    a = curent + 1;
                }
            }
            if (a >= 0) {
                dailyView.setText(String.valueOf(a));
            }
        }
    };
    View.OnClickListener setClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int curent = Integer.parseInt(SetView.getText().toString());
            int a = 0;
            String thing = ((TextView) v).getText().toString();
            if (thing.charAt(0) == '-') {
                if (thing.length() > 1) {
                    a = curent - Integer.parseInt(thing.substring(1, 3));
                } else {
                    a = curent - 1;
                }
            } else if (thing.charAt(0) == '+') {
                if (thing.length() > 1) {
                    a = curent + Integer.parseInt(thing.substring(1, 3));
                } else {
                    a = curent + 1;
                }
            }
            if (a >= 0) {
                SetView.setText(String.valueOf(a));
            }
        }
    };
}
