package hvasoftware.com.thongtindoino;

import java.util.Date;

/**
 * Created by HoangVuAnh on 3/7/18.
 */

public class Customer {

    private String objectID;
    private String documentId;
    private String displayName;
    private String assignTo;
    private Double moneyAmount;
    private Date ngayVay;
    private Date ngayPhaiTra;


    private Date createAt;
    private Date updateAt;
    private boolean state;


    public Customer() {
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAssignTo() {
        return assignTo;
    }

    public void setAssignTo(String assignTo) {
        this.assignTo = assignTo;
    }

    public Double getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(Double moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public Date getNgayVay() {
        return ngayVay;
    }

    public void setNgayVay(Date ngayVay) {
        this.ngayVay = ngayVay;
    }

    public Date getNgayPhaiTra() {
        return ngayPhaiTra;
    }

    public void setNgayPhaiTra(Date ngayPhaiTra) {
        this.ngayPhaiTra = ngayPhaiTra;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
