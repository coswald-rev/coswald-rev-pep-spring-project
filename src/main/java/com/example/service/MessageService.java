package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Create a new message.
     * Requirements:
     * - message_text is not blank
     * - message_text length is < 255
     * - posted_by is a real/existing account
     * 
     * @param message the message to create
     * @return empty if a validation check failed, present message if it was created successfully.
     */
    public Optional<Message> createMessage(Message message) {
        if (!messageTextIsValid(message.getMessage_text())) {
            return Optional.empty();
        }

        if(accountRepository.findById(message.getPosted_by()).isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(messageRepository.save(message));
    }

    /**
     * Get all the Messages
     * 
     * @return the messages
     */
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    /**
     * Validate message_text based on Message requirements.
     * Requirements:
     * - message_text is not blank
     * - message_text is < 255 characters
     * 
     * @param message_text the message_text to validate
     * @return true if valid, false if invalid
     */
    private boolean messageTextIsValid(String message_text) {
        return !message_text.isBlank() && message_text.length() < 255;
    }
}
