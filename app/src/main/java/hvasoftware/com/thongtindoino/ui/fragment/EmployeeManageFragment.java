package hvasoftware.com.thongtindoino.ui.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import hvasoftware.com.thongtindoino.R;
import hvasoftware.com.thongtindoino.base.BaseFragment;
import hvasoftware.com.thongtindoino.model.User;
import hvasoftware.com.thongtindoino.ui.adapter.EmployeeManageAdapter;
import hvasoftware.com.thongtindoino.utils.Constant;
import hvasoftware.com.thongtindoino.utils.IOnCompleteListener;
import hvasoftware.com.thongtindoino.utils.Utils;

/**
 * Created by Thanh on 03/10/2018.
 */

public class EmployeeManageFragment extends BaseFragment implements EmployeeManageAdapter.CallBack {
    private EmployeeManageAdapter employeeManageAdapter;
    private RecyclerView rcvUser;
    private List<User> userList = new ArrayList<>();
    private ProgressBar progressBar;

    @Override
    protected void OnViewCreated() {

    }

    private void fetchData() {
        Utils.setUpProgressBar(progressBar, false);
        userList.clear();
        firebaseFirestore.collection(Constant.COLLECTION_USER)
                .whereEqualTo("role", Constant.ROLE_STAFF)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        User user = documentSnapshot.toObject(User.class);
                        userList.add(user);
                    }
                    progressBar.setVisibility(View.GONE);
                    if (userList.size() > 0) {
                        addEmployeeView(userList);
                        rcvUser.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getContext(), "Không có nhân viên nào", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void OnBindView() {
        employeeManageAdapter = new EmployeeManageAdapter(getActivity());
        employeeManageAdapter.CallBack = this;
        rcvUser = (RecyclerView) findViewById(R.id.rcv_user);
        rcvUser.setVisibility(View.GONE);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        rcvUser.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvUser.setAdapter(employeeManageAdapter);
        employeeManageAdapter.setiOnCompleteListener(new IOnCompleteListener() {
            @Override
            public void onComplete(String staffName, String staffDocumentId) {
                fetchData();
            }
        });

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
        fetchData();
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

