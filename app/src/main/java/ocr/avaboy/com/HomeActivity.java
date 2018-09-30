package ocr.avaboy.com;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import ocr.avaboy.com.adapters.CompanyRecyclerViewAdapter;
import ocr.avaboy.com.data.Singleton;

public class HomeActivity extends AppCompatActivity {
    private SQLiteHelper sqLiteHelper;
    private CompanyRecyclerViewAdapter companyRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Company> companyList = new ArrayList<>();
    public static final int CRUD_REQUEST_CODE_FOR_RESULT = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        if (getSupportActionBar() != null) {
            View actionBar = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null, false);
            getSupportActionBar().setCustomView(actionBar);
            getSupportActionBar().setDisplayShowCustomEnabled(true);

        }

        sqLiteHelper = new SQLiteHelper(this, "phieDB", null, 1);

        recyclerView = findViewById(R.id.home_recycler_view);
        companyRecyclerViewAdapter = new CompanyRecyclerViewAdapter(companyList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(companyRecyclerViewAdapter);
        initDB();
        initImageBitmap();

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

    private void initImageBitmap() {
        companyList.clear();
        Bitmap newItem = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_add_circle_outline_grey_500_36dp);
        Company company = new Company();
        company.setName("New");
        company.setImage(Util.bitmapToByte(newItem));
        companyList.add(company);

        Cursor cursor = sqLiteHelper.getData("SELECT *FROM tbl_company order by id desc");

        if (cursor != null && cursor.getCount() > 0) {

            if (cursor.moveToFirst()) {
                do {

                    String name = cursor.getString(1);
                    String desc = cursor.getString(2);
                    String imieS = cursor.getString(3);
                    String imieE = cursor.getString(4);
                    int imieLength = cursor.getInt(5);
                    byte[] image = cursor.getBlob(6);
                    Company cpn = new Company();
                    cpn.setName(name);
                    cpn.setDesc(desc);
                    cpn.setImieStart(imieS);
                    cpn.setImieEnd(imieE);
                    cpn.setImieLength(imieLength);
                    cpn.setImage(image);
                    companyList.add(cpn);

                } while (cursor.moveToNext());
            }

        }
        Singleton singleton = Singleton.getInstance();
        singleton.setCompanyArrayList(companyList);
        companyRecyclerViewAdapter.notifyDataSetChanged();
    }

    private byte[] imageToByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        return ((BitmapDrawable) drawable).getBitmap();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CRUD_REQUEST_CODE_FOR_RESULT:
                initImageBitmap();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);

        }
    }
}
