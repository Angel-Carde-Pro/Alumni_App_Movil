package com.gamesmindt.myapplication.configs;

import android.app.Activity;
import android.graphics.Color;
import android.widget.EditText;

import com.gamesmindt.myapplication.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoadingDialog {

    private Activity activity;
    private SweetAlertDialog pDialog;

    public LoadingDialog(Activity myActivity) {
        activity = myActivity;
    }

    public void startLoagingDialog() {
        pDialog = new SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Cargando..");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public boolean questionDialog() {
        final boolean[] result = {false};
        new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this file!")
                .setConfirmText("Yes,delete it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        result[0] = true;
                    }
                })
                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        result[0] = false;
                    }
                })
                .show();
        return result[0];
    }

    public void questionAndConfirmDialog() {
        new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this file!")
                .setConfirmText("Yes,delete it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog
                                .setTitleText("Deleted!")
                                .setContentText("Your imaginary file has been deleted!")
                                .setConfirmText("OK")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    }
                })
                .show();
    }

    public void inputsDialog() {
        final EditText editText = new EditText(activity);
        new SweetAlertDialog(activity, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("Custom view")
                .setConfirmText("Ok")
                .setCustomView(editText)
                .show();
    }

    public void warningDialog(String title, String message) {

        new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText("Volver")
                .show();
    }

    public void showSimpleDialog(String title, String message) {
        cerrarLoadingDialog();
        new SweetAlertDialog(activity, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setCustomImage(R.drawable.alumni_logo)
                .show();
    }

    public void showSuccessDialog(String title, String message) {
        cerrarLoadingDialog();
        new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .show();
    }

    public void showFailureDialog(String title, String message) {
        cerrarLoadingDialog();
        new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .show();
    }

    public void cerrarLoadingDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }
}
