package com.david0926.safepad.Main2;

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
import com.david0926.safepad.databinding.FragmentMain2Binding;
import com.david0926.safepad.util.SharedPreferenceUtil;

import org.jetbrains.annotations.NotNull;

public class MainFragment2 extends Fragment {

    public static MainFragment2 newInstance() {
        return new MainFragment2();
    }

    private BroadcastReceiver broadcastReceiverAlert;
    private BroadcastReceiver broadcastReceiverTime;

    private Context mContext;
    private FragmentMain2Binding binding;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main2, container, false);
        binding.setAlert(SharedPreferenceUtil.getInt(mContext, "alert", 0));
        binding.setTime(SharedPreferenceUtil.getInt(mContext, "time", 0));

        binding.btnMain2Share.setOnClickListener(view -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Wanna join SafePad? \n" +
                    "https://github.com/roian6/SafePad");
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        });

        broadcastReceiverAlert = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action != null && action.equals("main_alert")) {
                    SharedPreferenceUtil.putInt(mContext, "alert",
                            SharedPreferenceUtil.getInt(mContext, "alert", 0)+1);
                    binding.setAlert(SharedPreferenceUtil.getInt(mContext, "alert", 0));
                }
            }
        };
        mContext.registerReceiver(broadcastReceiverAlert, new IntentFilter("main_alert"));

        broadcastReceiverTime = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action != null && action.equals("main_time")) {
                    binding.setTime(SharedPreferenceUtil.getInt(mContext, "time", 0));
                }
            }
        };
        mContext.registerReceiver(broadcastReceiverTime, new IntentFilter("main_time"));

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        mContext.unregisterReceiver(broadcastReceiverAlert);
        mContext.unregisterReceiver(broadcastReceiverTime);
        super.onDestroy();
    }
}
