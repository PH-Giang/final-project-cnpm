package tdtu.finalproject.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import tdtu.finalproject.R;
import tdtu.finalproject.activity.Details;
import tdtu.finalproject.activity.MainActivity;
import tdtu.finalproject.activity.MyCart;
import tdtu.finalproject.model.Cart;
import tdtu.finalproject.model.Category;

public class CartAdapter extends BaseAdapter {
    public ArrayList<Cart> carts;
    Context context;

    public CartAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<Cart> list){
        this.carts = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return carts.size();
    }

    @Override
    public Object getItem(int i) {
        return carts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.row_item_cart, null);
            viewHolder.img = view.findViewById(R.id.img_cart);
            viewHolder.name = view.findViewById(R.id.tv_name_cart);
            viewHolder.price = view.findViewById(R.id.tv_price_cart);
            viewHolder.quantity = view.findViewById(R.id.tv_quantity_cart);
            viewHolder.delete = view.findViewById(R.id.delete_item);
            view.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        final Cart cart = carts.get(i);
        viewHolder.name.setText(cart.getName());
        Picasso.with(context).load(cart.getImage()).into(viewHolder.img);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.price.setText(decimalFormat.format(cart.getPrice()) + " Đ");
        viewHolder.quantity.setText(Integer.toString(cart.getQuantity()));
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder b = new AlertDialog.Builder(context);
                b.setTitle("Xác nhận");
                b.setMessage("Xóa sản phẩm ra khỏi giỏ hàng ?");
                b.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int a) {
                        int firstPrice = Integer.parseInt(MyCart.tv_first_price.getText().toString());
                        int totalPrice = Integer.parseInt(MyCart.tv_total.getText().toString());
                        MyCart.tv_first_price.setText(Integer.toString(firstPrice - cart.getPrice()));
                        if(totalPrice - cart.getPrice() < 0){
                            MyCart.tv_total.setText(Integer.toString(0));
                        }else{
                            MyCart.tv_total.setText(Integer.toString(totalPrice - cart.getPrice()));
                        }
                        carts.remove(i);
                        notifyDataSetChanged();
                    }
                });
                b.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog al = b.create();
                al.show();
            }
        });
        return view;
    }

    public class ViewHolder{
        ImageView img, delete;
        TextView name, price, quantity;
    }
}
