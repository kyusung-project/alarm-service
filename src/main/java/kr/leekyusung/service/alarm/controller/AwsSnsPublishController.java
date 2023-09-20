package kr.leekyusung.service.alarm.controller;

import kr.leekyusung.service.alarm.service.AwsSnsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/aws/sns/publish")
@RequiredArgsConstructor
public class AwsSnsPublishController {
    private final AwsSnsService awsSnsService;

    @PostMapping
    public String sendAlarm(@RequestBody String body) {
        log.info("Received body: " + body);
        awsSnsService.publish(body);
        return "success";
    }

    @PostMapping("/{subject}")
    public String sendAlarm(@PathVariable String subject, @RequestBody String body) {
        log.info("subject: {}, body: {}", subject, body);
        awsSnsService.publish(subject, body);
        return "success";
    }
}
