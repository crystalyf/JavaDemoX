package com.change.javaandroidtry.okhttpthree.bean;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
    model for プレミアムチケット付与API（Androidアプリ専用）
 */


public class Receipt {
    @SerializedName("error_code")
    private int errorCode;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

}

