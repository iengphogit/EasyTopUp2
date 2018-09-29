package ocr.avaboy.com;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

/**
 * Created by ILSP on 3/10/2017.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void init(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO tbl_company_detail VALUES(null,'test1','#123#',1)");
        db.execSQL("INSERT INTO tbl_company_detail VALUES(null,'test1','#123#',1)");
        db.execSQL("INSERT INTO tbl_company_detail VALUES(null,'test1','#123#',1)");
        db.execSQL("INSERT INTO tbl_company_detail VALUES(null,'test1','#123#',1)");
        db.execSQL("INSERT INTO tbl_company_detail VALUES(null,'test1','#123#',1)");
        db.execSQL("INSERT INTO tbl_company_detail VALUES(null,'test1','#123#',1)");
        db.execSQL("INSERT INTO tbl_company_detail VALUES(null,'test1','#123#',1)");
    }

    public void queryData(String sql){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL(sql);
    }

    public void insertData(String name, String desc,String imie_start,String imie_end,int imie_limit, byte[] image){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO tbl_company VALUES (null, ?, ?, ?, ?, ?, ?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1,name);
        statement.bindString(2,desc);
        statement.bindString(3,imie_start);
        statement.bindString(4,imie_end);
        statement.bindLong(5,imie_limit);
        statement.bindBlob(6,image);
        statement.executeInsert();
    }

    public void insertDataDetail(String name, String num,int company_id){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql ="INSERT INTO tbl_company_detail VALUES (null,? ,? ,?)";
        SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(sql);
        sqLiteStatement.clearBindings();

        sqLiteStatement.bindString(1,name);
        sqLiteStatement.bindString(2,num);
        sqLiteStatement.bindLong(3,company_id);
        sqLiteStatement.executeInsert();

    }

    public int updateData(String name, String desc,String imie_start,String imie_end,int imie_limit, byte[] image, String where){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE tbl_company SET name=?, desc=?, imie_start=?, imie_end=?, imie_limit=?, image=? WHERE name=?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1,name);
        statement.bindString(2,desc);
        statement.bindString(3,imie_start);
        statement.bindString(4,imie_end);
        statement.bindLong(5,imie_limit);
        statement.bindBlob(6,image);
        statement.bindString(7,where);
        return statement.executeUpdateDelete();
    }



    public void execSQL(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }


    public int updateDataDetail(String name, String service_num, String where){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE tbl_company_detail SET name=?, service_num=? WHERE Id=?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1,name);
        statement.bindString(2,service_num);
        statement.bindString(3,where);
        return statement.executeUpdateDelete();
    }

    public int deleteData(String name){
        SQLiteDatabase database = getWritableDatabase();
       return database.delete("tbl_company","name=?",new String[]{name});
    }

    public int deleteDataDetail(String name){
        SQLiteDatabase database = getWritableDatabase();
        return database.delete("tbl_company_detail","name=?",new String[]{name});
    }

    public int deleteDataDetailById(String id){
        SQLiteDatabase database = getWritableDatabase();
        return database.delete("tbl_company_detail","Id=?",new String[]{id});
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return  database.rawQuery(sql,null);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
