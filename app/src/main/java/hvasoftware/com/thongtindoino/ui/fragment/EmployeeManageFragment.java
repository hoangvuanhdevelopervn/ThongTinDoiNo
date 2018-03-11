package hvasoftware.com.thongtindoino.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import hvasoftware.com.thongtindoino.R;
import hvasoftware.com.thongtindoino.base.BaseFragment;
import hvasoftware.com.thongtindoino.model.User;
import hvasoftware.com.thongtindoino.ui.adapter.EmployeeManageAdapter;

/**
 * Created by Thanh on 03/10/2018.
 */

public class EmployeeManageFragment extends BaseFragment implements EmployeeManageAdapter.CallBack {
    EmployeeManageAdapter employeeManageAdapter;
    RecyclerView rcvUser;

    @Override
    protected void OnViewCreated() {
        //todo:dummy
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        users.add(new User());

        //todo: call this method after get data success
        addEmployeeView(users);
    }

    @Override
    protected void OnBindView() {
        employeeManageAdapter = new EmployeeManageAdapter();
        employeeManageAdapter.CallBack = this;
        rcvUser = (RecyclerView) findViewById(R.id.rcv_user);
        rcvUser.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvUser.setAdapter(employeeManageAdapter);
    }

    public void addEmployeeView(List<User> users) {
        employeeManageAdapter.insertUser(users);
    }

    @Override
    public int GetLayoutId() {
        return R.layout.fragment_manage_employee;
    }

    @Override
    public void onResume() {
        super.onResume();
        getMainAcitivity().setScreenOrientation(true);
    }


    @Override
    public void onMenuAction(User object, MenuItem item, int position) {
        switch (item.getItemId()) {
            case R.id.remove:
                break;
        }
    }

    @Override
    protected String getScreenTitle() {
        return getString(R.string.manage_user);
    }

    @Override
    public boolean isShowOverFlowMenu() {
        return false;
    }

    @Override
    public boolean isImvAddUserVisible() {
        return true;
    }
}

