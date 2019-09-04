package com.example.demo.spock

import spock.lang.Specification

import java.math.RoundingMode

class CalculateTest extends Specification {

    def "Calculation.round_금액의 퍼센트 계산 결과값의 소수점 버림 검증"() {
        setup:
        RoundingMode 소수점버림 = RoundingMode.DOWN

        when:
        def result = Calculation.round(10000L, 0.1f, 소수점버림)

        then:
        result == 10L
    }

    def "Calculation.round_여러 금액의 퍼센트 계산 결과값의 소수점 버림 검증"() {
        given:  // =setup
        RoundingMode 소수점버림 = RoundingMode.DOWN

        expect: // =(when + then)
        Calculation.round(amount, rate, 소수점버림) == result

        where:  // 다양한 케이스에 대하여 검증
        amount  | rate  | result
        10000L  | 0.1f  | 10L
        2799L   | 0.2f  | 5L
        159L    | 0.15f | 0L
        //2299L   | 0.15f | 2L      // 일부러 틀려봄. 되게 친절하게 가르쳐준다
    }

    def "Calculation.round_음수가 들어오면 예외가 발생하는지 확인"() {
        given:
        RoundingMode 소수점버림 = RoundingMode.DOWN

        when:
        Calculation.round(-10000L, 0.1f, 소수점버림)

        then:
        thrown NegativeNumberNotAllowException
    }

    def "Calculation.round_주문 금액의 소수점 버림을 검증(Mock 사용)"() {
        given:
        RoundingMode 소수점버림 = RoundingMode.DOWN
        def orderSheet = Mock(OrderSheet.class)

        when:
        long amount = orderSheet.getTotalOrderAmount()

        then:
        orderSheet.getTotalOrderAmount() >> 10000L      // >>: 가짜 객체의 반환 값 설정
        10L == Calculation.round(amount, 0.1f, 소수점버림)
    }

}
