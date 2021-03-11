package lenddo.com.lenddoonboarding;

import androidx.multidex.MultiDexApplication;

import com.lenddo.mobile.core.LenddoCoreInfo;

public class SimpleLoan extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        LenddoCoreInfo.initCoreInfo(getApplicationContext());
        LenddoCoreInfo.setOnboardingPartnerScriptId("");
    }
}
