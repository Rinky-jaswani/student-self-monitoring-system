package com.studyspark.checkin;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Frontend calls this — Java is the only service the browser talks to
 * (see docs/system_architecture_and_db_design.md section 2). This proxies
 * to Flask internally via FlaskClient.
 *
 * TODO once Postgres is wired up (build order step 1): replace the
 * hardcoded sample logs below with a real query for this student's
 * recent study_logs rows.
 */
@RestController
@RequestMapping("/api/checkin")
public class CheckinController {

    private final FlaskClient flaskClient;

    public CheckinController(FlaskClient flaskClient) {
        this.flaskClient = flaskClient;
    }

    @PostMapping
    public CheckinResponse checkin(@RequestBody CheckinRequest request) {
        if (request.getRecentLogs() == null || request.getRecentLogs().isEmpty()) {
            request.setRecentLogs(sampleRecentLogs());
        }
        return flaskClient.getCheckinReply(request);
    }

    // Placeholder data so this endpoint is testable before Postgres exists.
    private List<StudyLogSummary> sampleRecentLogs() {
        return List.of(
            new StudyLogSummary("2026-08-15", "Operating Systems", "Scheduling", 1.0),
            new StudyLogSummary("2026-08-16", "Mathematics", "Calculus", 2.0)
        );
    }
}
