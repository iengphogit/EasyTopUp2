package ocr.avaboy.com;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<Bitmap> images = new ArrayList<>();
    private Context mContext;

    RecyclerViewAdapter(ArrayList<String> names, ArrayList<Bitmap> images, Context mContext) {
        this.names = names;
        this.images = images;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.image.setImageBitmap(images.get(position));
        holder.name.setText(names.get(position));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"Hahah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
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
