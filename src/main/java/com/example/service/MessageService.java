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
     * Find a message by id 
     * 
     * @param id the id of the message
     * @return an empty if not found, present message if exists 
     */
    public Optional<Message> getMessageById(Integer id) {
        return messageRepository.findById(id);
    }

    /**
     * Attempt to delete a message by id
     * 
     * @param id the id of the message to delete
     * @return true if the message exists and was deleted, false if nothing happened
     */
    public boolean deleteMessageById(Integer id) {
        Optional<Message> existingMessage = messageRepository.findById(id);
        if (existingMessage.isPresent()) {
            messageRepository.deleteById(id);
            return true;
        }

        return false;
    }

    /**
     * Attempt to update a message by id
     * 
     * @param message_text the message text to update
     * @param message_id the id of the message to update
     * @return empty if the message doesn't exist or a validation check failed, present message if the update was successful
     */
    public Optional<Message> updateMessageById(String message_text, Integer message_id) {
        Optional<Message> existingMessage = messageRepository.findById(message_id);
        if (existingMessage.isEmpty()) {
            return Optional.empty();
        }

        if (!messageTextIsValid(message_text)) {
            return Optional.empty();
        }

        Message message = existingMessage.get();
        message.setMessage_text(message_text);

        return Optional.of(messageRepository.save(message));
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
