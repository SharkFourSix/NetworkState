package lib.gintec_rdl.network_state.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import lib.gintec_rdl.network_state.NetworkStateCallback;
import lib.gintec_rdl.network_state.utils.PlatformUtils;

/**
 * <p>This class specifies the properties of which network connection to listen changes for</p>
 * <p>Depending on the API use (legacy or v21), some methods may not be called at all.
 * <br /><br />
 * The corresponding callbacks for following methods are guaranteed to be called:
 * <ul>
 * <li>{@link Builder#ifSupported(NetworkStateCallback)}</li>
 * <li>{@link Builder#ifNotSupported(NetworkStateCallback)}</li>
 * <li>{@link Builder#whenAvailable(NetworkStateCallback)}</li>
 * <li>{@link Builder#whenNotAvailable(NetworkStateCallback)}</li>
 * </ul>
 * </p>
 */
public final class NetworkSpec {
    private final DefaultLifecycleObserver lifecycleObserver;
    final Builder builder;
    final Handler handler;
    private final NetworkChangeListener listener;

    private NetworkSpec(Builder builder) {
        this.builder = builder;
        this.handler = new Handler(Looper.getMainLooper());
        if (PlatformUtils.isLollipop()) {
            if (builder.legacy) {
                listener = new LegacyNetworkChangeListener(this);
            } else {
                listener = new V21NetworkChangeListener(this);
            }
        } else {
            listener = new LegacyNetworkChangeListener(this);
        }
        this.lifecycleObserver = new DefaultLifecycleObserver() {
            @Override
            public void onStart(@NonNull LifecycleOwner owner) {
                listener.registerSelf(builder.context);
            }

            @Override
            public void onStop(@NonNull LifecycleOwner owner) {
                listener.unregisterSelf(builder.context);
            }
        };
    }

    /**
     * Attaches the listener lifecycle owner
     *
     * @param lifecycleOwner Lifecycle owner to attach to
     */
    public void attachTo(@NonNull LifecycleOwner lifecycleOwner) {
        if (!builder.networkSupported) {
            listener.callback(builder.ifNotSupported, this);
            // Nothing to observe
        } else {
            listener.callback(builder.ifSupported, this);
            lifecycleOwner.getLifecycle().addObserver(lifecycleObserver);
        }
    }

    /**
     * Detach the network state change listener from the given lifecycle owner
     *
     * @param lifecycleOwner The lifecycle owner to detach from
     */
    public void detachFrom(LifecycleOwner lifecycleOwner) {
        if (lifecycleOwner != null) {
            lifecycleOwner.getLifecycle().removeObserver(lifecycleObserver);

            // No need to keep the listener active
            if (listener.isRegistered()) {
                listener.unregisterSelf(builder.context);
            }
        }
    }

    /**
     * Listener builder class.
     * <p>Note that callback methods specified here will be called on the U.I thread</p>
     */
    public static final class Builder {
        NetworkStateCallback whenIdle;
        NetworkStateCallback ifFailed;
        NetworkStateCallback ifBlocked;
        NetworkStateCallback whenScanning;
        NetworkStateCallback ifSuspended;
        NetworkStateCallback whenConnecting;
        NetworkStateCallback whenAuthenticating;
        NetworkStateCallback whenVerifyingPoorLink;
        NetworkStateCallback whenCheckingCaptivePortal;
        NetworkStateCallback whenObtainingIpAddress;
        NetworkStateCallback whenLosing;
        NetworkStateCallback whenLost;
        NetworkStateCallback whenNotAvailable;
        NetworkStateCallback whenAvailable;
        NetworkStateCallback ifSupported;
        NetworkStateCallback ifNotSupported;

        final int legacyNetworkType;
        final int v21NetworkType;
        private boolean legacy;
        private boolean networkSupported;
        private final Context context;

        private boolean mutable;

        /**
         * @param context Application context
         * @param specs   Network specification
         */
        public Builder(Context context, NetworkSpecs specs) {
            this.context = context;
            this.legacyNetworkType = specs.legacyType;
            this.v21NetworkType = specs.v21Type;
            mutable = true;
        }

        private void assertMutable() {
            if (!mutable) {
                throw new IllegalStateException("You can no longer modify this builder");
            }
        }

        /**
         * Use pre-Lollipop API for older devices
         *
         * @return .
         */
        public Builder legacy() {
            this.legacy = true;
            return this;
        }

        /**
         * Use APIs available since Lollipop
         *
         * @return .
         */
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public Builder v21() {
            this.legacy = false;
            return this;
        }

        /**
         * Set callback when network is idle
         * <p><font color="red">Call not guaranteed</font></p>
         *
         * @param callback .
         * @return .
         * @see android.net.NetworkInfo.DetailedState#IDLE
         */
        public Builder whenIdle(NetworkStateCallback callback) {
            this.whenIdle = callback;
            return this;
        }

        /**
         * <p>Callback if attempt to connect to network failed</p>
         * <p><font color="red">Call not guaranteed</font></p>
         *
         * @param callback .
         * @return .
         * @see android.net.NetworkInfo.DetailedState#FAILED
         */
        public Builder ifFailed(NetworkStateCallback callback) {
            this.ifFailed = callback;
            return this;
        }

        /**
         * <p>Callback when access to network is blocked</p>
         * <p><font color="red">Call not guaranteed</font></p>
         *
         * @param callback .
         * @return .
         * @see android.net.NetworkInfo.DetailedState#BLOCKED
         */
        public Builder ifBlocked(NetworkStateCallback callback) {
            ifBlocked = callback;
            return this;
        }

        /**
         * <p>Callback when searching for an available access point</p>
         * <p><font color="red">Call not guaranteed</font></p>
         *
         * @param callback .
         * @return .
         * @see android.net.NetworkInfo.DetailedState#SCANNING
         */
        public Builder whenScanning(NetworkStateCallback callback) {
            whenScanning = callback;
            return this;
        }

        /**
         * <p>Callback when IP traffic suspended</p>
         * <p><font color="red">Call not guaranteed</font></p>
         *
         * @param callback .
         * @return .
         * @see android.net.NetworkInfo.DetailedState#SUSPENDED
         */
        public Builder ifSuspended(NetworkStateCallback callback) {
            ifSuspended = callback;
            return this;
        }

        public Builder whenConnecting(NetworkStateCallback callback) {
            whenConnecting = callback;
            return this;
        }

        public Builder whenAuthenticating(NetworkStateCallback callback) {
            whenAuthenticating = callback;
            return this;
        }

        public Builder whenVerifyingPoorLink(NetworkStateCallback callback) {
            whenVerifyingPoorLink = callback;
            return this;
        }

        public Builder whenCheckingCaptivePortal(NetworkStateCallback callback) {
            whenCheckingCaptivePortal = callback;
            return this;
        }

        public Builder whenObtainingIpAddress(NetworkStateCallback callback) {
            whenObtainingIpAddress = callback;
            return this;
        }

        public Builder whenLosing(NetworkStateCallback callback) {
            whenLosing = callback;
            return this;
        }

        public Builder whenLost(NetworkStateCallback callback) {
            whenLost = callback;
            return this;
        }

        /**
         * <p>Callback when network connection is not available, i.e in a disconnected state</p>
         * <p>This callback is guaranteed to be called. Use this callback to be notified when
         * the specified network is not available.</p>
         *
         * @param callback .
         * @return .
         */
        public Builder whenNotAvailable(NetworkStateCallback callback) {
            whenNotAvailable = callback;
            return this;
        }

        /**
         * <p>Callback when network connection is available, i.e in a connected state</p>
         * <p>This callback is guaranteed to be called when the specified network is connected
         * and traffic can be exchanged with remote hosts</p>
         *
         * @param callback .
         * @return .
         * @see android.net.NetworkInfo.DetailedState#CONNECTED
         */
        public Builder whenAvailable(NetworkStateCallback callback) {
            whenAvailable = callback;
            return this;
        }

        /**
         * <p>Callback if device supports networking as well as the specified network type</p>
         * <p>This callback is guaranteed to be called and will be the first to be called if all
         * the pre-conditions are met</p>
         *
         * @param callback .
         * @return .
         * @see #ifNotSupported(NetworkStateCallback)
         */
        public Builder ifSupported(NetworkStateCallback callback) {
            ifSupported = callback;
            return this;
        }

        /**
         * <p>Callback if device does not support networking or the specified network type</p>
         * <p>If the right conditions are not met, this will be the first and probably also
         * the last callback to be called</p>
         *
         * @param callback .
         * @return .
         * @see #ifSupported(NetworkStateCallback)
         */
        public Builder ifNotSupported(NetworkStateCallback callback) {
            ifNotSupported = callback;
            return this;
        }

        public NetworkSpec create() {
            assertMutable();
            ConnectivityManager mgr
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (mgr != null) {
                if (PlatformUtils.isLollipop()) {
                    final Network[] networks = mgr.getAllNetworks();
                    for (Network network : networks) {
                        final NetworkInfo networkInfo = mgr.getNetworkInfo(network);
                        // because >= v21 doesn't have corresponding API
                        if (legacyNetworkType == networkInfo.getType()) {
                            networkSupported = true;
                            break;
                        }
                    }
                    // Fallback to legacy checks as well just to be sure
                    final NetworkInfo networkInfo = mgr.getNetworkInfo(legacyNetworkType);
                    networkSupported = networkInfo != null;
                } else {
                    final NetworkInfo networkInfo = mgr.getNetworkInfo(legacyNetworkType);
                    networkSupported = networkInfo != null;
                }
            } else {
                networkSupported = false;
            }
            mutable = false;
            return new NetworkSpec(this);
        }
    }
}
