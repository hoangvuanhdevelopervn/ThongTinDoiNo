package hvasoftware.com.thongtindoino.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

import hvasoftware.com.thongtindoino.R;
import hvasoftware.com.thongtindoino.base.BaseFragment;
import hvasoftware.com.thongtindoino.model.Customer;
import hvasoftware.com.thongtindoino.model.User;
import hvasoftware.com.thongtindoino.ui.adapter.AdapterAssign;
import hvasoftware.com.thongtindoino.utils.CheckInternet;
import hvasoftware.com.thongtindoino.utils.Constant;
import hvasoftware.com.thongtindoino.utils.DateTimeUtils;
import hvasoftware.com.thongtindoino.utils.IOnCompleteListener;
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
    private ProgressDialog progressDialog;

    @Override
    protected void OnBindView() {
        deptTable = (TableLayout) findViewById(R.id.dept_table);
        table_header = (TableRow) findViewById(R.id.table_header);
        horizontalView = (HorizontalScrollView) findViewById(R.id.horizontalView);
        checkInternet = CheckInternet.getInstance(getContext());
        progressDialog = new ProgressDialog(getActivity());
    }

    @Override
    protected void OnViewCreated() {

    }

    public void bindDataToTable() {
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Đang load dữ liệu, đợi xíu");
        progressDialog.show();
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
                            progressDialog.dismiss();
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
                        showDialogAssignToStaff(customer.getDocumentId());

                        /**
                         *  ChooseEmployeeDialog chooseEmployeeDialog = new ChooseEmployeeDialog();
                         Bundle bundle = new Bundle();
                         bundle.putString(Constant.KEY, customer.getDocumentId());
                         chooseEmployeeDialog.setArguments(bundle);
                         chooseEmployeeDialog.show(getMainAcitivity().getFragmentManager(), TAG);
                         */
                        break;
                    case R.id.money:
                        showDialogInputmoney(customer.getDocumentId(), customer.getSotien());
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

    private void showDialogAssignToStaff(final String customerDocumentId) {
        final List<User> userList = new ArrayList<>();
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        @SuppressLint("InflateParams") View dialogView = layoutInflater.inflate(R.layout.view_choose_employee, null);
        dialog.setContentView(dialogView);
        final ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
        Utils.setUpProgressBar(progressBar, false);
        final ListView lvUser = dialog.findViewById(R.id.lvUser);
        firebaseFirestore.collection(Constant.COLLECTION_USER)
                .whereEqualTo("role", Constant.ROLE_STAFF)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        User user = documentSnapshot.toObject(User.class);
                        userList.add(user);
                    }
                    progressBar.setVisibility(View.GONE);
                    AdapterAssign adapterAssign = new AdapterAssign(customerDocumentId, getActivity(), userList);
                    lvUser.setAdapter(adapterAssign);
                    adapterAssign.setOnCompleteListener(new IOnCompleteListener() {
                        @Override
                        public void onComplete(String staffName, String staffDocumentId) {
                            dialog.dismiss();
                            bindDataToTable();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
            }
        });
        dialog.show();
    }


    private void showDialogInputmoney(final String documentId, final long oldMoney) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        @SuppressLint("InflateParams") View dialogView = layoutInflater.inflate(R.layout.view_input_money, null);
        dialog.setContentView(dialogView);
        final EditText edt_input = dialog.findViewById(R.id.edt_input);
        View cancelBtn = dialog.findViewById(R.id.cancel);
        View sendBtn = dialog.findViewById(R.id.send);
        TextView tvMoney = dialog.findViewById(R.id.tvMoney);
        tvMoney.setText("Tổng số tiền phải thu: " + Utils.formatCurrency(oldMoney));
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edtText = edt_input.getText().toString().trim();
                if (TextUtils.isEmpty(edtText)) {
                    Toast.makeText(getActivity(), "Bạn chưa nhập số tiền", Toast.LENGTH_SHORT).show();
                    return;
                }

                long newMoney = Integer.parseInt(edtText);
                long updateMoney = oldMoney - newMoney;
                if (newMoney > oldMoney) {
                    Toast.makeText(getActivity(), "Số tiền thu được không thể lớn hơn số tiền đang nợ", Toast.LENGTH_SHORT).show();
                    return;
                }

                WriteBatch writeBatch = firebaseFirestore.batch();
                DocumentReference updateQuoteShareAmount = firebaseFirestore.collection(Constant.COLLECTION_CUSTOMER).document(documentId);
                writeBatch.update(updateQuoteShareAmount, "sotien", updateMoney);
                writeBatch.update(updateQuoteShareAmount, "updateAt", DateTimeUtils.getDateTime());
                writeBatch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity(), getString(R.string.updapte_success), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        bindDataToTable();
                    }
                });
            }
        });


        dialog.show();
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
