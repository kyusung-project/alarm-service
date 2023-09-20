package kr.leekyusung.service.alarm.dto.naver;

import lombok.Getter;

import java.util.List;

@Getter
public class AlarmReqDto {
    String type = "SMS";
    String from = "01045219320";
    String subject;
    String content;
    List<MessageReqDto> messages;

    public AlarmReqDto(String subject, String content, List<MessageReqDto> messages) {
        this.subject = subject;
        this.content = content;
        this.messages = messages;
    }
}
