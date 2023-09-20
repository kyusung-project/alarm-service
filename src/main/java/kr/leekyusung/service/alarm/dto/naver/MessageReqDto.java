package kr.leekyusung.service.alarm.dto.naver;

import lombok.Getter;

@Getter
public class MessageReqDto {
    String to;
//    String subject;
//    String content;

    public MessageReqDto(String to) {
        this.to = to;
    }
}
