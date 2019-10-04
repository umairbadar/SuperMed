package managment.protege.supermed.Adapter;

import android.content.Context;
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

import managment.protege.supermed.Activity.Register;
import managment.protege.supermed.Model.Model_Category;
import managment.protege.supermed.Model.Model_SubCategory;
import managment.protege.supermed.R;

public class Adapter_Category extends RecyclerView.Adapter<Adapter_Category.ViewHolder> {

    List<Model_Category> list;
    Context context;
    List<Model_SubCategory> list1;

    public Adapter_Category(List<Model_Category> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Model_Category item = list.get(position);

        holder.tv_name.setText(item.getName());

        list1 = new ArrayList<>();

        /*holder.adapter = new Adapter_SubCategory(list1, context);
        holder.recyclerViewSubCategory.setAdapter(holder.adapter);*/

        holder.img_drop_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.recyclerViewSubCategory.getVisibility() == View.VISIBLE){
                    holder.recyclerViewSubCategory.setVisibility(View.GONE);
                } else {
                    list1.clear();
                    holder.recyclerViewSubCategory.setVisibility(View.VISIBLE);
                    getSubCategory(item.getSlug(), holder);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void getSubCategory(final String Sluq, final ViewHolder holder){
        String URL = Register.Base_URL + "relational-categories";
        StringRequest req = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject innerObj1 = jsonArray.getJSONObject(i);
                                String slug = innerObj1.getString("slug");
                                if (Sluq.equals(slug)){
                                    JSONArray jsonArray1 = innerObj1.getJSONArray("subcate");
                                    for (int j = 0; j < jsonArray1.length(); j++){
                                        JSONObject innerObj2 = jsonArray1.getJSONObject(j);
                                        String id = innerObj2.getString("id");
                                        String name = innerObj2.getString("name");
                                        String slugs = innerObj2.getString("slug");
                                        String thirdlevel = innerObj2.getString("thridLevels");

                                        Model_SubCategory item = new Model_SubCategory(
                                                id,
                                                name,
                                                slugs,
                                                thirdlevel
                                        );

                                        list1.add(item);
                                    }

                                    holder.adapter = new Adapter_SubCategory(list1, context);
                                    holder.recyclerViewSubCategory.setAdapter(holder.adapter);
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

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_name;
        ImageView img_drop_down;
        RecyclerView recyclerViewSubCategory;
        Adapter_SubCategory adapter;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_cat_name);
            img_drop_down = itemView.findViewById(R.id.img_drop_down);
            recyclerViewSubCategory = itemView.findViewById(R.id.recyclerViewSubCategory);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            recyclerViewSubCategory.setLayoutManager(mLayoutManager);
            recyclerViewSubCategory.setItemAnimator(new DefaultItemAnimator());
            recyclerViewSubCategory.addItemDecoration(new DividerItemDecoration(context, 1));
        }
    }
}
