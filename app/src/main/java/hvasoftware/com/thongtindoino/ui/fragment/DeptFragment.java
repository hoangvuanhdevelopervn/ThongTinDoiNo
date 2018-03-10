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
        deptTable = (TableLayout) FindViewById(R.id.dept_table);
    }

    @Override
    protected void OnViewCreated() {
        GetMainAcitivity().setScreenOrientation(false);

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
                        chooseEmployeeDialog.show(GetMainAcitivity().getFragmentManager(), "");
                        break;
                    case R.id.money:
                        InputMoneyDialog inputMoneyDialog = new InputMoneyDialog();
                        inputMoneyDialog.show(GetMainAcitivity().getFragmentManager(), "");
                        break;
                }
                return true;
            }
        });
        menu.inflate(R.menu.menu_dept_row);
        menu.show();
    }


    @Override
    public int GetLayoutId() {
        return R.layout.dept_fragment;
    }

    @Override
    protected String GetScreenTitle() {
        return getResources().getString(R.string.app_name);
    }
}
