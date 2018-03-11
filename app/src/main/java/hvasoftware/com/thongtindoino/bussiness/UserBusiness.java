package hvasoftware.com.thongtindoino.bussiness;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.Map;

import hvasoftware.com.thongtindoino.utils.Constant;
import hvasoftware.com.thongtindoino.utils.DateTimeUtils;

/**
 * Created by HoangVuAnh on 3/11/18.
 */

public class UserBusiness {

    public static UserBusiness userBusiness;
    private FirebaseFirestore firebaseFirestore;
    private Context mContext;

    public UserBusiness(Context mContext) {
        this.mContext = mContext;
    }

    public static UserBusiness newInstance(Context mContext) {
        if (userBusiness == null) {
            userBusiness = new UserBusiness(mContext);
        }
        return userBusiness;
    }

    public void updateUser(String documentId, String field, String content) {
        WriteBatch writeBatch = firebaseFirestore.batch();
        DocumentReference updateQuoteShareAmount = firebaseFirestore.collection(Constant.COLLECTION_CUSTOMER).document(documentId);
        writeBatch.update(updateQuoteShareAmount, field, content);
        writeBatch.update(updateQuoteShareAmount, "updateAt", DateTimeUtils.getDateTime());
        writeBatch.commit();
    }

    public void uploadUser(Map<String, Object> objectMap, String document, final ProgressBar progressBar) {
        CollectionReference collectionReference = firebaseFirestore.collection(Constant.COLLECTION_USER);
        collectionReference.document(document).set(objectMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(mContext, "Thêm nhân viên thành công", Toast.LENGTH_SHORT).show();
                Log.wtf("TAG", "========================> UPLOAD DONE");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(mContext, "Thêm nhân viên thất bại! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                Log.wtf("TAG", "========================> UPLOAD FAILED: " + e.getMessage());
            }
        });
    }


}
