package com.david0926.safepad.Main1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.david0926.safepad.R;
import com.david0926.safepad.databinding.FragmentMain1Binding;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainFragment1 extends Fragment {

    public static MainFragment1 newInstance() {
        return new MainFragment1();
    }

    public static MainFragment1 newInstanceConnected(String name) {
        return new MainFragment1(name);
    }

    private String name;
    private int x = 0;

    private List<Entry> entries1 = new ArrayList<>();
    private List<Entry> entries2 = new ArrayList<>();
    private List<Entry> entries3 = new ArrayList<>();
    private List<Entry> entries4 = new ArrayList<>();

    private BroadcastReceiver broadcastReceiverConnect;
    private BroadcastReceiver broadcastReceiverDisConnect;
    private BroadcastReceiver broadcastReceiverFailed;

    private BroadcastReceiver broadcastReceiverTime;
    private BroadcastReceiver broadcastReceiverPos;
    private BroadcastReceiver broadcastReceiverAlert;

    private Context mContext;
    private FragmentMain1Binding binding;

    private MainFragment1(String name) {
        this.name = name;
    }

    private MainFragment1() {
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main1, container, false);
        if (name != null) {
            binding.setName(name);
            binding.setIsConnected(true);
        } else {
            binding.setName("Device Name");
            binding.setIsConnected(false);
        }
        binding.setTime("0");
        binding.setOnProgress(false);
        binding.setIsAlert(false);


        binding.btnMain1Scan.setOnClickListener(view -> {
            if (!binding.getIsConnected()) {
                mContext.sendBroadcast(new Intent("main1_scan_device"));
                binding.setOnProgress(true);
            } else {
                mContext.sendBroadcast(new Intent("main1_disconnect"));
                binding.setIsConnected(false);
            }
        });

        broadcastReceiverConnect = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action != null && action.equals("main_connected")) {
                    binding.setOnProgress(false);
                    binding.setIsConnected(true);
                    binding.setName(intent.getStringExtra("name"));
                }
            }
        };
        mContext.registerReceiver(broadcastReceiverConnect, new IntentFilter("main_connected"));

        broadcastReceiverDisConnect = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action != null && action.equals("main_disconnected")) {
                    binding.setOnProgress(false);
                    binding.setIsConnected(false);
                }
            }
        };
        mContext.registerReceiver(broadcastReceiverDisConnect, new IntentFilter("main_disconnected"));

        broadcastReceiverFailed = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action != null && action.equals("main_failed")) {
                    binding.setOnProgress(false);
                }
            }
        };
        mContext.registerReceiver(broadcastReceiverFailed, new IntentFilter("main_failed"));

        broadcastReceiverTime = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action != null && action.equals("main_time")) {
                    binding.setTime(intent.getStringExtra("time"));
                }
            }
        };
        mContext.registerReceiver(broadcastReceiverTime, new IntentFilter("main_time"));

        broadcastReceiverPos = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action != null && action.equals("main_pos")) {
                    entries1.add(new Entry(x, Integer.parseInt(intent.getStringExtra("pos1"))));
                    entries2.add(new Entry(x, Integer.parseInt(intent.getStringExtra("pos2"))));
                    entries3.add(new Entry(x, Integer.parseInt(intent.getStringExtra("pos3"))));
                    entries4.add(new Entry(x, Integer.parseInt(intent.getStringExtra("pos4"))));

                    if(entries1.size()>10){
                        entries1.remove(0);
                        entries2.remove(0);
                        entries3.remove(0);
                        entries4.remove(0);
                    }
                    x++;
                    refreshChart();
                }
            }
        };
        mContext.registerReceiver(broadcastReceiverPos, new IntentFilter("main_pos"));

        broadcastReceiverAlert = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action != null && action.equals("main_alert")) {
                    binding.setIsAlert(intent.getBooleanExtra("alert", false));
                }
            }
        };
        mContext.registerReceiver(broadcastReceiverAlert, new IntentFilter("main_alert"));

        clearChartAttributes();

        return binding.getRoot();
    }

    private void clearChartAttributes(){
        binding.chartMain1.setDrawBorders(false);
        Description description = new Description();
        description.setText("");
        binding.chartMain1.setDescription(description);
        binding.chartMain1.setDrawGridBackground(false);
        binding.chartMain1.setDragXEnabled(false);
        binding.chartMain1.setDragYEnabled(false);
        binding.chartMain1.setHorizontalScrollBarEnabled(false);
        binding.chartMain1.setHorizontalFadingEdgeEnabled(false);
        binding.chartMain1.setVerticalFadingEdgeEnabled(false);
        binding.chartMain1.setVerticalScrollBarEnabled(false);
        binding.chartMain1.setDoubleTapToZoomEnabled(false);
        binding.chartMain1.setClickable(false);
        binding.chartMain1.setFocusable(false);
        binding.chartMain1.setTouchEnabled(false);
        binding.chartMain1.setExtraBottomOffset(48);

        YAxis leftAxis = binding.chartMain1.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setEnabled(false);
        YAxis rightAxis = binding.chartMain1.getAxisRight();
        leftAxis.setDrawGridLines(false);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setEnabled(false);

        Legend legend = binding.chartMain1.getLegend();
        legend.setEnabled(true);
    }

    private void refreshChart(){
        binding.chartMain1.clear();

        LineDataSet dataSet1 = new LineDataSet(entries1, "Right-Top");
        LineDataSet dataSet2 = new LineDataSet(entries2, "Right-Bottom");
        LineDataSet dataSet3 = new LineDataSet(entries3, "Left-Top");
        LineDataSet dataSet4 = new LineDataSet(entries4, "Left-Bottom");

        List<ILineDataSet> dataSets = new ArrayList<>();

        dataSets.add(dataSet1);
        dataSets.add(dataSet2);
        dataSets.add(dataSet3);
        dataSets.add(dataSet4);

        dataSet1.setColor(mContext.getColor(R.color.materialRed));
        dataSet2.setColor(mContext.getColor(R.color.materialYellow));
        dataSet3.setColor(mContext.getColor(R.color.materialGreen));
        dataSet4.setColor(mContext.getColor(R.color.colorPrimary));

        LineData lineData = new LineData(dataSets);
        binding.chartMain1.setData(lineData);
        binding.chartMain1.invalidate();
    }

    @Override
    public void onDestroy() {
        mContext.unregisterReceiver(broadcastReceiverConnect);
        mContext.unregisterReceiver(broadcastReceiverFailed);
        mContext.unregisterReceiver(broadcastReceiverDisConnect);
        mContext.unregisterReceiver(broadcastReceiverTime);
        mContext.unregisterReceiver(broadcastReceiverPos);
        mContext.unregisterReceiver(broadcastReceiverAlert);
        super.onDestroy();
    }
}
