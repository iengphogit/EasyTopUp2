package ocr.avaboy.com.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ocr.avaboy.com.CRUDItemActivity;
import ocr.avaboy.com.Company;
import ocr.avaboy.com.HomeActivity;
import ocr.avaboy.com.MainActivity;
import ocr.avaboy.com.R;
import ocr.avaboy.com.data.Singleton;

public class CompanyRecyclerViewAdapter extends RecyclerView.Adapter<CompanyRecyclerViewAdapter.ViewHolder> {


    private ArrayList<Company> companies = new ArrayList<>();
    private Context mContext;

    public CompanyRecyclerViewAdapter(ArrayList<Company> companyArrayList, Context mContext) {
        this.companies = companyArrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_company_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Company company = companies.get(position);
        try{
            Bitmap bitmap = BitmapFactory.decodeByteArray(company.getImage(),0,company.getImage().length);
            holder.image.setImageBitmap(bitmap);
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }

        holder.name.setText(company.getName());
        if(company.getDesc() == null){
            holder.desc.setVisibility(View.GONE);
        }else{
            holder.desc.setText(company.getDesc());
        }
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( holder.getAdapterPosition() == 0) {
                    Intent crudActivity = new Intent(mContext, CRUDItemActivity.class);
                    ((Activity)mContext).startActivityForResult(crudActivity, HomeActivity.CRUD_REQUEST_CODE_FOR_RESULT);

                } else {

                    Singleton singleton = Singleton.getInstance();
                    singleton.setCurrentCompany(companies.get(holder.getAdapterPosition()));
                    Intent mainActivity = new Intent(mContext, MainActivity.class);
                    mContext.startActivity(mainActivity);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return companies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView name;
        TextView desc;
        ImageView addOn;
        View view;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            image = itemView.findViewById(R.id.company_image_view);
            name = itemView.findViewById(R.id.company_name_text_view);
            desc = itemView.findViewById(R.id.company_description_text_view);
            addOn = itemView.findViewById(R.id.company_add_on_text_view);
        }
    }
}
