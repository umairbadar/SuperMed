package managment.protege.supermed.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Activity.Register;
import managment.protege.supermed.Fragment.ProductDetail;
import managment.protege.supermed.Model.Model_Cat_Product;
import managment.protege.supermed.R;
import managment.protege.supermed.Tools.GlobalHelper;

public class Adapter_Cat_Product extends RecyclerView.Adapter<Adapter_Cat_Product.ViewHolder> {

    private List<Model_Cat_Product> list;
    Context context;

    public Adapter_Cat_Product(List<Model_Cat_Product> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cat_product, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Model_Cat_Product item = list.get(position);

        String ImageLink = item.getProductImage();
        if (!ImageLink.equals("")) {
            ImageLink = ImageLink.replaceAll(" ", "%20");
            Picasso.get()
                    .load(ImageLink)
                    .resize(80, 80)
                    .centerCrop()
                    .placeholder(R.drawable.tab_miss)
                    .into(holder.img);
        }

        holder.tv_name.setText(item.getProductName());
        holder.tv_subcatname.setText(item.getSubCatName());
        holder.tv_price.setText("RS " + item.getPrice());
        String price = item.getPrice();
        double p = Double.parseDouble(price) + 113.99;
        holder.tv_old_price.setText(String.format("Rs %.2f",p));
        holder.tv_old_price.setPaintFlags(holder.tv_old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if (item.getQty().equals("0")) {
            holder.btn_add_cart.setEnabled(false);
            holder.btn_add_cart.setText("OUT OF STOCK");
        }
        else if (item.getPrice().equals("0.00")) {
            holder.btn_add_cart.setEnabled(false);
            //holder.detail.setTextColor(context.getResources().getColor(R.color.cartCouponText));
            holder.btn_add_cart.setText("OUT OF STOCK");
        } else {
            holder.btn_add_cart.setEnabled(true);
            holder.btn_add_cart.setTextColor(context.getResources().getColor(R.color.white));
            holder.btn_add_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    /*Main_Apps.status = false;
                    ProductDetail.cartAction(Pro.getProductId(), GlobalHelper.getUserProfile(context).getProfile().getId(), "1", "0", context, holder.detail);

                    Log.e("cart counter", String.valueOf(ProductDetailCartCounter));
                    Toasty.success(context, "Item Added to Cart",
                            Toast.LENGTH_SHORT, true).show();*/
                    addToCart(item.getProductId());
                }
            });
        }

        holder.layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                ProductDetail myFragment = new ProductDetail();
                Bundle args = new Bundle();
                args.putString("ProductID", item.getProductId());
                args.putString("cateslug", item.getCateSlug());
                myFragment.setArguments(args);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_main, myFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addToCart(final String productId){
        Main_Apps.hud.show();
        String URL = Register.Base_URL + "add-to-cart";
        StringRequest req = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("status");
                            String msg = jsonObject.getString("msg");
                            if (status){
                                Main_Apps.hud.dismiss();
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
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("productId", productId);
                map.put("productQTY", "1");
                map.put("userId", GlobalHelper.getUserProfile(context).getProfile().getId());
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(req);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView tv_name, tv_subcatname, tv_price, tv_old_price;
        Button btn_add_cart;
        LinearLayout layout1;

        public ViewHolder(View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_subcatname = itemView.findViewById(R.id.tv_subcatname);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_old_price = itemView.findViewById(R.id.tv_old_price);
            btn_add_cart = itemView.findViewById(R.id.btn_add_cart);
            layout1 = itemView.findViewById(R.id.layout1);
        }
    }
}
