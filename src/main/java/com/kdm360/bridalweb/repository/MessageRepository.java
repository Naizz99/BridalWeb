package com.kdm360.bridalweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.kdm360.bridalweb.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

	int getCountByIsRead(boolean isRead);
}
