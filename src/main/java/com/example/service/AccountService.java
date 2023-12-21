package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.AccountAlreadyExistsException;
import com.example.exception.AccountException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Register a new account.
     * Requirements:
     * - Username is not blank.
     * - Password is at least 4 characters long.
     * - Username is unique within our database.
     * 
     * @param account
     * @return
     * @throws Account
     */
    public Account createAccount(Account account) throws AccountException {
        if (account.getUsername().isBlank()) {
            throw new AccountException("Username cannot be blank");
        }

        if (account.getPassword().length() < 4) {
            throw new AccountException("Password must be at least 4 characters long");
        }

        Optional<Account> existingAccount = accountRepository.findAccountByUsername(account.getUsername());
        if (existingAccount.isPresent()) {
            throw new AccountAlreadyExistsException("An account with the provided username already exists");
        }

        return accountRepository.save(account);
    }
}
