package hvasoftware.com.thongtindoino.utils;
import android.content.OperationApplicationException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import java.util.Calendar;

import hvasoftware.com.thongtindoino.R;


/**
 * Created by Thanh on 03/07/2018.
 */

public class FragmentHelper {
    public static String LAST_FRAGMENT_TAG;

    public static void SwitchFragment(FragmentManager fm, Fragment fragment, boolean IsAddToBackStack) {
        try {
            if (fragment == null || fm == null) {
                throw new OperationApplicationException("FragmentManager null or fragment null");
            }

            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            Calendar c = Calendar.getInstance();
            String tag = fragment.getTag() + c.get(Calendar.MILLISECOND);
            Fragment lastFragment = fm.findFragmentByTag(LAST_FRAGMENT_TAG);
            //if(tag == LAST_FRAGMENT_TAG && lastFragment!= null && lastFragment.IsVisible)
            //{
            //    //the same fragment is displaying, so return to avoid UI bug
            //    return;
            //}

            if (IsAddToBackStack) {
                fragmentTransaction.addToBackStack(null);
            } else {
                //if last fragment is not add to backstack, so remove it to avoid UI bug
                if (lastFragment != null) {
                    fragmentTransaction.remove(lastFragment);
                }
            }

            fragmentTransaction.replace(R.id.root, fragment, tag);
            //the fragment incoming will be set as last fragment
            LAST_FRAGMENT_TAG = tag;
            fragmentTransaction.commitAllowingStateLoss();
        } catch (Exception e) {

        }
    }

    public static void ReloadFragment(FragmentManager fm, Fragment fragment) {
        if (fragment == null || fm == null) {
            try {
                throw new OperationApplicationException("FragmentManager null or fragment null");
            } catch (OperationApplicationException e) {
                e.printStackTrace();
            }
        }
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.detach(fragment);
        fragmentTransaction.attach(fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public static void RemoveLastFragment(FragmentManager fm) {
        Fragment lastFragment = fm.findFragmentByTag(LAST_FRAGMENT_TAG);
        if (lastFragment != null) {
            fm.beginTransaction().remove(lastFragment).commitAllowingStateLoss();
        }
        LAST_FRAGMENT_TAG = fm.findFragmentById(R.id.root).getTag();
    }

    public static void ReloadFragment(FragmentManager fm, int fragmentId) {
        if (fm == null) {
            try {
                throw new OperationApplicationException("FragmentManager null");
            } catch (OperationApplicationException e) {
                e.printStackTrace();
            }
        }
        Fragment currentFragment = fm.findFragmentById(fragmentId);
        if (currentFragment != null) {
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.detach(currentFragment);
            fragmentTransaction.attach(currentFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    public static void PopBackStack(FragmentManager fm) {
        try {
            fm.popBackStack();
            RemoveLastFragment(fm);
        } catch (Exception e) {
        }
    }

    public static void StartFragmentClearTop(FragmentManager fm, Fragment fragment, boolean IsAddToBackStack) {
        try {
            if (fm == null || fragment == null) {
                return;
            }
            //fm.PopBackStack(null, FragmentManager.PopBackStackInclusive);
            while (fm.getBackStackEntryCount() > 0) {
                fm.popBackStackImmediate();
            }
            Calendar c = Calendar.getInstance();
            String tag = fragment.getTag() + c.get(Calendar.MILLISECOND);
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.root, fragment, tag);
            ft.commitAllowingStateLoss();
        } catch (Exception e) {
        }
    }
}