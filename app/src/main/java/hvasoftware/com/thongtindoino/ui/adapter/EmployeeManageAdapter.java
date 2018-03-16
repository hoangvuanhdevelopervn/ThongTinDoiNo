package hvasoftware.com.thongtindoino.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

import hvasoftware.com.thongtindoino.R;
import hvasoftware.com.thongtindoino.model.User;
import hvasoftware.com.thongtindoino.utils.Constant;
import hvasoftware.com.thongtindoino.utils.DateTimeUtils;
import hvasoftware.com.thongtindoino.utils.IOnCompleteListener;
import hvasoftware.com.thongtindoino.utils.Utils;

/**
 * Created by Thanh on 03/10/2018.
 */

public class EmployeeManageAdapter extends RecyclerView.Adapter {

    public CallBack CallBack;
    public List<User> users;
    private Activity activity;
    private FirebaseFirestore firebaseFirestore;
    private IOnCompleteListener iOnCompleteListener;

    public EmployeeManageAdapter(Activity activity) {
        users = new ArrayList<>();
        this.activity = activity;
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void insertUser(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_employee, parent, false);
        return new UserViewHolder(v, CallBack);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof UserViewHolder) {
            UserViewHolder timeViewHolder = (UserViewHolder) holder;
            timeViewHolder.bindView(users.get(position));
            final User user = users.get(position);
            ((UserViewHolder) holder).tvUserName.setText(": " + user.getDisplayName());
            ((UserViewHolder) holder).tvUserCreateAt.setText("Tạo ngày: " + DateTimeUtils.formatDatetime(activity, user.getCreateAt()));
            ((UserViewHolder) holder).tvUserAccount.setText(": " + user.getAccount());
            ((UserViewHolder) holder).tvUserPhone.setText(": " + user.getPhone());
            ((UserViewHolder) holder).tvUserRole.setText(": " + user.getRole());


            ((UserViewHolder) holder).relativeLayoutUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog dialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    assert layoutInflater != null;
                    @SuppressLint("InflateParams") View dialogView = layoutInflater.inflate(R.layout.fragment_add_user, null);
                    dialog.setContentView(dialogView);
                    final EditText edt_userName = dialog.findViewById(R.id.edt_obj_name);
                    edt_userName.setText(user.getDisplayName());
                    final EditText edt_userAccount = dialog.findViewById(R.id.edt_take_date);
                    edt_userAccount.setText(user.getAccount());
                    final EditText edt_userPassword = dialog.findViewById(R.id.edt_money);
                    edt_userPassword.setText(user.getPassword());
                    final EditText edt_userInputPassAgain = dialog.findViewById(R.id.edt_day);
                    edt_userInputPassAgain.setText(user.getPassword());
                    final EditText edt_userAddress = dialog.findViewById(R.id.edt_address);
                    edt_userAddress.setText(user.getAddress());
                    final EditText edt_userPhone = dialog.findViewById(R.id.edt_phone);
                    edt_userPhone.setText(user.getPhone());
                    final ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
                    Utils.setUpProgressBar(progressBar, true);
                    TextView tvAddUser = dialog.findViewById(R.id.btn_login);
                    tvAddUser.setText("Cập nhật");
                    tvAddUser.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String userName = edt_userName.getText().toString().trim();
                            String userAccount = edt_userAccount.getText().toString().trim();
                            String userPassword = edt_userPassword.getText().toString().trim();
                            String userInputPassAgain = edt_userInputPassAgain.getText().toString().trim();
                            String userAddress = edt_userAddress.getText().toString().trim();
                            String userPhone = edt_userPhone.getText().toString().trim();

                            if (TextUtils.isEmpty(userName)) {
                                Toast.makeText(activity, "Bạn chưa nhập tên nhân viên", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if (TextUtils.isEmpty(userAccount)) {
                                Toast.makeText(activity, "Bạn chưa nhập tên tài khoản", Toast.LENGTH_SHORT).show();
                                return;
                            }


                            if (TextUtils.isEmpty(userPassword)) {
                                Toast.makeText(activity, "Bạn chưa nhập mật khẩu", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if (TextUtils.isEmpty(userInputPassAgain)) {
                                Toast.makeText(activity, "Bạn chưa nhập lại mật khẩu", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if (TextUtils.isEmpty(userAddress)) {
                                Toast.makeText(activity, "Bạn chưa nhập địa chỉ của nhân viên", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if (TextUtils.isEmpty(userPhone)) {
                                Toast.makeText(activity, "Bạn chưa nhập số điện thoại của nhân viên", Toast.LENGTH_SHORT).show();
                                return;
                            }


                            if (!userPassword.equals(userInputPassAgain)) {
                                Toast.makeText(activity, "Hai mật khẩu không giống nhau! Vui lòng nhập lại", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            /**
                             if (userPhone.length() < 10 || userPhone.length() > 11){
                             Toast.makeText(getContext(), "Định dạng điện thoại không đúng! Vui lòng nhập lại", Toast.LENGTH_SHORT).show();
                             return;
                             }
                             */

                            progressBar.setVisibility(View.VISIBLE);

                            WriteBatch writeBatch = firebaseFirestore.batch();
                            DocumentReference updateQuoteShareAmount = firebaseFirestore.collection(Constant.COLLECTION_USER).document(user.getDocumentId());
                            writeBatch.update(updateQuoteShareAmount, "displayName", userName);
                            writeBatch.update(updateQuoteShareAmount, "account", userAccount);
                            writeBatch.update(updateQuoteShareAmount, "password", userPassword);
                            writeBatch.update(updateQuoteShareAmount, "address", userAddress);
                            writeBatch.update(updateQuoteShareAmount, "phone", userPhone);
                            writeBatch.update(updateQuoteShareAmount, "updateAt", DateTimeUtils.getDateTime());
                            writeBatch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(activity, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    dialog.dismiss();
                                    iOnCompleteListener.onComplete("Name", "Name");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(activity, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    dialog.dismiss();
                                }
                            });
                        }
                    });
                    dialog.show();
                }
            });

            ((UserViewHolder) holder).ibRemoveUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Xoá nhân viên")
                            .setMessage("Bạn có thực sự muốn xoá nhân viên này không?")
                            .setNegativeButton("Xoá", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    firebaseFirestore.collection(Constant.COLLECTION_USER)
                                            .document(user.getDocumentId())
                                            .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(activity, "Xoá nhân viên thành công", Toast.LENGTH_SHORT).show();
                                            users.remove(position);
                                            notifyDataSetChanged();
                                        }
                                    });
                                }
                            }).setPositiveButton("Huỷ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create().show();
                }
            });
        }
    }

    public void setiOnCompleteListener(IOnCompleteListener onCompleteListener) {
        this.iOnCompleteListener = onCompleteListener;
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public interface CallBack {
        void onMenuAction(User object, MenuItem item, int position);
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, PopupMenu.OnMenuItemClickListener {
        CallBack callBack;
        User user;
        private TextView tvUserName;
        private TextView tvUserCreateAt;
        private TextView tvUserAccount;
        private TextView tvUserPhone;
        private TextView tvUserRole;
        private RelativeLayout relativeLayoutUser;
        private ImageButton ibRemoveUser;

        public UserViewHolder(View itemView, CallBack callBack) {
            super(itemView);
            this.callBack = callBack;
            itemView.setOnCreateContextMenuListener(this);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserCreateAt = itemView.findViewById(R.id.tvUserCreateAt);
            tvUserAccount = itemView.findViewById(R.id.tvUserAccount);
            tvUserPhone = itemView.findViewById(R.id.tvUserPhone);
            tvUserRole = itemView.findViewById(R.id.tvUserRole);
            relativeLayoutUser = itemView.findViewById(R.id.relativeLayoutUser);
            ibRemoveUser = itemView.findViewById(R.id.ibRemove);
        }

        public void bindView(User user) {
            this.user = user;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            PopupMenu popup = new PopupMenu(itemView.getContext(), v);
            popup.getMenuInflater().inflate(R.menu.menu_manage_user, popup.getMenu());
            popup.setOnMenuItemClickListener(this);
            popup.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (CallBack != null) {
                CallBack.onMenuAction(user, menuItem, getAdapterPosition());
            }
            return false;

        }
    }
}
