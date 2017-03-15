package lenddo.com.lenddoonboarding;

import android.app.Application;

import com.lenddo.mobile.core.LenddoCoreInfo;


public class SimpleLoan extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LenddoCoreInfo.initCoreInfo(getApplicationContext());
    }
}
