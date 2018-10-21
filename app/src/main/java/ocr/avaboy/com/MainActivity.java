package ocr.avaboy.com;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import ocr.avaboy.com.data.Singleton;
import ocr.avaboy.com.fragment.BaseFragment;
import ocr.avaboy.com.fragment.ServiceFragment;
import ocr.avaboy.com.fragment.TopUpFragment;
import ocr.avaboy.com.model.Company;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ConstraintLayout rootView;
    private BottomNavigationView bottomNavigationView;
    private ImageView optionMenu;
    private SQLiteHelper sqLiteHelper;

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
            optionMenu = actionBar.findViewById(R.id.option_menu);

            optionMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popUpAddUpdateService(false, null, null, -1);
                }
            });

        }
        sqLiteHelper = new SQLiteHelper(this, Config.dbName2, null, 1);

    }

    private void popUpAddUpdateService(final boolean isEdit, String serviceName, String serviceNum, final int id) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new service");
        final EditText serviceNameEdt = new EditText(this);
        final EditText serviceNumberEdt = new EditText(this);


        final Company company = Singleton.getInstance().getCurrentCompany();

        if (isEdit) {
            serviceNameEdt.setText(serviceName != null ? serviceName : "");
            serviceNumberEdt.setText(serviceNum != null ? serviceNum : "");
        }

        serviceNameEdt.setHint("Name");
        serviceNumberEdt.setHint("Service numbers");

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        LinearLayout.LayoutParams lpEdt = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        lpEdt.setMargins(8, 8, 8, 0);

        serviceNameEdt.setLayoutParams(lpEdt);
        serviceNumberEdt.setLayoutParams(lpEdt);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(lp);

        linearLayout.addView(serviceNameEdt);
        linearLayout.addView(serviceNumberEdt);

        builder.setView(linearLayout);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isEdit) {
                    int check = sqLiteHelper.updateDataDetail(
                            serviceNameEdt.getText().toString(),
                            serviceNumberEdt.getText().toString(),
                            String.valueOf(id)
                    );
                    if (check > 0) {
                        ((ServiceFragment) baseFragment).getData();
                        Toast.makeText(MainActivity.this, "Update Success!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(MainActivity.this, "Update Failed!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        sqLiteHelper.insertDataDetail(
                                serviceNameEdt.getText().toString(),
                                serviceNumberEdt.getText().toString(),
                                company.getId()
                        );
                        ((ServiceFragment) baseFragment).getData();
                        dialog.dismiss();

                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
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
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case 0:
                //Edit
                popUpAddUpdateService(true,
                        ((ServiceFragment) baseFragment).currentService.getName(),
                        ((ServiceFragment) baseFragment).currentService.getServiceNum(),
                        ((ServiceFragment) baseFragment).currentService.getId()
                );
                return true;
            case 1:
                //Delete
                confirmAlert();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private void confirmAlert(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure to delete?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String idStr =  String.valueOf(((ServiceFragment) baseFragment).currentService.getId());
                int check = sqLiteHelper.deleteDataDetailById(idStr);
                if(check > 0){
                    ((ServiceFragment) baseFragment).getData();
                    Toast.makeText(MainActivity.this,"Delete Success!",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }else{
                    Toast.makeText(MainActivity.this,"Delete Failed!",Toast.LENGTH_SHORT).show();
                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
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

            case TopUpFragment.REQUEST_CALL_PHONE_CODE:
                ((TopUpFragment) baseFragment).callPhoneIntent();
                break;

            case ServiceFragment.REQUEST_SEND_SMS_CODE:
                ((ServiceFragment) baseFragment).sendSmsIntent(((ServiceFragment) baseFragment).currentService.getServiceNum());
                break;

            case ServiceFragment.REQUEST_CALL_PHONE_CODE:
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
                if (optionMenu != null)
                    optionMenu.setVisibility(View.GONE);
                return true;
            case R.id.menu_navi_services:
                baseFragment = ServiceFragment.newInstance();
                setFragement(baseFragment);
                if (optionMenu != null)
                    optionMenu.setVisibility(View.VISIBLE);
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
