package ocr.avaboy.com;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CAMERA_CODE = 203;
    private ConstraintLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (getSupportActionBar() != null){
            View actionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar,null, false);
            getSupportActionBar().setCustomView(actionBar);
            getSupportActionBar().setDisplayShowCustomEnabled(true);

        }

        rootView = findViewById(R.id.root_view);

        View takeVideoMenu = findViewById(R.id.take_video);
        takeVideoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHasCameraPermission()) {
                    startOcrIntent();
                } else {
                    requestCamera();
                }
            }
        });
    }

    private void startOcrIntent() {
        Intent intent = new Intent(this, OcrActivity.class);
        startActivity(intent);
    }


    private boolean isHasCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCamera() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startOcrIntent();
                } else {
                    snackAlert();
                }
                break;
        }
    }

    private void snackAlert() {
        Snackbar snackbar = Snackbar.make(rootView, "PERMISSION", Snackbar.LENGTH_LONG)
                .setAction("GO TO SETTING", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        snackbar.setActionTextColor(getResources().getColor(android.R.color.white));
        snackbar.show();
    }

}
