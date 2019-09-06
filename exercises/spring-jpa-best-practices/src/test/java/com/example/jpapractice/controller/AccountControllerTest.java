package com.example.jpapractice.controller;

import com.example.jpapractice.common.error.ErrorCode;
import com.example.jpapractice.common.error.ErrorExceptionController;
import com.example.jpapractice.common.exception.EmailDuplicationException;
import com.example.jpapractice.domain.Account;
import com.example.jpapractice.dto.AccountDto;
import com.example.jpapractice.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.validation.ConstraintViolationException;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @RunWith(MockitoJUnitRunner.class)
// solve UnnecessaryStubbingException(https://stackoverflow.com/questions/42947613/how-to-resolve-unneccessary-stubbing-exception)
@RunWith(MockitoJUnitRunner.Silent.class)
public class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountService accountService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController)
                .setControllerAdvice(new ErrorExceptionController())
                .build();
    }

    @Test
    public void signUp() throws Exception {
        // given
        final AccountDto.SignUpReq dto = buildSignUpReq();
        given(accountService.create(any())).willReturn(dto.toEntity());

        // when
        final ResultActions resultActions = requestSignUp(dto);

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.address1", is(dto.getAddress1())))
                .andExpect(jsonPath("$.address2", is(dto.getAddress2())))
                .andExpect(jsonPath("$.zip", is(dto.getZip())))
                .andExpect(jsonPath("$.email", is(dto.getEmail())))
                .andExpect(jsonPath("$.firstName", is(dto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(dto.getLastName())));
    }

    @Test
    public void signUp_이메일_형식_유효하지_않을경우() throws Exception {
        // given
        final AccountDto.SignUpReq dto = AccountDto.SignUpReq.builder()
                .address1("서울")
                .address2("성동구")
                .zip("052-2344")
                .email("emailtest.com")
                .firstName("길동")
                .lastName("홍")
                .password("password")
                .build();
        given(accountService.create(any())).willReturn(dto.toEntity());

        // when
        final ResultActions resultActions = requestSignUp(dto);

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(ErrorCode.INPUT_VALUE_INVALID.getMessage())))
                .andExpect(jsonPath("$.code", is(ErrorCode.INPUT_VALUE_INVALID.getCode())))
                .andExpect(jsonPath("$.status", is(ErrorCode.INPUT_VALUE_INVALID.getStatus())))
                .andExpect(jsonPath("$.errors[0].field", is("email")))
                .andExpect(jsonPath("$.errors[0].value", is(dto.getEmail())));
    }


    @Test
    public void signUp_이메일_이미_존재하는_경우() throws Exception {
        // given
        final AccountDto.SignUpReq dto = buildSignUpReq();
        given(accountService.create(any())).willThrow(EmailDuplicationException.class);

        // when
        final ResultActions resultActions = requestSignUp(dto);

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(ErrorCode.EMAIL_DUPLICATION.getMessage())))
                .andExpect(jsonPath("$.code", is(ErrorCode.EMAIL_DUPLICATION.getCode())))
                .andExpect(jsonPath("$.status", is(ErrorCode.EMAIL_DUPLICATION.getStatus())));
    }

    @Test
    public void signUp_데이터_무결성_예외() throws Exception {
        // given
        final AccountDto.SignUpReq dto = buildSignUpReq();
        given(accountService.create(any())).willThrow(DataIntegrityViolationException.class);

        // when
        final ResultActions resultActions = requestSignUp(dto);

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(ErrorCode.INPUT_VALUE_INVALID.getMessage())))
                .andExpect(jsonPath("$.code", is(ErrorCode.INPUT_VALUE_INVALID.getCode())))
                .andExpect(jsonPath("$.status", is(ErrorCode.INPUT_VALUE_INVALID.getStatus())));
    }

    @Test
    @Ignore // 임시
    public void signUp_데이터_무결성_예외2() throws Exception {
        // given
        final AccountDto.SignUpReq dto = buildSignUpReq();
        given(accountService.create(any())).willThrow(ConstraintViolationException.class);

        // when
        final ResultActions resultActions = requestSignUp(dto);

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(ErrorCode.INPUT_VALUE_INVALID.getMessage())))
                .andExpect(jsonPath("$.code", is(ErrorCode.INPUT_VALUE_INVALID.getCode())))
                .andExpect(jsonPath("$.status", is(ErrorCode.INPUT_VALUE_INVALID.getStatus())));
    }

    @Test
    public void getUser() throws Exception {
        // given
        final AccountDto.SignUpReq dto = buildSignUpReq();
        given(accountService.findById(anyLong())).willReturn(dto.toEntity());

        // when
        final ResultActions resultActions = requestGetUser();

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address1", is(dto.getAddress1())))
                .andExpect(jsonPath("$.address2", is(dto.getAddress2())))
                .andExpect(jsonPath("$.zip", is(dto.getZip())))
                .andExpect(jsonPath("$.email", is(dto.getEmail())))
                .andExpect(jsonPath("$.firstName", is(dto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(dto.getLastName())));
    }

    @Test
    public void updateMyAccount() throws Exception {
        // given
        final AccountDto.MyAccountReq dto = buildMyAccountReq();
        final Account account = Account.builder()
                .address1(dto.getAddress1())
                .address2(dto.getAddress2())
                .zip(dto.getZip())
                .build();

        given(accountService.updateMyAccount(anyLong(), any(AccountDto.MyAccountReq.class))).willReturn(account);

        // when
        final ResultActions resultActions = requestMyAccount(dto);

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address1", is(dto.getAddress1())))
                .andExpect(jsonPath("$.address2", is(dto.getAddress2())))
                .andExpect(jsonPath("$.zip", is(dto.getZip())));
    }

    private ResultActions requestMyAccount(AccountDto.MyAccountReq dto) throws Exception {
        return mockMvc.perform(put("/accounts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print());
    }

    private AccountDto.MyAccountReq buildMyAccountReq() {
        return AccountDto.MyAccountReq.builder()
                .address1("주소수정1")
                .address2("주소수정2")
                .zip("012-345-678")
                .build();
    }

    private ResultActions requestGetUser() throws Exception {
        return mockMvc.perform(get("/accounts/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions requestSignUp(AccountDto.SignUpReq dto) throws Exception {
        return mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print());
    }

    private AccountDto.SignUpReq buildSignUpReq() {
        return AccountDto.SignUpReq.builder()
                .address1("서울")
                .address2("성동구")
                .zip("12345")
                .email("email@test.com")
                .firstName("길동")
                .lastName("홍")
                .password("password")
                .build();
    }
}