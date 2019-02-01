package com.lenddo.mobile.mobiledatacollectiondemo;

import android.support.multidex.MultiDexApplication;

import com.lenddo.mobile.BuildConfig;
import com.lenddo.mobile.core.LenddoCoreInfo;
import com.lenddo.mobile.datasdk.AndroidData;
import com.lenddo.mobile.datasdk.models.ClientOptions;

/**
 * Created by Joey Mar Antonio on 09/02/2018.
 */

public class MobileDataCollectionDemo extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidData.clear(getApplicationContext());
        LenddoCoreInfo.initCoreInfo(getApplicationContext());
        ClientOptions clientOptions = new ClientOptions();
        clientOptions.enableLogDisplay(BuildConfig.DEBUG);
        LenddoCoreInfo.setDataPartnerScriptId(getApplicationContext(), getString(R.string.partner_script_id));
        AndroidData.setup(getApplicationContext(), clientOptions);
    }

}
