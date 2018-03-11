package hvasoftware.com.thongtindoino.bussiness;

import android.content.Context;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import hvasoftware.com.thongtindoino.utils.Constant;
import hvasoftware.com.thongtindoino.utils.DateTimeUtils;

/**
 * Created by HoangVuAnh on 3/11/18.
 */

public class CustomerBusiness {
    public static CustomerBusiness customerBusiness;
    private FirebaseFirestore firebaseFirestore;
    private Context mContext;

    public CustomerBusiness(Context mContext) {
        this.mContext = mContext;
    }

    public static CustomerBusiness newInstance(Context mContext) {
        if (customerBusiness == null) {
            customerBusiness = new CustomerBusiness(mContext);
        }
        return customerBusiness;
    }

    public void updateCustomer(String documentId, String field, String content) {
        WriteBatch writeBatch = firebaseFirestore.batch();
        DocumentReference updateQuoteShareAmount = firebaseFirestore.collection(Constant.COLLECTION_CUSTOMER).document(documentId);
        writeBatch.update(updateQuoteShareAmount, field, content);
        writeBatch.update(updateQuoteShareAmount, "updateAt", DateTimeUtils.getDateTime());
        writeBatch.commit();
    }


}
