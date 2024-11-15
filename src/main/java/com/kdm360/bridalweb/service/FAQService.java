package com.kdm360.bridalweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kdm360.bridalweb.model.FAQ;
import com.kdm360.bridalweb.repository.FAQRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FAQService {
	@Autowired
    private FAQRepository faqRepository;

    public List<FAQ> get() {
        return faqRepository.findAll();
    }

    public Optional<FAQ> get(Long id) {
        return faqRepository.findById(id);
    }
    
    public List<FAQ> get(boolean isActive) {
        return faqRepository.findByIsActive(isActive);
    }

    public FAQ save(FAQ faq) {
        return faqRepository.save(faq);
    }

    public void delete(Long id) {
        faqRepository.deleteById(id);
    }
}
