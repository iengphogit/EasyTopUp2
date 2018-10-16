package ocr.avaboy.com;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.ArrayList;

import ocr.avaboy.com.adapters.RecyclerViewAdapter;
import ocr.avaboy.com.model.Company;

public class OcrActivity extends AppCompatActivity {
    private static final String TAG = "OcrActivity";
    private RecyclerViewAdapter recyclerViewAdapter;
    private SurfaceView mCameraView;
    private CameraSource mCameraSource;
    private TextView mTextView;
    private ImageButton cameraCtlr;

    private ArrayList<Company> companies = new ArrayList<>();
    private boolean isPlay;
    private SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);


        if (getSupportActionBar() != null){
            View actionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar,null, false);
            getSupportActionBar().setCustomView(actionBar);
            getSupportActionBar().setDisplayShowCustomEnabled(true);

        }

        sqLiteHelper = new SQLiteHelper(this, Config.dbName2, null, 1);

        mCameraView = findViewById(R.id.surfaceView);
        mTextView = findViewById(R.id.text_view);
        cameraCtlr = findViewById(R.id.camera_controller);

        cameraCtlr.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                if (mCameraSource != null) {

                    if (isPlay) {
                        isPlay = false;
                        mCameraSource.stop();
                        mCameraView.clearAnimation();
                        cameraCtlr.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_circle_outline_white_36dp));
                    } else {
                        isPlay = true;
                        try {
                            mCameraSource.start(mCameraView.getHolder());
                            cameraCtlr.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_circle_outline_white_36dp));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }


                }
            }
        });

        startCameraSource();
        initImageBitmap();
        initRecyclerView();

    }

    private void initImageBitmap() {
        companies.clear();
        Bitmap newItem = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_add_circle_outline_grey_500_36dp);
        Company company = new Company();
        company.setName("New");
        company.setImage(Util.bitmapToByte(newItem));
        companies.add(company);

        Cursor cursor = sqLiteHelper.getData("SELECT *FROM tbl_company order by id desc");

        if (cursor != null && cursor.getCount() > 0) {

            if (cursor.moveToFirst()) {
                do {

                    String name = cursor.getString(1);
                    String desc = cursor.getString(2);
                    byte[] image = cursor.getBlob(6);
                    Company cpn = new Company();
                    cpn.setName(name);
                    cpn.setDesc(desc);
                    cpn.setImage(image);
                    companies.add(cpn);

                } while (cursor.moveToNext());
            }

        }

        notifyDataSetChanged();
    }


    @Override
    protected void onStart() {
        super.onStart();
        isPlay = true;
        cameraCtlr.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_circle_outline_white_36dp));
        initImageBitmap();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter(companies, this);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void notifyDataSetChanged() {
        if (recyclerViewAdapter != null) {
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }


    private String[] txtStr = null;
    private String imieNumber = null;

    private void startCameraSource() {

        //Create the TextRecognizer
        final TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!textRecognizer.isOperational()) {
            Toast.makeText(this, "Detector dependencies not loaded yet", Toast.LENGTH_SHORT).show();
        } else {

            //Initialize camerasource to use high resolution and set Autofocus on.
            mCameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(2.0f)
                    .build();

            /**
             * Add call back to SurfaceView and check if camera permission is granted.
             * If permission is granted we can start our cameraSource and pass it to surfaceView
             */
            mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(OcrActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    202);
                            return;
                        }
                        mCameraSource.start(mCameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                /**
                 * Release resources for cameraSource
                 */
                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    mCameraSource.stop();
                }
            });

            //Set the TextRecognizer's Processor.
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {
                }

                /**
                 * Detect all the text from camera using TextBlock and the values into a stringBuilder
                 * which will then be set to the textView.
                 * */
                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (items.size() != 0) {
                        mTextView.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 0; i < items.size(); i++) {
                                    TextBlock item = items.valueAt(i);
                                    String str = item.getValue().replace(" ", "");
                                    str = str.trim();
                                    stringBuilder.append(str);
                                    stringBuilder.append("\n");
                                    txtStr = stringBuilder.toString().split("\\r?\\n");

                                    if ((txtStr[i].length()) == 15) {
                                        if (txtStr[i].matches("[0-9]+")) {
                                            MediaPlayer mediaPlayer = MediaPlayer.create(OcrActivity.this, R.raw.buttonclick);
                                            mediaPlayer.start();
                                            isPlay = false;
                                            mTextView.setText(txtStr[i]);
                                            imieNumber = txtStr[i];
                                            mCameraSource.stop();
                                            break;
                                        } else {
                                            isPlay = true;
                                        }
                                    }

                                }

                            }
                        });
                    }
                }
            });
        }
    }

}
