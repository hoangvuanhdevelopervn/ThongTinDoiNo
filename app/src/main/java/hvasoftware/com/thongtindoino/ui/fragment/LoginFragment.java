package hvasoftware.com.thongtindoino.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import hvasoftware.com.thongtindoino.R;
import hvasoftware.com.thongtindoino.base.BaseFragment;
import hvasoftware.com.thongtindoino.model.User;
import hvasoftware.com.thongtindoino.utils.CheckInternet;
import hvasoftware.com.thongtindoino.utils.Constant;
import hvasoftware.com.thongtindoino.utils.DatabaseUser;
import hvasoftware.com.thongtindoino.utils.Utils;

/**
 * Created by Thanh on 03/07/2018.
 */

public class LoginFragment extends BaseFragment {
    private static final String TAG = "LoginFragment";
    private EditText edtAccount, edtPass;
    private View btnLogin;
    private ProgressBar progressBar;
    private DatabaseUser databaseUser;
    private CheckInternet checkInternet;

    @Override
    protected void OnViewCreated() {

    }

    @Override
    protected void OnBindView() {
        checkInternet = CheckInternet.getInstance(getContext());
        databaseUser = DatabaseUser.newInstance(getContext());
        edtAccount = (EditText) findViewById(R.id.edt_acc);
        edtPass = (EditText) findViewById(R.id.edt_pass);
        btnLogin = findViewById(R.id.btn_login);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Utils.setUpProgressBar(progressBar, true);
         SwitchFragment(new DeptFragment(), false);

        if (!checkInternet.isOnline()) {
            showDialogNoInternet();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkInternet.isOnline()) {
                    showDialogNoInternet();
                    return;
                }

                Utils.HideSoftKeyboard(getContext(), btnLogin);
                final String account = edtAccount.getText().toString().trim();
                final String password = edtPass.getText().toString().trim();

                if (TextUtils.isEmpty(account)) {
                    Toast.makeText(getContext(), R.string.not_input_account, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getContext(), R.string.not_input_password, Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                firebaseFirestore.collection(Constant.COLLECTION_USER)
                        .document(account).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                User user = document.toObject(User.class);
                                if (account.equals(user.getAccount()) && password.equals(user.getPassword())) {
                                    Toast.makeText(getContext(), R.string.login_success, Toast.LENGTH_SHORT).show();
                                    //SwitchFragment(new DeptFragment(), false);
                                    databaseUser.insertUser(user);
                                } else {
                                    Toast.makeText(getContext(), R.string.wrong_account_pass_check_again, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getContext(), R.string.account_not_exitsts, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), R.string.error_check_connected, Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), R.string.error_check_connected, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showDialogNoInternet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Không kết nối!")
                .setMessage("Vui lòng bật kết nối để tiếp tục sử dụng")
                .setNegativeButton("Bật", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                    }
                }).setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                getMainAcitivity().onBackPressed();
            }
        }).create().show();
    }

    @Override
    public int GetLayoutId() {
        return R.layout.login_fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        getMainAcitivity().setScreenOrientation(true);
    }

    @Override
    public boolean isHeaderVisible() {
        return false;
    }

    @Override
    public boolean isToolbarVisible() {
        return false;
    }


}
