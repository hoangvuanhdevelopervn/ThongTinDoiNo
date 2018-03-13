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
 * Created by Thanh on 03/12/2018.
 */

public class DateSortDialog extends DialogFragment {
    View wrapFrom, wrapTo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.view_pick_date, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        wrapFrom = view.findViewById(R.id.wrap_from);
        wrapTo = view.findViewById(R.id.wrap_to);
        super.onViewCreated(view, savedInstanceState);
    }
}