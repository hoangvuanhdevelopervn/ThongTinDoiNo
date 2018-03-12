package hvasoftware.com.thongtindoino.ui.fragment;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hvasoftware.com.thongtindoino.R;
import hvasoftware.com.thongtindoino.base.BaseFragment;
import hvasoftware.com.thongtindoino.bussiness.CustomerBusiness;
import hvasoftware.com.thongtindoino.model.User;
import hvasoftware.com.thongtindoino.utils.Constant;
import hvasoftware.com.thongtindoino.utils.Utils;

/**
 * Created by Thanh on 03/10/2018.
 */

public class AddCustomerFragment extends BaseFragment {
    private static final String TAG = "AddCustomerFragment";
    private EditText edt_CustomerName;
    private EditText edt_NgayVay;
    private EditText edt_NgayTra;
    private EditText edt_SoTienVay;
    private EditText edt_SoNgayVay;
    private EditText edt_HetDay;
    private EditText edt_Note;
    private EditText edt_Address;
    private EditText edt_Phone;
    private EditText edt_CMND;
    private Spinner spinner_Staff;
    private TextView tvUpload;
    private ProgressBar progressBar;
    private CustomerBusiness customerBusiness;


    @Override
    protected void OnViewCreated() {

    }

    @Override
    protected void OnBindView() {
        edt_CustomerName = (EditText) findViewById(R.id.edt_obj_name);
        edt_NgayVay = (EditText) findViewById(R.id.edt_take_date);
        edt_NgayTra = (EditText) findViewById(R.id.edt_pay_day);
        edt_SoTienVay = (EditText) findViewById(R.id.edt_money);
        edt_SoNgayVay = (EditText) findViewById(R.id.edt_day);
        edt_HetDay = (EditText) findViewById(R.id.edt_out);
        edt_Note = (EditText) findViewById(R.id.edt_note);
        edt_Address = (EditText) findViewById(R.id.edt_address);
        edt_Phone = (EditText) findViewById(R.id.edt_phone);
        edt_CMND = (EditText) findViewById(R.id.edt_id);
        spinner_Staff = (Spinner) findViewById(R.id.spinner_staff);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Utils.setUpProgressBar(progressBar, true);
        tvUpload = (TextView) findViewById(R.id.tvUpload);
        customerBusiness = CustomerBusiness.newInstance(getContext());
        setUpSpiner();


    }

    private void setUpSpiner() {
        final User[] user = new User[1];
        final List<String> stringList = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, stringList);
        firebaseFirestore.collection(Constant.COLLECTION_USER)
                .whereEqualTo("role", Constant.ROLE_STAFF)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        user[0] = documentSnapshot.toObject(User.class);
                        stringList.add(user[0].getDisplayName());
                    }
                    spinner_Staff.setAdapter(arrayAdapter);
                    spinner_Staff.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Log.wtf(TAG, "======================> " + stringList.get(position));
                            user[0].setDisplayName(stringList.get(position));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                    uploadCustomer(user[0]);
                }
            }
        });
    }

    private void uploadCustomer(final User user) {
        tvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String customerName = edt_CustomerName.getText().toString().trim();

                Date customerNgayVay = Utils.parseStringToDate(edt_NgayVay.getText().toString().trim());
                Date customerNgayTra = Utils.parseStringToDate(edt_NgayTra.getText().toString().trim());
                Date customerHetDay = Utils.parseStringToDate(edt_HetDay.getText().toString().trim());


                Double customerSoTienVay = null;
                try {
                    customerSoTienVay = Double.valueOf(edt_SoTienVay.getText().toString().trim());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    //customerSoTienVay = 0d;
                }


                Double customerSoNgayVay = null;
                try {
                    customerSoNgayVay = Double.valueOf(edt_SoNgayVay.getText().toString().trim());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    // customerSoNgayVay = 0d;
                }

                String customerGhiChu = edt_Note.getText().toString().trim();
                String customerDiaChi = edt_Address.getText().toString().trim();
                String customerSoDienThoai = edt_Phone.getText().toString().trim();
                String customerCMND = edt_CMND.getText().toString().trim();


                if (TextUtils.isEmpty(customerName)) {
                    Toast.makeText(getContext(), "Bạn chưa nhập tên khách hàng", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (customerNgayVay == null) {
                    Toast.makeText(getContext(), "Bạn chưa nhập ngày vay", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (customerNgayTra == null) {
                    Toast.makeText(getContext(), "Bạn chưa nhập ngày trả", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (customerSoTienVay == null) {
                    Toast.makeText(getContext(), "Bạn chưa nhập số tiền vay", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (customerSoNgayVay == null) {
                    Toast.makeText(getContext(), "Bạn chưa nhập số ngày vay", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (customerHetDay == null) {
                    Toast.makeText(getContext(), "Bạn chưa nhập hết dây", Toast.LENGTH_SHORT).show();
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
                objectMap.put("hetday", customerHetDay);
                objectMap.put("sotien", customerSoTienVay);
                objectMap.put("songayvay", customerSoNgayVay);
                objectMap.put("ghichu", customerGhiChu);
                objectMap.put("diachi", customerDiaChi);
                objectMap.put("sodienthoai", customerSoDienThoai);
                objectMap.put("cmnd", customerCMND);
                objectMap.put("nhanvienthu", user.getDisplayName());
                objectMap.put("nhanvienthuDocumentId", user.getDocumentId());
                objectMap.put("createAt", Utils.getCurrentDateTime());
                objectMap.put("updateAt", Utils.getCurrentDateTime());
                objectMap.put("trangthai", Constant.STATE_ONE);

                collectionReference.document(objectId).set(objectMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Thêm khách hàng thành công", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Log.wtf(TAG, "==============================>" + e.getMessage());
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
        return getString(R.string.add_customer);
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
}
