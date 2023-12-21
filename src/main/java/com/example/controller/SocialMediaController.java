package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.entity.Account;
import com.example.exception.AccountAlreadyExistsException;
import com.example.exception.AccountException;
import com.example.service.AccountService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@Controller
public class SocialMediaController {

    private final Logger logger = LoggerFactory.getLogger(SocialMediaController.class);

    private final AccountService accountService;

    @Autowired
    public SocialMediaController(AccountService accountService) {
        this.accountService = accountService;
    }
    
    /**
     * POST /register
     * Attempts to register a user in the database.
     * 
     * @param account the account to register
     * @return
     */
    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<Account> register(@RequestBody Account account) {
        try {
            accountService.createAccount(account);
        } catch (AccountException ex) {
            logger.error("/register exception: body: {}, message: {}", account, ex.getMessage());

            if (ex instanceof AccountAlreadyExistsException) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.status(200).body(account);
    }

}
