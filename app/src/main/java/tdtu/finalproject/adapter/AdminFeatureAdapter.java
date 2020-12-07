package tdtu.finalproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import tdtu.finalproject.R;
import tdtu.finalproject.activity.ManageCoupon;
import tdtu.finalproject.activity.ManageProduct;
import tdtu.finalproject.model.Feature;

public class AdminFeatureAdapter  extends RecyclerView.Adapter<AdminFeatureAdapter.FeatureViewHolder>{
    private ArrayList<Feature> features;
    private Context context;

    public AdminFeatureAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<Feature> data){
        this.features = data;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public FeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_feature, parent, false);
        return new FeatureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FeatureViewHolder holder, int position) {
        final Feature feature = features.get(position);
        if(feature == null){
            return;
        }
        holder.title.setText(feature.getName());
        if(feature.getName().equals("Sản phẩm")){
            holder.img.setImageResource(R.drawable.ic_product);
        }else if(feature.getName().equals("Coupon")){
            holder.img.setImageResource(R.drawable.ic_coupon);
        }else if(feature.getName().equals("Người dùng")){
            holder.img.setImageResource(R.drawable.ic_user);
        }else if(feature.getName().equals("Đơn hàng")){
            holder.img.setImageResource(R.drawable.ic_order);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(feature.getName() == "Sản phẩm"){
                    Intent intent = new Intent(context, ManageProduct.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }else if(feature.getName() == "Coupon"){
                    Intent intent = new Intent(context, ManageCoupon.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(features != null){
            return features.size();
        }
        return 0;
    }

    public class FeatureViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView title;

        public FeatureViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.feature_icon);
            title = itemView.findViewById(R.id.feature_name);
        }
    }
}
