package ocr.avaboy.com.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import ocr.avaboy.com.Config;
import ocr.avaboy.com.R;
import ocr.avaboy.com.SQLiteHelper;
import ocr.avaboy.com.adapters.ServiceDetailRecyclerViewAdapter;
import ocr.avaboy.com.data.Singleton;
import ocr.avaboy.com.model.ServiceDetail;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceFragment extends BaseFragment {

    private static BaseFragment fragment;
    private RecyclerView detailRecyclerView;
    private ArrayList<ServiceDetail> serviceDetails;
    private ServiceDetailRecyclerViewAdapter adapter;
    private SQLiteHelper sqLiteHelper;
    private Context mContext;
    public ServiceDetail currentService = null;

    public static BaseFragment newInstance() {
        Bundle args = new Bundle();
        fragment = new ServiceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mContext = inflater.getContext();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_service, container, false);

        sqLiteHelper = new SQLiteHelper(inflater.getContext(), Config.currentDBName, null, 1);
        initData();
        detailRecyclerView = view.findViewById(R.id.service_recycler_view);
        detailRecyclerView.setItemAnimator(new DefaultItemAnimator());
        detailRecyclerView.addItemDecoration(new DividerItemDecoration(inflater.getContext(), LinearLayoutManager.VERTICAL));
        serviceDetails = new ArrayList<>();
        adapter = new ServiceDetailRecyclerViewAdapter(serviceDetails, inflater.getContext());
        detailRecyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        detailRecyclerView.setAdapter(adapter);
        getData();
        return view;
    }


    public void getData() {
        String strId = String.valueOf(Singleton.getInstance().getCurrentCompany().getId());
        Cursor cursor1 = sqLiteHelper.getData("SELECT *FROM tbl_company_detail WHERE company_id=" + strId);
        int c = 1;
        serviceDetails.clear();
        while (cursor1.moveToNext()) {
            ServiceDetail serviceDetail = new ServiceDetail();
            NumberFormat f = new DecimalFormat("00");
            serviceDetail.setCardinalNumber(f.format(c));
            serviceDetail.setId(cursor1.getInt(0));
            serviceDetail.setName(cursor1.getString(1));
            serviceDetail.setServiceNum(cursor1.getString(2));
            serviceDetails.add(serviceDetail);
            c++;
        }
        adapter.notifyDataSetChanged();

    }


    private void initData() {

        final Cursor cursor = sqLiteHelper.getData("SELECT *FROM tbl_company_detail WHERE company_id=1");
        cursor.moveToNext();

        if (!(cursor.getCount() > 0)) {
            //Cellcard id 3
            sqLiteHelper.insertDataDetail("Check Balance (#124#)", "#124#", 3);
            sqLiteHelper.insertDataDetail("Check iNet Balance (#823#)", "#823#", 3);
            sqLiteHelper.insertDataDetail("Osja Xchange $1-$100 (#168#100#)", "#168#100#", 3);
            sqLiteHelper.insertDataDetail("Osja 10c to $2.50 (#8383#1#)", "#8383#1#", 3);
            sqLiteHelper.insertDataDetail("XG $1.5 to $35 (#8282#15#)", "#8282#15#", 3);
            sqLiteHelper.insertDataDetail("XG $3 to $50 (#8282#30#)", "#8282#30#", 3);
            sqLiteHelper.insertDataDetail("XG $4.5 to $75 (#8282#45#)", "#8282#45#", 3);
            sqLiteHelper.insertDataDetail("XG $6 to $200 (#8282#60#)", "#8282#60#", 3);

            sqLiteHelper.insertDataDetail("Check My Number (*1#)", "*1#", 3);
            sqLiteHelper.insertDataDetail("Check My Number (#1#)", "#1#", 3);
            sqLiteHelper.insertDataDetail("Check My Number (#2#)", "#2#", 3);
            sqLiteHelper.insertDataDetail("Call Center (812)", "812", 3);

            //Smart
            sqLiteHelper.insertDataDetail("Check internal balance (*656*0#)", "*656*0#", 2);
            sqLiteHelper.insertDataDetail("Check external balance (*888#)", "*888#", 2);
            sqLiteHelper.insertDataDetail("Smart Loan $0.50 (*911#)", "*911#", 2);
            sqLiteHelper.insertDataDetail("Exchange $1 (*656*10#)", "*656*10#", 2);
            sqLiteHelper.insertDataDetail("Exchange $3 (*656*20#)", "*656*20#", 2);
            sqLiteHelper.insertDataDetail("Exchange $8 (*656*50#)", "*656*50#", 2);
            sqLiteHelper.insertDataDetail("Exchange $30 (*656*100#)", "*656*100#", 2);
            sqLiteHelper.insertDataDetail("Exchange $60 (*656*200#)", "*656*200#", 2);

            sqLiteHelper.insertDataDetail("TalkLikeCrazy $2\n (120min/day) (*015*200#)", "*015*200#", 2);
            sqLiteHelper.insertDataDetail("TalkLikeCrazy $3.5\n (150min/day) (*015*350#)", "*015*350#", 2);
            sqLiteHelper.insertDataDetail("To Unsubscribe TalkLikeCrazy (*015*0#)", "*015*0#", 2);

            sqLiteHelper.insertDataDetail("Check My Number (*887#)", "*887#", 2);
            sqLiteHelper.insertDataDetail("Call Center (888)", "888", 2);

            //Metfone
            sqLiteHelper.insertDataDetail("Check Balance (*097#)", "*097#", 1);
            sqLiteHelper.insertDataDetail("Check Exchange Balance (*167*097#)", "*167*097#", 1);

            sqLiteHelper.insertDataDetail("Exchange 10cent to $1 (*167*10#)", "*167*10#", 1);
            sqLiteHelper.insertDataDetail("Exchange 20cent to $3 (*167*20#)", "*167*20#", 1);
            sqLiteHelper.insertDataDetail("Exchange 50cent to $9 (*167*50#)", "*167*50#", 1);
            sqLiteHelper.insertDataDetail("Exchange $1 to $30 (*167*100#)", "*167*100#", 1);
            sqLiteHelper.insertDataDetail("Exchange $2 to $60 (*167*200#)", "*167*200#", 1);

            sqLiteHelper.insertDataDetail("Check My Number (*99#)", "*99#", 1);
            sqLiteHelper.insertDataDetail("Call Center (1777)", "1777", 1);

        }
    }


    private Uri ussdToCallableUri(String ussd) {
        StringBuilder uriSting = new StringBuilder();
        if (!ussd.startsWith("tel:"))
            uriSting.append("tel:");

        for (char c : ussd.toCharArray()) {
            if (c == '#')
                uriSting.append(Uri.encode("#"));
            else
                uriSting.append(c);
        }
        return Uri.parse(uriSting.toString());
    }

    public void callPhoneIntent(String imieNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, ussdToCallableUri(imieNumber));
        this.confirmAlert(intent, "Dial number", "Are you sure to make a dial?");
    }

    public void sendMessageIntent(String body, Context context) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("sms_body", body);
        intent.setType("vnd.android-dir/mms-sms");
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
        }

    }

    private void confirmAlert(final Intent intent, String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(intent);
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


}
