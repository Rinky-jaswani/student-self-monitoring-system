package com.studyspark.checkin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Java is the only service the frontend talks to; this is the internal
 * call it makes to Flask (see docs/system_architecture_and_db_design.md
 * section 2, "Contract between them").
 */
@Component
public class FlaskClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${ai.service.base-url}")
    private String aiServiceBaseUrl;

    public CheckinResponse getCheckinReply(CheckinRequest request) {
        String url = aiServiceBaseUrl + "/checkin";
        return restTemplate.postForObject(url, request, CheckinResponse.class);
    }
}
