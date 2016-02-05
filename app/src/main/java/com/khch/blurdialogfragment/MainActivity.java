package com.khch.blurdialogfragment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_show_blur_dialog_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBlurDialogFragment();
            }
        });
    }

    private void showBlurDialogFragment() {
        BlurDialogFragment blurDialogFragment = new BlurDialogFragment();
        blurDialogFragment.showFragment(this, "BlurDialogFragment");
    }
}
