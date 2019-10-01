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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Activity.Register;
import managment.protege.supermed.Fragment.Cart;
import managment.protege.supermed.Model.CartActionResponse;
import managment.protege.supermed.Model.CartModel;
import managment.protege.supermed.Model.cartList;
import managment.protege.supermed.R;
import managment.protege.supermed.Retrofit.API;
import managment.protege.supermed.Retrofit.RetrofitAdapter;
import managment.protege.supermed.Tools.GlobalHelper;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by wahaj on 6/12/2018.
 */

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.MyViewHolder> {

    List<CartModel> catalist;
    Context context;
    double total = 0, coupon_price = 0;

    public cartAdapter(List<CartModel> catalist, Context context) {
        this.catalist = catalist;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final CartModel item = catalist.get(position);

        Picasso.get()
                .load(item.getProductImage().replaceAll(" ", "%20"))
                .resize(80, 80)
                .centerCrop()
                .placeholder(R.drawable.tab_miss)
                .into(holder.iv);

        holder.title.setText(item.getProductName());
        holder.Price.setText(item.getProductPrice());
        holder.cartProductQuantity.setText(item.getProductQty());

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double qty = Double.parseDouble(holder.cartProductQuantity.getText().toString());
                double price = Double.parseDouble(holder.Price.getText().toString());

                //Getting old price
                double sub_total1 = price * qty;

                qty = qty + 1;
                holder.cartProductQuantity.setText(String.format("%.0f",qty));
                updateCart(item.getProductID(),holder.cartProductQuantity.getText().toString());

                //Getting new price
                double sub_total = price * qty;

                double total = Double.parseDouble(Cart.tv_price.getText().toString());
                double f_total = (total - sub_total1) + sub_total;
                Cart.tv_price.setText(String.format("%.2f",f_total));
                Cart.tv_total.setText(String.format("%.2f",f_total));
            }
        });

        holder.min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double qty = Double.parseDouble(holder.cartProductQuantity.getText().toString());
                double price = Double.parseDouble(holder.Price.getText().toString());
                double total = Double.parseDouble(Cart.tv_price.getText().toString());

                if (qty == 1 || qty < 1){
                    //Delete item
                    deleteCart(item.getCartID(), qty, price, total);
                    catalist.remove(position);
                    Cart.adapter.notifyDataSetChanged();
                } else if (qty > 1){
                    //Getting old price
                    double sub_total1 = price * qty;

                    qty = qty - 1;
                    holder.cartProductQuantity.setText(String.format("%.0f",qty));
                    updateCart(item.getProductID(),holder.cartProductQuantity.getText().toString());

                    //Getting new price
                    double sub_total = price * qty;

                    double f_total = (total - sub_total1) + sub_total;
                    Cart.tv_price.setText(String.format("%.2f",f_total));
                    Cart.tv_total.setText(String.format("%.2f",f_total));
                }
            }
        });

        double price = Double.parseDouble(holder.Price.getText().toString());
        double qty = Double.parseDouble(holder.cartProductQuantity.getText().toString());
        double sub_total = price * qty;
        total += sub_total;
        Cart.tv_price.setText(String.format("%.2f",total));
        Cart.tv_total.setText(String.format("%.2f",total));
    }

    @Override
    public int getItemCount() {
        return catalist.size();
    }

    public void updateCart(final String productId, final String qty){
        Main_Apps.hud.show();
        String URL = Register.Base_URL + "update-cart";
        StringRequest req = new StringRequest(Request.Method.POST, URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("status");
                            String msg = jsonObject.getString("msg");
                            if (status){
                                Main_Apps.hud.dismiss();
                                /*Toast.makeText(context, msg,
                                        Toast.LENGTH_LONG).show();*/
                            } else {
                                Main_Apps.hud.dismiss();
                                Toast.makeText(context, msg,
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Main_Apps.hud.dismiss();
                        Toast.makeText(context, error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("productId", productId);
                map.put("productQTY", qty);
                map.put("userId", GlobalHelper.getUserProfile(context).getProfile().getId());
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(req);
    }

    public void deleteCart(final String cartId, final double qty, final double price, final double total){
        Main_Apps.hud.show();
        String URL = Register.Base_URL + "delete-cart/" + cartId;
        StringRequest req = new StringRequest(Request.Method.DELETE, URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("status");
                            String msg = jsonObject.getString("message");
                            if (status){
                                Main_Apps.hud.dismiss();

                                double sub_total1 = price * qty;

                                double f_total = total - sub_total1;
                                Cart.tv_price.setText(String.format("%.2f",f_total));
                                Cart.tv_total.setText(String.format("%.2f",f_total));

                                Toast.makeText(context, msg,
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Main_Apps.hud.dismiss();
                                Toast.makeText(context, msg,
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Main_Apps.hud.dismiss();
                        Toast.makeText(context, error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(req);
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
