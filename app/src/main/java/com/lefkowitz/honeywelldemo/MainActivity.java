package com.lefkowitz.honeywelldemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    private TextView _upcTextTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _upcTextTV = (TextView) findViewById(R.id.activity_main_upcTV);

        Button openDialogBtn = (Button) findViewById(R.id.activity_main_open_dialogBTN);
        openDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UPCDialog dialog = new UPCDialog();
                dialog.show(getSupportFragmentManager(), dialog.getClass().getName());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScanEvent(BarCodeScannedEvent barCode) {
        _upcTextTV.setText(barCode.UPC);
    }
}
