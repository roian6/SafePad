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

import org.jetbrains.annotations.NotNull;

public class MainFragment1 extends Fragment {

    public static MainFragment1 newInstance() {
        return new MainFragment1();
    }

    private BroadcastReceiver broadcastReceiverConnect;
    private BroadcastReceiver broadcastReceiverDisConnect;
    private BroadcastReceiver broadcastReceiverFailed;

    private Context mContext;
    private FragmentMain1Binding binding;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main1, container, false);
        binding.setName("Device Name");
        binding.setTime("2h 30m");
        binding.setIsConnected(false);
        binding.setOnProgress(false);

        binding.btnMain1Scan.setOnClickListener(view -> {
            if (!binding.getIsConnected()) {
                mContext.sendBroadcast(new Intent("main1_scan_device"));
                binding.setOnProgress(true);
            } else {
                mContext.sendBroadcast(new Intent("main1_disconnect"));
                binding.setIsConnected(false);
            }
        });

        binding.btnMain1Send.setOnClickListener(view -> {
            Intent intent = new Intent("main1_send_message");
            intent.putExtra("message", "1");
            mContext.sendBroadcast(intent);
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

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        mContext.unregisterReceiver(broadcastReceiverConnect);
        mContext.unregisterReceiver(broadcastReceiverFailed);
        mContext.unregisterReceiver(broadcastReceiverDisConnect);
        super.onDestroy();
    }
}
