package com.chteuchteu.gifapplicationlibrary.hlpr;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chteuchteu.gifapplicationlibrary.R;

public class MainUtil {
    public static final class Prefs {
        public static String getPref(Context c, String key) {
            return c.getSharedPreferences("user_pref", Context.MODE_PRIVATE).getString(key, "");
        }

        public static void setPref(Context c, String key, String value) {
            if (value.equals(""))
                removePref(c, key);
            else {
                SharedPreferences prefs = c.getSharedPreferences("user_pref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(key, value);
                editor.apply();
            }
        }

        public static void removePref(Context c, String key) {
            SharedPreferences prefs = c.getSharedPreferences("user_pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove(key);
            editor.apply();
        }
    }

    public static final class Fonts {
        /* ENUM Custom Fonts */
        public enum CustomFont {
            Roboto_Regular("Roboto-Regular.ttf"),
            Roboto_Light("Roboto-Light.ttf");

            final String file;
            private CustomFont(String fileName) { this.file = fileName; }
            public String getValue() { return this.file; }
        }

        /* Fonts */
        public static void setFont(Context c, ViewGroup g, CustomFont font) {
            Typeface mFont = Typeface.createFromAsset(c.getAssets(), font.getValue());
            setFont(g, mFont);
        }

        public static void setFont(Context c, TextView t, CustomFont font) {
            Typeface mFont = Typeface.createFromAsset(c.getAssets(), font.getValue());
            t.setTypeface(mFont);
        }

        private static void setFont(ViewGroup group, Typeface font) {
            int count = group.getChildCount();
            View v;
            for (int i = 0; i < count; i++) {
                v = group.getChildAt(i);
                if (v instanceof TextView)
                    ((TextView) v).setTypeface(font);
                else if (v instanceof ViewGroup)
                    setFont((ViewGroup) v, font);
            }
        }
    }

    public static final class Transitions {
        public enum TransitionStyle { DEEPER, SHALLOWER }

        public static void setTransition(Activity activity, TransitionStyle transitionStyle) {
            switch (transitionStyle) {
                case DEEPER:
                    activity.overridePendingTransition(R.anim.deeper_in, R.anim.deeper_out);
                    break;
                case SHALLOWER:
                    activity.overridePendingTransition(R.anim.shallower_in, R.anim.shallower_out);
                    break;
            }
        }
    }
}
