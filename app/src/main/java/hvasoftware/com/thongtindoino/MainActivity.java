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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import hvasoftware.com.thongtindoino.utils.DatabaseUser;
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
    FloatingActionButton fab, fab1, fab2, fab3;
    //Animations
    Animation show_fab_1, hide_fab_1, show_fab_2, hide_fab_2, show_fab_3, hide_fab_3;
    View wrapFab1, wrapFab2, wrapFab3;
    private FirebaseFirestore firebaseFirestore;
    private boolean FAB_Status = false;
    private String role = null;

    @Override
    protected String GetScreenTitle() {
        return super.GetScreenTitle();
    }

    @Override
    protected void OnViewCreated() {
    }

    @Override
    protected void OnBindView() {
        DatabaseUser databaseUser = DatabaseUser.newInstance(MainActivity.this);
        role = databaseUser.getAllUsers().get(0).getRole();
        Log.wtf(TAG, "============================>: " + role);
        firebaseFirestore = FirebaseFirestore.getInstance();
        wrapFab1 = findViewById(R.id.wrap_fab1);
        wrapFab2 = findViewById(R.id.wrap_fab2);
        wrapFab3 = findViewById(R.id.wrap_fab3);
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
        if (role.equals(Constant.ROLE_STAFF)) {
            wrapFab1.setVisibility(View.GONE);
        }
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
                // STATUS
                final String[] status = {"1"};
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                assert layoutInflater != null;
                @SuppressLint("InflateParams") View dialogView = layoutInflater.inflate(R.layout.choose_status, null);
                dialog.setContentView(dialogView);
                Spinner spinner = dialog.findViewById(R.id.spinnerStatus);
                TextView tvCancel = dialog.findViewById(R.id.tvCancel);
                TextView tvChoose = dialog.findViewById(R.id.tvChoose);
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                final String[] strings = {"1", "2", "3"};
                ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, strings);
                spinner.setAdapter(stringArrayAdapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        status[0] = strings[i];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                tvChoose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DeptFragment deptFragment = new DeptFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.KEY, status[0]);
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

                /*

                 */

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
            SwitchFragment(new LoginFragment(), false);
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
            wrapFab1.setAlpha(0);
            wrapFab2.setAlpha(0);
            wrapFab3.setAlpha(0);
        } else {
            wrapFab1.setAlpha(1);
            wrapFab2.setAlpha(1);
            wrapFab3.setAlpha(1);
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
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) wrapFab1.getLayoutParams();
        layoutParams.rightMargin = (int) (wrapFab1.getWidth() * 1.5);
        wrapFab1.setLayoutParams(layoutParams);
        wrapFab1.startAnimation(show_fab_1);
        wrapFab1.setClickable(true);

        //Floating Action Button 2
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) wrapFab2.getLayoutParams();
        layoutParams2.rightMargin = (int) (wrapFab2.getWidth() * 1.3);
        layoutParams2.bottomMargin = (int) (wrapFab2.getHeight() * 1.3);
        wrapFab2.setLayoutParams(layoutParams2);
        wrapFab2.startAnimation(show_fab_2);
        wrapFab2.setClickable(true);

        //Floating Action Button 3
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) wrapFab3.getLayoutParams();
        layoutParams3.bottomMargin = (int) (wrapFab3.getHeight() * 1.5);
        wrapFab3.setLayoutParams(layoutParams3);
        wrapFab3.startAnimation(show_fab_3);
        wrapFab3.setClickable(true);
    }

    private void hideFAB() {
        //Floating Action Button 1
        wrapFab1.startAnimation(hide_fab_1);
        wrapFab1.setClickable(false);
        //Floating Action Button 2
        wrapFab2.startAnimation(hide_fab_2);
        wrapFab2.setClickable(false);
        //Floating Action Button 3
        wrapFab3.startAnimation(hide_fab_3);
        wrapFab3.setClickable(false);
    }

}
