package hvasoftware.com.thongtindoino.ui.dialog;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.List;

import hvasoftware.com.thongtindoino.MainActivity;
import hvasoftware.com.thongtindoino.R;
import hvasoftware.com.thongtindoino.model.User;
import hvasoftware.com.thongtindoino.ui.fragment.LoginFragment;
import hvasoftware.com.thongtindoino.utils.Constant;
import hvasoftware.com.thongtindoino.utils.DatabaseUser;
import hvasoftware.com.thongtindoino.utils.DateTimeUtils;
import hvasoftware.com.thongtindoino.utils.Utils;

/**
 * Created by Thanh on 03/10/2018.
 */

@SuppressLint("ValidFragment")
public class ChangePassDialog extends DialogFragment {
    private final String TAG = "ChangePassDialog";
    private EditText edt_pass;
    private EditText edt_new_pass;
    private EditText edt_new_pass2;
    private TextView tvChangePass;
    private DatabaseUser databaseUser;
    private List<User> userList = null;
    private ProgressBar progressBar;
    private User user;
    private FirebaseFirestore firebaseFirestore;
    private MainActivity mainActivity;
    private String userPass = null;

    public ChangePassDialog(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.view_change_pass, container);
        edt_pass = view.findViewById(R.id.edt_pass);
        edt_new_pass = view.findViewById(R.id.edt_new_pass);
        edt_new_pass2 = view.findViewById(R.id.edt_new_pass2);
        tvChangePass = view.findViewById(R.id.tvChangePass);
        progressBar = view.findViewById(R.id.progressBar);
        Utils.setUpProgressBar(progressBar, true);
        firebaseFirestore = FirebaseFirestore.getInstance();
        databaseUser = DatabaseUser.newInstance(getActivity());
        userList = databaseUser.getAllUsers();
        user = userList.get(0);
        userPass = user.getPassword();


        changePass(user.getDocumentId());

        return view;
    }

    private void changePass(final String documentId) {
        tvChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPass = edt_pass.getText().toString().trim();
                String newPass = edt_new_pass.getText().toString().trim();
                String newPass2 = edt_new_pass2.getText().toString().trim();

                if (TextUtils.isEmpty(oldPass)) {
                    Toast.makeText(getActivity(), "Bạn chưa nhập mật khẩu cũ", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!oldPass.equals(userPass)) {
                    Toast.makeText(getActivity(), "Mật khẩu cũ không chính xác", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (TextUtils.isEmpty(newPass)) {
                    Toast.makeText(getActivity(), "Bạn chưa nhập mật khẩu mới", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (newPass.length() < 6) {
                    Toast.makeText(getActivity(), "Mật khẩu phải ít nhất 6 kí tự", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(newPass2)) {
                    Toast.makeText(getActivity(), "Bạn chưa nhập mật khẩu mới", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newPass.equals(newPass2)) {
                    Toast.makeText(getActivity(), "Hai mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                tvChangePass.setVisibility(View.GONE);

                WriteBatch writeBatch = firebaseFirestore.batch();
                DocumentReference updateQuoteShareAmount = firebaseFirestore.collection(Constant.COLLECTION_USER).document(documentId);
                writeBatch.update(updateQuoteShareAmount, "password", newPass2);
                writeBatch.update(updateQuoteShareAmount, "updateAt", DateTimeUtils.getDateTime());
                writeBatch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        mainActivity.SwitchFragment(new LoginFragment(), false);
                        dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        tvChangePass.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
