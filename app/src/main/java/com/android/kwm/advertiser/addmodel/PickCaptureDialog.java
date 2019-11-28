package com.android.kwm.advertiser.addmodel;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.android.kwm.R;

import androidx.annotation.NonNull;

public class PickCaptureDialog extends Dialog implements View.OnClickListener {

    public static final int PICK_MULTIPLE_IMAGE = 2;

    public static final int CAMERA_REQUEST_CODE = 1;

    public static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private AddModel activity;

    public PickCaptureDialog(@NonNull AddModel context) {
        super(context);
        activity = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pick_capture_image_dialog);

        TextView takePhoto = findViewById(R.id.take_photo);
        TextView chooseImage = findViewById(R.id.choose_image);
        Button cancelButton = findViewById(R.id.cancel_button);

        takePhoto.setOnClickListener(this);
        chooseImage.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_photo:
                activity.captureImage();
                break;
            case R.id.choose_image:
                activity.pickImage();
                break;
            case R.id.cancel_button:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

}
