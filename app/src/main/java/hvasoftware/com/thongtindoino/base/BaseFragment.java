package hvasoftware.com.thongtindoino.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import hvasoftware.com.thongtindoino.MainActivity;
import hvasoftware.com.thongtindoino.R;
import hvasoftware.com.thongtindoino.utils.FragmentHelper;
import hvasoftware.com.thongtindoino.utils.Utils;


/**
 * Created by Thanh on 03/07/2018.
 */

public abstract class BaseFragment extends Fragment implements View.OnTouchListener {
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
        setupUI(rootView);
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

    public void setupUI(View view)
    {
        if (!(view instanceof EditText))
        {
            view.setOnTouchListener(this);
        }
        if (view instanceof ViewGroup)
        {
            for (int i = 0; i < ((ViewGroup)view).getChildCount(); i++)
            {
                View innerView = ((ViewGroup)view).getChildAt(i);
                boolean isNeedTouch = true;
                if (innerView.getId() == R.id.btn_login)
                {
                    isNeedTouch = false;
                }
                else
                {
                    isNeedTouch = true;
                }
                if (isNeedTouch)
                {
                    setupUI(innerView);
                }
            }
        }
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        OnTouchView(view, motionEvent);
        Utils.HideSoftKeyboard(getContext(), view);
        return false;
    }

    public  boolean OnTouchView(View v, MotionEvent e)
    {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            GetMainAcitivity().SetScreenTitle(GetScreenTitle());
            GetMainAcitivity().setHeaderVisible(IsHeaderVisible());
            GetMainAcitivity().setBackButtonVisible(IsBackButtonVisible());
            GetMainAcitivity().setMenuVisible(IsMenuVisible());
            GetMainAcitivity().setFloatButtonVisible(IsFloatButtonVisible());
            GetMainAcitivity().setToolbarVisible(IsToolbarVisible());
        } catch (Exception e) {

        }
    }

    public boolean IsFloatButtonVisible() {
        return false;
    }

    public boolean IsToolbarVisible() {
        return true;
    }

    public boolean IsMenuVisible() {
        return true;
    }

    public boolean IsBackButtonVisible() {
        return true;
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