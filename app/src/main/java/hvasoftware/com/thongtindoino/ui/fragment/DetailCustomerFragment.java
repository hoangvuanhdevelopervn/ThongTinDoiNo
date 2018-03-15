package hvasoftware.com.thongtindoino.ui.fragment;

import hvasoftware.com.thongtindoino.R;
import hvasoftware.com.thongtindoino.base.BaseFragment;

/**
 * Created by HoangVuAnh on 3/15/18.
 */

public class DetailCustomerFragment extends BaseFragment {




    /*
      if (!TextUtils.isEmpty(documentId)) {
            tvUpload.setText("Cập nhật");
            firebaseFirestore.collection(Constant.COLLECTION_CUSTOMER)
                    .document(documentId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document != null && document.exists()) {
                                    Customer customer = document.toObject(Customer.class);
                                    edt_CustomerName.setText(customer.getTen());
                                    tvNgayVay.setText(DateTimeUtils.formatDatetime(getActivity(), customer.getNgayVay()));
                                    tvNgayTra.setText(DateTimeUtils.formatDatetime(getActivity(), customer.getNgayPhaiTra()));
                                    edt_SoTienVay.setText("" + customer.getSotien());
                                    tvSoNgayVay.setText("" + customer.getSongayvay());
                                    tvHetHan.setText(DateTimeUtils.formatDatetime(getActivity(), customer.getHethan()));
                                    edt_Note.setText(customer.getGhichu());
                                    edt_Address.setText(customer.getDiachi());
                                    edt_Phone.setText(customer.getSodienthoai());
                                    edt_CMND.setText(customer.getCmnd());
                                }
                            }
                        }
                    });
        }
     */

    @Override
    protected void OnViewCreated() {

    }

    @Override
    protected void OnBindView() {

    }

    @Override
    public int GetLayoutId() {
        return R.layout.fragment_add_customer;
    }


}
