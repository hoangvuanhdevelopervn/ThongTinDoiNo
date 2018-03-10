package hvasoftware.com.thongtindoino.ui.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import hvasoftware.com.thongtindoino.R;


/**
 * Created by Thanh on 03/07/2018.
 */

public class InputMoneyDialog extends DialogFragment implements View.OnClickListener {

    View cancelBtn, sendBtn;

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
        cancelBtn = view.findViewById(R.id.cancel);
        sendBtn = view.findViewById(R.id.send);

        cancelBtn.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel:
                getDialog().dismiss();
                break;
            case R.id.send:
                break;
        }
    }
}
