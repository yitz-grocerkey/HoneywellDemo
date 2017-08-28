package com.lefkowitz.honeywelldemo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by yitz on 8/28/2017.
 */

public class UPCDialog extends AppCompatDialogFragment {

    private TextView _upcTextTV;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        _upcTextTV = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.dialog_upc, null, false);

        return new AlertDialog.Builder(getActivity())
                .setView(_upcTextTV)
                .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .show();
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
