package com.lefkowitz.honeywelldemo;

import android.app.Application;

import com.honeywell.aidc.AidcManager;
import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.ScannerUnavailableException;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yitz on 8/28/2017.
 */

public class DemoApp extends Application implements BarcodeReader.BarcodeListener {

    private BarcodeReader _honeywellBarcodeReader;
    private AidcManager _honeywellManager;

    @Override
    public void onCreate() {
        super.onCreate();
        startHoneywell();
    }

    @Override
    public void onTerminate() {
        stopHoneywell();
        super.onTerminate();
    }

    @Override
    public void onBarcodeEvent(BarcodeReadEvent barcodeReadEvent) {
        EventBus.getDefault().post(new BarCodeScannedEvent(barcodeReadEvent.getBarcodeData()));
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {

    }

    private void startHoneywell() {
        AidcManager.create(this, new AidcManager.CreatedCallback() {
            @Override
            public void onCreated(AidcManager aidcManager) {
                _honeywellManager = aidcManager;
                _honeywellBarcodeReader = _honeywellManager.createBarcodeReader();
                _honeywellBarcodeReader.addBarcodeListener(DemoApp.this);
                _honeywellBarcodeReader.setProperties(generateHoneywellScannerProperties());
                try {
                    _honeywellBarcodeReader.claim();
                } catch (ScannerUnavailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    Map<String, Object> generateHoneywellScannerProperties() {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,
                BarcodeReader.TRIGGER_CONTROL_MODE_AUTO_CONTROL);
        properties.put(BarcodeReader.PROPERTY_EAN_8_ENABLED, true);
        properties.put(BarcodeReader.PROPERTY_EAN_8_CHECK_DIGIT_TRANSMIT_ENABLED, true);
        properties.put(BarcodeReader.PROPERTY_UPC_E_ENABLED, true);
        properties.put(BarcodeReader.PROPERTY_UPC_E_CHECK_DIGIT_TRANSMIT_ENABLED, true);
        properties.put(BarcodeReader.PROPERTY_EAN_13_ENABLED, true);
        properties.put(BarcodeReader.PROPERTY_EAN_13_CHECK_DIGIT_TRANSMIT_ENABLED, true);
        return properties;
    }

    private void stopHoneywell() {
        if (_honeywellBarcodeReader != null) {
            try {
                _honeywellBarcodeReader.release();
                _honeywellBarcodeReader.removeBarcodeListener(this);
                _honeywellBarcodeReader.close();
                _honeywellBarcodeReader = null;
                if (_honeywellManager != null) {
                    _honeywellManager.close();
                    _honeywellManager = null;
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }
}
