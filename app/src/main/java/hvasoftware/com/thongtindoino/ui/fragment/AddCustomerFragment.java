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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import hvasoftware.com.thongtindoino.bussiness.CustomerBusiness;
import hvasoftware.com.thongtindoino.model.Customer;
import hvasoftware.com.thongtindoino.model.User;
import hvasoftware.com.thongtindoino.ui.adapter.AdapterAssign;
import hvasoftware.com.thongtindoino.ui.dialog.ChooseEmployeeDialog;
import hvasoftware.com.thongtindoino.utils.Constant;
import hvasoftware.com.thongtindoino.utils.DateTimeUtils;
import hvasoftware.com.thongtindoino.utils.IOnCompleteListener;
import hvasoftware.com.thongtindoino.utils.Utils;

/**
 * Created by Thanh on 03/10/2018.
 */

@SuppressLint("ValidFragment")
public class AddCustomerFragment extends BaseFragment implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
    private static final String TAG = "AddCustomerFragment";
    private ScreenType screenType;
    private EditText edt_CustomerName;
    private TextView tvNgayVay;
    private TextView tvNgayTra;
    private TextView tvSoNgayVay;
    private EditText edt_SoTienVay;
    private TextView tvHetHan;
    private EditText edt_Note;
    private EditText edt_Address;
    private EditText edt_Phone;
    private EditText edt_CMND;
    private TextView tvChooseStaff;
    private TextView tvUpload;
    private ProgressBar progressBar;
    private DatePickerDialog ngayVay;
    private DatePickerDialog ngayTra;
    private DatePickerDialog hetHan;
    private String userName = null;
    private String userDocumentId = null;
    private FirebaseFirestore firebaseFirestore;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ValidFragment")
    public AddCustomerFragment(ScreenType screenType) {
        this.screenType = screenType;
    }

    @Override
    protected void OnViewCreated() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        Calendar calendar = Calendar.getInstance();
        ngayVay = DatePickerDialog.newInstance(this, calendar);
        ngayTra = DatePickerDialog.newInstance(this, calendar);
        hetHan = DatePickerDialog.newInstance(this, calendar);
    }

    @Override
    protected void OnBindView() {
        edt_CustomerName = (EditText) findViewById(R.id.edt_obj_name);
        tvNgayVay = (TextView) findViewById(R.id.edt_take_date);
        tvNgayVay.setText(DateTimeUtils.getDateToday());
        tvNgayTra = (TextView) findViewById(R.id.edt_pay_day);
        tvSoNgayVay = (TextView) findViewById(R.id.edt_day);
        edt_SoTienVay = (EditText) findViewById(R.id.edt_money);
        tvHetHan = (TextView) findViewById(R.id.edt_out);
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

        tvNgayVay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ngayVay.show(getActivity().getFragmentManager(), TAG);
            }
        });

        tvNgayTra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ngayTra.show(getActivity().getFragmentManager(), TAG);
            }
        });

        tvHetHan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hetHan.show(getActivity().getFragmentManager(), TAG);
            }
        });
        uploadCustomer();
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
                    progressBar.setVisibility(View.GONE);
                    AdapterAssign adapterAssign = new AdapterAssign("", getActivity(), userList);
                    lvUser.setAdapter(adapterAssign);
                    adapterAssign.setOnCompleteListener(new IOnCompleteListener() {
                        @Override
                        public void onComplete(String staffName, String staffDocumentId) {
                            userName = staffName;
                            userDocumentId = staffDocumentId;
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

    @SuppressLint("ClickableViewAccessibility")
    private void uploadCustomer() {
        tvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String customerName = edt_CustomerName.getText().toString().trim();

                Date customerNgayVay = Utils.parseStringToDate(tvNgayVay.getText().toString().trim());
                Date customerNgayTra = Utils.parseStringToDate(tvNgayTra.getText().toString().trim());
                Date customerHetHan = Utils.parseStringToDate(tvHetHan.getText().toString().trim());


                int customerSoTienVay = 0;
                try {
                    customerSoTienVay = Integer.valueOf(edt_SoTienVay.getText().toString().trim());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }


                int customerSoNgayVay = Integer.parseInt(tvSoNgayVay.getText().toString());

                String customerGhiChu = edt_Note.getText().toString().trim();
                String customerDiaChi = edt_Address.getText().toString().trim();
                String customerSoDienThoai = edt_Phone.getText().toString().trim();
                String customerCMND = edt_CMND.getText().toString().trim();


                if (TextUtils.isEmpty(customerName)) {
                    Toast.makeText(getContext(), "Bạn chưa nhập tên khách hàng", Toast.LENGTH_SHORT).show();
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
                CollectionReference collectionReference = firebaseFirestore.collection(Constant.COLLECTION_CUSTOMER);


                String objectId = Utils.getRandomUUID();
                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("objectID", objectId);
                objectMap.put("documentId", objectId);
                objectMap.put("ten", customerName);
                objectMap.put("ngayVay", customerNgayVay);
                objectMap.put("ngayPhaiTra", customerNgayTra);
                objectMap.put("hethan", customerHetHan);
                objectMap.put("sotien", customerSoTienVay);
                objectMap.put("songayvay", customerSoNgayVay);
                objectMap.put("ghichu", customerGhiChu);
                objectMap.put("diachi", customerDiaChi);
                objectMap.put("sodienthoai", customerSoDienThoai);
                objectMap.put("cmnd", customerCMND);
                objectMap.put("nhanvienthu", userName);
                objectMap.put("nhanvienthuDocumentId", userDocumentId);
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int mStartYear = 0, mStartMonth = 0, mStartDay = 0;
        int mEndYear = 0, mEndMonth = 0, mEndDay = 0;
        if (view == ngayVay) {
            mStartYear = year;
            mStartMonth = monthOfYear + 1;
            mStartDay = dayOfMonth;
            tvNgayVay.setText(mStartDay + "/" + (mStartMonth) + "/" + mStartYear);
        } else if (view == ngayTra) {
            mEndYear = year;
            mEndMonth = monthOfYear + 1;
            mEndDay = dayOfMonth;
            tvNgayTra.setText(mEndDay + "/" + (mEndMonth) + "/" + mEndYear);
        } else {
            tvHetHan.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
        }

        String startDate = mStartDay + "/" + mStartMonth + "/" + mStartYear;
        String endDate = mEndDay + "/" + mEndMonth + "/" + mEndYear;
        tvSoNgayVay.setText(get_count_of_days(startDate, endDate));
    }

    public String get_count_of_days(String Created_date_String, String Expire_date_String) {
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
        return ("" + (int) dayCount + " Ngày");
    }

    public enum ScreenType {
        Add, View
    }
}
