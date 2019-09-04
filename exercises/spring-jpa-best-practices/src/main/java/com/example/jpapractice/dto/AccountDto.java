package com.example.jpapractice.dto;

import com.example.jpapractice.domain.Account;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO 클래스의 필요 이유
 *
 * Account에 정보를 변경하는 API가 있다고 가정했을 경우, RequestBody를 Account 클래스로 받게 된다면 다음과 같은 문제 발생:
 * 1. 데이터 안전성
 *    - 원하는 정보만 노출시킬 수 없고, 원치 않은 데이터 변경이 일어날 수 있다.
 *    - 그렇다고 JsonIgnore 속성들을 두어 임시로 막는 것은 바람직하지 않다.
 * 2. 명확해지는 요구사항
 *    - DTO를 사용할 경우 특정 API가 어떤 값들을 변경할 수 있는지 명확해진다.
 *    - Request 값과 Response 값이 명확하게 되어 API가 더더욱 명확해진다.
 */
public class AccountDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SignUpReq {
        private String email;
        private String firstName;
        private String lastName;
        private String password;
        private String address1;
        private String address2;
        private String zip;

        @Builder
        public SignUpReq(String email, String firstName, String lastName, String password, String address1, String address2, String zip) {
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.password = password;
            this.address1 = address1;
            this.address2 = address2;
            this.zip = zip;
        }

        public Account toEntity() {
            return Account.builder()
                    .email(this.email)
                    .firstName(this.firstName)
                    .lastName(this.lastName)
                    .password(this.password)
                    .address1(this.address1)
                    .address2(this.address2)
                    .zip(this.zip)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MyAccountReq {
        private String address1;
        private String address2;
        private String zip;

        @Builder
        public MyAccountReq(String address1, String address2, String zip) {
            this.address1 = address1;
            this.address2 = address2;
            this.zip = zip;
        }
    }

    @Getter
    public static class Res {
        private String email;
        private String firstName;
        private String lastName;
        private String address1;
        private String address2;
        private String zip;

        public Res(Account account) {
            this.email = account.getEmail();
            this.firstName = account.getFirstName();
            this.lastName = account.getLastName();
            this.address1 = account.getAddress1();
            this.address2 = account.getAddress2();
            this.zip = account.getZip();
        }
    }
}
