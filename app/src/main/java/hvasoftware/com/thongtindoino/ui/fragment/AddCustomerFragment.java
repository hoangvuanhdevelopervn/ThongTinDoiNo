package hvasoftware.com.thongtindoino.ui.fragment;

import hvasoftware.com.thongtindoino.R;
import hvasoftware.com.thongtindoino.base.BaseFragment;

/**
 * Created by Thanh on 03/10/2018.
 */

public class AddCustomerFragment extends BaseFragment {
    @Override
    protected void OnViewCreated() {

    }

    @Override
    protected void OnBindView() {
    }

    @Override
    public int GetLayoutId() {
        return R.layout.add_customer_fragment;
    }

    @Override
    protected String GetScreenTitle() {
        return getString(R.string.add_customer);
    }

    @Override
    public void onResume() {
        super.onResume();
        GetMainAcitivity().setScreenOrientation(true);
    }

    @Override
    public boolean IsMenuVisible() {
        return false;
    }
}
