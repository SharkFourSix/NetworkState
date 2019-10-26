package lib.gintec_rdl.network_state.network;

import android.content.Context;

import lib.gintec_rdl.network_state.NetworkStateException;

public interface NetworkState {
    static NetworkSpec.Builder of(NetworkSpecs specs, Context context) throws NetworkStateException {
        return new NetworkSpec.Builder(context, specs);
    }
}
