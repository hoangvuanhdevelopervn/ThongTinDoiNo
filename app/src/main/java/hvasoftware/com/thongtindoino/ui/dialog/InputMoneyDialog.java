package hvasoftware.com.thongtindoino.ui.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import hvasoftware.com.thongtindoino.R;
import hvasoftware.com.thongtindoino.utils.Constant;
import hvasoftware.com.thongtindoino.utils.DateTimeUtils;
import hvasoftware.com.thongtindoino.utils.Utils;


/**
 * Created by Thanh on 03/07/2018.
 */

public class InputMoneyDialog extends DialogFragment implements View.OnClickListener {
    private static final String TAG = "InputMoneyDialog";
    private View cancelBtn, sendBtn;
    private String documentId;
    private long oldMoney;
    private EditText edt_input;
    private FirebaseFirestore firebaseFirestore;
    private TextView tvMoney;

    public InputMoneyDialog() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.view_input_money, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edt_input = view.findViewById(R.id.edt_input);
        cancelBtn = view.findViewById(R.id.cancel);
        sendBtn = view.findViewById(R.id.send);
        tvMoney = view.findViewById(R.id.tvMoney);
        cancelBtn.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
        savedInstanceState = this.getArguments();
        documentId = savedInstanceState.getString(Constant.KEY);
        oldMoney = savedInstanceState.getLong(Constant.MONEY);
        firebaseFirestore = FirebaseFirestore.getInstance();
        tvMoney.setText("Tổng số tiền phải thu: " + Utils.formatCurrency(oldMoney));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                getDialog().dismiss();
                break;
            case R.id.send:

                updateCustomer();

                break;
        }
    }

    private void updateCustomer() {
        String edtText = edt_input.getText().toString().trim();
        if (TextUtils.isEmpty(edtText)) {
            Toast.makeText(getActivity(), "Bạn chưa nhập số tiền", Toast.LENGTH_SHORT).show();
            return;
        }

        long newMoney = Integer.parseInt(edtText);
        long updateMoney = oldMoney - newMoney;
        if (newMoney > oldMoney) {
            Toast.makeText(getActivity(), "Số tiền thu được không thể lớn hơn số tiền đang nợ", Toast.LENGTH_SHORT).show();
            return;
        }

        WriteBatch writeBatch = firebaseFirestore.batch();
        DocumentReference updateQuoteShareAmount = firebaseFirestore.collection(Constant.COLLECTION_CUSTOMER).document(documentId);
        writeBatch.update(updateQuoteShareAmount, "sotien", updateMoney);
        writeBatch.update(updateQuoteShareAmount, "updateAt", DateTimeUtils.getDateTime());
        writeBatch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getActivity(), getString(R.string.updapte_success), Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
            }
        });

    }
}
