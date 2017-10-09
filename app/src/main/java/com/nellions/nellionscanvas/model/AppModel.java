package com.nellions.nellionscanvas.model;

import android.graphics.Bitmap;

/**
 * Created by Chris Muiruri on 2/4/2016.
 */
public class AppModel {

    //moves details
    String m_moveId;
    String m_clientCode;
    String m_clientName;
    String m_emailClient;
    String m_phoneNumber;
    String m_jobType;
    String m_origin_;
    String m_dest_;
    String m_originArea;
    String m_destArea;
    String m_originStreet;
    String m_destStreet;
    String m_originName;
    String m_destName;
    String m_surveyDate;
    String m_moveRep;
    String m_smsstatus;
    String m_description;
    String m_statusId;

    //users details
    String u_id;
    String u_name;
    String u_email;
    String u_phoneNumber;
    String u_username;
    String u_password;
    String u_level;

    //category
    String c_id;
    String c_name;
    String c_type;
    String c_code;

    //items
    String i_moveId;
    String i_id;
    String i_name;
    String i_vol;
    String i_quantity;
    String i_total;
    String i_categoryId;
    String i_userId;
    String i_categoryName;
    String i_categoryCode;

    public String getI_categoryName() {
        return i_categoryName;
    }

    public void setI_categoryName(String i_categoryName) {
        this.i_categoryName = i_categoryName;
    }

    //survey_item
    String s_id;
    String s_moveId;
    String s_itemId;
    String s_itemName;
    String s_itemVolume;
    String s_itemTotal;
    String s_categoryId;
    String s_idUser;
    String s_date;
    String s_sync;
    String s_surveyItemId;
    String s_itemQuantity;
    String s_categoryName;
    String s_categoryCode;

    //database
    String categoryType;
    String categoryTypeNumber;

    //room
    String roomId;
    String roomName;

    //photo
    String photoId;
    String photoName;
    String photoCategoryId;
    String photoBase64;
    String photoMoveid;
    String photoSync;

    //notes
    String n_notesId;
    String n_softIssues;
    String n_hardIssues;
    String n_moveid;

    //timestamps
    String t_id;
    String t_move_id;
    String t_time;
    String t_status;

    public Bitmap getPhotoImage() {
        return photoImage;
    }

    public void setPhotoImage(Bitmap photoImage) {
        this.photoImage = photoImage;
    }

    Bitmap photoImage;

    public AppModel() {
    }

    public String getT_id() {
        return t_id;
    }

    public void setT_id(String t_id) {
        this.t_id = t_id;
    }

    public String getT_move_id() {
        return t_move_id;
    }

    public void setT_move_id(String t_move_id) {
        this.t_move_id = t_move_id;
    }

    public String getT_time() {
        return t_time;
    }

    public void setT_time(String t_time) {
        this.t_time = t_time;
    }

    public String getT_status() {
        return t_status;
    }

    public void setT_status(String t_status) {
        this.t_status = t_status;
    }

    public String getN_notesId() {
        return n_notesId;
    }

    public void setN_notesId(String n_notesId) {
        this.n_notesId = n_notesId;
    }

    public String getN_softIssues() {
        return n_softIssues;
    }

    public void setN_softIssues(String n_softIssues) {
        this.n_softIssues = n_softIssues;
    }

    public String getN_hardIssues() {
        return n_hardIssues;
    }

    public void setN_hardIssues(String n_hardIssues) {
        this.n_hardIssues = n_hardIssues;
    }

    public String getN_moveid() {
        return n_moveid;
    }

    public void setN_moveid(String n_moveid) {
        this.n_moveid = n_moveid;
    }

    public String getS_categoryName() {
        return s_categoryName;
    }

    public void setS_categoryName(String s_categoryName) {
        this.s_categoryName = s_categoryName;
    }

    public String getPhotoSync() {
        return photoSync;
    }

    public void setPhotoSync(String photoSync) {
        this.photoSync = photoSync;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getC_code() {
        return c_code;
    }

    public void setC_code(String c_code) {
        this.c_code = c_code;
    }

    public String getI_categoryCode() {
        return i_categoryCode;
    }

    public void setI_categoryCode(String i_categoryCode) {
        this.i_categoryCode = i_categoryCode;
    }

    public String getS_categoryCode() {
        return s_categoryCode;
    }

    public void setS_categoryCode(String s_categoryCode) {
        this.s_categoryCode = s_categoryCode;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getPhotoCategoryId() {
        return photoCategoryId;
    }

    public void setPhotoCategoryId(String photoCategoryId) {
        this.photoCategoryId = photoCategoryId;
    }

    public String getPhotoBase64() {
        return photoBase64;
    }

    public void setPhotoBase64(String photoBase64) {
        this.photoBase64 = photoBase64;
    }

    public String getPhotoMoveid() {
        return photoMoveid;
    }

    public void setPhotoMoveid(String photoMoveid) {
        this.photoMoveid = photoMoveid;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getCategoryTypeNumber() {
        return categoryTypeNumber;
    }

    public void setCategoryTypeNumber(String categoryTypeNumber) {
        this.categoryTypeNumber = categoryTypeNumber;
    }

    public String getS_itemQuantity() {
        return s_itemQuantity;
    }

    public void setS_itemQuantity(String s_itemQuantity) {
        this.s_itemQuantity = s_itemQuantity;
    }

    public String getS_surveyItemId() {
        return s_surveyItemId;
    }

    public void setS_surveyItemId(String s_surveyItemId) {
        this.s_surveyItemId = s_surveyItemId;
    }

    public String getI_moveId() {
        return i_moveId;
    }

    public String getI_userId() {
        return i_userId;
    }

    public void setI_userId(String i_userId) {
        this.i_userId = i_userId;
    }

    public void setI_moveId(String i_moveId) {
        this.i_moveId = i_moveId;
    }

    public String getI_categoryId() {
        return i_categoryId;
    }

    public void setI_categoryId(String i_categoryId) {
        this.i_categoryId = i_categoryId;
    }

    public String getI_id() {
        return i_id;
    }

    public void setI_id(String i_id) {
        this.i_id = i_id;
    }

    public String getI_name() {
        return i_name;
    }

    public void setI_name(String i_name) {
        this.i_name = i_name;
    }

    public String getI_vol() {
        return i_vol;
    }

    public void setI_vol(String i_vol) {
        this.i_vol = i_vol;
    }

    public String getI_quantity() {
        return i_quantity;
    }

    public void setI_quantity(String i_quantity) {
        this.i_quantity = i_quantity;
    }

    public String getI_total() {
        return i_total;
    }

    public void setI_total(String i_total) {
        this.i_total = i_total;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getC_type() {
        return c_type;
    }

    public void setC_type(String c_type) {
        this.c_type = c_type;
    }

    public String getM_moveId() {
        return m_moveId;
    }

    public void setM_moveId(String m_moveId) {
        this.m_moveId = m_moveId;
    }

    public String getM_clientCode() {
        return m_clientCode;
    }

    public void setM_clientCode(String m_clientCode) {
        this.m_clientCode = m_clientCode;
    }

    public String getM_clientName() {
        return m_clientName;
    }

    public void setM_clientName(String m_clientName) {
        this.m_clientName = m_clientName;
    }

    public String getM_emailClient() {
        return m_emailClient;
    }

    public void setM_emailClient(String m_emailClient) {
        this.m_emailClient = m_emailClient;
    }

    public String getM_phoneNumber() {
        return m_phoneNumber;
    }

    public void setM_phoneNumber(String m_phoneNumber) {
        this.m_phoneNumber = m_phoneNumber;
    }

    public String getM_jobType() {
        return m_jobType;
    }

    public void setM_jobType(String m_jobType) {
        this.m_jobType = m_jobType;
    }

    public String getM_origin_() {
        return m_origin_;
    }

    public void setM_origin_(String m_origin_) {
        this.m_origin_ = m_origin_;
    }

    public String getM_dest_() {
        return m_dest_;
    }

    public void setM_dest_(String m_dest_) {
        this.m_dest_ = m_dest_;
    }

    public String getM_originArea() {
        return m_originArea;
    }

    public void setM_originArea(String m_originArea) {
        this.m_originArea = m_originArea;
    }

    public String getM_destArea() {
        return m_destArea;
    }

    public void setM_destArea(String m_destArea) {
        this.m_destArea = m_destArea;
    }

    public String getM_originStreet() {
        return m_originStreet;
    }

    public void setM_originStreet(String m_originStreet) {
        this.m_originStreet = m_originStreet;
    }

    public String getM_destStreet() {
        return m_destStreet;
    }

    public void setM_destStreet(String m_destStreet) {
        this.m_destStreet = m_destStreet;
    }

    public String getM_originName() {
        return m_originName;
    }

    public void setM_originName(String m_originName) {
        this.m_originName = m_originName;
    }

    public String getM_destName() {
        return m_destName;
    }

    public void setM_destName(String m_destName) {
        this.m_destName = m_destName;
    }

    public String getM_surveyDate() {
        return m_surveyDate;
    }

    public void setM_surveyDate(String m_surveyDate) {
        this.m_surveyDate = m_surveyDate;
    }

    public String getM_moveRep() {
        return m_moveRep;
    }

    public void setM_moveRep(String m_moveRep) {
        this.m_moveRep = m_moveRep;
    }

    public String getM_smsstatus() {
        return m_smsstatus;
    }

    public void setM_smsstatus(String m_smsstatus) {
        this.m_smsstatus = m_smsstatus;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public String getU_email() {
        return u_email;
    }

    public void setU_email(String u_email) {
        this.u_email = u_email;
    }

    public String getU_phoneNumber() {
        return u_phoneNumber;
    }

    public void setU_phoneNumber(String u_phoneNumber) {
        this.u_phoneNumber = u_phoneNumber;
    }

    public String getU_username() {
        return u_username;
    }

    public void setU_username(String u_username) {
        this.u_username = u_username;
    }

    public String getU_password() {
        return u_password;
    }

    public void setU_password(String u_password) {
        this.u_password = u_password;
    }

    public String getU_level() {
        return u_level;
    }

    public void setU_level(String u_level) {
        this.u_level = u_level;
    }

    public String getM_description() {
        return m_description;
    }

    public void setM_description(String m_description) {
        this.m_description = m_description;
    }

    public String getM_statusId() {
        return m_statusId;
    }

    public void setM_statusId(String m_statusId) {
        this.m_statusId = m_statusId;
    }

    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
    }

    public String getS_moveId() {
        return s_moveId;
    }

    public void setS_moveId(String s_moveId) {
        this.s_moveId = s_moveId;
    }

    public String getS_itemId() {
        return s_itemId;
    }

    public void setS_itemId(String s_itemId) {
        this.s_itemId = s_itemId;
    }

    public String getS_itemName() {
        return s_itemName;
    }

    public void setS_itemName(String s_itemName) {
        this.s_itemName = s_itemName;
    }

    public String getS_itemVolume() {
        return s_itemVolume;
    }

    public void setS_itemVolume(String s_itemVolume) {
        this.s_itemVolume = s_itemVolume;
    }

    public String getS_itemTotal() {
        return s_itemTotal;
    }

    public void setS_itemTotal(String s_itemTotal) {
        this.s_itemTotal = s_itemTotal;
    }

    public String getS_categoryId() {
        return s_categoryId;
    }

    public void setS_categoryId(String s_categoryId) {
        this.s_categoryId = s_categoryId;
    }

    public String getS_idUser() {
        return s_idUser;
    }

    public void setS_idUser(String s_idUser) {
        this.s_idUser = s_idUser;
    }

    public String getS_date() {
        return s_date;
    }

    public void setS_date(String s_date) {
        this.s_date = s_date;
    }

    public String getS_sync() {
        return s_sync;
    }

    public void setS_sync(String s_sync) {
        this.s_sync = s_sync;
    }
}
