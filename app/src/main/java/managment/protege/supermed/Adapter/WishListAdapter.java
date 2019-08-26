package managment.protege.supermed.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import managment.protege.supermed.Fragment.ProductDetail;
import managment.protege.supermed.Fragment.Wishlist_Fragment;
import managment.protege.supermed.Model.AlternativeModel;
import managment.protege.supermed.Model.CartModel;
import managment.protege.supermed.Model.WishlistModel;
import managment.protege.supermed.R;
import managment.protege.supermed.Tools.GlobalHelper;

/**
 * Created by wahaj on 6/12/2018.
 */

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.MyViewHolder> {
    Context context;
    TextView wl_atc;
    private OnItemClickListener onItemClickListeners;

    public WishListAdapter(List<WishlistModel> Prolist, Context context) {
        this.Prolist = Prolist;
        this.context = context;
    }

    private List<WishlistModel> Prolist;

    public interface OnItemClickListener {
        void onItemClick(View view, WishlistModel obj);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListeners = onItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wish_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        final WishlistModel Pro = Prolist.get(position);
        holder.title.setText(Pro.getProductName());
        holder.price.setText("RS " + Pro.getProductPrice());
        holder.priceoff.setText(Pro.getProductOldPrice());
        holder.wl_atc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductDetail.cartAction(Pro.getProductID(), GlobalHelper.getUserProfile(context).getProfile().getId().toString(), "1", "0", context, holder.wl_atc);
            }
        });
        holder.del_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListeners != null) {
                    onItemClickListeners.onItemClick(view, Pro);
                }
            }
        });
        Picasso.with(context).load(Pro.getProductImage())
                .placeholder(R.drawable.tab_miss)
                .into(holder.iv);
        holder.tv_subcatname.setText(Pro.getSubCatName());
        String price = Pro.getProductPrice();
        double p = Double.parseDouble(price) + 113.99;
        holder.priceoff.setText(String.valueOf(p));
        holder.priceoff.setPaintFlags(holder.priceoff.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public int getItemCount() {
        return Prolist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, detail, genre, price, priceoff, tv_subcatname;
        Button wl_atc;
        ImageView del_wishlist;
        ImageView iv;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            priceoff = (TextView) view.findViewById(R.id.priceoff);
            price = (TextView) view.findViewById(R.id.price);
            detail = (TextView) view.findViewById(R.id.detail);
            iv = (ImageView) view.findViewById(R.id.iv);
            del_wishlist = (ImageView) view.findViewById(R.id.del_wishlist);
            wl_atc = (Button) view.findViewById(R.id.wl_atc);
            tv_subcatname = (TextView) view.findViewById(R.id.tv_subcatname);

        }
    }
}
