package hvasoftware.com.thongtindoino.ui.fragment;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TextView;
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
import hvasoftware.com.thongtindoino.model.Customer;
import hvasoftware.com.thongtindoino.model.User;
import hvasoftware.com.thongtindoino.ui.dialog.ChooseEmployeeDialog;
import hvasoftware.com.thongtindoino.ui.dialog.InputMoneyDialog;
import hvasoftware.com.thongtindoino.utils.Constant;
import hvasoftware.com.thongtindoino.utils.DateTimeUtils;


/**
 * Created by Thanh on 03/07/2018.
 */

public class DeptFragment extends BaseFragment {
    TableLayout deptTable;
    private final String TAG = "DeptFragment";

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
        firebaseFirestore.collection(Constant.COLLECTION_CUSTOMER)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() == 0){
                                Toast.makeText(getContext(), "Không có khách hàng nào", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                Customer customer = documentSnapshot.toObject(Customer.class);
                                final View dataRow = LayoutInflater.from(getContext()).inflate(R.layout.view_dept_row, null);
                                final TextView tvCustomerId = dataRow.findViewById(R.id.tvCustomerId);
                                final TextView tvCustomerName = dataRow.findViewById(R.id.tvCustomerName);
                                final TextView tvCustomerNgayVay = dataRow.findViewById(R.id.tvCustomerNgayVay);
                                final TextView tvCustomerSoTien = dataRow.findViewById(R.id.tvCustomerSoTien);
                                final TextView tvCustomerSoNgayVay = dataRow.findViewById(R.id.tvCustomerSoNgayVay);
                                final TextView tvCustomerHetHan = dataRow.findViewById(R.id.tvCustomerHetHan);
                                final TextView tvCustomerGhiChu = dataRow.findViewById(R.id.tvCustomerGhiChu);
                                final TextView tvCustomerNhanVienThu = dataRow.findViewById(R.id.tvCustomerNhanVienThu);

                                tvCustomerId.setText(customer.getObjectID().substring(0, 10) + "...");
                                tvCustomerName.setText(customer.getTen());
                                tvCustomerNgayVay.setText(DateTimeUtils.formatDatetime(getMainAcitivity(), customer.getNgayVay()));
                                tvCustomerSoTien.setText("" + customer.getSotien());
                                tvCustomerSoNgayVay.setText("" + customer.getSongayvay());
                                tvCustomerHetHan.setText(DateTimeUtils.formatDatetime(getMainAcitivity(), customer.getHethan()));
                                tvCustomerGhiChu.setText(customer.getGhichu());
                                tvCustomerNhanVienThu.setText(customer.getNhanvienthu());
                                deptTable.addView(dataRow);

                                dataRow.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        showMenu(dataRow);
                                    }
                                });


                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.wtf(TAG, "=================================> " + e.getMessage());
            }
        });
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
