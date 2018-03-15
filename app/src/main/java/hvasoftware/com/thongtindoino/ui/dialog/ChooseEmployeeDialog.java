package hvasoftware.com.thongtindoino.ui.dialog;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import hvasoftware.com.thongtindoino.R;
import hvasoftware.com.thongtindoino.model.User;
import hvasoftware.com.thongtindoino.ui.adapter.AdapterAssign;
import hvasoftware.com.thongtindoino.utils.Constant;
import hvasoftware.com.thongtindoino.utils.IOnCompleteListener;
import hvasoftware.com.thongtindoino.utils.Utils;


/**
 * Created by Thanh on 03/08/2018.
 */

public class ChooseEmployeeDialog extends DialogFragment {
    private FirebaseFirestore firebaseFirestore;
    private String documentId = null;
    private final String TAG = "ChooseEmployeeDialog";
    private ListView lvUser;
    private ProgressBar progressBar;
    private List<User> userList = new ArrayList<>();

    public ChooseEmployeeDialog() {

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.view_choose_employee, container);
        savedInstanceState = this.getArguments();
        documentId = savedInstanceState.getString(Constant.KEY);
        lvUser = view.findViewById(R.id.lvUser);
        progressBar = view.findViewById(R.id.progressBar);
        Utils.setUpProgressBar(progressBar, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        getListUser();
        return view;
    }

    private void getListUser() {
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
                    AdapterAssign adapterAssign = new AdapterAssign(documentId, getActivity(), userList);
                    lvUser.setAdapter(adapterAssign);
                    adapterAssign.setOnCompleteListener(new IOnCompleteListener() {
                        @Override
                        public void onComplete(String staffName, String staffDocumentId) {
                            getDialog().dismiss();
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
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
