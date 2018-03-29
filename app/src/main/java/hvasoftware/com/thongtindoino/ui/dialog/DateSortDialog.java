package hvasoftware.com.thongtindoino.ui.dialog;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import hvasoftware.com.thongtindoino.MainActivity;
import hvasoftware.com.thongtindoino.R;
import hvasoftware.com.thongtindoino.utils.DateTimeUtils;

/**
 * Created by Thanh on 03/12/2018.
 */

@SuppressLint("ValidFragment")
public class DateSortDialog extends DialogFragment implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
    public interface IOnCompleteListener {
        void onComplete(Date start, Date end);
    }

    private static final String TAG = "DateSortDialog";
    public static DateSortDialog dateSortDialog;
    private View wrapFrom, wrapTo;
    private DatePickerDialog fromDateDialog;
    private DatePickerDialog toDateDialog;
    private TextView tvFrom, tvTo;
    private String startDate;
    private String endDate;
    private TextView tvLoc;
    private IOnCompleteListener iOnCompleteListener;
    private MainActivity mainActivity;
    private Timestamp dateStart, dateEnd;
    private Calendar cal = Calendar.getInstance();

    public DateSortDialog(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND,0);
    }

    public static DateSortDialog newInstance(MainActivity mainActivity) {
        if (dateSortDialog == null) {
            dateSortDialog = new DateSortDialog(mainActivity);
        }
        return dateSortDialog;
    }

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
        startDate = DateTimeUtils.getDateToday();
        dateStart = new Timestamp(cal.getTime().getTime());
        dateEnd = new Timestamp(cal.getTime().getTime());
        tvFrom.setText(startDate);
        tvTo = view.findViewById(R.id.tv_to);
        endDate = DateTimeUtils.getDateToday();
        tvTo.setText(endDate);
        tvLoc = view.findViewById(R.id.tvLoc);
        wrapTo = view.findViewById(R.id.wrap_to);
        wrapFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromDateDialog.show(getFragmentManager(), "");
            }
        });
        wrapTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toDateDialog.show(getFragmentManager(), "");
            }
        });

        tvLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iOnCompleteListener.onComplete(dateStart, dateEnd);
                getDialog().dismiss();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    public void setiOnCompleteListener(IOnCompleteListener onCompleteListener) {
        this.iOnCompleteListener = onCompleteListener;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND,0);
        if (view == fromDateDialog) {
            dateStart = new Timestamp(cal.getTime().getTime());
            int month = monthOfYear + 1;
            if (month < 10) {
                startDate = dayOfMonth + "/0" + (monthOfYear + 1) + "/" + year;
            } else {
                startDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
            }
            tvFrom.setText(startDate);
        } else {
            dateEnd = new Timestamp(cal.getTime().getTime());
            int month = monthOfYear + 1;
            if (month < 10) {
                endDate = dayOfMonth + "/0" + (monthOfYear + 1) + "/" + year;
            } else {
                endDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
            }
            tvTo.setText(endDate);
        }
    }
}
