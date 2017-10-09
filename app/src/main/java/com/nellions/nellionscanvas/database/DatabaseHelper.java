package com.nellions.nellionscanvas.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nellions.nellionscanvas.model.AppModel;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Chris Muiruri on 2/10/2016.
 */
public class DatabaseHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "nellions_db.db";
    private static final int DATABASE_VERSION = 6;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade();
    }

    //get categories by type
    public List<AppModel> getCategories(String jobType) {
        List<AppModel> arrayList = new ArrayList<AppModel>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM c_category WHERE category_type = ? ORDER BY category_name ASC", new String[]{jobType});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            AppModel appModel = new AppModel();
            appModel.setC_id(cursor.getString(cursor.getColumnIndex("category_id")));
            appModel.setC_name(cursor.getString(cursor.getColumnIndex("category_name")));
            appModel.setC_type(cursor.getString(cursor.getColumnIndex("category_type")));
            appModel.setC_code(cursor.getString(cursor.getColumnIndex("category_code")));
            arrayList.add(appModel);
            Log.i("NELLIONS_DB", cursor.getString(cursor.getColumnIndex("category_name")).toLowerCase());
            cursor.moveToNext();
        }
        cursor.close();
        return arrayList;
    }

    //get all categories
    public List<AppModel> getAllCategories() {
        List<AppModel> arrayList = new ArrayList<AppModel>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM c_category", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            AppModel appModel = new AppModel();
            appModel.setC_id(cursor.getString(cursor.getColumnIndex("category_id")));
            appModel.setC_name(cursor.getString(cursor.getColumnIndex("category_name")));
            appModel.setC_type(cursor.getString(cursor.getColumnIndex("category_type")));
            appModel.setC_code(cursor.getString(cursor.getColumnIndex("category_code")));
            arrayList.add(appModel);
            Log.i("NELLIONS_DB", cursor.getString(cursor.getColumnIndex("category_name")).toLowerCase());
            cursor.moveToNext();
        }
        cursor.close();
        return arrayList;
    }

    //delete items
    public boolean deleteItems() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete("c_survey_item", null, null) > 0;
    }

    //get survey items
    public List<AppModel> getSurveyItems(int moveId) {
        List<AppModel> arrayList = new ArrayList<AppModel>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM c_survey_item WHERE moveid='" + moveId + "' ORDER BY category_id DESC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            AppModel appModel = new AppModel();
            appModel.setS_surveyItemId(cursor.getString(cursor.getColumnIndex("survey_item_id")));
            appModel.setS_moveId(cursor.getString(cursor.getColumnIndex("moveid")));
            appModel.setS_itemId(cursor.getString(cursor.getColumnIndex("item_id")));
            appModel.setS_itemName(cursor.getString(cursor.getColumnIndex("item_name")));
            appModel.setS_itemVolume(cursor.getString(cursor.getColumnIndex("item_volume")));
            appModel.setS_itemQuantity(cursor.getString(cursor.getColumnIndex("item_quantity")));
            appModel.setS_itemTotal(cursor.getString(cursor.getColumnIndex("item_total")));
            appModel.setS_categoryId(cursor.getString(cursor.getColumnIndex("category_id")));
            appModel.setS_idUser(cursor.getString(cursor.getColumnIndex("id_user")));
            appModel.setS_sync(cursor.getString(cursor.getColumnIndex("sync")));
            appModel.setS_categoryName(cursor.getString(cursor.getColumnIndex("category_name")));
            arrayList.add(appModel);
            cursor.moveToNext();
        }
        cursor.close();
        return arrayList;
    }

    //get survey items
    public List<AppModel> getSurveyItemsNotSynced(int moveId) {
        List<AppModel> arrayList = new ArrayList<AppModel>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String[] args = new String[]{Integer.toString(moveId), Integer.toString(2), "0"};
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM c_survey_item WHERE moveid=? AND sync=? AND item_quantity !=?", args);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            AppModel appModel = new AppModel();
            appModel.setS_surveyItemId(cursor.getString(cursor.getColumnIndex("survey_item_id")));
            appModel.setS_moveId(cursor.getString(cursor.getColumnIndex("moveid")));
            appModel.setS_itemId(cursor.getString(cursor.getColumnIndex("item_id")));
            appModel.setS_itemName(cursor.getString(cursor.getColumnIndex("item_name")));
            appModel.setS_itemVolume(cursor.getString(cursor.getColumnIndex("item_volume")));
            appModel.setS_itemQuantity(cursor.getString(cursor.getColumnIndex("item_quantity")));
            appModel.setS_itemTotal(cursor.getString(cursor.getColumnIndex("item_total")));
            appModel.setS_categoryId(cursor.getString(cursor.getColumnIndex("category_id")));
            appModel.setS_idUser(cursor.getString(cursor.getColumnIndex("id_user")));
            appModel.setS_sync(cursor.getString(cursor.getColumnIndex("sync")));
            appModel.setS_categoryName(cursor.getString(cursor.getColumnIndex("category_name")));
            arrayList.add(appModel);
            cursor.moveToNext();
        }
        cursor.close();
        return arrayList;
    }

    //get category items
    public List<AppModel> getCategoryItems(int categoryId) {
        List<AppModel> arrayList = new ArrayList<AppModel>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM c_category_item WHERE category_code = ? ORDER BY item_name ASC", new String[]{Integer.toString(categoryId)});
        //Cursor cursor = sqLiteDatabase.rawQuery("SELECT c_category_item.item_id, c_category_item.item_name, c_category_item.item_volume, c_category_item.category_id, c_category_item.category_id, c_category_item.category_name, c_survey_item.item_quantity, c_survey_item.item_total FROM c_category_item LEFT JOIN c_survey_item ON c_category_item.category_id=c_survey_item.category_id AND c_category_item.item_id = c_survey_item.item_id AND c_survey_item.category_id = ? AND c_survey_item.moveid = ? ORDER BY c_category_item.item_id", new String[]{Integer.toString(categoryId), Integer.toString(0022)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            AppModel appModel = new AppModel();
            appModel.setI_id(cursor.getString(cursor.getColumnIndex("item_id")));
            appModel.setI_name(cursor.getString(cursor.getColumnIndex("item_name")));
            appModel.setI_vol(cursor.getString(cursor.getColumnIndex("item_volume")));
            appModel.setI_quantity(cursor.getString(cursor.getColumnIndex("item_quantity")));
            appModel.setI_total(cursor.getString(cursor.getColumnIndex("item_total")));
            appModel.setI_categoryId(cursor.getString(cursor.getColumnIndex("category_id")));
            appModel.setI_categoryCode(cursor.getString(cursor.getColumnIndex("category_code")));
            arrayList.add(appModel);
            cursor.moveToNext();
        }
        cursor.close();
        return arrayList;
    }

    //get survey item volume by id
    public String getSurveyItemVolumeByName(String itemName, String itemCategory, String moveId) {
        String sql = "SELECT item_volume FROM c_survey_item WHERE item_name='" + itemName + "' AND  moveid='" + moveId + "' AND category_name='" + itemCategory + "'";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            String vol = cursor.getString(0);
            cursor.close();
            return vol;
        } else {
            cursor.close();
            return null;
        }
    }

    //get survey item qty by id
    public String getSurveyItemQuantityByName(String itemName, String itemCategory, String moveId) {
        String sql = "SELECT item_quantity FROM c_survey_item WHERE item_name='" + itemName + "' AND moveid='" + moveId + "' AND category_name='" + itemCategory + "'";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            String qty = cursor.getString(0);
            cursor.close();
            return qty;
        } else {
            cursor.close();
            return null;
        }
    }

    //get timestamp
    public String getStatusTime(String moveId, String status) {
        String sql = "SELECT time FROM timestamps WHERE move_id='" + moveId + "' AND status='" + status + "'";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            String time = cursor.getString(0);
            cursor.close();
            return time;
        } else {
            cursor.close();
            return null;
        }
    }

    //save timestamp time
    //ARRIVED
    //STARTED
    public boolean saveStatusTime(String moveId, String time, String status) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("move_id", moveId);
        contentValues.put("time", time);
        contentValues.put("status", status);
        sqLiteDatabase.insert("timestamps", null, contentValues);
        return true;
    }

    //get timestamps
    public List<AppModel> getTimestamps(int moveId) {
        List<AppModel> arrayList = new ArrayList<AppModel>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM timestamps WHERE move_id = ?", new String[]{Integer.toString(moveId)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            AppModel appModel = new AppModel();
            appModel.setT_id(cursor.getString(cursor.getColumnIndex("id")));
            appModel.setT_move_id(cursor.getString(cursor.getColumnIndex("move_id")));
            appModel.setT_time(cursor.getString(cursor.getColumnIndex("time")));
            appModel.setT_status(cursor.getString(cursor.getColumnIndex("status")));
            arrayList.add(appModel);
            cursor.moveToNext();
        }
        cursor.close();
        return arrayList;
    }

    //save items
    public boolean saveSurveyItems(String moveId, String itemId, String itemName,
                                   double itemVolume, int itemQuantity, double itemTotal, int categoryId, int userId, String date, String categoryName) {
        //save timestamp
        if (getStatusTime(moveId, "STARTED") == null) {
            DateFormat df = new SimpleDateFormat("dd MM yyyy, HH:mm", Locale.ENGLISH);
            String time = df.format(Calendar.getInstance().getTime());
            saveStatusTime(moveId, time, "STARTED");
        }

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Double totalVol = itemQuantity * itemVolume;  //calculate total
        contentValues.put("moveid", moveId);
        contentValues.put("item_id", itemId);
        contentValues.put("item_name", itemName);
        contentValues.put("item_volume", itemVolume);
        contentValues.put("item_quantity", itemQuantity);
        contentValues.put("item_total", totalVol);
        contentValues.put("category_id", categoryId);
        contentValues.put("id_user", userId);
        contentValues.put("sync", 2);
        contentValues.put("date", date);
        contentValues.put("category_name", categoryName);

        if (this.checkIfItemExists(moveId, itemId, categoryId, itemName, categoryName) >= 1) {
            String[] args = new String[]{moveId, itemName, categoryName};
            sqLiteDatabase.update("c_survey_item", contentValues, "moveid = ? AND item_name = ? AND category_name = ?", args);
        } else {
            sqLiteDatabase.insert("c_survey_item", null, contentValues);
        }
        return true;
    }

    //check if item exists in the local database
    public int checkIfItemExists(String moveId, String itemId, int categoryId, String itemName, String categoryName) {
        String sql = "SELECT * FROM c_survey_item WHERE moveid = '" + moveId + "' AND item_name = '" + itemName + "' AND category_name = '" + categoryName + "'";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    //check if room exists
    public int checkIfRoomExists(String roomName, String type) {
        String sql = "SELECT * FROM c_category WHERE category_name = '" + roomName + "' AND category_type = '" + type + "'";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    //get total volume
    public double getTotalVolume(String moveId) {
        String sql = "SELECT SUM(item_total) FROM c_survey_item WHERE moveid='" + moveId + "'";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            double total = cursor.getDouble(0);
            cursor.close();
            return total;
        } else {
            cursor.close();
            return 0.0;
        }
    }

    /* method to update status */
    public void updateItemStatus(int surveyItemId, int status) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("sync", status);
        String[] args = new String[]{Integer.toString(surveyItemId)};
        sqLiteDatabase.update("c_survey_item", contentValues, "survey_item_id = ?", args);
    }

    //update item
    public boolean updateItem(String itemId, String volume, String quantity) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Double totalVol = Double.parseDouble(volume) * Integer.parseInt(quantity);  //calculate total
        ContentValues contentValues = new ContentValues();
        contentValues.put("item_volume", volume);
        contentValues.put("item_quantity", quantity);
        contentValues.put("item_total", Double.toString(totalVol));
        String[] args = new String[]{itemId};
        Log.i("NELLIONS UPDATE", itemId);
        return sqLiteDatabase.update("c_survey_item", contentValues, "survey_item_id = ?", args) > 1;
    }

    /* save new room */
    public boolean saveNewRoom(String type, String roomName, int sync) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("category_name", roomName);
        contentValues.put("category_type", type);
        contentValues.put("sync", sync);

        if (this.checkIfRoomExists(roomName, type) >= 1) {
            return false;
        } else {
            sqLiteDatabase.insert("c_category", null, contentValues);
        }
        return true;
    }

    /* insert new room */
    public boolean insertNewRoom(String categoryId, String type, String roomName, int sync) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("category_id", categoryId);
        contentValues.put("category_name", roomName);
        contentValues.put("category_type", type);
        contentValues.put("sync", sync);

        if (this.checkIfRoomExists(roomName, type) >= 1) {
            return false;
        } else {
            sqLiteDatabase.insert("c_category", null, contentValues);
        }
        return true;
    }

    //check if item already exists, this is when adding a new item
    public int checkIfItemAlreadyExists(String itemName, String categoryName) {
        String sql = "SELECT * FROM c_category_item WHERE item_name = '" + itemName + "' AND category_name = '" + categoryName + "'";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    //get categoryName
    public String getCategoryName(int categoryId) {
        String sql = "SELECT category_name FROM c_category WHERE category_id='" + categoryId + "'";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex("category_name"));
            cursor.close();
            return name;
        } else {
            cursor.close();
            return null;
        }
    }

    //save item
    public boolean saveNewItem(String itemName, double itemVolume, int categoryId, int sync) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("item_name", itemName);
        contentValues.put("item_volume", itemVolume);
        contentValues.put("item_quantity", 0);
        contentValues.put("item_total", 0);
        contentValues.put("category_id", categoryId);
        contentValues.put("category_name", this.getCategoryName(categoryId));
        contentValues.put("sync", sync);

        if (this.checkIfItemAlreadyExists(itemName, this.getCategoryName(categoryId)) >= 1) {
            return false;
        } else {
            sqLiteDatabase.insert("c_category_item", null, contentValues);
        }
        return true;
    }

    //insert item
    public boolean insertNewItem(String itemId, String itemName, double itemVolume, int categoryId, String categoryName, int sync) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("item_id", itemId);
        contentValues.put("item_name", itemName);
        contentValues.put("item_volume", itemVolume);
        contentValues.put("item_quantity", 0);
        contentValues.put("item_total", 0);
        contentValues.put("category_id", categoryId);
        contentValues.put("category_name", categoryName);
        contentValues.put("sync", sync);

        if (this.checkIfItemAlreadyExists(itemName, this.getCategoryName(categoryId)) >= 1) {
            return false;
        } else {
            sqLiteDatabase.insert("c_category_item", null, contentValues);
        }
        return true;
    }

    //check if photo exists
    public int checkIfPhotoExists(String photoName, int categoryId, int moveId) {
        String sql = "SELECT * FROM c_photo WHERE name = '" + photoName + "' AND category_id = '" + categoryId + "' AND moveid = '" + moveId + "'";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    //check if all survey items are synced
    //if int is >= 1; status NOT ALL SYNCED
    public int checkIfAllitemsAreSynced(int moveId) {
        int sync = 2;
        String sync2 = "NULL";
        String sql = "SELECT * FROM c_survey_item WHERE moveid = ? AND sync = ? AND item_quantity != ?";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String[] args = new String[]{Integer.toString(moveId), Integer.toString(sync), "0"};
        Cursor cursor = sqLiteDatabase.rawQuery(sql, args);
        int cnt = cursor.getCount();
        cursor.close();
        Log.i("SYNCED_ITEMS", Integer.toString(cnt));
        return cnt;
    }

    //save image
    public boolean savePhoto(String photoName, int categoryId, String base64, int moveId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", photoName);
        contentValues.put("category_id", categoryId);
        contentValues.put("base64", base64);
        contentValues.put("moveid", moveId);
        contentValues.put("sync", 0);

        if (this.checkIfPhotoExists(photoName, categoryId, moveId) >= 1) {
            return false;
        } else {
            sqLiteDatabase.insert("c_photo", null, contentValues);
        }

        return true;
    }

    //get photos
    public List<AppModel> getPhotos(String moveId) {
        List<AppModel> arrayList = new ArrayList<AppModel>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM c_photo WHERE moveid = ?", new String[]{moveId});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            AppModel appModel = new AppModel();
            appModel.setPhotoId(cursor.getString(cursor.getColumnIndex("photo_id")));
            appModel.setPhotoName(cursor.getString(cursor.getColumnIndex("name")));
            appModel.setPhotoCategoryId(cursor.getString(cursor.getColumnIndex("category_id")));
            appModel.setPhotoBase64(cursor.getString(cursor.getColumnIndex("base64")));
            appModel.setPhotoMoveid(cursor.getString(cursor.getColumnIndex("moveid")));
            appModel.setPhotoSync(cursor.getString(cursor.getColumnIndex("sync")));
            arrayList.add(appModel);
            cursor.moveToNext();
        }
        cursor.close();
        return arrayList;
    }

    //delete photo
    public boolean deletePhotos() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete("c_photo", null, null) > 0;
    }

    //get rooms that are not synced
    public List<AppModel> getRoomsNotSynced(int syncStatus) {
        List<AppModel> arrayList = new ArrayList<AppModel>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM c_category WHERE sync = ?", new String[]{Integer.toString(syncStatus)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            AppModel appModel = new AppModel();
            appModel.setC_id(cursor.getString(cursor.getColumnIndex("category_id")));
            appModel.setC_name(cursor.getString(cursor.getColumnIndex("category_name")));
            appModel.setC_type(cursor.getString(cursor.getColumnIndex("category_type")));
            arrayList.add(appModel);
            Log.i("NELLIONS_ROOMS", cursor.getString(cursor.getColumnIndex("category_name")).toLowerCase());
            cursor.moveToNext();
        }
        cursor.close();
        return arrayList;
    }

    //update room sync status on sync
    public void updateRoomSyncStatus(int roomId, int status) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("sync", status);
        String[] args = new String[]{Integer.toString(roomId)};
        sqLiteDatabase.update("c_category", contentValues, "category_id = ?", args);
    }

    //get room items not synced
    public List<AppModel> getRoomItemsNotSynced(int syncStatus) {
        List<AppModel> arrayList = new ArrayList<AppModel>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM c_category_item WHERE sync = ?", new String[]{Integer.toString(syncStatus)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            AppModel appModel = new AppModel();
            appModel.setI_id(cursor.getString(cursor.getColumnIndex("item_id")));
            appModel.setI_name(cursor.getString(cursor.getColumnIndex("item_name")));
            appModel.setI_vol(cursor.getString(cursor.getColumnIndex("item_volume")));
            appModel.setI_quantity(cursor.getString(cursor.getColumnIndex("item_quantity")));
            appModel.setI_total(cursor.getString(cursor.getColumnIndex("item_total")));
            appModel.setI_categoryId(cursor.getString(cursor.getColumnIndex("category_id")));
            appModel.setI_categoryName(cursor.getString(cursor.getColumnIndex("category_name")));
            arrayList.add(appModel);
            cursor.moveToNext();
        }
        cursor.close();
        return arrayList;
    }

    //update room item sync status on sync
    public void updateRoomItemSyncStatus(int itemId, int status) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("sync", status);
        String[] args = new String[]{Integer.toString(itemId)};
        sqLiteDatabase.update("c_category_item", contentValues, "item_id = ?", args);
    }

    //check if notes exists
    public int checkIfNotesExists(int moveId) {
        String sql = "SELECT * FROM notes WHERE moveid = '" + moveId + "'";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    //save/update notes
    public void saveNotes(int moveId, String softIssues, String hardIssues) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("soft", softIssues);
        contentValues.put("hard", hardIssues);
        contentValues.put("moveid", moveId);

        if (this.checkIfNotesExists(moveId) >= 1) {
            String[] args = new String[]{Integer.toString(moveId)};
            sqLiteDatabase.update("notes", contentValues, "moveid = ?", args);
        } else {
            sqLiteDatabase.insert("notes", null, contentValues);
        }
    }

    //get notes
    public List<AppModel> getNotes(int moveId) {
        List<AppModel> arrayList = new ArrayList<AppModel>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM notes WHERE moveid = ?", new String[]{Integer.toString(moveId)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            AppModel appModel = new AppModel();
            appModel.setN_notesId(cursor.getString(cursor.getColumnIndex("note_id")));
            appModel.setN_softIssues(cursor.getString(cursor.getColumnIndex("soft")));
            appModel.setN_hardIssues(cursor.getString(cursor.getColumnIndex("hard")));
            appModel.setN_moveid(cursor.getString(cursor.getColumnIndex("moveid")));
            arrayList.add(appModel);
            cursor.moveToNext();
        }
        cursor.close();
        return arrayList;
    }

    //delete submitted survey
    public boolean deleteSubmittedMoveItems(String moveId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String[] args = new String[]{moveId};
        String clause = "moveid = ?";
        return sqLiteDatabase.delete("c_survey_item", clause, args) > 0;
    }

}
