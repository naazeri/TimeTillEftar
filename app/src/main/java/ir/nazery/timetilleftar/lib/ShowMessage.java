package ir.nazery.timetilleftar.lib;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

/***
 * Created by reza on 95/2/12.
 ***/
public class ShowMessage {

    private static Context context;
    private static View view;

    public static void setContext(Context context) {
        ShowMessage.context = context;
    }

    public static void setView(View view) {
        ShowMessage.view = view;
    }

    public static void longMessage(int text) {
        if (view != null) {
            Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
        } else if (context != null) {
            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        }
    }

    public static void longMessage(View view, int text) {
        if (view != null) {
            Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
        } else if (context != null) {
            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        }
    }

    public static void longMessage(View view, Context context, int text) {
        if (view != null) {
            Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
        } else if (context != null) {
            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        }
    }

    public static void shortMessage(int text) {
        if (view != null) {
            Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
        } else if (context != null) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    }

    public static void shortMessage(View view, int text) {
        if (view != null) {
            Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
        } else if (context != null) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    }

    public static void shortMessage(View view, Context context, int text) {
        if (view != null) {
            Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
        } else if (context != null) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    }
}
