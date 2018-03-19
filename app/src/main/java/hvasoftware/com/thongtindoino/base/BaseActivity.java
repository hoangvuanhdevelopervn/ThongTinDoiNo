package hvasoftware.com.thongtindoino.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import hvasoftware.com.thongtindoino.utils.Constant;
import hvasoftware.com.thongtindoino.utils.DatabaseUser;
import hvasoftware.com.thongtindoino.utils.FragmentHelper;


/**
 * Created by Thanh on 03/07/2018.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    public static String role;
    private boolean isVisible;

    protected String GetScreenTitle() {
        return null;
    }

    protected abstract void OnViewCreated();

    protected abstract void OnBindView();

    protected abstract int GetLayoutId();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(GetLayoutId());
        SetUpBaseView();
        OnBindView();
        OnViewCreated();
    }

    private void SetUpBaseView() {
        DatabaseUser databaseUser = DatabaseUser.newInstance(BaseActivity.this);
        if (databaseUser.getAllUsers().size() > 0) {
            role = databaseUser.getAllUsers().get(0).getRole();
            Log.wtf(TAG, "============================>: " + role);
        } else {
            role = Constant.ROLE_STAFF;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        isVisible = true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isVisible = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isVisible = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isVisible = true;
    }

    public boolean isActivityActive() {
        return isVisible;
    }

    public void SwitchFragment(final Fragment fragment, final boolean IsAddToBackStack) {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        FragmentHelper.SwitchFragment(getSupportFragmentManager(),fragment,IsAddToBackStack);
                    }
                }

        );
    }

    public void StartFragmentClearTop(Fragment fragment,boolean IsAddToBackStack){
        FragmentHelper.StartFragmentClearTop(getSupportFragmentManager(),fragment,IsAddToBackStack);
    }


}