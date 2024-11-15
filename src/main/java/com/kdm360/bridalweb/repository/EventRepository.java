package com.kdm360.bridalweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kdm360.bridalweb.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

	List<Event> getByActive(boolean active);

}
