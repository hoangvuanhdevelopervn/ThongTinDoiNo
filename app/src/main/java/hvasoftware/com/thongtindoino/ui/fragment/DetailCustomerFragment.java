package hvasoftware.com.thongtindoino.ui.fragment;

import hvasoftware.com.thongtindoino.R;
import hvasoftware.com.thongtindoino.base.BaseFragment;

/**
 * Created by HoangVuAnh on 3/15/18.
 */

public class DetailCustomerFragment extends BaseFragment {

    /*
       WriteBatch writeBatch = firebaseFirestore.batch();
            DocumentReference updateCustomer = firebaseFirestore.collection(Constant.COLLECTION_CUSTOMER).document(customerDocumentId);
            writeBatch.update(updateCustomer, "ten", customerName);
            writeBatch.update(updateCustomer, "ngayVay", ngayVay);
            writeBatch.update(updateCustomer, "hethan", ngayHetHan);
            writeBatch.update(updateCustomer, "sotien", customerSoTienVay);
            writeBatch.update(updateCustomer, "songayvay", soNgayVay);
            writeBatch.update(updateCustomer, "ghichu", customerGhiChu);
            writeBatch.update(updateCustomer, "diachi", customerDiaChi);
            writeBatch.update(updateCustomer, "sodienthoai", customerSoDienThoai);
            writeBatch.update(updateCustomer, "cmnd", customerCMND);
            writeBatch.update(updateCustomer, "nhanvienthu", staffName);
            writeBatch.update(updateCustomer, "nhanvienthuDocumentId", staffDocumentId);
            writeBatch.update(updateCustomer, "updateAt", Utils.getCurrentDateTime());
            writeBatch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), getActivity().getString(R.string.updapte_success), Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), getActivity().getString(R.string.updapte_failed), Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                    // Log.wtf(TAG, "==============================>" + e.getMessage());
                }
            });
     */

    /*
     private void bindData() {
        firebaseFirestore.collection(Constant.COLLECTION_CUSTOMER)
                .document(customerDocumentId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                Customer customer = document.toObject(Customer.class);
                                staffName = customer.getNhanvienthu();
                                staffDocumentId = customer.getNhanvienthuDocumentId();
                                edt_CustomerName.setText(customer.getTen());
                                edt_NgayVay.setText(customer.getNgayVay());
                                edt_NgayTra.setText(customer.getNgayPhaiTra());
                                edt_SoTienVay.setText("" + customer.getSotien());
                                tvSoNgayVay.setText("" + customer.getSongayvay());
                                edt_HetHan.setText(customer.getHethan());
                                edt_Note.setText(customer.getGhichu());
                                edt_Address.setText(customer.getDiachi());
                                edt_Phone.setText(customer.getSodienthoai());
                                edt_CMND.setText(customer.getCmnd());
                                tvChooseStaff.setText(staffName);
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
