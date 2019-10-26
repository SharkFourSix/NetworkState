package lib.gintec_rdl.network_state;

public class ExampleActivity /*extends AppCompatActivity*/ {
/*

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        ArrayList<String> loglist = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
            android.R.layout.simple_list_item_1, android.R.id.text1, loglist);

        ((ListView) findViewById(R.id.listView)).setAdapter(adapter);

        NetworkState.of(NetworkSpecs.ETHERNET_SPEC, this)
            .v21()
            .ifSupported(networkSpec -> adapter.add("Network is supported"))
            .ifNotSupported(networkSpec -> adapter.add("Network not supported"))
            .whenLosing(networkSpec -> adapter.add("Losing network..."))
            .whenNotAvailable(networkSpec -> adapter.add("Network not available"))
            .whenLost(networkSpec -> adapter.add("Network lost"))
            .whenAvailable(networkSpec -> adapter.add("Network available"))
            .ifFailed(networkSpec -> adapter.add("Connection failed"))
            .whenAuthenticating(networkSpec -> adapter.add("Authenticating"))
            .whenCheckingCaptivePortal(networkSpec -> adapter.add("Checking captive portal..."))
            .whenObtainingIpAddress(networkSpec -> adapter.add("Obtaining IP..."))
            .ifBlocked(networkSpec -> adapter.add("Blocked"))
            .ifSuspended(networkSpec -> adapter.add("Suspended"))
            .whenConnecting(networkSpec -> adapter.add("Connecting..."))
            .whenIdle(networkSpec -> adapter.add("Idle"))
            .whenScanning(networkSpec -> adapter.add("Scanning..."))
            .whenVerifyingPoorLink(networkSpec -> adapter.add("Verifying poor link..."))
            .create()
            .attachTo(this);

    }*/
}
