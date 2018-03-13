package hvasoftware.com.thongtindoino.ui.fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TableLayout;

import hvasoftware.com.thongtindoino.R;
import hvasoftware.com.thongtindoino.base.BaseFragment;
import hvasoftware.com.thongtindoino.ui.dialog.ChooseEmployeeDialog;
import hvasoftware.com.thongtindoino.ui.dialog.InputMoneyDialog;


/**
 * Created by Thanh on 03/07/2018.
 */

public class DeptFragment extends BaseFragment {

    TableLayout deptTable;

    @Override
    protected void OnBindView() {
        deptTable = (TableLayout) findViewById(R.id.dept_table);
    }

    @Override
    protected void OnViewCreated() {

        //todo:dummy data
        BindDataToTable();
    }

    public void BindDataToTable() {
        for (int i = 0; i < 10; i++) {
            final View dataRow = LayoutInflater.from(getContext()).inflate(R.layout.view_dept_row, null);
            dataRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showMenu(dataRow);
                }
            });
            deptTable.addView(dataRow);
        }
    }

    public void showMenu(View view) {
        PopupMenu menu = new PopupMenu(getContext(), view, Gravity.END);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.assign:
                        ChooseEmployeeDialog chooseEmployeeDialog = new ChooseEmployeeDialog();
                        chooseEmployeeDialog.show(getMainAcitivity().getFragmentManager(), "");
                        break;
                    case R.id.money:
                        InputMoneyDialog inputMoneyDialog = new InputMoneyDialog();
                        inputMoneyDialog.show(getMainAcitivity().getFragmentManager(), "");
                        break;
                    case R.id.detail:
                        SwitchFragment(new AddCustomerFragment(AddCustomerFragment.ScreenType.View), true);
                        break;
                }
                return true;
            }
        });
        menu.inflate(R.menu.menu_dept_row);
        menu.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        getMainAcitivity().setScreenOrientation(false);
    }

    @Override
    public int GetLayoutId() {
        return R.layout.fragment_dept;
    }

    @Override
    protected String getScreenTitle() {
        return getResources().getString(R.string.app_name);
    }

    @Override
    public boolean isFloatButtonVisible() {
        return true;
    }

    @Override
    public boolean isBackButtonVisible() {
        return false;
    }
}
