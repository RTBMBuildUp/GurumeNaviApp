package com.oxymoron.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.oxymoron.request.LocationInformation;
import com.oxymoron.request.PageState;

public class GurumeNaviIntent {
    private Intent intent;

    public GurumeNaviIntent(Context packageContext, Class<?> cls) {
        this.intent = new Intent(packageContext, cls);
    }

    public GurumeNaviIntent(Intent intent) {
        this.intent = intent;
    }

    public static void startActivity(Activity activity, GurumeNaviIntent gurumeNaviIntent) {
        activity.startActivity(gurumeNaviIntent.intent);
    }

    public void putExtra(String name, LocationInformation locationInformation) {
        intent.putExtra(name, locationInformation);
    }

    public void putExtra(String name, PageState pageState) {
        intent.putExtra(name, pageState);
    }

    public Optional<LocationInformation> getLocationInformationExtra() {
        return Optional.of((LocationInformation) intent.getSerializableExtra(LocationInformation.name));
    }

    public Optional<PageState> getPageStateExtra() {
        return Optional.of((PageState) intent.getSerializableExtra(PageState.name));
    }
}
