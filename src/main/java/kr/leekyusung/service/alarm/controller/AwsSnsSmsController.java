package kr.leekyusung.service.alarm.controller;

import kr.leekyusung.service.alarm.dto.aws.SnsReqDto;
import kr.leekyusung.service.alarm.service.SmsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@RestController
@RequestMapping("/aws/sns/sms")
@RequiredArgsConstructor
public class AwsSnsSmsController {
    private final SmsService smsService;
    private final ObjectMapper objectMapper;

    @PostMapping
    public String sendAlarm(@RequestBody String body) throws NoSuchAlgorithmException, InvalidKeyException {
        log.info("Received body: " + body);
        try {
            SnsReqDto snsReqDto = objectMapper.readValue(body, SnsReqDto.class);
            if (snsReqDto.getType() != null
                    && "Notification".equals(snsReqDto.getType())) {
                if (snsReqDto.getMessage() == null) {
                    smsService.send(snsReqDto.getSubject(), body);
                } else {
                    smsService.send(snsReqDto.getSubject(), snsReqDto.getMessage());
                }
            } else {
                smsService.send(null, body);
            }
        } catch (Exception e) {
            log.error(e.toString(), e);
            smsService.send(null, body);
        }
        return "success";
    }

    @PostMapping("/{phone}")
    public String sendAlarm(@PathVariable String phone, @RequestBody String body) throws NoSuchAlgorithmException, InvalidKeyException {
        log.info("phone: {}, body: {}", phone, body);
        try {
            SnsReqDto snsReqDto = objectMapper.readValue(body, SnsReqDto.class);
            if (snsReqDto.getType() != null
                    && "Notification".equals(snsReqDto.getType())) {
                if (snsReqDto.getMessage() == null) {
                    smsService.send(phone, snsReqDto.getSubject(), body);
                } else {
                    smsService.send(phone, snsReqDto.getSubject(), snsReqDto.getMessage());
                }
            } else {
                smsService.send(phone, null, body);
            }
        } catch (Exception e) {
            log.error(e.toString(), e);
            smsService.send(phone, null, body);
        }
        return "success";
    }
}
