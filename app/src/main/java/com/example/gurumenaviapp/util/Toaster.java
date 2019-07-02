package com.example.gurumenaviapp.util;

import android.content.Context;
import android.widget.Toast;

public class Toaster {
    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
