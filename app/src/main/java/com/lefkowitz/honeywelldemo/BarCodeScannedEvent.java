package com.lefkowitz.honeywelldemo;

/**
 * Created by yitz on 8/28/2017.
 */

public class BarCodeScannedEvent {

    public final String UPC;

    public BarCodeScannedEvent(String UPC) {
        this.UPC = UPC;
    }
}
