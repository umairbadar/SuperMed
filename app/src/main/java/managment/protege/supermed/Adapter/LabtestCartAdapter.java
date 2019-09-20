package managment.protege.supermed.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import managment.protege.supermed.Fragment.Fragment_Book_an_Appointment;
import managment.protege.supermed.Model.TestModel;
import managment.protege.supermed.R;

/**
 * Created by wahaj on 6/12/2018.
 */

public class LabtestCartAdapter extends RecyclerView.Adapter<LabtestCartAdapter.MyViewHolder> {

    public LabtestCartAdapter(List<TestModel> list) {
        this.list = list;
    }

    private List<TestModel> list;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_cart_way, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        TestModel Pro = list.get(position);
        holder.title.setText(Pro.getLabname());
        holder.testname.setText(Pro.getTest_name());
        holder.price.setText(Pro.getPrice());
   /*     holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(position);
                Fragment_Book_an_Appointment.delete(list);
                Fragment_Book_an_Appointment.jsonArray.remove(position);


            }
        });*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, testname, price;
        ImageView iv;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.labname);
            testname = (TextView) view.findViewById(R.id.testname);
            price = (TextView) view.findViewById(R.id.price);
            iv = (ImageView) view.findViewById(R.id.delete);
        }
    }
}
