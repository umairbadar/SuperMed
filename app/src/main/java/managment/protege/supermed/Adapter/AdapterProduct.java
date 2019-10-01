package managment.protege.supermed.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Activity.Register;
import managment.protege.supermed.Fragment.Home;
import managment.protege.supermed.Fragment.ProductDetail;
import managment.protege.supermed.Model.GetProductsModel;
import managment.protege.supermed.Model.Model_Brand;
import managment.protege.supermed.Model.Model_PopularProducts;
import managment.protege.supermed.R;
import managment.protege.supermed.Tools.GlobalHelper;

import static managment.protege.supermed.Fragment.Home.ProductDetailCartCounter;

public class AdapterProduct extends RecyclerView.Adapter<AdapterProduct.ViewHolder> {

    private List<Model_PopularProducts> Prolist;
    Context context;

    public AdapterProduct(List<Model_PopularProducts> prolist, Context context) {
        Prolist = prolist;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Model_PopularProducts Pro = Prolist.get(position);
        holder.title.setText(Pro.getProductName());
        double original_price = Double.parseDouble(Pro.getPrice());
        holder.price.setText(String.format("Rs %.0f",original_price));

        String ImageLink = Pro.getProductImage();
        if (!ImageLink.equals("")) {
            ImageLink = ImageLink.replaceAll(" ", "%20");
            Picasso.get()
                    .load(ImageLink)
                    .resize(80, 80)
                    .centerCrop()
                    .placeholder(R.drawable.tab_miss)
                    .into(holder.iv);
        }
        if (Pro.getQty().equals("0")) {
            holder.detail.setEnabled(false);
            holder.detail.setText("OUT OF STOCK");
        }
        else if (Pro.getPrice().equals("0.00")) {
            holder.detail.setEnabled(false);
            //holder.detail.setTextColor(context.getResources().getColor(R.color.cartCouponText));
            holder.detail.setText("OUT OF STOCK");
        } else {
            holder.detail.setEnabled(true);
            holder.detail.setTextColor(context.getResources().getColor(R.color.white));
            holder.detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    /*Main_Apps.status = false;
                    ProductDetail.cartAction(Pro.getProductId(), GlobalHelper.getUserProfile(context).getProfile().getId(), "1", "0", context, holder.detail);

                    Log.e("cart counter", String.valueOf(ProductDetailCartCounter));
                    Toasty.success(context, "Item Added to Cart",
                            Toast.LENGTH_SHORT, true).show();*/
                    addToCart(Pro.getProductId());
                }
            });
        }

        holder.tv_subcatname.setText(Pro.getSubCatName());
        String price = Pro.getPrice();
        double p = Double.parseDouble(price) + 113.99;
        holder.priceoff.setText(String.format("Rs %.2f",p));
        holder.priceoff.setPaintFlags(holder.priceoff.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        holder.ll_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                ProductDetail myFragment = new ProductDetail();
                Bundle args = new Bundle();
                args.putString("ProductID", Pro.getProductId());
                args.putString("cateslug", Pro.getCateSlug());
                myFragment.setArguments(args);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_main, myFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return Prolist.size();
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

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, price, priceoff, tv_subcatname;
        Button detail;
        ImageView iv;
        LinearLayout ll_cardView;

        public ViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.title);
            priceoff = (TextView) view.findViewById(R.id.priceoff);
            price = (TextView) view.findViewById(R.id.price);
            detail = (Button) view.findViewById(R.id.detail);
            iv = (ImageView) view.findViewById(R.id.iv);
            ll_cardView = (LinearLayout) view.findViewById(R.id.ll_cardView);
            tv_subcatname = (TextView) view.findViewById(R.id.tv_subcatname);
        }
    }
}
