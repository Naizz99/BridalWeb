package com.kdm360.bridalweb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.kdm360.bridalweb.model.Settings;
import com.kdm360.bridalweb.model.User;

@Repository
public interface SettingsRepository extends JpaRepository<Settings, Long> {

	Optional<Settings> findBySystemSeq(int i);
}
