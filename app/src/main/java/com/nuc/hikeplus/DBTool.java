package com.nuc.hikeplus;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by weeznn on 2016/7/18.
 */
public class DBTool extends SQLiteOpenHelper {
    private static final String TAG="SearchActivity";

    //数据库版本
    private static final int DB_VERSION=1;
    //数据库路径
    private static String DB_PATH="/data/data/com.nuc.hikeplus/databases/";
    //数据库名称
    private static String DB_NAME="hikeplus.db3";
    //在raw文件夹下的名称
    private static String RAW_DB_NAME="hikeplus.db3";

    private Context context=null;
    private static SQLiteDatabase database=null;
    private static Cursor cursor=null;


    public DBTool(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
        this.context=context;
    }

    public DBTool(Context context, String name, int version){
        this(context,name,null,version);
    }

    public DBTool(Context context, String name){
        this(context,name,DB_VERSION);
    }

    public DBTool (Context context) {
        this(context, DB_PATH + DB_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    //升级数据库   不写了
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public synchronized void close() {
        if(database!=null){
            database.close();
        }
        super.close();
    }

    public void createDB() throws IOException{
        boolean dbExist=checkDB();
        if(dbExist)
        {
            Log.i(TAG,"数据库已存在，不做任何操作");
        }
        else
        {
            Log.i(TAG,"创建数据库");
            //创建数据库
            try {
                File dir = new File(DB_PATH);
                if(!dir.exists()){
                    dir.mkdirs();
                }
                File dbf = new File(DB_PATH + DB_NAME);
                if(dbf.exists()){
                    dbf.delete();
                }
                SQLiteDatabase.openOrCreateDatabase(dbf, null);
                Log.i("SearchActivity","无数据库文件，准备复制");
                // 复制asseets中的数据库文件到DB_PATH下
                copyDataBase();
            } catch (IOException e) {
                throw new Error("数据库创建失败");
            }
        }
    }

    /**
     * 复制assets文件中的数据库到指定路径
     * 使用输入输出流进行复制
     **/
    private void copyDataBase() throws IOException{

        Log.i(TAG,"copydatabase");
        InputStream myInput = context.getResources().openRawResource(R.raw.hikeplus);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }
    /**
     * 判断数据库是否存在
     */

    public boolean checkDB() {
        Log.i(TAG,"checkdb");
        String path = DB_PATH+DB_NAME;
        SQLiteDatabase checkdb = null;
        try {
            checkdb = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        }catch (SQLiteException e){
            if(checkdb!=null){
                checkdb.close();
            }
        }
        return checkdb!=null?true:false;
    }

    //以下是项目对数据库的各种操作

    /**
     * 搜索数据库中older表中的所有数据
     * @return
     */
    public Cursor searchDB(){
        database=getReadableDatabase();
        final String SELETE_ALL="select * from olderTable";
        Cursor cursor=database.rawQuery(SELETE_ALL,null);
        return cursor;
    }

    /**
     * 通过名字搜索数据库中older表中的所有数据
     * @param name
     * @return
     */
    public Cursor searchDBFromName(String name){
        database=getReadableDatabase();
        final String SELECT_NAME="select * from olderTable where receiverName='"+name+"'";
        Cursor cursor=database.rawQuery(SELECT_NAME,null);
        return cursor;
    }

    /**
     * 通过手机号码搜索数据库中older表中的所有数据
     * @param tel
     * @return
     */
    public Cursor searchDBFronTel(String tel){
        database=getReadableDatabase();
        final String SELECT_TEL="select * from olderTable where receiverTel='"+tel+"'";
        Cursor cursor=database.rawQuery(SELECT_TEL,null);
        return cursor;

    }

    /**
     * 通过订单编号搜索数据库中older表中的所有数据
     * @param postNO
     * @return
     * receiverName,receiverTel,olderNo
     */
    public Cursor searchDBFromPostNo(String postNO){
        database=getReadableDatabase();
        final String SELECT_POSTNO="select * from olderTable where olderNo='"+postNO+"'";
        Cursor cursor=database.rawQuery(SELECT_POSTNO,null);
        return cursor;

    }

    /**
     * 通过快递单号搜索数据库中older表中的所有数据
     * @param logistic
     * @return
     */
    public Cursor searchDBFromLogistic(String logistic){
        database=getReadableDatabase();
        final String SELECT_POSTNO="select * from olderTable where LogisticCode='"+logistic+"'";
        Cursor cursor=database.rawQuery(SELECT_POSTNO,null);
        return cursor;
    }



    /**
     * 通过货物名称搜索数据库中goods表中的所有数据
     * @param goodsName
     * @return
     */
    public Cursor searchGoodsFromName(String goodsName){
        database=getReadableDatabase();
        final String SELECT_GOODS = "select * from goodsTable where goodsName='" + goodsName + "'";
        Cursor cursor=database.rawQuery(SELECT_GOODS,null);
        return cursor;
    }

    /**
     * 添加一条订单到olderTable；
     * @param receiverName
     * @param receiverTel
     * @param receiverProvince
     * @param receiverCity
     * @param receiverAddress
     * @param goodsName
     */
    public void addItem(String orderNo, String orderDate, String receiverName, String receiverTel, String receiverProvince, String receiverCity, String receiverAddress, String goodsName,String logistic,Boolean isPay,int DBid){
        database=getWritableDatabase();
        boolean state=false;
        final String ADD="insert into olderTable values('"+orderNo+"','"+orderDate+"','"+receiverName+"','"+receiverTel+"','"
                +receiverProvince+"','"+receiverCity+"','"+receiverAddress+"','"+goodsName+"','"+logistic+"','"+isPay+"','"+state+"','"+DBid+"')";
        Log.i(TAG,ADD);
        database.execSQL(ADD);
    }

    /**
     * 删除一条订单
     * @param olderNo
     */
    public void deleteItem(String olderNo){
        database=getWritableDatabase();
        final String DELETE="delete from olderTable where olderNo='"+olderNo+"'";
        database.execSQL(DELETE);
    }

    /**
     * 将一条记录设置为已经取货
     * @param olderNo
     */
    public void updataOlderTable(String olderNo){
        database=getWritableDatabase();
        final String UPDATAOLDERTABLE="updata olderTable set state='1' where olderNo='"+olderNo+"'";
        database.execSQL(UPDATAOLDERTABLE);
    }
}
