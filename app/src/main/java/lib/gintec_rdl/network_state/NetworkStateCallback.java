package lib.gintec_rdl.network_state;

import lib.gintec_rdl.network_state.network.NetworkSpec;

public interface NetworkStateCallback {
    void action(NetworkSpec networkSpec);
}
