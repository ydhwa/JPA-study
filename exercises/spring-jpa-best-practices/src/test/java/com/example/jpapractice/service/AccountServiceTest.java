package com.example.jpapractice.service;

import com.example.jpapractice.common.exception.AccountNotFoundException;
import com.example.jpapractice.domain.Account;
import com.example.jpapractice.dto.AccountDto;
import com.example.jpapractice.repository.AccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Test
    public void create_회원가입_성공() {
        // given
        final AccountDto.SignUpReq dto = buildSignUpReq();
        given(accountRepository.save(any(Account.class))).willReturn(dto.toEntity());

        // when
        final Account account = accountService.create(dto);

        // then
        verify(accountRepository, atLeastOnce()).save(any(Account.class));
        assertThatEqual(dto, account);
    }

    @Test
    public void findById_존재하는_경우_회원_리턴() {
        // given
        final AccountDto.SignUpReq dto = buildSignUpReq();
        given(accountRepository.getOne(anyLong())).willReturn(dto.toEntity());

        // when
        final Account account = accountService.findById(anyLong());

        // then
        verify(accountRepository, atLeastOnce()).getOne(anyLong());
        assertThatEqual(dto, account);
    }

    @Test(expected = AccountNotFoundException.class)
    public void findById_존재하지_않는_경우_AccountNotFoundException() {
        // given
        given(accountRepository.getOne(anyLong())).willReturn(null);

        // when
        accountService.findById(anyLong());
    }

    @Test
    public void updateMyAccount() {
        // given
        final AccountDto.SignUpReq signUpReq = buildSignUpReq();
        final AccountDto.MyAccountReq dto = buildMyAccountReq();
        given(accountRepository.getOne(anyLong())).willReturn(signUpReq.toEntity());

        // when
        final Account account = accountService.updateMyAccount(anyLong(), dto);

        // then
        assertThat(dto.getAddress1(), is(account.getAddress1()));
        assertThat(dto.getAddress2(), is(account.getAddress2()));
        assertThat(dto.getZip(), is(account.getZip()));
    }

    private AccountDto.MyAccountReq buildMyAccountReq() {
        return AccountDto.MyAccountReq.builder()
                .address1("주소수정1")
                .address2("주소수정2")
                .zip("012-345-678")
                .build();
    }

    private void assertThatEqual(AccountDto.SignUpReq dto, Account account) {
        assertThat(dto.getAddress1(), is(account.getAddress1()));
        assertThat(dto.getAddress2(), is(account.getAddress2()));
        assertThat(dto.getZip(), is(account.getZip()));
        assertThat(dto.getEmail(), is(account.getEmail()));
        assertThat(dto.getFirstName(), is(account.getFirstName()));
        assertThat(dto.getLastName(), is(account.getLastName()));
        assertThat(dto.getPassword(), is(account.getPassword()));
    }

    private AccountDto.SignUpReq buildSignUpReq() {
        return AccountDto.SignUpReq.builder()
                .address1("서울")
                .address2("성동구")
                .zip("12345")
                .email("email")
                .firstName("길동")
                .lastName("홍")
                .password("password")
                .build();
    }
}