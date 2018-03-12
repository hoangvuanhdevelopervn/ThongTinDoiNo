package hvasoftware.com.thongtindoino.ui.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import hvasoftware.com.thongtindoino.R;

/**
 * Created by Thanh on 03/12/2018.
 */

public class DateSortDialog extends DialogFragment implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
    View wrapFrom, wrapTo;
    DatePickerDialog fromDateDialog;
    DatePickerDialog toDateDialog;
    TextView tvFrom, tvTo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.view_pick_date, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        fromDateDialog = DatePickerDialog.newInstance(this, calendar);
        toDateDialog = DatePickerDialog.newInstance(this, calendar);
        wrapFrom = view.findViewById(R.id.wrap_from);
        tvFrom = view.findViewById(R.id.tv_from);
        tvTo = view.findViewById(R.id.tv_to);
        wrapFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromDateDialog.show(getFragmentManager(), "");
            }
        });
        wrapTo = view.findViewById(R.id.wrap_to);
        wrapTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toDateDialog.show(getFragmentManager(), "");
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if (view == fromDateDialog) {
            tvFrom.setText(dayOfMonth + " Tháng " + (monthOfYear + 1) + " Năm " + year);
        } else {
            tvTo.setText(dayOfMonth + " Tháng " + (monthOfYear + 1) + " Năm " + year);
        }
    }
}
