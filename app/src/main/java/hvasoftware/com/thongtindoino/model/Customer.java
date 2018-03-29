package hvasoftware.com.thongtindoino.model;

import java.util.Date;

/**
 * Created by HoangVuAnh on 3/7/18.
 */

public class Customer {
    private String objectID;
    private String documentId;
    private String ten;
    private String ngayVay;
    private String ngayHetHan;


    private long sotien;
    private int songayvay;
    private int dayleft;

    private String ghichu;
    private String diachi;
    private String sodienthoai;
    private String cmnd;
    private String nhanvienthu;
    private String nhanvienthuDocumentId;

    private Date createAt;
    private Date updateAt;
    private int trangthai;
    private Date ngayVayDate;

    public Customer() {

    }

    public Date getNgayVayDate() {
        return ngayVayDate;
    }

    public void setNgayVayDate(Date ngayVayDate) {
        this.ngayVayDate = ngayVayDate;
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

    public String getNgayVay() {
        return ngayVay;
    }

    public void setNgayVay(String ngayVay) {
        this.ngayVay = ngayVay;
    }

    public int getDayleft() {
        return dayleft;
    }

    public void setDayleft(int dayleft) {
        this.dayleft = dayleft;
    }

    public String getNgayHetHan() {
        return ngayHetHan;
    }

    public void setNgayHetHan(String ngayHetHan) {
        this.ngayHetHan = ngayHetHan;
    }

    public long getSotien() {
        return sotien;
    }

    public void setSotien(long sotien) {
        this.sotien = sotien;
    }

    public int getSongayvay() {
        return songayvay;
    }

    public void setSongayvay(int songayvay) {
        this.songayvay = songayvay;
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

    public int getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(int trangthai) {
        this.trangthai = trangthai;
    }

    public String getNhanvienthuDocumentId() {
        return nhanvienthuDocumentId;
    }

    public void setNhanvienthuDocumentId(String nhanvienthuDocumentId) {
        this.nhanvienthuDocumentId = nhanvienthuDocumentId;
    }
}
