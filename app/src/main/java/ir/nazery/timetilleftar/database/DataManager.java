package ir.nazery.timetilleftar.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;

import ir.nazery.timetilleftar.lib.Remember;

/***
 * Created by reza on 95/01/04.
 ***/
public class DataManager {

    private Context context;
    private String TAG = "reza";

    public DataManager(Context context) {
        new DatabaseHelper(context).getWritableDatabase();
        Remember.init(context, context.getPackageName());
        this.context = context;
    }

    public DateTime getMaghrebTime(String cityName, String date) throws Exception {
        Log.d(TAG, "getMaghrebTime: " + cityName);
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(cityName);
        String[] columns = {"maghreb"};
        String whereClause = "miladi_date = ?";
        String[] whereArgs = {date};

        Cursor cursor = queryBuilder.query(database, columns, whereClause, whereArgs, null, null, null);
        DateTime time = new DateTime();

        if (cursor != null && cursor.moveToFirst()) {
            String maghreb = cursor.getString(cursor.getColumnIndex(columns[0]));
            time = LocalTime.parse(maghreb).toDateTimeToday();
            Log.d(TAG, "getMaghrebTime: " + time.toString());

            cursor.close();
        }

        database.close();
        return time;
    }

    public void saveCityName(String cityName) {
        Log.d(TAG, "saveCityName: " + cityName);
        Remember.putString("cityName", cityName);
    }

    public String getCityName() {
        String cityName = Remember.getString("cityName", null);
        Log.d(TAG, "getCityName: " + cityName);
        return cityName;
    }

    public SortedMap<String, String> getCityNames() throws Exception {
        SortedMap<String, String> cityNames = new TreeMap<>();

        cityNames.put("اهواز", "ahvaz");
        cityNames.put("اراک", "arak");
        cityNames.put("اردبیل", "ardebil");
        cityNames.put("بندرعباس", "bandarabas");
        cityNames.put("بیرجند", "birjand");
        cityNames.put("بجنورد", "bojnord");
        cityNames.put("بوشهر", "boshehr");
        cityNames.put("اصفهان", "esfahan");
        cityNames.put("قزوین", "ghazvin");
        cityNames.put("گرگان", "gorgan");
        cityNames.put("همدان", "hamedan");
        cityNames.put("ایلام", "ilam");
        cityNames.put("کرج", "karaj");
        cityNames.put("کرمان", "kerman");
        cityNames.put("کرمانشاه", "kermanshah");
        cityNames.put("خرم آباد", "khoramabad");
        cityNames.put("مشهد", "mashhad");
        cityNames.put("ارومیه", "oromie");
        cityNames.put("قم", "qom");
        cityNames.put("رشت", "rasht");
        cityNames.put("سنندج", "sanandaj");
        cityNames.put("ساری", "sari");
        cityNames.put("سمنان", "semnan");
        cityNames.put("شهرکرد", "shahrkord");
        cityNames.put("شیراز", "shiraz");
        cityNames.put("تبریز", "tabriz");
        cityNames.put("تهران", "tehran");
        cityNames.put("یاسوج", "yasoj");
        cityNames.put("یزد", "yazd");
        cityNames.put("زاهدان", "zahedan");
        cityNames.put("زنجان", "zanjan");
        return cityNames;
//        DatabaseHelper databaseHelper = new DatabaseHelper(context);
//        SQLiteDatabase database = databaseHelper.getReadableDatabase();
//        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
//        queryBuilder.setTables("sqlite_sequence");
//        String[] columns = {"name"};
//
//        Cursor cursor = queryBuilder.query(database, columns, null, null, null, null, null);
//        List<String> list = new LinkedList<>();
//
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                list.add(cursor.getString(cursor.getColumnIndex(columns[0])));
//            } while (cursor.moveToNext());
//            cursor.close();
//        }
//
//        database.close();
//        return list;
    }

    public void clearData() {
        Remember.clear();
    }

    public class DatabaseHelper extends SQLiteAssetHelper {
        private static final String DATABASE_NAME = "oghate_shaei.db";
        private static final int DATABASE_VERSION = 1;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            setForcedUpgrade();
        }
    }
}
