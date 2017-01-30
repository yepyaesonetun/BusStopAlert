package com.prime.busstopalert.mflib;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by SantaClaus on 12/01/2017.
 */

public class PrimeMMEditText extends EditText {

    public PrimeMMEditText(final Context context) {
        super(context);
        if (MDetect.isUnicode()) {
            setHint(getHint());
        } else {
            setHint(Rabbit.uni2zg(getHint().toString()));
        }
    }

    public PrimeMMEditText(final Context context, AttributeSet attrs) {
        super(context, attrs);
        if (MDetect.isUnicode()) {
            setHint(getHint());
        } else {
            setHint(Rabbit.uni2zg(getHint().toString()));
        }
    }

    public PrimeMMEditText(final Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (MDetect.isUnicode()) {
            setHint(getHint());
        } else {
            setHint(Rabbit.uni2zg(getHint().toString()));
        }
    }

    /**
     * method for setting text
     *
     * @param unicodeString string in Myanmar Unicode
     */
    public void setMMText(String unicodeString) {
        if (MDetect.isUnicode()) {
            setText(unicodeString);
        } else {
            setText(Rabbit.uni2zg(unicodeString));
        }
    }

    /**
     * method for getting text
     *
     * @return CharSequence in Myanmar Unicode
     */
    public CharSequence getMMText() {
        return MDetect.isUnicode() ? getText() : Rabbit.zg2uni(getText().toString());
    }
}
