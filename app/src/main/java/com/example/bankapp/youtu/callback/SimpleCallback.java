package com.example.bankapp.youtu.callback;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

public abstract class SimpleCallback<T> extends Callback<T> {

    private Context mContext;
    private Activity mActivity;
    private Dialog spotsDialog;

    public SimpleCallback(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public void onBefore() {
        super.onBefore();
        showDialog();
    }

    @Override
    public void onEnd() {
        super.onEnd();
        dismissDialog();
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }

    private void showDialog() {
        if (spotsDialog == null) {
            spotsDialog = new Dialog(mActivity);
        }
//        spotsDialog.show();
    }

    private void dismissDialog() {
        if (spotsDialog != null && spotsDialog.isShowing()) {
            spotsDialog.dismiss();
        }
    }
}
