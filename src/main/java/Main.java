import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    final static String INSTRUMENTATION_NAME = "Remote-DB-Connector";

    public static void main(String [] args) {
        OpenTelemetry openTelemetry = new OTelConfiguration().initializeOpenTel();
        Tracer tracer =
                openTelemetry.getTracer(INSTRUMENTATION_NAME);  //modify name
        LongCounter counter = openTelemetry.getMeter(INSTRUMENTATION_NAME).counterBuilder("work_done").build();

        RemoteCaller remoteCaller = new RemoteCaller();
        Span fetchRecordSpan = tracer.spanBuilder("FetchRecord").startSpan();
        Span iterateSpan;
        try {
            ResultSet resultSet = remoteCaller.fetchRecords();
            iterateSpan = tracer.spanBuilder("Iterate Through Record").startSpan();
            while (resultSet.next()) {
                counter.add(1);
            }
            iterateSpan.end();

        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        } finally {
            fetchRecordSpan.end();
        }

        try {
            // Flush out the metrics that have not yet been exported
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            // ignore since we're exiting
        }
    }

}
