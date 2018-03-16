package hvasoftware.com.thongtindoino.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import hvasoftware.com.thongtindoino.R;
import hvasoftware.com.thongtindoino.base.BaseFragment;
import hvasoftware.com.thongtindoino.model.Customer;
import hvasoftware.com.thongtindoino.model.User;
import hvasoftware.com.thongtindoino.ui.adapter.AdapterAssign;
import hvasoftware.com.thongtindoino.utils.Constant;
import hvasoftware.com.thongtindoino.utils.DateTimeUtils;
import hvasoftware.com.thongtindoino.utils.IOnCompleteListener;
import hvasoftware.com.thongtindoino.utils.Utils;

/**
 * Created by Thanh on 03/10/2018.
 */

@SuppressWarnings("SpellCheckingInspection")
@SuppressLint("ValidFragment")
public class AddCustomerFragment extends BaseFragment {
    private static final String TAG = "AddCustomerFragment";
    private ScreenType screenType;
    private EditText edt_CustomerName;
    private EditText edt_NgayVay;
    private EditText edt_NgayTra;
    private TextView tvSoNgayVay;
    private EditText edt_SoTienVay;
    private EditText edt_HetHan;
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
    private String customerDocumentId;
    private int soNgayVay = 0;
    private TextView tvCount;
    private String ngayVay = null;
    private String ngayTra = null;


    @SuppressLint("ValidFragment")
    public AddCustomerFragment(ScreenType screenType) {
        this.screenType = screenType;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedInstanceState = this.getArguments();
        if (savedInstanceState != null) {
            customerDocumentId = savedInstanceState.getString(Constant.KEY);
        }
    }

    @Override
    protected void OnViewCreated() {
    }

    @Override
    protected void OnBindView() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        edt_CustomerName = (EditText) findViewById(R.id.edt_obj_name);
        edt_NgayVay = (EditText) findViewById(R.id.edt_take_date);
        edt_NgayTra = (EditText) findViewById(R.id.edt_pay_day);
        tvSoNgayVay = (TextView) findViewById(R.id.edt_day);
        tvSoNgayVay.setText("" + soNgayVay);
        tvCount = (TextView) findViewById(R.id.tvCount);
        edt_SoTienVay = (EditText) findViewById(R.id.edt_money);
        edt_HetHan = (EditText) findViewById(R.id.edt_out);
        edt_Note = (EditText) findViewById(R.id.edt_note);
        edt_Address = (EditText) findViewById(R.id.edt_address);
        edt_Phone = (EditText) findViewById(R.id.edt_phone);
        edt_CMND = (EditText) findViewById(R.id.edt_id);
        tvChooseStaff = (TextView) findViewById(R.id.tvChooseStaff);
        tvChooseStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpNhanVienThu();
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Utils.setUpProgressBar(progressBar, true);
        tvUpload = (TextView) findViewById(R.id.tvUpload);

        if (!TextUtils.isEmpty(customerDocumentId)) {
            bindData();
            tvUpload.setText(R.string.update);
        } else {
            tvUpload.setText(getActivity().getString(R.string.add_customer));
            edt_NgayVay.setText(DateTimeUtils.getDateToday());
        }

        tvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadCustomer();
            }
        });

        tvCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countSoNgayVay();
            }
        });

    }

    private void bindData() {
        firebaseFirestore.collection(Constant.COLLECTION_CUSTOMER)
                .document(customerDocumentId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                Customer customer = document.toObject(Customer.class);
                                staffName = customer.getNhanvienthu();
                                staffDocumentId = customer.getNhanvienthuDocumentId();
                                edt_CustomerName.setText(customer.getTen());
                                edt_NgayVay.setText(customer.getNgayVay());
                                edt_NgayTra.setText(customer.getNgayPhaiTra());
                                edt_SoTienVay.setText("" + customer.getSotien());
                                tvSoNgayVay.setText("" + customer.getSongayvay());
                                edt_HetHan.setText(customer.getHethan());
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
                            AddCustomerFragment.this.staffName = staffName;
                            AddCustomerFragment.this.staffDocumentId = staffDocumentId;
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


    @SuppressLint("SetTextI18n")
    private void countSoNgayVay() {
        ngayVay = edt_NgayVay.getText().toString().trim();
        ngayTra = edt_NgayTra.getText().toString().trim();

        if (TextUtils.isEmpty(ngayVay)) {
            Toast.makeText(getContext(), "Bạn chưa nhập ngày vay", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(ngayTra)) {
            Toast.makeText(getContext(), "Bạn chưa nhập ngày trả", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            soNgayVay = get_count_of_days(ngayVay, ngayTra);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Bạn nhập sai định dạng. Vui lòng nhập lại", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        tvSoNgayVay.setText("" + soNgayVay);
        if (soNgayVay < 0) {
            Toast.makeText(getActivity(), "Số ngày vay không thể nhỏ hơn 0", Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    private void uploadCustomer() {
        long customerSoTienVay;
        String customerName = edt_CustomerName.getText().toString().trim();
        ngayVay = edt_NgayVay.getText().toString().trim();
        ngayTra = edt_NgayTra.getText().toString().trim();
        String hetHan = edt_HetHan.getText().toString().trim();

        String customerGhiChu = edt_Note.getText().toString().trim();
        String customerDiaChi = edt_Address.getText().toString().trim();
        String customerSoDienThoai = edt_Phone.getText().toString().trim();
        String customerCMND = edt_CMND.getText().toString().trim();


        if (TextUtils.isEmpty(customerName)) {
            Toast.makeText(getContext(), "Bạn chưa nhập tên khách hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(ngayVay)) {
            Toast.makeText(getContext(), "Bạn chưa nhập ngày vay", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(ngayTra)) {
            Toast.makeText(getContext(), "Bạn chưa nhập ngày trả", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            soNgayVay = get_count_of_days(ngayVay, ngayTra);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Bạn nhập sai định dạng. Vui lòng nhập lại", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        tvSoNgayVay.setText("" + soNgayVay);
        if (soNgayVay < 0) {
            Toast.makeText(getActivity(), "Số ngày vay không thể nhỏ hơn 0", Toast.LENGTH_SHORT).show();
        }


        if (TextUtils.isEmpty(edt_SoTienVay.getText().toString().trim())) {
            Toast.makeText(getContext(), "Bạn chưa nhập số tiền vay", Toast.LENGTH_SHORT).show();
            return;
        } else {
            customerSoTienVay = Long.valueOf(edt_SoTienVay.getText().toString().trim());
        }

        if (TextUtils.isEmpty(hetHan)) {
            Toast.makeText(getContext(), "Bạn chưa nhập ngày hết hạn", Toast.LENGTH_SHORT).show();
            return;
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

        if (TextUtils.isEmpty(customerDocumentId)) {
            CollectionReference collectionReference = firebaseFirestore.collection(Constant.COLLECTION_CUSTOMER);
            String objectId = Utils.getRandomUUID();
            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("objectID", objectId);
            objectMap.put("documentId", objectId);
            objectMap.put("ten", customerName);
            objectMap.put("ngayVay", ngayVay);
            objectMap.put("ngayPhaiTra", ngayTra);
            objectMap.put("hethan", hetHan);
            objectMap.put("sotien", customerSoTienVay);
            objectMap.put("songayvay", soNgayVay);
            objectMap.put("ghichu", customerGhiChu);
            objectMap.put("diachi", customerDiaChi);
            objectMap.put("sodienthoai", customerSoDienThoai);
            objectMap.put("cmnd", customerCMND);
            objectMap.put("nhanvienthu", staffName);
            objectMap.put("nhanvienthuDocumentId", staffDocumentId);
            objectMap.put("createAt", Utils.getCurrentDateTime());
            objectMap.put("updateAt", Utils.getCurrentDateTime());
            objectMap.put("trangthai", Constant.STATE_ONE);
            collectionReference.document(objectId).set(objectMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Thêm khách hàng thành công", Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Thêm khách hàng thất bại! Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                    Log.wtf(TAG, "==============================>" + e.getMessage());
                    getActivity().onBackPressed();
                }
            });
        } else {
            WriteBatch writeBatch = firebaseFirestore.batch();
            DocumentReference updateCustomer = firebaseFirestore.collection(Constant.COLLECTION_CUSTOMER).document(customerDocumentId);
            writeBatch.update(updateCustomer, "ten", customerName);
            writeBatch.update(updateCustomer, "ngayVay", ngayVay);
            writeBatch.update(updateCustomer, "ngayPhaiTra", ngayTra);
            writeBatch.update(updateCustomer, "hethan", hetHan);
            writeBatch.update(updateCustomer, "sotien", customerSoTienVay);
            writeBatch.update(updateCustomer, "songayvay", soNgayVay);
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
                    getActivity().onBackPressed();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), getActivity().getString(R.string.updapte_failed), Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                    // Log.wtf(TAG, "==============================>" + e.getMessage());
                }
            });
        }
    }

    @Override
    public int GetLayoutId() {
        return R.layout.fragment_add_customer;
    }

    @Override
    protected String getScreenTitle() {
        return getString(screenType == ScreenType.Add ? R.string.add_customer : R.string.view_customer);
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

    public int get_count_of_days(String Created_date_String, String Expire_date_String) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date Created_convertedDate = null, Expire_CovertedDate = null, todayWithZeroTime = null;
        try {
            Created_convertedDate = dateFormat.parse(Created_date_String);
            Expire_CovertedDate = dateFormat.parse(Expire_date_String);

            Date today = new Date();

            todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int c_year = 0, c_month = 0, c_day = 0;

        if (Created_convertedDate.after(todayWithZeroTime)) {
            Calendar c_cal = Calendar.getInstance();
            c_cal.setTime(Created_convertedDate);
            c_year = c_cal.get(Calendar.YEAR);
            c_month = c_cal.get(Calendar.MONTH);
            c_day = c_cal.get(Calendar.DAY_OF_MONTH);

        } else {
            Calendar c_cal = Calendar.getInstance();
            c_cal.setTime(todayWithZeroTime);
            c_year = c_cal.get(Calendar.YEAR);
            c_month = c_cal.get(Calendar.MONTH);
            c_day = c_cal.get(Calendar.DAY_OF_MONTH);
        }


        Calendar e_cal = Calendar.getInstance();
        e_cal.setTime(Expire_CovertedDate);

        int e_year = e_cal.get(Calendar.YEAR);
        int e_month = e_cal.get(Calendar.MONTH);
        int e_day = e_cal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(c_year, c_month, c_day);
        date2.clear();
        date2.set(e_year, e_month, e_day);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();
        float dayCount = (float) diff / (24 * 60 * 60 * 1000);
        return (int) dayCount;
    }

    public enum ScreenType {
        Add, View
    }
}
