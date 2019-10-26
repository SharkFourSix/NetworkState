package lib.gintec_rdl.network_state.network;

import android.content.Context;

import lib.gintec_rdl.network_state.NetworkStateCallback;
import lib.gintec_rdl.network_state.utils.LangUtils;

/**
 * Callback interface for receiving connection state change events
 */
interface NetworkChangeListener {
    NetworkStateCallback NOP_CALLBACK = network -> {
    };

    default void callback(NetworkStateCallback callback, NetworkSpec spec) {
        spec.handler.post(() -> LangUtils.Optional.of(callback).orElse(NOP_CALLBACK).action(spec));
    }

    /**
     * Instructs this listener to register itself.
     *
     * @param context .
     */
    void registerSelf(Context context);

    /**
     * Instructs this listener to unregister itself.
     *
     * @param context .
     */
    void unregisterSelf(Context context);

    /**
     * @return Whether registered or not
     */
    boolean isRegistered();
}
