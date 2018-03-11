package hvasoftware.com.thongtindoino.model;

import java.util.Date;

/**
 * Created by HoangVuAnh on 3/7/18.
 */

public class Customer {
    private String objectID;
    private String documentId;
    private String ten;
    private Date ngayVay;
    private Date ngayPhaiTra;

    private Double sotien;
    private Double songayvay;

    private String hetday;
    private String ghichu;
    private String diachi;
    private String sodienthoai;
    private String cmnd;
    private String nhanvienthu;
    private Date createAt;
    private Date updateAt;
    private boolean trangthai;


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

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getNhanvienthu() {
        return nhanvienthu;
    }

    public void setNhanvienthu(String nhanvienthu) {
        this.nhanvienthu = nhanvienthu;
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

    public Double getSotien() {
        return sotien;
    }

    public void setSotien(Double sotien) {
        this.sotien = sotien;
    }

    public Double getSongayvay() {
        return songayvay;
    }

    public void setSongayvay(Double songayvay) {
        this.songayvay = songayvay;
    }

    public String getHetday() {
        return hetday;
    }

    public void setHetday(String hetday) {
        this.hetday = hetday;
    }

    public String getGhichu() {
        return ghichu;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getSodienthoai() {
        return sodienthoai;
    }

    public void setSodienthoai(String sodienthoai) {
        this.sodienthoai = sodienthoai;
    }

    public String getCmnd() {
        return cmnd;
    }

    public void setCmnd(String cmnd) {
        this.cmnd = cmnd;
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

    public boolean isTrangthai() {
        return trangthai;
    }

    public void setTrangthai(boolean trangthai) {
        this.trangthai = trangthai;
    }
}
