package com.example.demo.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})  // 도메인 객체가 Serialize 되지 않는 문제 해결
public class Post {
    @Id
    @GeneratedValue
    Long id;
    String subject;

    @Column(length=10000000)
    String content;

    Date regDate;
}
