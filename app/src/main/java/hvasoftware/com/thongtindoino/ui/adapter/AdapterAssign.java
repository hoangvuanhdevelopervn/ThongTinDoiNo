package hvasoftware.com.thongtindoino.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.List;

import hvasoftware.com.thongtindoino.R;
import hvasoftware.com.thongtindoino.model.User;
import hvasoftware.com.thongtindoino.utils.Constant;
import hvasoftware.com.thongtindoino.utils.DateTimeUtils;
import hvasoftware.com.thongtindoino.utils.IOnCompleteListener;

/**
 * Created by HoangVuAnh on 3/15/18.
 */

public class AdapterAssign extends BaseAdapter {
    private static final String TAG = "AdapterAssign";
    private Context context;
    private List<User> userList;
    private String documentId = null;
    private FirebaseFirestore firebaseFirestore;
    private IOnCompleteListener onCompleteListener;

    public AdapterAssign(String documentId, Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
        this.documentId = documentId;
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int i) {
        return userList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class Holder {
        private TextView tvUserName;
    }

    public void setOnCompleteListener(IOnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            view = layoutInflater.inflate(R.layout.custom_for_adapter_assign, viewGroup, false);
            holder = new Holder();
            holder.tvUserName = view.findViewById(R.id.tvUserName);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.tvUserName.setText(userList.get(i).getDisplayName());
        if (!TextUtils.isEmpty(documentId)) {
            holder.tvUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    WriteBatch writeBatch = firebaseFirestore.batch();
                    DocumentReference updateQuoteShareAmount = firebaseFirestore.collection(Constant.COLLECTION_CUSTOMER).document(documentId);
                    writeBatch.update(updateQuoteShareAmount, "nhanvienthu", userList.get(i).getDisplayName());
                    writeBatch.update(updateQuoteShareAmount, "nhanvienthuDocumentId", userList.get(i).getDocumentId());
                    writeBatch.update(updateQuoteShareAmount, "updateAt", DateTimeUtils.getDateTime());
                    writeBatch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(context, "Gán nhân viên thành công", Toast.LENGTH_SHORT).show();
                            onCompleteListener.onComplete(userList.get(i).getDisplayName(), userList.get(i).getDocumentId());
                        }
                    });
                }
            });
        } else {
            holder.tvUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCompleteListener.onComplete(userList.get(i).getDisplayName(), userList.get(i).getDocumentId());
                }
            });
        }

        return view;
    }
}
