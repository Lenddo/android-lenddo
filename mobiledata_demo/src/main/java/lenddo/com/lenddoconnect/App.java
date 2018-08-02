package lenddo.com.lenddoconnect;

import android.support.multidex.MultiDexApplication;

import com.lenddo.mobile.core.LenddoCoreInfo;
import com.lenddo.mobile.datasdk.AndroidData;
import com.lenddo.mobile.datasdk.models.ClientOptions;


public class App extends MultiDexApplication {

    public void setupDataSDK(String PSID) {
        LenddoCoreInfo.initCoreInfo(getApplicationContext());
        LenddoCoreInfo.setDataPartnerScriptId(getApplicationContext(), PSID);
    }
}
