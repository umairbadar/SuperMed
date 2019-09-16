package managment.protege.supermed.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Fragment.AlternativeListFragment;
import managment.protege.supermed.Fragment.ProductDetail;
import managment.protege.supermed.Model.AlternativeModel;
import managment.protege.supermed.Model.GetProductsModel;
import managment.protege.supermed.Model.SearchModel;
import managment.protege.supermed.Model.SearchProductModel;
import managment.protege.supermed.R;
import managment.protege.supermed.Tools.GlobalHelper;

import static managment.protege.supermed.Fragment.Home.ProductDetailCartCounter;

/**
 * Created by wahaj on 6/12/2018.
 */

public class SearchProductAdapter extends RecyclerView.Adapter<SearchProductAdapter.MyViewHolder> {
    Context context;

    public SearchProductAdapter(List<SearchModel> Prolist, Context context) {
        this.Prolist = Prolist;
        this.context = context;
    }

    private List<SearchModel> Prolist;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alternative_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final SearchModel Pro = Prolist.get(position);
        holder.title.setText(Pro.getProductName());
        double original_price = Double.parseDouble(Pro.getPrice());
        holder.price.setText(String.format("Rs %.0f",original_price));
        Picasso.get()
                .load(Pro.getProductImage().replaceAll(" ", "%20"))
                .resize(80, 80)
                .centerCrop()
                .placeholder(R.drawable.tab_miss)
                .into(holder.iv);
        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Main_Apps.status = false;
                AlternativeListFragment.cartActionSearch(Pro.getProductId(), GlobalHelper.getUserProfile(context).getProfile().getId(), "1", "0", context, holder.detail);
                Log.e("cart counter", String.valueOf(ProductDetailCartCounter));
            }
        });
        /*holder.ll_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                SearchModel obj = Prolist.get(position);
                args.putSerializable("your_obj_2", obj);

                Main_Apps.getMainActivity().backfunctions(new ProductDetail(), args);
            }
        });*/

        if (Pro.getQty().equals("0")) {
            holder.detail.setEnabled(false);
            holder.detail.setText("OUT OF STOCK");
        }

        String price = Pro.getPrice();
        double p = Double.parseDouble(price) + 113.99;
        holder.priceoff.setText(String.format("Rs %.2f",p));
        holder.priceoff.setPaintFlags(holder.priceoff.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tv_subcatname.setText(Pro.getSubcateName());
    }

    @Override
    public int getItemCount() {
        return Prolist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, price, priceoff, tv_subcatname;
        ImageView iv;
        Button detail;
        LinearLayout ll_cardView;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            detail = (Button) view.findViewById(R.id.alt_detail);
            price = (TextView) view.findViewById(R.id.price);
            priceoff = (TextView) view.findViewById(R.id.priceoff);
            iv = (ImageView) view.findViewById(R.id.iv);
            ll_cardView = (LinearLayout) view.findViewById(R.id.LL_search);
            tv_subcatname = (TextView) view.findViewById(R.id.tv_subcatname);
        }
    }
}
