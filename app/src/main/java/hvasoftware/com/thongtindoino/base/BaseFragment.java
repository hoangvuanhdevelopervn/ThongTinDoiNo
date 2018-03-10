package hvasoftware.com.thongtindoino.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hvasoftware.com.thongtindoino.MainActivity;
import hvasoftware.com.thongtindoino.R;
import hvasoftware.com.thongtindoino.utils.FragmentHelper;

/**
 * Created by Thanh on 03/07/2018.
 */

public abstract class BaseFragment extends Fragment {
    public View rootView;

    protected abstract void OnViewCreated();

    protected abstract void OnBindView();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(GetLayoutId(), container, false);
            OnBindView();
            OnViewCreated();
        }
        return rootView;
    }

    public abstract int GetLayoutId();

    public View FindViewById(int resId) {
        return rootView.findViewById(resId);
    }

    public void SwitchFragment(Fragment fragment, boolean IsAddToBackStack) {
        GetMainAcitivity().SwitchFragment(fragment, IsAddToBackStack);
    }

    public void ReloadFragment() {
        FragmentHelper.ReloadFragment(getFragmentManager(), this);
    }

    public void FinishFragment() {
        FragmentHelper.PopBackStack(GetMainAcitivity().getSupportFragmentManager());
    }

    public Fragment GetCurrentFragment() {
        return GetMainAcitivity().getSupportFragmentManager().findFragmentById(R.id.root);
    }

    protected MainActivity GetMainAcitivity() {
        if (getActivity() instanceof MainActivity) {
            return ((MainActivity) getActivity());
        }
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            GetMainAcitivity().SetScreenTitle(GetScreenTitle());
            GetMainAcitivity().setHeaderVisible(IsHeaderVisible());
            GetMainAcitivity().setBackButtonVisible(IsBackButtonVisible());
            GetMainAcitivity().setMenuVisible(IsMenuVisible());
        } catch (Exception e) {

        }
    }

    public boolean IsMenuVisible() {
        return false;
    }

    public boolean IsBackButtonVisible() {
        return false;
    }

    public boolean IsHeaderVisible() {
        return true;
    }

    protected String GetScreenTitle() {
        return "";
    }

    public boolean OnBackPress() {
        return false;
    }
}