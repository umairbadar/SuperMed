package managment.protege.supermed.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Activity.Register;
import managment.protege.supermed.Fragment.ChangePassword;
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
        holder.price.setText("RS " + Pro.getPrice());
        holder.wl_atc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ProductDetail.cartAction(Pro.getProductID(), GlobalHelper.getUserProfile(context).getProfile().getId().toString(), "1", "0", context, holder.wl_atc);
            }
        });
        holder.del_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String URL = Register.Base_URL + "delete-wishlist/" + Pro.getWishlistId();
                StringRequest req = new StringRequest(Request.Method.DELETE, URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean status = jsonObject.getBoolean("status");
                                    if (status){
                                       /* AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                        Wishlist_Fragment myFragment = new Wishlist_Fragment();
                                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_main, myFragment).addToBackStack(myFragment.getClass().getName()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();*/
                                        Main_Apps.getMainActivity().backfunction(new Wishlist_Fragment());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context, error.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(req);
            }
        });
        Picasso.get()
                .load(Pro.getProductImage().replaceAll(" ", "%20"))
                .resize(80, 80)
                .centerCrop()
                .placeholder(R.drawable.tab_miss)
                .into(holder.iv);
        holder.tv_subcatname.setText(Pro.getSubcateName());
        String price = Pro.getPrice();
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
