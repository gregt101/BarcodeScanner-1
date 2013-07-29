package com.google.zxing.client.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import com.google.zxing.client.android.camera.CameraManager;
import com.google.zxing.client.android.camera.FrontLightMode;

/**
 * User: Silence
 * Date: 27.06.2013 19:22
 */
public class FlashImageButtonWrapper implements View.OnClickListener {

    private ImageButton imageButton;
    private SharedPreferences sharedPrefs;
    private FrontLightMode state;
    private CameraManager cameraManager;

    private FlashImageButtonWrapper(ImageButton imageButton, Context context, CameraManager cameraManager) {
        this.imageButton = imageButton;
        this.cameraManager = cameraManager;

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        state = FrontLightMode.readPref(sharedPrefs);
        setState(state);
        imageButton.setOnClickListener(this);
    }

    public static void wrap(ImageButton imageButton, Context context, CameraManager cameraManager) {
        new FlashImageButtonWrapper(imageButton, context, cameraManager);
    }

    private void setState(FrontLightMode state) {
        this.state = state;

        switch (state) {
            case ON:
                setFlashImageResource(R.drawable.flash_on);
                cameraManager.setTorch(true);
                break;
            case OFF:
                setFlashImageResource(R.drawable.flash_off);
                cameraManager.setTorch(false);
                break;
            case AUTO:
                setFlashImageResource(R.drawable.flash_auto);
                break;
        }

        saveState();
    }

    private void setFlashImageResource(int resourceId) {
        imageButton.setImageResource(resourceId);
    }

    private void saveState() {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(PreferencesActivity.KEY_FRONT_LIGHT_MODE, state.toString());
        editor.commit();
    }

    @Override
    public void onClick(View v) {
        switchState();
    }

    private void switchState() {
        switch (state) {
            case ON:
                setState(FrontLightMode.OFF);
                break;
            case OFF:
                setState(FrontLightMode.ON);
                break;
            case AUTO:
                setState(FrontLightMode.OFF);
                break;
        }
    }

}
