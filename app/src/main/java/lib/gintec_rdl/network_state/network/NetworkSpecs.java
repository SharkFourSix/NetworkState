package lib.gintec_rdl.network_state.network;

import android.net.NetworkCapabilities;
import android.os.Build;

import androidx.annotation.RequiresApi;


/**
 * <p>Network connection specifications</p>
 */
public enum NetworkSpecs {
    /**
     * Listen for mobile network state events
     */
    MOBILE_SPEC(0, 0),
    /**
     * Listen for wifi network state events
     */
    WIFI_SPEC(1, 1),
    /**
     * Listen for bluetooth (tethered pan) network events
     */
    BLUETOOTH_SPEC(7, 2),

    /**
     * Listen for VPN network events
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    VPN_SPEC(17, NetworkCapabilities.TRANSPORT_VPN),
    /**
     * Listen for ethernet network events
     */
    ETHERNET_SPEC(9, 3),

    /**
     * Listen for any network connection
     */
    ANY_SPEC(-1, -1);

    NetworkSpecs(int legacy, int v21) {
        legacyType = legacy;
        v21Type = v21;
    }

    final int legacyType, v21Type;
}
