package com.example.jpapractice.controller;

import com.example.jpapractice.domain.Account;
import com.example.jpapractice.dto.AccountDto;
import com.example.jpapractice.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountService accountService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
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
                .email("email")
                .firstName("길동")
                .lastName("홍")
                .password("password")
                .build();
    }
}