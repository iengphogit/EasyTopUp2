package ocr.avaboy.com;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CAMERA_CODE = 203;
    private LinearLayout rootView;
    private SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initDB();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    private void initDB() {

        sqLiteHelper = new SQLiteHelper(this, "phieDB", null, 1);
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS tbl_company(Id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR NOT NULL UNIQUE,desc VARCHAR,imie_start VARCHAR,imie_end VARCHAR,imie_limit INTEGER, image BLOG)");
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS tbl_company_detail(Id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR, service_num VARCHAR,company_id INTEGER)");
        Cursor cs = sqLiteHelper.getData("SELECT Id FROM tbl_company WHERE Id=1 OR Id=2 OR Id=3;");

        Drawable cellcard = getResources().getDrawable(R.drawable.cellcard);
        Drawable smart = getResources().getDrawable(R.drawable.smart);
        Drawable metfone = getResources().getDrawable(R.drawable.metfone);

        if (!(cs.getCount() > 0)) {
            sqLiteHelper.insertData("Metfone", "Prefixes: 088, 097", "*197*", "#", 14, imageToByte(drawableToBitmap(metfone)));
            sqLiteHelper.insertData("Smart", "Prefixes: 010, 069, 070, 086, 093, 098, 096, 015, 016, 081, 087", "*888*", "#", 14, imageToByte(drawableToBitmap(smart)));
            sqLiteHelper.insertData("Cellcard", "Prefixes: 012, 017, 077, 078, 089, 092, 095, 011, 076, 085, 099, 061, 036, 014", "*123*", "#", 14, imageToByte(drawableToBitmap(cellcard)));
        }

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

    private byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    private byte[] imageToByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        return ((BitmapDrawable) drawable).getBitmap();
    }
}
