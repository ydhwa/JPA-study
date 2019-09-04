package com.example.demo.spock;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Spock 실습용 클래스
 * http://woowabros.github.io/study/2018/03/01/spock-test.html
 * https://d2.naver.com/helloworld/568425
 */
public class Calculation {

    // 소수점 버림 기능을 테스트 해볼 예정
    public static long round(long amount, float rate, RoundingMode roundingMode) {
        if(amount < 0) {
            throw new NegativeNumberNotAllowException("음수는 계산할 수 없습니다.");
        }

        // BigDecimal: Java에서 실수 연산의 오차를 줄이기 위해 사용하는 클래스
        return BigDecimal.valueOf(amount * rate * 0.01).setScale(0, roundingMode).longValue();
    }
}
