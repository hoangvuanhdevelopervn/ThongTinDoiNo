package hvasoftware.com.thongtindoino.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import hvasoftware.com.thongtindoino.R;
import hvasoftware.com.thongtindoino.base.BaseFragment;
import hvasoftware.com.thongtindoino.model.Customer;
import hvasoftware.com.thongtindoino.ui.dialog.ChooseEmployeeDialog;
import hvasoftware.com.thongtindoino.ui.dialog.InputMoneyDialog;
import hvasoftware.com.thongtindoino.utils.CheckInternet;
import hvasoftware.com.thongtindoino.utils.Constant;
import hvasoftware.com.thongtindoino.utils.Utils;


/**
 * Created by Thanh on 03/07/2018.
 */

public class DeptFragment extends BaseFragment {
    private final String TAG = "DeptFragment";
    private TableLayout deptTable;
    private CheckInternet checkInternet;
    private HorizontalScrollView horizontalView;
    private TableRow table_header;

    @Override
    protected void OnBindView() {
        deptTable = (TableLayout) findViewById(R.id.dept_table);
        table_header = (TableRow) findViewById(R.id.table_header);
        horizontalView = (HorizontalScrollView) findViewById(R.id.horizontalView);
        checkInternet = CheckInternet.getInstance(getContext());
    }

    @Override
    protected void OnViewCreated() {

    }

    public void bindDataToTable() {
        final Customer[] customer = {null};
        final View[] dataRow = new View[1];
        deptTable.removeAllViews();
        deptTable.addView(table_header);
        horizontalView.setVisibility(View.GONE);
        firebaseFirestore.collection(Constant.COLLECTION_CUSTOMER)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() == 0) {
                                Toast.makeText(getContext(), R.string.no_customer, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                customer[0] = documentSnapshot.toObject(Customer.class);
                                dataRow[0] = LayoutInflater.from(getContext()).inflate(R.layout.view_dept_row, null);
                                final TextView tvCustomerId = dataRow[0].findViewById(R.id.tvCustomerId);
                                final TextView tvCustomerName = dataRow[0].findViewById(R.id.tvCustomerName);
                                final TextView tvCustomerNgayVay = dataRow[0].findViewById(R.id.tvCustomerNgayVay);
                                final TextView tvCustomerSoTien = dataRow[0].findViewById(R.id.tvCustomerSoTien);
                                final TextView tvCustomerSoNgayVay = dataRow[0].findViewById(R.id.tvCustomerSoNgayVay);
                                final TextView tvCustomerHetHan = dataRow[0].findViewById(R.id.tvCustomerHetHan);
                                final TextView tvCustomerGhiChu = dataRow[0].findViewById(R.id.tvCustomerGhiChu);
                                final TextView tvCustomerNhanVienThu = dataRow[0].findViewById(R.id.tvCustomerNhanVienThu);

                                tvCustomerId.setText(customer[0].getObjectID().substring(0, 10) + "...");
                                tvCustomerName.setText(customer[0].getTen());
                                tvCustomerNgayVay.setText(customer[0].getNgayVay());
                                tvCustomerSoTien.setText("" + Utils.formatCurrency(customer[0].getSotien()));
                                tvCustomerSoNgayVay.setText("" + customer[0].getSongayvay());
                                tvCustomerHetHan.setText(customer[0].getHethan());
                                tvCustomerGhiChu.setText(customer[0].getGhichu());
                                tvCustomerNhanVienThu.setText(customer[0].getNhanvienthu());
                                horizontalView.setVisibility(View.VISIBLE);
                                deptTable.addView(dataRow[0]);
                                dataRow[0].setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        showMenu(dataRow[0], customer[0]);
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

    public void showMenu(View view, final Customer customer) {
        PopupMenu menu = new PopupMenu(getContext(), view, Gravity.END);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.assign:
                        ChooseEmployeeDialog chooseEmployeeDialog = new ChooseEmployeeDialog();
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.KEY, customer.getDocumentId());
                        chooseEmployeeDialog.setArguments(bundle);
                        chooseEmployeeDialog.show(getMainAcitivity().getFragmentManager(), TAG);
                        break;
                    case R.id.money:
                        InputMoneyDialog inputMoneyDialog = new InputMoneyDialog();
                        Bundle bundle2 = new Bundle();
                        bundle2.putString(Constant.KEY, customer.getDocumentId());
                        bundle2.putLong(Constant.MONEY, customer.getSotien());
                        inputMoneyDialog.setArguments(bundle2);
                        inputMoneyDialog.show(getMainAcitivity().getFragmentManager(), TAG);
                        break;
                    case R.id.detail:
                        AddCustomerFragment addCustomerFragment = new AddCustomerFragment(AddCustomerFragment.ScreenType.View);
                        Bundle bundle1 = new Bundle();
                        bundle1.putString(Constant.KEY, customer.getDocumentId());
                        addCustomerFragment.setArguments(bundle1);
                        SwitchFragment(addCustomerFragment, true);
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
        bindDataToTable();
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
