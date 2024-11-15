package com.kdm360.bridalweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kdm360.bridalweb.model.Message;
import com.kdm360.bridalweb.repository.MessageRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

	@Autowired
    private MessageRepository repo;

    public List<Message> get() {
        return repo.findAll();
    }

    public Optional<Message> get(Long id) {
        return repo.findById(id);
    }

    public Message save(Message contactUs) {
        return repo.save(contactUs);
    }

    public void delete(Long id) {
    	repo.deleteById(id);
    }

	public int getCountByIsRead(boolean isRead) {
		return repo.getCountByIsRead(isRead);
	}
}
