package ocr.avaboy.com;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class CRUDItemActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nameEdt, descEdt, imieStartEdt, imieEndEdt, imieLenghtEdt;
    private Button saveBtn;
    private CircleImageView image;
    private Uri uri;


    final int REQUEST_CODE_GALLERY = 999;
    private SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cruditem);

        if (getSupportActionBar() != null){
            View actionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar,null, false);
            getSupportActionBar().setCustomView(actionBar);
            getSupportActionBar().setDisplayShowCustomEnabled(true);

        }

        sqLiteHelper = new SQLiteHelper(this,"phieDB",null,1);

        nameEdt = findViewById(R.id.crud_name);
        descEdt = findViewById(R.id.crud_description);
        imieStartEdt = findViewById(R.id.crud_imie_start);
        imieEndEdt = findViewById(R.id.crud_imie_end);
        imieLenghtEdt = findViewById(R.id.crud_imie_length);

        saveBtn = findViewById(R.id.crud_save);
        saveBtn.setOnClickListener(this);
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

            case R.id.crud_save:
                doSubmitSaveItem();
                break;

        }
    }


    private void doSubmitSaveItem() {

        if(nameEdt.getText().toString().equals("")){
            nameEdt.setError("Please input company name.");
        }else if(imieStartEdt.getText().toString().equals("")){
            imieStartEdt.setError("Please input imie start.");
        }else{

            byte[] imgArr = null;
            try {

                if( uri == null){
                    imgArr = Util.getBytes(getResources().getDrawable(R.mipmap.ic_launcher));
                }else{
                    imgArr = Util.uriToByteArray(this, uri);
                }

                int imieLength = imieLenghtEdt.getText().toString().length() > 0? Integer.parseInt(imieLenghtEdt.getText().toString()): Config.imieLength;
                sqLiteHelper.insertData(
                        nameEdt.getText().toString(),
                        descEdt.getText().toString(),
                        imieStartEdt.getText().toString(),
                        imieEndEdt.getText().toString().isEmpty()? Config.imieEnd: imieEndEdt.getText().toString(),
                        imieLength,
                        imgArr);
                Toast.makeText(this,getResources().getString(R.string.add_success),Toast.LENGTH_LONG).show();
                finish();
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.add_not_success),Toast.LENGTH_SHORT).show();
            }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            if (uri != null) {
                try {
                    InputStream inputStream;
                    inputStream = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    image.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



}
