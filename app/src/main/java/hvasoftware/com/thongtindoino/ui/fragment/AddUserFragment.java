package hvasoftware.com.thongtindoino.ui.fragment;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

import hvasoftware.com.thongtindoino.R;
import hvasoftware.com.thongtindoino.base.BaseFragment;
import hvasoftware.com.thongtindoino.bussiness.UserBusiness;
import hvasoftware.com.thongtindoino.utils.Constant;
import hvasoftware.com.thongtindoino.utils.Utils;

/**
 * Created by Thanh on 03/10/2018.
 */

public class AddUserFragment extends BaseFragment {
    private EditText edt_userName;
    private EditText edt_userAccount;
    private EditText edt_userPassword;
    private EditText edt_userInputPassAgain;
    private EditText edt_userAddress;
    private EditText edt_userPhone;
    private ProgressBar progressBar;
    private TextView tvAddUser;
    private UserBusiness userBusiness;


    @Override
    protected void OnViewCreated() {
        userBusiness = UserBusiness.newInstance(getContext());
    }

    @Override
    protected void OnBindView() {
        edt_userName = (EditText) findViewById(R.id.edt_obj_name);
        edt_userAccount = (EditText) findViewById(R.id.edt_take_date);
        edt_userPassword = (EditText) findViewById(R.id.edt_money);
        edt_userInputPassAgain = (EditText) findViewById(R.id.edt_day);
        edt_userAddress = (EditText) findViewById(R.id.edt_address);
        edt_userPhone = (EditText) findViewById(R.id.edt_phone);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Utils.setUpProgressBar(progressBar, true);
        tvAddUser = (TextView) findViewById(R.id.btn_login);
        tvAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userName = edt_userName.getText().toString().trim();
                final String userAccount = edt_userAccount.getText().toString().trim();
                final String userPassword = edt_userPassword.getText().toString().trim();
                String userInputPassAgain = edt_userInputPassAgain.getText().toString().trim();
                final String userAddress = edt_userAddress.getText().toString().trim();
                final String userPhone = edt_userPhone.getText().toString().trim();

                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(getContext(), "Bạn chưa nhập tên nhân viên", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(userAccount)) {
                    Toast.makeText(getContext(), "Bạn chưa nhập tên tài khoản", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!Utils.isValidEmail(userAccount)){
                    Toast.makeText(getContext(), "Email chưa đúng định dạng", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(userPassword) ) {
                    Toast.makeText(getContext(), "Bạn chưa nhập mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(userPassword.length() < 6){
                    Toast.makeText(getContext(), "Mật khẩu phải ít nhất 6 kí tự", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(userInputPassAgain)) {
                    Toast.makeText(getContext(), "Bạn chưa nhập lại mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(userAddress)) {
                    Toast.makeText(getContext(), "Bạn chưa nhập địa chỉ của nhân viên", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(userPhone)) {
                    Toast.makeText(getContext(), "Bạn chưa nhập số điện thoại của nhân viên", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (!userPassword.equals(userInputPassAgain)) {
                    Toast.makeText(getContext(), "Hai mật khẩu không giống nhau! Vui lòng nhập lại", Toast.LENGTH_SHORT).show();
                    return;
                }

                /*
                if (userPhone.length() < 10 || userPhone.length() > 11){
                    Toast.makeText(getContext(), "Định dạng điện thoại không đúng! Vui lòng nhập lại", Toast.LENGTH_SHORT).show();
                    return;
                }
                 */


                progressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(userAccount, userPassword).addOnCompleteListener(getMainAcitivity(),
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    firebaseFirestore.collection(Constant.COLLECTION_USER).document(userAccount)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        if (task.getResult().exists()) {
                                                            Toast.makeText(getActivity(), R.string.account_exists, Toast.LENGTH_SHORT).show();
                                                            progressBar.setVisibility(View.GONE);
                                                        } else {
                                                            CollectionReference collectionReference = firebaseFirestore.collection(Constant.COLLECTION_USER);
                                                            Map<String, Object> objectMap = new HashMap<>();
                                                            objectMap.put("objectID", mAuth.getCurrentUser().getUid());
                                                            objectMap.put("documentId", userAccount);
                                                            objectMap.put("displayName", userName);
                                                            objectMap.put("account", userAccount);
                                                            objectMap.put("password", userPassword);
                                                            objectMap.put("address", userAddress);
                                                            objectMap.put("phone", userPhone);
                                                            objectMap.put("createAt", Utils.getCurrentDateTime());
                                                            objectMap.put("updateAt", Utils.getCurrentDateTime());
                                                            objectMap.put("role", Constant.ROLE_STAFF);
                                                            collectionReference.document(userAccount).set(objectMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    progressBar.setVisibility(View.GONE);
                                                                    Toast.makeText(getContext(), R.string.add_success, Toast.LENGTH_SHORT).show();
                                                                    getActivity().onBackPressed();
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    progressBar.setVisibility(View.GONE);
                                                                    Toast.makeText(getContext(), R.string.add_failed, Toast.LENGTH_SHORT).show();
                                                                    Log.wtf("TAG", "========================> UPLOAD FAILED: " + e.getMessage());
                                                                }
                                                            });
                                                        }
                                                    }
                                                }
                                            });
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getContext(), R.string.add_failed, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }


    @Override
    public int GetLayoutId() {
        return R.layout.fragment_add_user;
    }

    @Override
    protected String getScreenTitle() {
        return getString(R.string.add_user);
    }

    @Override
    public boolean isShowOverFlowMenu() {
        return false;
    }
}
