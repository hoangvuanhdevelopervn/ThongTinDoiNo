package hvasoftware.com.thongtindoino;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import hvasoftware.com.thongtindoino.base.BaseActivity;
import hvasoftware.com.thongtindoino.base.BaseFragment;
import hvasoftware.com.thongtindoino.model.User;
import hvasoftware.com.thongtindoino.ui.adapter.AdapterAssign;
import hvasoftware.com.thongtindoino.ui.dialog.ChangePassDialog;
import hvasoftware.com.thongtindoino.ui.dialog.DateSortDialog;
import hvasoftware.com.thongtindoino.ui.fragment.AddCustomerFragment;
import hvasoftware.com.thongtindoino.ui.fragment.AddUserFragment;
import hvasoftware.com.thongtindoino.ui.fragment.DeptFragment;
import hvasoftware.com.thongtindoino.ui.fragment.EmployeeManageFragment;
import hvasoftware.com.thongtindoino.ui.fragment.LoginFragment;
import hvasoftware.com.thongtindoino.utils.Constant;
import hvasoftware.com.thongtindoino.utils.FragmentHelper;
import hvasoftware.com.thongtindoino.utils.IOnCompleteListener;
import hvasoftware.com.thongtindoino.utils.Utils;


public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    View imvBack;
    View imvAddUser;
    TextView tvTitle;
    android.support.v7.widget.Toolbar MainToolbar;
    boolean isMenuVisible;
    FloatingActionButton fab, fab1, fab2, fab3, fab4;
    //Animations
    Animation show_fab_1, hide_fab_1, show_fab_2, hide_fab_2, show_fab_3, hide_fab_3;
    View wrapFabUser, wrapFabStatus, wrapFabDate, wrapFabReset;
    private FirebaseFirestore firebaseFirestore;
    private boolean FAB_Status = false;


    @Override
    protected String GetScreenTitle() {
        return super.GetScreenTitle();
    }

    @Override
    protected void OnViewCreated() {
    }

    @Override
    protected void OnBindView() {

        firebaseFirestore = FirebaseFirestore.getInstance();
        wrapFabUser = findViewById(R.id.wrap_fab1);
        wrapFabStatus = findViewById(R.id.wrap_fab2);
        wrapFabDate = findViewById(R.id.wrap_fab3);
        wrapFabReset = findViewById(R.id.wrap_fab4);
        ChangeStatusBar();
        MainToolbar = findViewById(R.id.mainToolbar);
        imvBack = findViewById(R.id.imvBack);
        tvTitle = findViewById(R.id.tvTitle);
        imvAddUser = findViewById(R.id.add_user);
        imvAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwitchFragment(new AddUserFragment(), true);
            }
        });
        imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setSupportActionBar(MainToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        //Floating Action Buttons
        fab = findViewById(R.id.fab);
        fab.bringToFront();
        fab1 = findViewById(R.id.fab_1);
        fab2 = findViewById(R.id.fab_2);
        fab3 = findViewById(R.id.fab_3);
        fab4 = findViewById(R.id.fab_4);
        //Animations
        show_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_show);
        hide_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_hide);
        show_fab_2 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab2_show);
        hide_fab_2 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab2_hide);
        show_fab_3 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab3_show);
        hide_fab_3 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab3_hide);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!FAB_Status) {
                    //Display FAB menu
                    expandFAB();
                    FAB_Status = true;
                } else {
                    //Close FAB menu
                    hideFAB();
                    FAB_Status = false;
                }
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFAB();
                FAB_Status = false;
                // STAFF
                final List<User> userList = new ArrayList<>();
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                assert layoutInflater != null;
                @SuppressLint("InflateParams") View dialogView = layoutInflater.inflate(R.layout.view_choose_employee, null);
                dialog.setContentView(dialogView);
                final ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
                Utils.setUpProgressBar(progressBar, false);
                final ListView lvUser = dialog.findViewById(R.id.lvUser);
                firebaseFirestore.collection(Constant.COLLECTION_USER)
                        .whereEqualTo("role", Constant.ROLE_STAFF)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                User user = documentSnapshot.toObject(User.class);
                                userList.add(user);
                            }
                            progressBar.setVisibility(View.GONE);
                            AdapterAssign adapterAssign = new AdapterAssign(null, MainActivity.this, userList);
                            lvUser.setAdapter(adapterAssign);
                            adapterAssign.setOnCompleteListener(new IOnCompleteListener() {
                                @Override
                                public void onComplete(String staffName, String staffDocumentId) {
                                    dialog.dismiss();
                                    DeptFragment deptFragment = new DeptFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString(Constant.KEY, staffName);
                                    bundle.putString(Constant.TYPE, Constant.STAFF);
                                    deptFragment.setArguments(bundle);
                                    SwitchFragment(deptFragment, false);
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
                dialog.show();
            }
        });


        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFAB();
                FAB_Status = false;
                // STATUS
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                assert layoutInflater != null;
                @SuppressLint("InflateParams") View dialogView = layoutInflater.inflate(R.layout.choose_status, null);
                dialog.setContentView(dialogView);
                TextView tvCancel = dialog.findViewById(R.id.tvCancel);
                LinearLayout linearLayout_Status1 = dialog.findViewById(R.id.linearLayout_Status1);
                LinearLayout linearLayout_Status2 = dialog.findViewById(R.id.linearLayout_Status2);
                LinearLayout linearLayout_Status3 = dialog.findViewById(R.id.linearLayout_Status3);
                LinearLayout linearLayout_Status4 = dialog.findViewById(R.id.linearLayout_Status4);


                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                linearLayout_Status1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DeptFragment deptFragment = new DeptFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.KEY, "1");
                        bundle.putString(Constant.TYPE, Constant.STATUS);
                        deptFragment.setArguments(bundle);
                        SwitchFragment(deptFragment, false);
                        dialog.dismiss();
                    }
                });

                linearLayout_Status2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DeptFragment deptFragment = new DeptFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.KEY, "2");
                        bundle.putString(Constant.TYPE, Constant.STATUS);
                        deptFragment.setArguments(bundle);
                        SwitchFragment(deptFragment, false);
                        dialog.dismiss();
                    }
                });

                linearLayout_Status3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DeptFragment deptFragment = new DeptFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.KEY, "3");
                        bundle.putString(Constant.TYPE, Constant.STATUS);
                        deptFragment.setArguments(bundle);
                        SwitchFragment(deptFragment, false);
                        dialog.dismiss();
                    }
                });

                linearLayout_Status4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DeptFragment deptFragment = new DeptFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.KEY, "4");
                        bundle.putString(Constant.TYPE, Constant.STATUS);
                        deptFragment.setArguments(bundle);
                        SwitchFragment(deptFragment, false);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFAB();
                FAB_Status = false;
                // DATE TIME
                DateSortDialog dateSortDialog = new DateSortDialog(MainActivity.this);
                dateSortDialog.show(getFragmentManager(), "");
                dateSortDialog.setiOnCompleteListener(new IOnCompleteListener() {
                    @Override
                    public void onComplete(String staffName, String staffDocumentId) {
                        DeptFragment deptFragment = new DeptFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.KEY, staffName);
                        bundle.putString(Constant.TYPE, Constant.DATETIME);
                        deptFragment.setArguments(bundle);
                        SwitchFragment(deptFragment, false);
                    }
                });
            }
        });

        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideFAB();
                FAB_Status = false;
                SwitchFragment(new DeptFragment(), false);
            }
        });
        SwitchFragment(new LoginFragment(), false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        FirebaseApp.initializeApp(MainActivity.this);
    }


    @Override
    protected int GetLayoutId() {
        return R.layout.activity_main;
    }

    public void ChangeStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
    }

    @Override
    public void SwitchFragment(Fragment fragment, boolean IsAddToBackStack) {
        super.SwitchFragment(fragment, IsAddToBackStack);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (role.equals(Constant.ROLE_STAFF)) {
            getMenuInflater().inflate(R.menu.menu_main_for_staff, menu);
        }

        if (role.equals(Constant.ROLE_ADMIN)) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }

        return isMenuVisible;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_log_out) {
            StartFragmentClearTop(new LoginFragment(), false);
            return true;
        }
        if (id == R.id.add_customer) {
            SwitchFragment(new AddCustomerFragment(AddCustomerFragment.ScreenType.Add), true);
            return true;
        }
        if (id == R.id.user_manage) {
            SwitchFragment(new EmployeeManageFragment(), true);
            return true;
        }
        if (id == R.id.change_pass) {
            ChangePassDialog changePassDialog = new ChangePassDialog(MainActivity.this);
            changePassDialog.show(getFragmentManager(), "");
        }
        return super.onOptionsItemSelected(item);
    }

    public void setScreenTitle(String title) {
        if (title == null) {
            return;
        }
        tvTitle.setText(title);
    }

    public void switchFragment(Fragment fragment, boolean isAddToBackStack) {
        SwitchFragment(fragment, true);
    }

    @Override
    public void onBackPressed() {
        BaseFragment currentFragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.root);
        if (currentFragment == null || !currentFragment.onBackPress()) {
            super.onBackPressed();
            FragmentHelper.RemoveLastFragment(getSupportFragmentManager());
        }
    }

    public void setMenuVisible(boolean visible) {
        isMenuVisible = visible;
        invalidateOptionsMenu();
    }

    public void setFloatButtonVisible(boolean visible) {
        fab.setVisibility(visible ? View.VISIBLE : View.GONE);
        if (!visible) {
            wrapFabUser.setAlpha(0);
            wrapFabStatus.setAlpha(0);
            wrapFabDate.setAlpha(0);
        } else {
            wrapFabUser.setAlpha(1);
            wrapFabStatus.setAlpha(1);
            wrapFabDate.setAlpha(1);
        }
    }

    public void setHeaderVisible(boolean isVisible) {
        if (isVisible) {
            MainToolbar.setVisibility(View.VISIBLE);
        } else {
            MainToolbar.setVisibility(View.GONE);
        }
    }

    public void setToolbarVisible(boolean visible) {
        MainToolbar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setImvAddUserVisible(boolean visible) {
        imvAddUser.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setBackButtonVisible(boolean isVisible) {
        if (isVisible) {
            imvBack.setVisibility(View.VISIBLE);
        } else {
            imvBack.setVisibility(View.GONE);
        }
    }

    public void setScreenOrientation(boolean isPotraitMode) {
        setRequestedOrientation(isPotraitMode ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }


    private void expandFAB() {

        //Floating Action Button 1
        wrapFabUser.startAnimation(show_fab_1);
        wrapFabUser.setClickable(true);
        wrapFabUser.bringToFront();

        //Floating Action Button 2
        wrapFabStatus.startAnimation(show_fab_2);
        wrapFabStatus.setClickable(true);
        wrapFabStatus.bringToFront();

        //Floating Action Button 3
        wrapFabDate.startAnimation(show_fab_3);
        wrapFabDate.setClickable(true);
        wrapFabDate.bringToFront();

        //Floating Action Button 4
        wrapFabReset.startAnimation(show_fab_3);
        wrapFabReset.setClickable(true);
        wrapFabReset.bringToFront();
    }

    private void hideFAB() {
        //Floating Action Button 1
        wrapFabUser.startAnimation(hide_fab_1);
        wrapFabUser.setClickable(false);
        wrapFabUser.setFocusableInTouchMode(false);
        wrapFabUser.setFocusable(false);
        //Floating Action Button 2
        wrapFabStatus.startAnimation(hide_fab_2);
        wrapFabStatus.setFocusable(false);
        wrapFabStatus.setClickable(false);
        wrapFabStatus.setFocusableInTouchMode(false);
        //Floating Action Button 3
        wrapFabDate.startAnimation(hide_fab_3);
        wrapFabDate.setClickable(false);
        wrapFabDate.setFocusable(false);
        wrapFabDate.setFocusableInTouchMode(false);
        //Floating Action Button 3
        wrapFabReset.startAnimation(hide_fab_3);
        wrapFabReset.setClickable(false);
        wrapFabReset.setFocusable(false);
        wrapFabReset.setFocusableInTouchMode(false);
    }

    public void showHideFloatButtonByRole() {
        hideFAB();
        FAB_Status = false;
        if (role.equals(Constant.ROLE_STAFF)) {
            wrapFabUser.setVisibility(View.GONE);
        } else {
            wrapFabUser.setVisibility(View.INVISIBLE);
        }
    }


}
