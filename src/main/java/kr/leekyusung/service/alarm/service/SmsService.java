package kr.leekyusung.service.alarm.service;

import kr.leekyusung.service.alarm.dto.naver.AlarmReqDto;
import kr.leekyusung.service.alarm.dto.naver.MessageReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService {
    private final String baseUrl = "https://sens.apigw.ntruss.com";
    @Value("${naver.api.access-key}")
    private String accessKey;
    @Value("${naver.api.secret-key}")
    private String secretKey;
    @Value("${naver.api.service-id}")
    private String serviceId;
    @Value("${naver.api.phone-number}")
    private String[] phoneNumber;
    private List<MessageReqDto> receivers;

    private final RestTemplate restTemplate;

    public void send(String subject, String message) throws NoSuchAlgorithmException, InvalidKeyException {
        initReceivers();

        String url = "/sms/v2/services/" + serviceId + "/messages";
        HttpMethod method = HttpMethod.POST;
        String timestamp = String.valueOf(System.currentTimeMillis());
        String signature = makeSignature(method.name(), url, timestamp);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        headers.add("x-ncp-apigw-timestamp", timestamp);
        headers.add("x-ncp-iam-access-key", accessKey);
        headers.add("x-ncp-apigw-signature-v2", signature);

        HttpEntity<AlarmReqDto> requestEntity = new HttpEntity<>(
                new AlarmReqDto(subject, message, receivers),
                headers);
        ResponseEntity<String> response = restTemplate.exchange(baseUrl + url, method, requestEntity, String.class);
        log.info("subject: " + subject + ", message: " + message +  ", response: " + response.getBody());
    }

    public void send(String phone, String subject, String message) throws NoSuchAlgorithmException, InvalidKeyException {
        String url = "/sms/v2/services/" + serviceId + "/messages";
        HttpMethod method = HttpMethod.POST;
        String timestamp = String.valueOf(System.currentTimeMillis());
        String signature = makeSignature(method.name(), url, timestamp);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        headers.add("x-ncp-apigw-timestamp", timestamp);
        headers.add("x-ncp-iam-access-key", accessKey);
        headers.add("x-ncp-apigw-signature-v2", signature);

        HttpEntity<AlarmReqDto> requestEntity = new HttpEntity<>(
                new AlarmReqDto(subject, message, List.of(new MessageReqDto(phone))),
                headers);
        ResponseEntity<String> response = restTemplate.exchange(baseUrl + url, method, requestEntity, String.class);
        log.info("phone: " + phone + ", subject: " + subject + ", message: " + message +  ", response: " + response.getBody());
    }

    private void initReceivers() {
        if (receivers == null) {
            receivers = Arrays.stream(phoneNumber)
                    .map(MessageReqDto::new)
                    .toList();
        }
    }

    private String makeSignature(String method, String url, String timestamp) throws NoSuchAlgorithmException, InvalidKeyException {
        String space = " "; // one space
        String newLine = "\n";  // new line

        String message = method + space + url + newLine + timestamp + newLine + accessKey;

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeBase64String(rawHmac);
    }
}
