package hvasoftware.com.thongtindoino.ui.fragment;

import android.view.View;
import android.widget.EditText;

import hvasoftware.com.thongtindoino.R;
import hvasoftware.com.thongtindoino.base.BaseFragment;
import hvasoftware.com.thongtindoino.utils.Utils;

/**
 * Created by Thanh on 03/07/2018.
 */

public class LoginFragment extends BaseFragment {

    EditText edtAccount, edtPass;
    View btnLogin;

    @Override
    protected void OnViewCreated() {
    }

    @Override
    protected void OnBindView() {
        edtAccount = (EditText) findViewById(R.id.edt_acc);
        edtPass = (EditText) findViewById(R.id.edt_pass);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.HideSoftKeyboard(getContext(), btnLogin);
                SwitchFragment(new DeptFragment(), false);
            }
        });
    }

    @Override
    public int GetLayoutId() {
        return R.layout.login_fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean IsHeaderVisible() {
        return false;
    }

    @Override
    public boolean IsToolbarVisible() {
        return false;
    }


}
