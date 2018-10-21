package ocr.avaboy.com;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import ocr.avaboy.com.fragment.BaseFragment;
import ocr.avaboy.com.fragment.ServiceFragment;
import ocr.avaboy.com.fragment.TopUpFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ConstraintLayout rootView;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootView = findViewById(R.id.root_view);

        bottomNavigationView = findViewById(R.id.bottom_navi_menu);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.menu_navi_top_up);

        if (getSupportActionBar() != null) {
            View actionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null, false);
            getSupportActionBar().setCustomView(actionBar);
            getSupportActionBar().setDisplayShowCustomEnabled(true);

        }


    }

    int optionSelectPosition = -1;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        optionSelectPosition = (int) v.getTag();
        menu.setHeaderTitle("Option");
        menu.add(Menu.NONE, 0, Menu.NONE, "Edit");
        menu.add(Menu.NONE, 1, Menu.NONE, "Delete");

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case TopUpFragment.REQUEST_CAMERA_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ((TopUpFragment) baseFragment).startOcrCamera();
                } else {
                    snackAlert();
                }
                break;

            case TopUpFragment.REQUEST_SEND_SMS_CODE:
                ((TopUpFragment) baseFragment).sendSmsIntent();
                break;

            case  TopUpFragment.REQUEST_CALL_PHONE_CODE:
                ((TopUpFragment) baseFragment).callPhoneIntent();
                break;

            case ServiceFragment.REQUEST_SEND_SMS_CODE:
                ((ServiceFragment) baseFragment).sendSmsIntent(((ServiceFragment) baseFragment).currentService.getServiceNum());
                break;

            case  ServiceFragment.REQUEST_CALL_PHONE_CODE:
                ((ServiceFragment) baseFragment).callPhoneIntent(((ServiceFragment) baseFragment).currentService.getServiceNum());
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

    public BaseFragment baseFragment;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_navi_top_up:
                baseFragment = TopUpFragment.newInstance();
                setFragement(baseFragment);
                return true;
            case R.id.menu_navi_services:
                baseFragment = ServiceFragment.newInstance();
                setFragement(baseFragment);
                return true;
            default:
                return false;
        }

    }


    private void setFragement(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame_view, fragment);
        fragmentTransaction.commit();

    }
}
