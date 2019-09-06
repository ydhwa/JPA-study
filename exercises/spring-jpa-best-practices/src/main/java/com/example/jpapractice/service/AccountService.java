package com.example.jpapractice.service;

import com.example.jpapractice.common.exception.AccountNotFoundException;
import com.example.jpapractice.common.exception.EmailDuplicationException;
import com.example.jpapractice.domain.Account;
import com.example.jpapractice.dto.AccountDto;
import com.example.jpapractice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account create(AccountDto.SignUpReq dto) {
        if(isExistedEmail(dto.getEmail())) {
            throw new EmailDuplicationException(dto.getEmail());
        }
        return accountRepository.save(dto.toEntity());
    }

    @Transactional(readOnly = true)
    public Account findById(long id) {
        final Account account = accountRepository.getOne(id);
        if(account == null) {
            throw new AccountNotFoundException(id);
        }
        return account;
    }

    public Account updateMyAccount(long id, AccountDto.MyAccountReq dto) {
        final Account account = findById(id);
        account.updateMyAccount(dto);
        return account;
    }

    @Transactional(readOnly = true)
    public boolean isExistedEmail(String email) {
        return accountRepository.findByEmail(email) != null;
    }
}
