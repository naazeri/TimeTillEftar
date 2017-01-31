package ir.nazery.timetilleftar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import ir.nazery.timetilleftar.database.DataManager;
import ir.nazery.timetilleftar.lib.ShowMessage;
import ir.nazery.timetilleftar.lib.StatusBarUtil;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "reza";
    private DataManager dataManager;
    private List<String> cityNames;
    private SortedMap<String, String> map;
    private String cityName;
    private Handler handler;
    private Runnable runnable;
    private Typeface font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            initDB();

            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                StatusBarUtil.setTranslucent(this, 0);
                StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimaryDark), 0);
            }

            font = Typeface.createFromAsset(getAssets(), "font/b_yekan.ttf");
            cityNames = getCityNames();
            cityName = getCityName();
            if (cityName == null) {
                getInput();
            } else {
                initView();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ShowMessage.longMessage(getCurrentFocus(), this, R.string.errorText);
            dataManager.clearData();
            startActivity(new Intent(MainActivity.this, MainActivity.class));
            finish();
        }

        String s = getKeyForValue(cityName);
        updateActionBarTitle((s != null) ? s : cityName);
    }

    private String getKeyForValue(String value) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void initDB() {
        dataManager = new DataManager(this);
    }

    private String getCityName() {
        return dataManager.getCityName();
    }

    private List<String> getCityNames() throws Exception {
        map = dataManager.getCityNames();
        List<String> list = new LinkedList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            list.add(entry.getKey());
        }
        return list;
    }

    private void getInput() throws Exception {
        final Dialog dialog = new Dialog(this, R.style.myDialog);
        dialog.setContentView(R.layout.dialog);
        dialog.setTitle("انتخاب شهر");

        final Spinner cityNames_spinner = (Spinner) dialog.findViewById(R.id.main_dialog_spinner_cityName);
        Button submit_button = (Button) dialog.findViewById(R.id.main_dialog_button_submit);
        initSpinner(cityNames_spinner, cityNames);
        submit_button.setTypeface(font);

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    cityName = (String) cityNames_spinner.getSelectedItem();
                    updateActionBarTitle(cityName);
                    cityName = map.get(cityName);
                    dataManager.saveCityName(cityName);
                    initView();
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "خطایی رخ داده است", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private void updateActionBarTitle(String s) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(s);
        }
    }

    private void initSpinner(Spinner spinner, List<String> list) {
        ArrayAdapter<String> colorAdapter;
        colorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(colorAdapter);
    }

    private void initView() throws Exception {
        TextView hour_textView = (TextView) findViewById(R.id.main_textView_hour);
        TextView minute_textView = (TextView) findViewById(R.id.main_textView_minute);
        TextView second_textView = (TextView) findViewById(R.id.main_textView_second);
        TextView footer_hour_textView = (TextView) findViewById(R.id.main_textView_footer_hour);
        TextView footer_minute_textView = (TextView) findViewById(R.id.main_textView_footer_minute);
        TextView footer_second_textView = (TextView) findViewById(R.id.main_textView_footer_second);
        TextView title_textView = (TextView) findViewById(R.id.main_textView_title);

        assert hour_textView != null;
        assert minute_textView != null;
        assert second_textView != null;
        assert footer_hour_textView != null;
        assert footer_minute_textView != null;
        assert footer_second_textView != null;
        assert title_textView != null;

        DateTime todayTime = DateTime.now();
        Log.d("reza", "today: " + todayTime.toString("d/M/yyy"));
        String name = map.get(this.cityName);
        DateTime eftarTime = dataManager.getMaghrebTime((name != null) ? name : cityName, todayTime.toString("d/M/yyy"));

        if (todayTime.isAfter(eftarTime)) {
            todayTime = todayTime.plusDays(1);
            Log.d("reza", "new today: " + todayTime.toString("d/M/yyy"));
            eftarTime = dataManager.getMaghrebTime((name != null) ? name : cityName, todayTime.toString("d/M/yyy"));
            eftarTime = eftarTime.plusDays(1);
        }

        hour_textView.setTypeface(font);
        minute_textView.setTypeface(font);
        second_textView.setTypeface(font);
        footer_hour_textView.setTypeface(font);
        footer_minute_textView.setTypeface(font);
        footer_second_textView.setTypeface(font);
        title_textView.setTypeface(font);
        updateTime(hour_textView, minute_textView, second_textView, eftarTime);
    }

    private void updateTime(final TextView hour_textView, final TextView minute_textView, final TextView second_textView, final DateTime eftarTime) {
        if (handler != null) {
            handler.removeCallbacks(runnable);
        } else {
            handler = new Handler();
        }

//        Log.d(TAG, "eftarTime: " + eftarTime.toString());

        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Period period = new Period(DateTime.now(), eftarTime);
                    PeriodFormatter formatter = new PeriodFormatterBuilder()
                            .appendHours().appendSuffix("h\n")
                            .appendMinutes().appendSuffix("m\n")
                            .appendSeconds().appendSuffix("s\n")
                            .printZeroNever()
                            .toFormatter();

//                    String duration = formatter.print(period);
//                    Log.d(TAG, "duration: " + duration);
                    String a[] = formatter.print(period).split("\n");
                    hour_textView.setText("0");
                    minute_textView.setText("0");
                    second_textView.setText("0");
                    for (String s : a) {
                        if (s.contains("h")) {
                            hour_textView.setText(s.replace("h", ""));
                        } else if (s.contains("m")) {
                            minute_textView.setText(s.replace("m", ""));
                        } else if (s.contains("s")) {
                            second_textView.setText(s.replace("s", ""));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    handler.postDelayed(this, 1000);
                }
            }
        };

        handler.post(runnable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_setCity:
                try {
                    getInput();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
//            case R.id.action_rate:
//                try {
//                    showBazarRate();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    ShowMessage.longMessage(getCurrentFocus(), this, R.string.bazar404);
//                }
//                return true;
            case R.id.action_aboutus:
                startActivity(new Intent(MainActivity.this, AboutusActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showBazarRate() throws Exception {
        // bazar rate
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setData(Uri.parse("bazaar://details?id=" + getPackageName()));
        intent.setPackage("com.farsitel.bazaar");
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            try {
                handler.removeCallbacks(runnable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
