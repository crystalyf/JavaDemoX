package com.change.javaandroidtry.okhttpthree.bean;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
    プレミアムチケット付与API（Androidアプリ専用）
 */
public class ENApplyInAppPurchaseResponse extends ENResponseInner {

    @SerializedName("receipt")
    private Receipt receipt;

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }
}


