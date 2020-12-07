package tdtu.finalproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import tdtu.finalproject.R;
import tdtu.finalproject.activity.Details;
import tdtu.finalproject.activity.MyCart;
import tdtu.finalproject.model.Cart;
import tdtu.finalproject.model.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private ArrayList<Product> products;
    private Context context;

    public ProductAdapter( Context context) {
        this.context = context;
    }

    public void setData(ArrayList<Product> list){
        this.products = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        final Product product = products.get(position);
        if(product == null){
            return;
        }
        holder.name.setText(product.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.price.setText("Giá: " + decimalFormat.format(product.getPrice()) + " Đ");
        Picasso.with(context).load(product.getImage()).
                placeholder(R.drawable.cancel).
                error(R.drawable.warning).
                into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Details.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("ID_PRODUCT", product.getId());
                context.startActivity(intent);
            }
        });
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isExist = false;
                for(int i = 0; i < MyCart.carts.size(); i++){
                    if(product.getId() == MyCart.carts.get(i).getId()){
                        isExist = true;
                    }
                }
                if(!isExist){
                    MyCart.carts.add(new Cart(product.getId(),
                            product.getImage(),
                            product.getName(),
                            product.getPrice(),
                            1,
                            "S",
                            product.getPrice()));
                    Toast.makeText(context, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Bạn đã thêm sản phẩm này rồi", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        if(products != null){
            return products.size();
        }
        return 0;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{
        public ImageView image;
        public TextView name, price;
        public Button button;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img_product);
            name = itemView.findViewById(R.id.tv_product_name);
            price = itemView.findViewById(R.id.tv_product_price);
            button = itemView.findViewById(R.id.btn_add_to_cart);
        }
    }
}
