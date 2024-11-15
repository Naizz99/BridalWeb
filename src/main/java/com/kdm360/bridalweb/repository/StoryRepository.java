package com.kdm360.bridalweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kdm360.bridalweb.model.Story;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {

	List<Story> getByActive(boolean active);
}
