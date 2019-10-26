package lib.gintec_rdl.network_state.utils;

import android.os.Build;

public final class PlatformUtils {
    private PlatformUtils() {
    }

    public static boolean isLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}
