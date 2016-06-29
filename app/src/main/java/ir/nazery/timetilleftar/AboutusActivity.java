package ir.nazery.timetilleftar;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import ir.nazery.timetilleftar.lib.ShowMessage;

public class AboutusActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        Toolbar toolbar = (Toolbar) findViewById(R.id.aboutus_toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            initView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() throws Exception {
        TextView text = (TextView) findViewById(R.id.aboutus_textView_text);
        Typeface font = Typeface.createFromAsset(getAssets(), "font/b_yekan.ttf");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.aboutus_fab_contactUs);
        CardView card_materialColors = (CardView) findViewById(R.id.cardView_MaterialColor);
        CardView card_mybooks = (CardView) findViewById(R.id.cardView_MyBooks);

        assert text != null;
        text.setTypeface(font);
        setAboutUsText(text);

        assert fab != null;
        assert card_materialColors != null;
        assert card_mybooks != null;
        fab.setOnClickListener(this);
        card_materialColors.setOnClickListener(this);
        card_mybooks.setOnClickListener(this);
    }

    private void setAboutUsText(TextView text) throws PackageManager.NameNotFoundException {
        String appVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        String aboutText = "این برنامه برای اعلام زمان باقی مانده تا افطار جهت راحتی روزه داران محترم توسط تیم برنامه نویسی ایران افزار در ماه مبارک رمضان سال 95 تهیه شده است.\n" +
                "\n" +
                "در صورت مشاهده اشکال و یا ارائه نظرات با ایمیل فوق در تماس باشید\n" +
                "\n" +
                "r.nazeri@rayana.ir\n" +
                "\n" +
                "\n" +
                "نسخه: " + appVersion +
                "\n" +
                "\n";
        text.setText(aboutText);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.aboutus_fab_contactUs: {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"r.nazeri@rayana.ir"});
                intent.putExtra(Intent.EXTRA_SUBJECT, String.format("نظر به برنامه %s ...", getAppName()));
                intent.putExtra(Intent.EXTRA_TEXT, "نظر خود را اینجا بنویسید");
                try {
                    startActivity(Intent.createChooser(intent, "ارسال ایمیل"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Snackbar.make(v, "نرم افزار ارسال ایمیل برروی دستگاه شما نصب نیست", Snackbar.LENGTH_LONG).show();
                }
                break;
            }
            case R.id.cardView_MyBooks: {
                try {
                    String url = "https://cafebazaar.ir/app/appandroid.seyedomid.ir.definebook/?l=fa";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case R.id.cardView_MaterialColor: {
                try {
                    String url = "https://cafebazaar.ir/app/ir.nazeri.materialcolor/?l=fa";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private String getAppName() {
        return getString(getApplicationInfo().labelRes);
    }
}
