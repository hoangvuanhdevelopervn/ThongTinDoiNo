package hvasoftware.com.thongtindoino.ui.fragment;

import android.annotation.SuppressLint;

import hvasoftware.com.thongtindoino.R;
import hvasoftware.com.thongtindoino.base.BaseFragment;

/**
 * Created by Thanh on 03/10/2018.
 */

@SuppressLint("ValidFragment")
public class AddCustomerFragment extends BaseFragment {

    ScreenType screenType;

    public enum ScreenType {
        Add, View
    }

    @SuppressLint("ValidFragment")
    public AddCustomerFragment(ScreenType screenType) {
        this.screenType = screenType;
    }

    @Override
    protected void OnViewCreated() {

    }

    @Override
    protected void OnBindView() {
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
}
