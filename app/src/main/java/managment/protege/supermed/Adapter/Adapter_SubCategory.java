package managment.protege.supermed.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Activity.Register;
import managment.protege.supermed.Fragment.Fragment_Sub_Cat_Product;
import managment.protege.supermed.Model.Model_SubCategory;
import managment.protege.supermed.Model.Model_Sub_Sub_Category;
import managment.protege.supermed.R;

public class Adapter_SubCategory extends RecyclerView.Adapter<Adapter_SubCategory.ViewHolder> {

    List<Model_SubCategory> list;
    Context context;
    List<Model_Sub_Sub_Category> list1;

    public Adapter_SubCategory(List<Model_SubCategory> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sub_category, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Model_SubCategory item = list.get(position);

        holder.tv_sub_cat_name.setText(item.getName());

        holder.tv_sub_cat_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putString("sub_slug", item.getSlug());
                args.putString("slug", item.getMainslug());
                Main_Apps.getMainActivity().backfunction(new Fragment_Sub_Cat_Product(), args);
            }
        });

        list1 = new ArrayList<>();

        if (item.getThirdlevels().equals("[]")) {
            holder.img_drop_down.setVisibility(View.GONE);
        } else {
            holder.img_drop_down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.recyclerViewSubSubCategory.getVisibility() == View.VISIBLE) {
                        holder.recyclerViewSubSubCategory.setVisibility(View.GONE);
                        holder.img_drop_down.setImageResource(R.drawable.ic_plus);
                    } else {
                        list1.clear();
                        holder.recyclerViewSubSubCategory.setVisibility(View.VISIBLE);
                        getSubSubCategory(item.getMainslug(), item.getSlug(), item.getThirdlevels(), holder);
                    }
                    //Toast.makeText(context, "Hello", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void getSubSubCategory(final String mainslug, final String slugs, final String thirdlevel, final ViewHolder holder) {
        String URL = Register.Base_URL + "relational-categories";
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject innerObj1 = jsonArray.getJSONObject(i);
                                String slug = innerObj1.getString("slug");
                                if (mainslug.equals(slug)) {
                                    holder.img_drop_down.setImageResource(R.drawable.ic_minus);
                                    JSONArray jsonArray1 = innerObj1.getJSONArray("subcate");
                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                        JSONObject innerObj2 = jsonArray1.getJSONObject(j);
                                        String slug1 = innerObj2.getString("slug");
                                        if (slugs.equals(slug1) && !thirdlevel.equals("[]")) {
                                            JSONArray jsonArray2 = innerObj2.getJSONArray("thridLevels");
                                            for (int k = 0; k < jsonArray2.length(); k++) {
                                                JSONObject innerObj3 = jsonArray2.getJSONObject(k);
                                                String id = innerObj3.getString("id");
                                                String name = innerObj3.getString("name");
                                                String slug2 = innerObj3.getString("slug");

                                                Model_Sub_Sub_Category item = new Model_Sub_Sub_Category(
                                                        id,
                                                        name,
                                                        slug2,
                                                        slugs,
                                                        mainslug
                                                );

                                                list1.add(item);
                                            }
                                        }
                                    }
                                    holder.adapter = new Adapter_Sub_Sub_Category(list1, context);
                                    holder.recyclerViewSubSubCategory.setAdapter(holder.adapter);
                                }
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

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_sub_cat_name;
        ImageView img_drop_down;
        RecyclerView recyclerViewSubSubCategory;
        Adapter_Sub_Sub_Category adapter;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_sub_cat_name = itemView.findViewById(R.id.tv_sub_cat_name);
            img_drop_down = itemView.findViewById(R.id.img_drop_down);
            recyclerViewSubSubCategory = itemView.findViewById(R.id.recyclerViewSubSubCategory);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            recyclerViewSubSubCategory.setLayoutManager(mLayoutManager);
            recyclerViewSubSubCategory.setItemAnimator(new DefaultItemAnimator());
            recyclerViewSubSubCategory.addItemDecoration(new DividerItemDecoration(context, 1));
        }
    }
}
