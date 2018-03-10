package hvasoftware.com.thongtindoino.ui.fragment;

import hvasoftware.com.thongtindoino.R;
import hvasoftware.com.thongtindoino.base.BaseFragment;

/**
 * Created by Thanh on 03/10/2018.
 */

public class AddUserFragment extends BaseFragment {
    @Override
    protected void OnViewCreated() {

    }

    @Override
    protected void OnBindView() {

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
