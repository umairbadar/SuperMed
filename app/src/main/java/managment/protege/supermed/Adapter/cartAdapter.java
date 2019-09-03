package managment.protege.supermed.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import es.dmoral.toasty.Toasty;
import managment.protege.supermed.Fragment.Cart;
import managment.protege.supermed.Model.CartActionResponse;
import managment.protege.supermed.Model.CartModel;
import managment.protege.supermed.Model.cartList;
import managment.protege.supermed.R;
import managment.protege.supermed.Retrofit.API;
import managment.protege.supermed.Retrofit.RetrofitAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by wahaj on 6/12/2018.
 */

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.MyViewHolder> {

    Context ct;
    private OnItemClickListener onItemClickListeners;
    OnItemClickListenerplus OnItemClickListenerplus;

    public interface OnItemClickListener {
        void onItemClick(View view, CartModel obj, String text);
    }

    public interface OnItemClickListenerplus {
        void onItemClick(View view, CartModel obj, String text);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListeners = onItemClickListener;
    }

    public void setOnItemClickListenerplus(OnItemClickListenerplus onItemClickListenerplus) {
        this.OnItemClickListenerplus = onItemClickListenerplus;
    }

    public cartAdapter(List<CartModel> catalist, Context contex) {
        this.catalist = catalist;
        ct = contex;
    }

    private List<CartModel> catalist;

    public cartAdapter(Context context) {

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        final CartModel cata = catalist.get(position);
        holder.title.setText(cata.getProductName());
        holder.Price.setText("RS " + cata.getProductPrice());

        holder.Sale.setText("RS " + cata.getProductOldPrice());
        holder.Sale.setPaintFlags(holder.Sale.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.cartProductQuantity.setText(cata.getUserQty());
        holder.cartSingleProductQuantity.setText("Quantity Left: " + cata.getProductQty());
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.cartProductQuantity.setText(String.valueOf(Integer.parseInt(holder.cartProductQuantity.getText().toString()) + 1));
                if (OnItemClickListenerplus != null) {
                    OnItemClickListenerplus.onItemClick(view, cata, holder.cartProductQuantity.getText().toString());
                }
            }

        });
        holder.min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (holder.cartProductQuantity.getText().equals("1")) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ct);
                    builder1.setMessage(Html.fromHtml("Are you sure you want to remove <b>" + cata.getProductName() + "</b> from cart ? "));

                    builder1.setCancelable(true);
                    builder1.setTitle(Html.fromHtml("<b>" + "Remove from Cart" + "</b>"));
                    builder1.setPositiveButton(
                            "Remove",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    holder.cartProductQuantity.setText(String.valueOf(Integer.parseInt(holder.cartProductQuantity.getText().toString()) - 1));
                                    if (onItemClickListeners != null) {
                                        onItemClickListeners.onItemClick(view, cata, holder.cartProductQuantity.getText().toString());
                                    }
                                }
                            });
                    builder1.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                } else if (!holder.cartProductQuantity.getText().equals("1")) {
                    holder.cartProductQuantity.setText(String.valueOf(Integer.parseInt(holder.cartProductQuantity.getText().toString()) - 1));
                    if (onItemClickListeners != null) {
                        onItemClickListeners.onItemClick(view, cata, holder.cartProductQuantity.getText().toString());
                    }
                }
            }
        });
        Picasso.get()
                .load(cata.getProductImage().replaceAll(" ", "%20"))
                .resize(80, 80)
                .centerCrop()
                .placeholder(R.drawable.tab_miss)
                .into(holder.iv);
        Log.e("ds", cata.getProductPrice());
    }

    @Override
    public int getItemCount() {
        return catalist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, Price, Sale, cartProductQuantity, cartSingleProductQuantity;
        ImageView iv, min, plus;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.cartProductName);
            min = (ImageView) view.findViewById(R.id.min);
            plus = (ImageView) view.findViewById(R.id.plus);
            title = (TextView) view.findViewById(R.id.cartProductName);
            cartProductQuantity = (TextView) view.findViewById(R.id.cartProductQuantity);
            Price = (TextView) view.findViewById(R.id.cartProductPrice);
            Sale = (TextView) view.findViewById(R.id.cartProductSalePrice);
            iv = (ImageView) view.findViewById(R.id.cartProductImage);
            cartSingleProductQuantity = (TextView) view.findViewById(R.id.cartSingleProductQuantity);
        }
    }


}
