package ocr.avaboy.com.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ocr.avaboy.com.CRUDItemActivity;
import ocr.avaboy.com.model.Company;
import ocr.avaboy.com.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    private ArrayList<Company> companies = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(ArrayList<Company> companyArrayList, Context mContext) {
        this.companies = companyArrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item, parent, false);
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
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( holder.getAdapterPosition() == 0) {
/*
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Title");
                    final EditText input = new EditText(mContext);
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    builder.setView(input);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //m_Text = input.getText().toString();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();

                    */

                    Intent crudActivity = new Intent(mContext, CRUDItemActivity.class);
                    mContext.startActivity(crudActivity);

                } else {
                    Toast.makeText(mContext, "Hahah", Toast.LENGTH_SHORT).show();

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
        View view;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            image = itemView.findViewById(R.id.image_view);
            name = itemView.findViewById(R.id.name_view);
        }
    }
}
