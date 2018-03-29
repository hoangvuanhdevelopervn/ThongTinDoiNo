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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hvasoftware.com.thongtindoino.R;
import hvasoftware.com.thongtindoino.base.BaseFragment;
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
public class AddCustomerFragment extends BaseFragment implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
    private static final String TAG = "AddCustomerFragment";
    private ScreenType screenType;
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

    private DatePickerDialog ngayVayDatePickerDialog;
    private DatePickerDialog ngayHetHanDatePickerDialog;

    private int soNgayVay = 0;
    private Timestamp ngayVayDate;
    Calendar cal = Calendar.getInstance();
    @SuppressLint("ValidFragment")
    public AddCustomerFragment(ScreenType screenType) {
        this.screenType = screenType;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND,0);
    }

    @Override
    protected void OnViewCreated() {
    }


    @Override
    protected void OnBindView() {
        Calendar calendar = Calendar.getInstance();
        ngayVayDatePickerDialog = DatePickerDialog.newInstance(this, calendar);
        ngayHetHanDatePickerDialog = DatePickerDialog.newInstance(this, calendar);
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
        ngayVay = DateTimeUtils.getDateToday();
        ngayVayDate = new Timestamp(cal.getTime().getTime());
        ngayHetHan = DateTimeUtils.getDateTodayOneMonthLater();
        tvUpload.setText(getActivity().getString(R.string.add_customer));
        tvNgayVay.setText(ngayVay);
        tvNgayHetHan.setText(ngayHetHan);

        tvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadCustomer();
            }
        });

        tvNgayHetHan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ngayHetHanDatePickerDialog.show(getActivity().getFragmentManager(), TAG);
            }
        });

        tvNgayVay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ngayVayDatePickerDialog.show(getActivity().getFragmentManager(), TAG);
            }
        });

        soNgayVay = Utils.daysBetween(Utils.parseStringToDate(ngayVay), Utils.parseStringToDate(ngayHetHan));
        tvSoNgayVay.setText("" + soNgayVay);

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

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    private void uploadCustomer() {
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

        //noinspection EqualsBetweenInconvertibleTypes
        if (TextUtils.isEmpty(staffName)) {
            Toast.makeText(getContext(), "Bạn chưa chọn nhân viên thu", Toast.LENGTH_SHORT).show();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);

        CollectionReference collectionReference = firebaseFirestore.collection(Constant.COLLECTION_CUSTOMER);
        String objectId = Utils.getRandomUUID();
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("objectID", objectId);
        objectMap.put("documentId", objectId);
        objectMap.put("ten", customerName);
        objectMap.put("ngayVay", ngayVay);
        objectMap.put("ngayHetHan", ngayHetHan);

        objectMap.put("ngayVayDate",ngayVayDate);
        objectMap.put("dayleft", soNgayVay);
        objectMap.put("songayvay", soNgayVay);
        objectMap.put("sotien", customerSoTienVay);
        objectMap.put("trangthai", Constant.STATE_ONE);


        objectMap.put("ghichu", customerGhiChu);
        objectMap.put("diachi", customerDiaChi);
        objectMap.put("sodienthoai", customerSoDienThoai);
        objectMap.put("cmnd", customerCMND);
        objectMap.put("nhanvienthu", staffName);
        objectMap.put("nhanvienthuDocumentId", staffDocumentId);
        objectMap.put("createAt", Utils.getCurrentDateTime());
        objectMap.put("updateAt", Utils.getCurrentDateTime());

        collectionReference.document(objectId).set(objectMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Thêm khách hàng thành công", Toast.LENGTH_SHORT).show();
                        SwitchFragment(new DeptFragment(), false);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Thêm khách hàng thất bại! Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                //Log.wtf(TAG, "==============================>" + e.getMessage());
                SwitchFragment(new DeptFragment(), false);
            }
        });
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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if (view == ngayHetHanDatePickerDialog) {
            ngayHetHan = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
            tvNgayHetHan.setText(ngayHetHan);
        } else {
            ngayVay = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
            tvNgayVay.setText(ngayVay);
        }

        //soNgayVay = Utils.get_count_of_days(ngayVay, ngayHetHan);
        soNgayVay = Utils.daysBetween(Utils.parseStringToDate(ngayVay), Utils.parseStringToDate(ngayHetHan));
        tvSoNgayVay.setText("" + soNgayVay);
    }

    public enum ScreenType {
        Add, View
    }
}
