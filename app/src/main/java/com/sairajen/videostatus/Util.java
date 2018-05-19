package com.sairajen.videostatus;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Gmonetix
 */

public class Util {

    public static void shareLink(Context context, String text){
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Sai Here");
            i.putExtra(Intent.EXTRA_TEXT, text);
            context.startActivity(Intent.createChooser(i, "choose one"));
        } catch(Exception e) {
            Log.e("ERROR",""+e.getMessage());
        }
    }

    public static void copyText(Context context, String text) {
        Toast.makeText(context,"copied", Toast.LENGTH_SHORT).show();
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied link", text);
        clipboard.setPrimaryClip(clip);
    }

}
