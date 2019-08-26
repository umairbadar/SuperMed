package managment.protege.supermed.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Fragment.Home;
import managment.protege.supermed.Model.AppoinmentModel;
import managment.protege.supermed.Model.AppointmentHistoryModel;
import managment.protege.supermed.Model.OrderHistoryList;
import managment.protege.supermed.R;

/**
 * Created by Developer on 6/21/2018.
 */

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.MyViewHolder> {

    private Context context;

    public AppointmentAdapter(List<AppointmentHistoryModel> orderHistoryLists, Context context) {
        this.orderHistoryLists = orderHistoryLists;
        this.context = context;
    }

    private List<AppointmentHistoryModel> orderHistoryLists;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_appointment, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final AppointmentHistoryModel cata = orderHistoryLists.get(position);
        holder.test.setText(cata.getTestname());
        holder.app_number.setText(cata.getAppointmentId());
        holder.patientname.setText("Patient Name: " + cata.getPatientName());
        holder.patientid.setText("Patient Id: " + cata.getPatientId());

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        String inputDateStr = cata.getDate();
        Date date = null;
        try {
            date = inputFormat.parse(inputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDateStr = outputFormat.format(date);
        holder.date.setText(outputDateStr);

        holder.laboratory_name.setText(cata.getLabname());

        if (cata != null && cata.getReport() != null) {
            if (!(cata.getReport().equalsIgnoreCase("Not Uploaded Yet"))) {
                holder.tv_downlaod.setText("Download Report");
            } else {
                holder.tv_downlaod.setText("Report Pending");
            }
        }

        holder.tv_downlaod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cata.getReport() != null && !(cata.getReport().equalsIgnoreCase(""))) {
                    DownloadBill(context, cata.getReport());
                }
            }
        });
    }

    public void DownloadBill(final Context context, final String recipt) {
        final Dialog downloadBillDialog = new Dialog(context);
        downloadBillDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        downloadBillDialog.setContentView(R.layout.bill_dialog_box);
        downloadBillDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        downloadBillDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        downloadBillDialog.setCancelable(false);
        final ImageView btn_close = (ImageView) downloadBillDialog.findViewById(R.id.iv_rp_close);
        Button sucess = (Button) downloadBillDialog.findViewById(R.id.btn_downloadBill);
        sucess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(recipt)));
                downloadBillDialog.dismiss();
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadBillDialog.dismiss();
            }
        });
        downloadBillDialog.show();
    }

    @Override
    public int getItemCount() {

        return orderHistoryLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView test, app_number, patientid, patientname, laboratory_name, date, orderHistoryNumber, tv_downlaod;
        ImageView iv;

        public MyViewHolder(View view) {
            super(view);
            test = (TextView) view.findViewById(R.id.test);
            patientname = (TextView) view.findViewById(R.id.patientname);
            app_number = (TextView) view.findViewById(R.id.app_number);
            patientid = (TextView) view.findViewById(R.id.patientid);
            laboratory_name = (TextView) view.findViewById(R.id.laboratory_name);
            tv_downlaod = (TextView) view.findViewById(R.id.tv_downlaod);
            date = (TextView) view.findViewById(R.id.date);
        }
    }
}
