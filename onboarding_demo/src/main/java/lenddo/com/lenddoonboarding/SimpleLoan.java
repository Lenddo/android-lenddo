package lenddo.com.lenddoonboarding;

import android.support.multidex.MultiDexApplication;

import com.lenddo.mobile.core.LenddoCoreInfo;
import com.lenddo.mobile.datasdk.AndroidData;
import com.lenddo.mobile.datasdk.models.ClientOptions;


public class SimpleLoan extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidData.clear(getApplicationContext());
        LenddoCoreInfo.initCoreInfo(getApplicationContext());
        ClientOptions clientOptions = new ClientOptions();
        clientOptions.enableLogDisplay(BuildConfig.DEBUG);
        AndroidData.setup(getApplicationContext(), clientOptions);
    }
}
