package hvasoftware.com.thongtindoino.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import java.util.ArrayList;
import java.util.List;

import hvasoftware.com.thongtindoino.R;
import hvasoftware.com.thongtindoino.model.User;

/**
 * Created by Thanh on 03/10/2018.
 */

public class EmployeeManageAdapter extends RecyclerView.Adapter {

    public CallBack CallBack;
    public List<User> users;
    public EmployeeManageAdapter() {
        users = new ArrayList<>();
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

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserViewHolder) {
            UserViewHolder timeViewHolder = (UserViewHolder) holder;
            timeViewHolder.bindView(users.get(position));
        }

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
        public UserViewHolder(View itemView,CallBack callBack) {
            super(itemView);
            this.callBack = callBack;
            itemView.setOnCreateContextMenuListener(this);

        }

        public void bindView(User user){
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
