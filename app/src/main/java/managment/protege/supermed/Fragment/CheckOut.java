package managment.protege.supermed.Fragment;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;
import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Activity.Register;
import managment.protege.supermed.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckOut extends Fragment {
    Button pay;

    public CheckOut() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_check_out, container, false);
        Main_Apps.hideToolBarItems("Check Out");
        initialization(v);
        onClickFunction();
        return v;
    }

    private void initialization(View v) {
        pay = (Button) v.findViewById(R.id.checkOut_btn_pay);
    }

    private void onClickFunction() {
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadBill(getContext());
            }
        });
    }

    public static void DownloadBill(final Context context) {
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

                Toasty.success(context, "Downloading Bill", Toast.LENGTH_SHORT, true).show();
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


}
