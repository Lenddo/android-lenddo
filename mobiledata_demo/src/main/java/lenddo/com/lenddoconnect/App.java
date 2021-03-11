package lenddo.com.lenddoconnect;

import androidx.multidex.MultiDexApplication;

import com.lenddo.mobile.core.LenddoCoreInfo;

public class App extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        LenddoCoreInfo.initCoreInfo(getApplicationContext());
    }
}
