package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.AccountAlreadyExistsException;
import com.example.exception.AccountException;
import com.example.service.AccountService;
import com.example.service.MessageService;

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
    private final MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
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

    /**
     * POST /login
     * Attempts to authenticate a user in the database
     * 
     * @param account the account to authenticate
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Optional<Account> authenticatedAccount = accountService.authenticate(account);
        if (authenticatedAccount.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(authenticatedAccount.get());
    }

    /**
     * POST /messages
     * Attempts to create a message in the database
     * 
     * @param message the message to create
     * @return
     */
    @PostMapping("/messages")
    @ResponseBody
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Optional<Message> createdMessage = messageService.createMessage(message);
        if (createdMessage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(createdMessage.get());
    }

    /**
     * GET /messages
     * Retrieves all the messages in the database
     * 
     * @return 
     */
    @GetMapping("/messages")
    @ResponseBody
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.status(HttpStatus.OK).body(messageService.getAllMessages());
    }

    /**
     * GET /messages/{message_id}
     * Retrieve a message by the message_id
     * 
     * @param message_id the message_id to look for
     * @return
     */
    @GetMapping("/messages/{message_id}")
    @ResponseBody
    public ResponseEntity<Message> getMessageById(@PathVariable Integer message_id) {
        Message message = null;
        
        Optional<Message> existingMessage = messageService.getMessageById(message_id);
        if (existingMessage.isPresent()) {
            message = existingMessage.get();
        }

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    /**
     * DELETE /messages/{message_id}
     * Delete a message by the message_id
     * 
     * @param message_id the message_id to delete
     * @return
     */
    @DeleteMapping("/messages/{message_id}")
    @ResponseBody
    public ResponseEntity<Integer> deleteMessageById(@PathVariable Integer message_id) {
        return ResponseEntity.status(HttpStatus.OK).body(
            messageService.deleteMessageById(message_id) ? 1 : null
        );
    }

    /**
     * PATCH /messages/{message_id}
     * Update a message_text by the message_id
     * 
     * @param message the body containing the message_text
     * @param message_id the id of the message to update
     * @return
     */
    @PatchMapping("/messages/{message_id}")
    @ResponseBody
    public ResponseEntity<Integer> patchMessageById(@RequestBody Message message, @PathVariable Integer message_id) {
        Optional<Message> updatedMessage = messageService.updateMessageById(message.getMessage_text(), message_id);
        if (updatedMessage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(1);
    }
}
