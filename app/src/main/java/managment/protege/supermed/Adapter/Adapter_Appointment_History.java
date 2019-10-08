package managment.protege.supermed.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import managment.protege.supermed.Fragment.Cart;
import managment.protege.supermed.Fragment.Fragment_Appointment_History;
import managment.protege.supermed.Model.Model_Appointment_History;
import managment.protege.supermed.R;

public class Adapter_Appointment_History extends RecyclerView.Adapter<Adapter_Appointment_History.ViewHolder> {

    List<Model_Appointment_History> list;
    Context context;
    double total = 0;

    public Adapter_Appointment_History(List<Model_Appointment_History> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_appointment_history, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Model_Appointment_History item = list.get(position);

        holder.tv_name.setText(item.getName());
        holder.tv_appointment_id.setText(item.getAppointment_id());
        holder.tv_dob.setText(item.getDob());
        holder.tv_gender.setText(item.getGender());
        holder.tv_date_time.setText(item.getDate_time());
        holder.tv_payment_method.setText(item.getPayment_method());
        holder.tv_address.setText(item.getAddress());
        holder.tv_problem.setText(item.getProblem());
        holder.tv_price.setText("RS. " + item.getPrice() + "/=");

        if (item.getStatus().equals("Pending")){
            holder.tv_status.setText(item.getStatus());
            holder.tv_status.setTextColor(Color.parseColor("#2BA8E4"));
        } else if (item.getStatus().equals("Cancel")){
            holder.tv_status.setText(item.getStatus());
            holder.tv_status.setTextColor(Color.parseColor("#D73030"));
        }  else if (item.getStatus().equals("Success")){
            holder.tv_status.setText(item.getStatus());
            holder.tv_status.setTextColor(Color.parseColor("#67AD51"));
        }

        double price = Double.parseDouble(item.getPrice());
        total += price;
        Fragment_Appointment_History.tv_total_price.setText(String.format("%.1f", total));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_name, tv_status, tv_appointment_id, tv_dob, tv_gender, tv_date_time, tv_payment_method, tv_address, tv_problem, tv_price;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_appointment_id = itemView.findViewById(R.id.tv_appointment_id);
            tv_dob = itemView.findViewById(R.id.tv_dob);
            tv_gender = itemView.findViewById(R.id.tv_gender);
            tv_date_time = itemView.findViewById(R.id.tv_date_time);
            tv_payment_method = itemView.findViewById(R.id.tv_payment_method);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_problem = itemView.findViewById(R.id.tv_problem);
            tv_price = itemView.findViewById(R.id.tv_price);
        }
    }
}
