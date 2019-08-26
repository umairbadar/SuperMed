package managment.protege.supermed.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;

import java.util.List;

import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Fragment.Home;
import managment.protege.supermed.Fragment.ProductDetail;
import managment.protege.supermed.Model.GetProductsModel;
import managment.protege.supermed.Model.Product;
import managment.protege.supermed.R;
import managment.protege.supermed.Response.GetAllProductsResponse;
import managment.protege.supermed.Tools.GlobalHelper;

import static managment.protege.supermed.Fragment.Home.ProductDetailCartCounter;

/**
 * Created by wahaj on 6/12/2018.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private List<GetProductsModel> Prolist;
    Context context;

    public ProductAdapter(List<GetProductsModel> Prolist, Context context) {
        this.Prolist = Prolist;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final GetProductsModel Pro = Prolist.get(position);
        holder.title.setText(Pro.getProductName());
        double original_price = Double.parseDouble(Pro.getProductPrice());
        holder.price.setText(String.format("Rs %.0f",original_price));

        String ImageLink = Pro.getProductImage();
        ImageLink = ImageLink.replaceAll(" ", "%20");
        Picasso.with(context).load(ImageLink).placeholder(R.drawable.tab_miss).into(holder.iv);
        if (Pro.getProductQty().equals("0")) {
            holder.detail.setEnabled(false);
            holder.detail.setText("OUT OF STOCK");
        }
        if (Pro.getProductPrice().equals("0.00")) {
            holder.detail.setEnabled(false);
            holder.detail.setTextColor(context.getResources().getColor(R.color.cartCouponText));
        } else {
            holder.detail.setEnabled(true);
            holder.detail.setTextColor(context.getResources().getColor(R.color.white));
        }
        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Main_Apps.status = false;
                ProductDetail.cartAction(Pro.getProductID(), GlobalHelper.getUserProfile(context).getProfile().getId(), "1", "0", context, holder.detail);

                Log.e("cart counter", String.valueOf(ProductDetailCartCounter));
            }
        });

        String price = Pro.getProductPrice();
        double p = Double.parseDouble(price) + 113.99;
        holder.priceoff.setText(String.valueOf(p));
        holder.priceoff.setPaintFlags(holder.priceoff.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tv_subcatname.setText(Pro.getSubCatName());

        holder.ll_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                GetProductsModel obj = Prolist.get(position);
                args.putSerializable("your_obj", obj);
                Main_Apps.getMainActivity().backfunctions(new ProductDetail(), args);

            }
        });

    }

    @Override
    public int getItemCount() {
        return Prolist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, price, priceoff, tv_subcatname;
        Button detail;
        ImageView iv;
        LinearLayout ll_cardView;

        public MyViewHolder(View view) {
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
