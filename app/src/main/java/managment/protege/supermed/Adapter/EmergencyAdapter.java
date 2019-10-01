package managment.protege.supermed.Adapter;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import managment.protege.supermed.Model.EmergencyModel;
import managment.protege.supermed.R;

public class EmergencyAdapter extends RecyclerView.Adapter<EmergencyAdapter.ViewHolder> {

    List<EmergencyModel> list;
    Context context;
    final int CODE_GALLERY_REQUEST = 999;

    public EmergencyAdapter(List<EmergencyModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.emergency_contact_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final EmergencyModel item = list.get(position);

        holder.btn_emergency_title.setText(item.getName());

        holder.btn_emergency_title.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + item.getContact()));
                    view.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        Button btn_emergency_title;

        public ViewHolder(View itemView) {
            super(itemView);

            btn_emergency_title = itemView.findViewById(R.id.btn_emergency_title);
        }
    }

}
