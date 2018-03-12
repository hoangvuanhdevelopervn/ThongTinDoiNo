package hvasoftware.com.thongtindoino;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hvasoftware.com.thongtindoino.base.BaseActivity;
import hvasoftware.com.thongtindoino.base.BaseFragment;
import hvasoftware.com.thongtindoino.model.User;
import hvasoftware.com.thongtindoino.ui.dialog.ChangePassDialog;
import hvasoftware.com.thongtindoino.ui.dialog.DateSortDialog;
import hvasoftware.com.thongtindoino.ui.fragment.AddCustomerFragment;
import hvasoftware.com.thongtindoino.ui.fragment.AddUserFragment;
import hvasoftware.com.thongtindoino.ui.fragment.EmployeeManageFragment;
import hvasoftware.com.thongtindoino.ui.fragment.LoginFragment;
import hvasoftware.com.thongtindoino.utils.Constant;
import hvasoftware.com.thongtindoino.utils.FragmentHelper;
import hvasoftware.com.thongtindoino.utils.Utils;


public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    View imvBack;
    View imvAddUser;
    TextView tvTitle;
    android.support.v7.widget.Toolbar MainToolbar;
    boolean isMenuVisible;
    FloatingActionButton fab, fab1, fab2, fab3;

    private FirebaseFirestore firebaseFirestore;
    //Animations
    Animation show_fab_1, hide_fab_1, show_fab_2, hide_fab_2, show_fab_3, hide_fab_3;
    View wrapFab1, wrapFab2, wrapFab3;

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

                if (FAB_Status == false) {
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
                Toast.makeText(getApplication(), "Floating Action Button 1", Toast.LENGTH_SHORT).show();
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "Floating Action Button 2", Toast.LENGTH_SHORT).show();
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "Floating Action Button 3", Toast.LENGTH_SHORT).show();
                DateSortDialog dateSortDialog = new DateSortDialog();
                dateSortDialog.show(getFragmentManager(), "");
            }
        });
        SwitchFragment(new LoginFragment(), false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        FirebaseApp.initializeApp(MainActivity.this);
        firebaseFirestore = FirebaseFirestore.getInstance();
        // uploadData();
        getData();
    }


    private void uploadData() {
        CollectionReference collectionReference = firebaseFirestore.collection(Constant.COLLECTION_USER);
        Map<String, Object> user = new HashMap<>();
        user.put("objectID", Utils.getRandomUUID());
        user.put("documentId", "Admin");
        user.put("account", "Admin");
        user.put("password", "12345678");
        user.put("displayName", "Doãn Chí Bình");
        user.put("createAt", Utils.getCurrentDateTime());
        user.put("updateAt", Utils.getCurrentDateTime());
        user.put("role", Constant.ROLE_ADMIN);
        collectionReference.document("admin").set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.wtf(TAG, "==============================> UPLOAD DONE");
            }
        });
    }


    private void getData() {
        final List<User> userList = new ArrayList<>();
        firebaseFirestore.collection(Constant.COLLECTION_USER)
                // .whereEqualTo()
                //  .orderBy()
                // .limit()
                //.startAt()
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        User user = documentSnapshot.toObject(User.class);
                        userList.add(user);
                        Log.wtf(TAG, "===========================> NAME: " + user.getDisplayName());
                    }
                }
            }
        });
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return isMenuVisible;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
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
            ChangePassDialog changePassDialog = new ChangePassDialog();
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
        fab1.setVisibility(visible ? View.VISIBLE : View.GONE);
        fab2.setVisibility(visible ? View.VISIBLE : View.GONE);
        fab3.setVisibility(visible ? View.VISIBLE : View.GONE);
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
        layoutParams.rightMargin += (int) (wrapFab1.getWidth() * 1.5);
        wrapFab1.setLayoutParams(layoutParams);
        wrapFab1.startAnimation(show_fab_1);
        wrapFab1.setClickable(true);

        //Floating Action Button 2
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) wrapFab2.getLayoutParams();
        layoutParams2.rightMargin += (int) (wrapFab2.getWidth() * 1.5);
        layoutParams2.bottomMargin += (int) (wrapFab2.getHeight() * 1.5);
        wrapFab2.setLayoutParams(layoutParams2);
        wrapFab2.startAnimation(show_fab_2);
        wrapFab2.setClickable(true);

        //Floating Action Button 3
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) wrapFab3.getLayoutParams();
        layoutParams3.bottomMargin += (int) (wrapFab3.getHeight() * 1.5);
        wrapFab3.setLayoutParams(layoutParams3);
        wrapFab3.startAnimation(show_fab_3);
        wrapFab3.setClickable(true);
    }


    private void hideFAB() {

        //Floating Action Button 1
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) wrapFab1.getLayoutParams();
        layoutParams.rightMargin -= (int) (wrapFab1.getWidth() * 1.5);
        wrapFab1.setLayoutParams(layoutParams);
        wrapFab1.startAnimation(hide_fab_1);
        wrapFab1.setClickable(false);

        //Floating Action Button 2
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) wrapFab2.getLayoutParams();
        layoutParams2.rightMargin -= (int) (wrapFab2.getWidth() * 1.5);
        layoutParams2.bottomMargin -= (int) (wrapFab2.getHeight() * 1.5);
        wrapFab2.setLayoutParams(layoutParams2);
        wrapFab2.startAnimation(hide_fab_2);
        wrapFab2.setClickable(false);

        //Floating Action Button 3
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) wrapFab3.getLayoutParams();
        layoutParams3.bottomMargin -= (int) (wrapFab3.getHeight() * 1.5);
        wrapFab3.setLayoutParams(layoutParams3);
        wrapFab3.startAnimation(hide_fab_3);
        wrapFab3.setClickable(false);
    }
}
