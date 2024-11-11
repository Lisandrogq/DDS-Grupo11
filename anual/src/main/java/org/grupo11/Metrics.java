package org.grupo11;

import io.prometheus.metrics.core.metrics.Gauge;
import io.prometheus.metrics.core.metrics.Counter;
import io.prometheus.metrics.exporter.httpserver.HTTPServer;
import io.prometheus.metrics.instrumentation.jvm.JvmMetrics;

public class Metrics {
    private static Metrics instance = null;
    Gauge openConnectionsCounter;
    Counter requestsServedCounter;
    Counter requestsFailedCounter;

    public static synchronized Metrics getInstance() {
        if (instance == null)
            instance = new Metrics();

        return instance;
    }

    public void startMetricsServer() throws Exception {
        JvmMetrics.builder().register();
        registerVariables();

        try {
            HTTPServer server = HTTPServer.builder()
                    .port(9091)
                    .buildAndStart();

            Logger.info("HTTPServer listening on port http://localhost:" + server.getPort() + "/metrics");

            Thread.currentThread().join();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public void registerVariables() {
        openConnectionsCounter = Gauge.builder()
                .name("open_connections")
                .help("number of current opened connections")
                .register();

        requestsServedCounter = Counter.builder()
                .name("requests_served")
                .help("number of requests served")
                .register();

        requestsFailedCounter = Counter.builder()
                .name("failed_requests")
                .help("numbers of request that failed")
                .register();
    }

    public Gauge getOpenConnectionsGauge() {
        return this.openConnectionsCounter;
    }

    public Counter getRequestsServedCounter() {
        return this.requestsServedCounter;
    }

    public Counter getRequestsFailedCounter() {
        return this.requestsFailedCounter;
    }
}
