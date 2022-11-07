package com.harshul.notify.util;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;

public class Utils {

    public static SpannableString getUnderlineColor(Context context, String text, int diffColor, int start, int end) {
        ForegroundColorSpan color = new ForegroundColorSpan(context.getColor(diffColor));
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(color, start, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        UnderlineSpan underlineSpan = new UnderlineSpan();
        spannableString.setSpan(underlineSpan, end, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

}
