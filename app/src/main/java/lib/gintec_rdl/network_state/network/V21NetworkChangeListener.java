package lib.gintec_rdl.network_state.network;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
final class V21NetworkChangeListener extends ConnectivityManager.NetworkCallback implements NetworkChangeListener {
    private final NetworkSpec networkSpec;
    private final NetworkRequest networkRequest;

    private boolean registered;

    V21NetworkChangeListener(NetworkSpec networkSpec) {
        this.networkSpec = networkSpec;
        NetworkRequest.Builder builder;
        if (networkSpec.builder.v21NetworkType == NetworkSpecs.ANY_SPEC.v21Type) {
            builder = new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_VPN)
                .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_BLUETOOTH)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR);
        } else {
            builder = new NetworkRequest.Builder()
                .addTransportType(networkSpec.builder.v21NetworkType);
        }

        this.networkRequest = builder
            .removeCapability(NetworkCapabilities.NET_CAPABILITY_NOT_VPN)
            .build();
    }

    @Override
    public void onAvailable(Network network) {
        callback(networkSpec.builder.whenAvailable, networkSpec);
    }

    @Override
    public void onLosing(Network network, int maxMsToLive) {
        callback(networkSpec.builder.whenLosing, networkSpec);
    }

    @Override
    public void onLost(Network network) {
        callback(networkSpec.builder.whenLost, networkSpec);
    }

    @Override
    public void onUnavailable() {
        callback(networkSpec.builder.whenNotAvailable, networkSpec);
    }

    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        // TODO Dispatch caps change event
    }

    @Override
    public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
        // TODO Dispatch link change event
    }

    @Override
    public void registerSelf(Context context) {
        synchronized (this) {
            getConnectivityManager(context).requestNetwork(networkRequest, this);
            registered = true;
        }
    }

    @Override
    public void unregisterSelf(Context context) {
        synchronized (this) {
            getConnectivityManager(context).unregisterNetworkCallback(this);
            registered = false;
        }
    }

    private ConnectivityManager getConnectivityManager(Context c) {
        return (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    public boolean isRegistered() {
        synchronized (this) {
            return registered;
        }
    }
}
