
//OpenTelemetry Imports Begin2

import io.opentelemetry.api.OpenTelemetry; //Allows to create an instance of the OpenTelemetry API
import io.opentelemetry.sdk.OpenTelemetrySdk; //Provides you with builders to create the OpenTelemetry API instance
import io.opentelemetry.sdk.metrics.export.MetricReader; //Allows you to record metric data
import io.opentelemetry.exporter.logging.LoggingMetricExporter; //Allows you to export and log recorded metric data
import io.opentelemetry.exporter.logging.LoggingSpanExporter; //Allows you to export trace data
import io.opentelemetry.sdk.metrics.SdkMeterProvider;            //Provides you with tools to extract meter telemetry
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader; //A metric reader that captures data periodically
import io.opentelemetry.sdk.trace.SdkTracerProvider;         //Allows you to build a tracer and export it
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor; //Allows you to create spans

import java.time.Duration;

//OpenTelemetry Imports End
public class OTelConfiguration {
    private static final String INSTRUMENTATION_NAME = OTelConfiguration.class.getName();
    private static final long METRIC_EXPORT_INTERVAL_MS = 800L;

    public OpenTelemetry initializeOpenTel() {


        SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(SimpleSpanProcessor.create(LoggingSpanExporter.create()))
                .build();


        MetricReader periodicReader =
                PeriodicMetricReader.builder(LoggingMetricExporter.create())
                        .setInterval(Duration.ofMillis(METRIC_EXPORT_INTERVAL_MS))
                        .build();

        SdkMeterProvider sdkMeterProvider =
                SdkMeterProvider.builder().registerMetricReader(periodicReader).build();


        OpenTelemetry openTelemetry = OpenTelemetrySdk.builder()
                .setTracerProvider(sdkTracerProvider)
                .setMeterProvider(sdkMeterProvider)
                .buildAndRegisterGlobal();

        return openTelemetry;
    }




}
