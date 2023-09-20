package kr.leekyusung.service.alarm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;

@Service
@RequiredArgsConstructor
public class AwsSnsService {
    @Value("${aws.sns.topic-arn}")
    private String topicArn;

    private final SnsClient snsClient;

    public void publish(String message) {
        snsClient.publish(publishRequest ->
                publishRequest.topicArn(topicArn)
                        .message(message));
    }

    public void publish(String subject, String body) {
        snsClient.publish(publishRequest ->
                publishRequest.topicArn(topicArn)
                        .message(body)
                        .subject(subject));
    }
}
