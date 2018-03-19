package hvasoftware.com.thongtindoino.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hvasoftware.com.thongtindoino.R;
import hvasoftware.com.thongtindoino.base.BaseActivity;
import hvasoftware.com.thongtindoino.base.BaseFragment;
import hvasoftware.com.thongtindoino.model.Customer;
import hvasoftware.com.thongtindoino.model.User;
import hvasoftware.com.thongtindoino.ui.adapter.AdapterAssign;
import hvasoftware.com.thongtindoino.utils.Constant;
import hvasoftware.com.thongtindoino.utils.DateTimeUtils;
import hvasoftware.com.thongtindoino.utils.IOnCompleteListener;
import hvasoftware.com.thongtindoino.utils.Utils;

/**
 * Created by HoangVuAnh on 3/15/18.
 */

public class DetailCustomerFragment extends BaseFragment implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
    private static final String TAG = "DetailCustomerFragment";
    private EditText edt_CustomerName;
    private TextView tvNgayVay;
    private TextView tvNgayHetHan;
    private TextView tvSoNgayVay;
    private EditText edt_SoTienVay;
    private EditText edt_Note;
    private EditText edt_Address;
    private EditText edt_Phone;
    private EditText edt_CMND;
    private TextView tvChooseStaff;
    private TextView tvUpload;
    private ProgressBar progressBar;
    private String staffName = null;
    private String staffDocumentId = null;
    private FirebaseFirestore firebaseFirestore;
    private String ngayVay = null;
    private String ngayHetHan = null;
    private DatePickerDialog datePickerDialog;
    private int soNgayVay = 0;
    private String customerDocumentID;
    private int status;

    public DetailCustomerFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedInstanceState = this.getArguments();
        customerDocumentID = savedInstanceState.getString(Constant.KEY);
    }

    @Override
    protected void OnViewCreated() {

    }

    @Override
    protected void OnBindView() {
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, calendar);
        firebaseFirestore = FirebaseFirestore.getInstance();
        edt_CustomerName = (EditText) findViewById(R.id.edt_obj_name);
        tvNgayVay = (TextView) findViewById(R.id.tvNgayVay);
        tvNgayHetHan = (TextView) findViewById(R.id.tvNgayHetHan);
        tvSoNgayVay = (TextView) findViewById(R.id.edt_day);
        edt_SoTienVay = (EditText) findViewById(R.id.edt_money);
        edt_Note = (EditText) findViewById(R.id.edt_note);
        edt_Address = (EditText) findViewById(R.id.edt_address);
        edt_Phone = (EditText) findViewById(R.id.edt_phone);
        edt_CMND = (EditText) findViewById(R.id.edt_id);
        tvUpload = (TextView) findViewById(R.id.tvUpload);
        tvChooseStaff = (TextView) findViewById(R.id.tvChooseStaff);
        tvChooseStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpNhanVienThu();
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Utils.setUpProgressBar(progressBar, true);
        tvUpload.setText(getActivity().getString(R.string.update));
        tvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCustomer();
            }
        });

        tvUpload.setVisibility(BaseActivity.role.equals(Constant.ROLE_STAFF)? View.GONE : View.VISIBLE);
        tvNgayHetHan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show(getActivity().getFragmentManager(), TAG);
            }
        });

        bindData();
    }


    private void bindData() {
        firebaseFirestore.collection(Constant.COLLECTION_CUSTOMER)
                .document(customerDocumentID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                Customer customer = document.toObject(Customer.class);
                                edt_CustomerName.setText(customer.getTen());

                                ngayVay = customer.getNgayVay();
                                ngayHetHan = customer.getNgayHetHan();
                                soNgayVay = customer.getSongayvay();
                                staffName = customer.getNhanvienthu();
                                staffDocumentId = customer.getNhanvienthuDocumentId();

                                tvNgayVay.setText(ngayVay);
                                tvNgayHetHan.setText(ngayHetHan);
                                tvSoNgayVay.setText("" + soNgayVay);

                                edt_SoTienVay.setText("" + customer.getSotien());
                                edt_Note.setText(customer.getGhichu());
                                edt_Address.setText(customer.getDiachi());
                                edt_Phone.setText(customer.getSodienthoai());
                                edt_CMND.setText(customer.getCmnd());

                                tvChooseStaff.setText(staffName);
                            }
                        }
                    }
                });
    }


    private void setUpNhanVienThu() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        @SuppressLint("InflateParams") View dialogView = layoutInflater.inflate(R.layout.view_choose_employee, null);
        dialog.setContentView(dialogView);

        final ListView lvUser = dialog.findViewById(R.id.lvUser);
        final ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
        Utils.setUpProgressBar(progressBar, false);
        final List<User> userList = new ArrayList<>();
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
                    if (userList.size() == 0) {
                        Toast.makeText(getActivity(), "Không có nhân viên nào", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                    AdapterAssign adapterAssign = new AdapterAssign(null, getActivity(), userList);
                    lvUser.setAdapter(adapterAssign);
                    adapterAssign.setOnCompleteListener(new IOnCompleteListener() {
                        @Override
                        public void onComplete(String staffName, String staffDocumentId) {
                            DetailCustomerFragment.this.staffName = staffName;
                            DetailCustomerFragment.this.staffDocumentId = staffDocumentId;
                            tvChooseStaff.setText(staffName);
                            dialog.dismiss();
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


    private void updateCustomer() {

        long customerSoTienVay;
        String customerName = edt_CustomerName.getText().toString().trim();
        String customerGhiChu = edt_Note.getText().toString().trim();
        String customerDiaChi = edt_Address.getText().toString().trim();
        String customerSoDienThoai = edt_Phone.getText().toString().trim();
        String customerCMND = edt_CMND.getText().toString().trim();


        if (TextUtils.isEmpty(customerName)) {
            Toast.makeText(getContext(), "Bạn chưa nhập tên khách hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        if (soNgayVay < 0) {
            Toast.makeText(getContext(), "Số ngày vay không thể nhỏ hơn 0", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(edt_SoTienVay.getText().toString().trim())) {
            Toast.makeText(getContext(), "Bạn chưa nhập số tiền vay", Toast.LENGTH_SHORT).show();
            return;
        } else {
            customerSoTienVay = Long.valueOf(edt_SoTienVay.getText().toString().trim());
        }

        if (TextUtils.isEmpty(customerGhiChu)) {
            Toast.makeText(getContext(), "Bạn chưa nhập ghi chú", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(customerDiaChi)) {
            Toast.makeText(getContext(), "Bạn chưa nhập địa chỉ của khách hàng", Toast.LENGTH_SHORT).show();
            return;
        }


        if (TextUtils.isEmpty(customerSoDienThoai)) {
            Toast.makeText(getContext(), "Bạn chưa nhập số điện thoại của khách hàng", Toast.LENGTH_SHORT).show();
            return;
        }


        if (TextUtils.isEmpty(customerCMND)) {
            Toast.makeText(getContext(), "Bạn chưa nhập số chứng minh nhân dân", Toast.LENGTH_SHORT).show();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);


        WriteBatch writeBatch = firebaseFirestore.batch();
        DocumentReference updateCustomer = firebaseFirestore.collection(Constant.COLLECTION_CUSTOMER).document(customerDocumentID);
        writeBatch.update(updateCustomer, "ten", customerName);
        writeBatch.update(updateCustomer, "ngayVay", ngayVay);
        writeBatch.update(updateCustomer, "ngayHetHan", ngayHetHan);

        writeBatch.update(updateCustomer, "sotien", customerSoTienVay);
        writeBatch.update(updateCustomer, "songayvay", soNgayVay);
        writeBatch.update(updateCustomer, "dayleft", Utils.daysBetween(DateTimeUtils.getDateTime(), Utils.parseStringToDate(ngayHetHan)));

        writeBatch.update(updateCustomer, "ghichu", customerGhiChu);
        writeBatch.update(updateCustomer, "diachi", customerDiaChi);
        writeBatch.update(updateCustomer, "sodienthoai", customerSoDienThoai);
        writeBatch.update(updateCustomer, "cmnd", customerCMND);
        writeBatch.update(updateCustomer, "nhanvienthu", staffName);
        writeBatch.update(updateCustomer, "nhanvienthuDocumentId", staffDocumentId);
        writeBatch.update(updateCustomer, "updateAt", Utils.getCurrentDateTime());
        writeBatch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), getActivity().getString(R.string.updapte_success), Toast.LENGTH_SHORT).show();
                SwitchFragment(new DeptFragment(), false);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), getActivity().getString(R.string.updapte_failed), Toast.LENGTH_SHORT).show();
                SwitchFragment(new DeptFragment(), false);
                // Log.wtf(TAG, "==============================>" + e.getMessage());
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        ngayHetHan = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
        tvNgayHetHan.setText(ngayHetHan);
        //soNgayVay = Utils.get_count_of_days(ngayVay, ngayHetHan);
        soNgayVay = Utils.daysBetween(Utils.parseStringToDate(ngayVay), Utils.parseStringToDate(ngayHetHan));
        tvSoNgayVay.setText("" + soNgayVay);
    }

    @Override
    public int GetLayoutId() {
        return R.layout.fragment_add_customer;
    }

    @Override
    public void onResume() {
        super.onResume();
        getMainAcitivity().setScreenOrientation(true);
    }

    @Override
    public boolean isShowOverFlowMenu() {
        return false;
    }

    @Override
    protected String getScreenTitle() {
        return getString(R.string.view_customer);
    }

}
