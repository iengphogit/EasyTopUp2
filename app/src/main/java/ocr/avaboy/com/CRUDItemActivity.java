package ocr.avaboy.com;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class CRUDItemActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nameEdt, descEdt, imieStartEdt, imieEndEdt, imieLenghtEdt;
    private Button saveBtn;
    private CircleImageView image;


    final int REQUEST_CODE_GALLERY = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cruditem);

        nameEdt = findViewById(R.id.crud_name);
        descEdt = findViewById(R.id.crud_description);
        imieStartEdt = findViewById(R.id.crud_imie_start);
        imieEndEdt = findViewById(R.id.crud_imie_end);
        imieLenghtEdt = findViewById(R.id.crud_imie_length);

        saveBtn = findViewById(R.id.crud_save);
        image = findViewById(R.id.crud_image);
        image.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            case R.id.crud_image:
                startPickImageFromGallery();
                break;

        }
    }

    private void startPickImageFromGallery() {
        if (checkWriteExternalPermission()) {
            startIntentPickImage();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startIntentPickImage();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_permission_msg), Toast.LENGTH_SHORT).show();
            }

            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean checkWriteExternalPermission() {
        String permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private void startIntentPickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

}
