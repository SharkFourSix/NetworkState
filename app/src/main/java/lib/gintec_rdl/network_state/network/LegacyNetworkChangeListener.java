package lib.gintec_rdl.network_state.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import lib.gintec_rdl.network_state.utils.PlatformUtils;

final class LegacyNetworkChangeListener extends BroadcastReceiver implements NetworkChangeListener {
    private static final String TAG = "LegacyListener";
    private final IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

    private final NetworkSpec networkSpec;

    private boolean registered;

    public LegacyNetworkChangeListener(NetworkSpec networkSpec) {
        this.networkSpec = networkSpec;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            final ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = mgr.getActiveNetworkInfo();
            if (networkInfo != null) {
                boolean isTargetNetwork = networkInfo.getType() == networkSpec.builder.legacyNetworkType;

                if (!isTargetNetwork) {
                    if (networkSpec.builder.legacyNetworkType == ConnectivityManager.TYPE_BLUETOOTH) {
                        networkInfo = mgr.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);
                        isTargetNetwork = true;
                    }
                    if (networkSpec.builder.legacyNetworkType == ConnectivityManager.TYPE_ETHERNET) {
                        networkInfo = mgr.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
                        isTargetNetwork = true;
                    } else if (networkSpec.builder.legacyNetworkType == ConnectivityManager.TYPE_VPN) {
                        if (PlatformUtils.isLollipop()) {
                            networkInfo = mgr.getNetworkInfo(ConnectivityManager.TYPE_VPN);
                            isTargetNetwork = true;
                        }
                    }
                }

                // Type all
                if (!isTargetNetwork && networkSpec.builder.legacyNetworkType == NetworkSpecs.ANY_SPEC.legacyType) {
                    isTargetNetwork = true;
                }

                if (isTargetNetwork) {
                    final NetworkInfo.DetailedState detailedState = networkInfo.getDetailedState();
                    switch (detailedState) {
                        case IDLE:
                            callback(networkSpec.builder.whenIdle, networkSpec);
                            break;
                        case FAILED:
                            callback(networkSpec.builder.ifFailed, networkSpec);
                            break;
                        case BLOCKED:
                            callback(networkSpec.builder.ifBlocked, networkSpec);
                            break;
                        case SCANNING:
                            callback(networkSpec.builder.whenScanning, networkSpec);
                            break;
                        case CONNECTED:
                            callback(networkSpec.builder.whenAvailable, networkSpec);
                            break;
                        case SUSPENDED:
                            callback(networkSpec.builder.ifSuspended, networkSpec);
                            break;
                        case CONNECTING:
                            callback(networkSpec.builder.whenConnecting, networkSpec);
                            break;
                        case AUTHENTICATING:
                            callback(networkSpec.builder.whenAuthenticating, networkSpec);
                            break;
                        case DISCONNECTED:
                            callback(networkSpec.builder.whenLost, networkSpec);
                            break;
                        case DISCONNECTING:
                            callback(networkSpec.builder.whenLosing, networkSpec);
                            break;
                        case CAPTIVE_PORTAL_CHECK:
                            callback(networkSpec.builder.whenCheckingCaptivePortal, networkSpec);
                            break;
                        case OBTAINING_IPADDR:
                            callback(networkSpec.builder.whenObtainingIpAddress, networkSpec);
                            break;
                        case VERIFYING_POOR_LINK:
                            callback(networkSpec.builder.whenVerifyingPoorLink, networkSpec);
                            break;
                    }
                } else {
                    Log.w(TAG, "NetworkSpec type not registered:" + networkInfo.getSubtype());
                }
            } else {
                callback(networkSpec.builder.whenNotAvailable, networkSpec);
            }
        }
    }

    @Override
    public void registerSelf(Context context) {
        synchronized (this) {
            context.registerReceiver(this, intentFilter);
            registered = true;
        }
    }

    @Override
    public void unregisterSelf(Context context) {
        synchronized (this) {
            context.unregisterReceiver(this);
            registered = false;
        }
    }

    @Override
    public boolean isRegistered() {
        synchronized (this) {
            return registered;
        }
    }
}