package managment.protege.supermed.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Activity.Register;
import managment.protege.supermed.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreditCardDetails extends Fragment {

    private static final String TAG = "CreditCardDetails";
    Button saveInformation;
    EditText fullName, cardNumber, cvv, pin;
    TextView expiryDate;
    TextView toolbar_text;

    public CreditCardDetails() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_credit_card_details, container, false);
        initialization(v);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Main_Apps.getMainActivity().addToolbarBack(getContext(), getString(R.string.card_details), v);
        ProceedFunction();
        onclickFunctions();
        return v;
    }

    private void initialization(View v) {
        toolbar_text = (TextView) v.findViewById(R.id.toolbar_text);
        saveInformation = (Button) v.findViewById(R.id.btn_saveInformation);
        fullName = (EditText) v.findViewById(R.id.credit_fullName);
        cardNumber = (EditText) v.findViewById(R.id.credit_cardNumber);
        expiryDate = (TextView) v.findViewById(R.id.credit_expiryDate);
        cvv = (EditText) v.findViewById(R.id.credit_cvv);
        pin = (EditText) v.findViewById(R.id.credit_pin);

    }

    private void onclickFunctions() {
        expiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LabTest.showDate(getContext(), expiryDate);
            }
        });
        cardNumber.addTextChangedListener(new LabTest.FourDigitCardFormatWatcher());

    }

    public void ProceedFunction() {
        // save information button
        saveInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Register.etValidate(fullName) && Register.etValidate(cardNumber) && Register.etValidateTv(expiryDate) && Register.etValidate(cvv) && Register.etValidate(pin)) {
                    Log.d(TAG, "onClick: ");
                }
            }
        });
    }

}
