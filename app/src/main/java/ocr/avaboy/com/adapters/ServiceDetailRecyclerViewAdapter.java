package ocr.avaboy.com.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ocr.avaboy.com.CRUDItemActivity;
import ocr.avaboy.com.fragment.ServiceFragment;
import ocr.avaboy.com.model.Company;
import ocr.avaboy.com.HomeActivity;
import ocr.avaboy.com.MainActivity;
import ocr.avaboy.com.R;
import ocr.avaboy.com.data.Singleton;
import ocr.avaboy.com.model.ServiceDetail;

public class ServiceDetailRecyclerViewAdapter extends RecyclerView.Adapter<ServiceDetailRecyclerViewAdapter.ViewHolder> {


    private ArrayList<ServiceDetail> serviceDetails = new ArrayList<>();
    private Context mContext;

    public ServiceDetailRecyclerViewAdapter(ArrayList<ServiceDetail> serviceDetails, Context mContext) {
        this.serviceDetails = serviceDetails;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final ServiceDetail serviceDetail = serviceDetails.get(holder.getAdapterPosition());
        holder.numTxv.setText(serviceDetail.getCardinalNumber());
        holder.titleTxv.setText(serviceDetail.getName());
        holder.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = ((MainActivity)mContext);
                ServiceFragment serviceFragment = (ServiceFragment) mainActivity.baseFragment;
                serviceFragment.currentService = serviceDetail;
                serviceFragment.sendSmsIntent(serviceDetail.getServiceNum());
            }
        });

        holder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = ((MainActivity)mContext);
                ServiceFragment serviceFragment = (ServiceFragment) mainActivity.baseFragment;
                serviceFragment.currentService = serviceDetail;
                serviceFragment.callPhoneIntent(serviceDetail.getServiceNum());
            }
        });

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)mContext).registerForContextMenu(holder.view);
                ((MainActivity)mContext).openContextMenu(holder.view);
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Log.i("my", "onLongClick: ");
                v.setTag(holder.getAdapterPosition());
                ((MainActivity)mContext).registerForContextMenu(holder.view);
                ((MainActivity)mContext).openContextMenu(holder.view);



                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceDetails.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView callBtn;
        ImageView sendBtn;
        TextView titleTxv;
        TextView numTxv;
        View view;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            callBtn = itemView.findViewById(R.id.list_item_detail_call_btn);
            sendBtn = itemView.findViewById(R.id.list_item_detail_send_btn);
            titleTxv = itemView.findViewById(R.id.list_item_detail_title);
            numTxv = itemView.findViewById(R.id.list_item_detail_cardinal_number);
        }
    }
}
