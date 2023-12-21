package com.example.repository;

import org.springframework.data.repository.Repository;

import com.example.entity.Message;

public interface MessageRepository extends Repository<Message, Long> {
}
