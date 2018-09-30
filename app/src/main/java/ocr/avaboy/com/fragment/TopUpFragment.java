package ocr.avaboy.com.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

import ocr.avaboy.com.Company;
import ocr.avaboy.com.R;
import ocr.avaboy.com.data.Singleton;
import ocr.avaboy.com.listener.CameraListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopUpFragment extends BaseFragment implements CameraListener {
    private static final String TAG = "myLog";
    private Context mContext;
    private SurfaceView mCameraView;
    private CameraSource mCameraSource;
    private boolean isPlay;
    private ImageButton cameraCtlr;
    private TextView mTextView;
    private final int REQUEST_CAMERA_CODE = 202;

    private Company cp = Singleton.getInstance().getCurrentCompany();
    private final String imieS = cp.getImieStart().isEmpty()?"N/A": cp.getImieStart();
    private final String imieE = cp.getImieEnd().isEmpty()?"#": cp.getImieEnd();
    private final int length = cp.getImieLength() == 0? 14: cp.getImieLength();
    private String[] txtStr = null;
    private String imieNumber;//number from scratch card.

    private static BaseFragment fragment;

    public static BaseFragment newInstance() {
        Bundle args = new Bundle();
        if (fragment == null) {
            fragment = new TopUpFragment();
        }
        fragment.setArguments(args);
        return fragment;
    }


    public TopUpFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle data) {
        mContext = container.getContext();
        View view = inflater.inflate(R.layout.fragment_top_up, container, false);

        Company company = Singleton.getInstance().getCurrentCompany();
        Log.i(TAG, "onCreateView: " + company.getName());

        mCameraView = view.findViewById(R.id.fgm_surface_view);
        mTextView = view.findViewById(R.id.fgm_text_view);
        cameraCtlr = view.findViewById(R.id.fgm_camera_controller);

        cameraCtlr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCameraSource != null) {

                    if (isPlay) {
                        stopOcrCamera();
                    } else {
                        startOcrCamera();
                    }

                }
            }
        });
        startCameraSource();
        return view;
    }

    @SuppressLint("MissingPermission")
    public void startOcrCamera() {
        isPlay = true;
        setDefaultDigits();
        try {
            mCameraSource.start(mCameraView.getHolder());
            cameraCtlr.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_circle_outline_white_36dp));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopOcrCamera() {
        isPlay = false;
        mCameraSource.stop();
        mCameraView.clearAnimation();
        cameraCtlr.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_circle_outline_white_36dp));
    }

    @SuppressLint("StringFormatMatches")
    private void setDefaultDigits(){
        mTextView.setText(String.format(getResources().getString(R.string.default_digits_str),length));
    }

    private void startCameraSource() {
        setDefaultDigits();
        //Create the TextRecognizer
        final TextRecognizer textRecognizer = new TextRecognizer.Builder(mContext.getApplicationContext()).build();

        if (!textRecognizer.isOperational()) {
            Toast.makeText(mContext, "Detector dependencies not loaded yet", Toast.LENGTH_SHORT).show();
        } else {

            //Initialize camerasource to use high resolution and set Autofocus on.
            mCameraSource = new CameraSource.Builder(mContext.getApplicationContext(), textRecognizer)
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

                        if (ActivityCompat.checkSelfPermission(mContext.getApplicationContext(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions((Activity) mContext,
                                    new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_CODE
                            );
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

                                    if ((txtStr[i].length()) == length) {
                                        if (txtStr[i].matches("[0-9]+")) {
                                            MediaPlayer mediaPlayer = MediaPlayer.create(mContext, R.raw.buttonclick);
                                            mediaPlayer.start();
                                            isPlay = false;
                                            mTextView.setText(String.format("%s%s%s",imieS,txtStr[i],imieE));
                                            imieNumber = txtStr[i];
                                            mCameraView.clearAnimation();
                                            stopOcrCamera();
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


    @Override
    public void onCameraGranted() {

    }
}
