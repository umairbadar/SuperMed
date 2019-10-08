package managment.protege.supermed.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import managment.protege.supermed.Fragment.Fragment_Appointment_History;
import managment.protege.supermed.Model.AppoinmentModel;
import managment.protege.supermed.R;


public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.MyViewHolder> {

    List<AppoinmentModel> list;
    Context context;

    public AppointmentAdapter(List<AppoinmentModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_appointment, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final AppoinmentModel item = list.get(position);

        holder.tv_day.setText(item.getDay());
        holder.tv_date.setText(item.getDate());
        holder.tv_time.setText(item.getTime());
        holder.tv_lab.setText(item.getLab());
        holder.tv_patient_id.setText(item.getPatient_id());
        holder.tv_desc.setText(item.getDesc());
        holder.tv_payment_method.setText(item.getPayment_method());
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

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment_Appointment_History myFragment = new Fragment_Appointment_History();
                Bundle args = new Bundle();
                args.putString("appointment_id", item.getAppointment_id());
                myFragment.setArguments(args);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_main, myFragment).addToBackStack(null).commit();
            }
        });
    }



    @Override
    public int getItemCount() {

        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_day, tv_date, tv_time, tv_lab, tv_patient_id, tv_desc, tv_status, tv_payment_method, tv_price;
        LinearLayout mainLayout;

        public MyViewHolder(View view) {
            super(view);

            tv_day = view.findViewById(R.id.tv_day);
            tv_date = view.findViewById(R.id.tv_date);
            tv_time = view.findViewById(R.id.tv_time);
            tv_lab = view.findViewById(R.id.tv_lab);
            tv_patient_id = view.findViewById(R.id.tv_patient_id);
            tv_desc = view.findViewById(R.id.tv_desc);
            tv_status = view.findViewById(R.id.tv_status);
            tv_payment_method = view.findViewById(R.id.tv_payment_method);
            tv_price = view.findViewById(R.id.tv_price);
            mainLayout = view.findViewById(R.id.mainLayout);
        }
    }
}
