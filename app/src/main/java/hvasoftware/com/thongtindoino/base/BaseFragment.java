package hvasoftware.com.thongtindoino.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import hvasoftware.com.thongtindoino.MainActivity;
import hvasoftware.com.thongtindoino.R;
import hvasoftware.com.thongtindoino.utils.Constant;
import hvasoftware.com.thongtindoino.utils.DatabaseUser;
import hvasoftware.com.thongtindoino.utils.FragmentHelper;
import hvasoftware.com.thongtindoino.utils.Utils;


/**
 * Created by Thanh on 03/07/2018.
 */

public abstract class BaseFragment extends Fragment implements View.OnTouchListener {
    public View rootView;
    public FirebaseFirestore firebaseFirestore;
    public String role = null;
    private DatabaseUser databaseUser;
    public String userName;
    public FirebaseAuth mAuth;

    protected abstract void OnViewCreated();

    protected abstract void OnBindView();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(GetLayoutId(), container, false);
            firebaseFirestore = FirebaseFirestore.getInstance();
            mAuth = FirebaseAuth.getInstance();
            databaseUser = DatabaseUser.newInstance(getContext());
            if (databaseUser.getAllUsers().size() > 0) {
                role = databaseUser.getAllUsers().get(0).getRole();
                userName = databaseUser.getAllUsers().get(0).getDisplayName();
            } else {
                role = Constant.ROLE_STAFF;
            }
            OnBindView();
            OnViewCreated();
        }
        setupUI(rootView);
        return rootView;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (FragmentHelper.sDisableFragmentAnimations) {
            AlphaAnimation a = new AlphaAnimation(getContext(), null);
            a.setDuration(0);
            return a;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    public abstract int GetLayoutId();

    public View findViewById(int resId) {
        return rootView.findViewById(resId);
    }

    public void SwitchFragment(Fragment fragment, boolean IsAddToBackStack) {
        getMainAcitivity().SwitchFragment(fragment, IsAddToBackStack);
    }

    public void ReloadFragment() {
        FragmentHelper.ReloadFragment(getFragmentManager(), this);
    }

    public void FinishFragment() {
        FragmentHelper.PopBackStack(getMainAcitivity().getSupportFragmentManager());
    }

    public Fragment GetCurrentFragment() {
        return getMainAcitivity().getSupportFragmentManager().findFragmentById(R.id.root);
    }

    protected MainActivity getMainAcitivity() {
        if (getActivity() instanceof MainActivity) {
            return ((MainActivity) getActivity());
        }
        return null;
    }

    public void setupUI(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(this);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                boolean isNeedTouch = true;
                isNeedTouch = innerView.getId() != R.id.btn_login;
                if (isNeedTouch) {
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

    public boolean OnTouchView(View v, MotionEvent e) {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            getMainAcitivity().setScreenTitle(getScreenTitle());
            getMainAcitivity().setHeaderVisible(isHeaderVisible());
            getMainAcitivity().setBackButtonVisible(isBackButtonVisible());
            getMainAcitivity().setMenuVisible(isShowOverFlowMenu());
            getMainAcitivity().setFloatButtonVisible(isFloatButtonVisible());
            getMainAcitivity().setToolbarVisible(isToolbarVisible());
            getMainAcitivity().setImvAddUserVisible(isImvAddUserVisible());
            getMainAcitivity().setTvTotalVisibile(isTotalNumberVisible());

        } catch (Exception e) {

        }
    }

    public boolean isTotalNumberVisible() {
        return false;
    }

    public boolean isImvAddUserVisible() {
        return false;
    }

    public boolean isFloatButtonVisible() {
        return false;
    }

    public boolean isToolbarVisible() {
        return true;
    }

    public boolean isShowOverFlowMenu() {
        return true;
    }

    public boolean isBackButtonVisible() {
        return true;
    }

    public boolean isHeaderVisible() {
        return true;
    }

    protected String getScreenTitle() {
        return "";
    }

    public boolean onBackPress() {
        return false;
    }
}