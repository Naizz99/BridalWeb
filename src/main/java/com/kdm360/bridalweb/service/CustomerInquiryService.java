package com.kdm360.bridalweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kdm360.bridalweb.model.CustomerInquiry;
import com.kdm360.bridalweb.model.User;
import com.kdm360.bridalweb.repository.CustomerInquiryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerInquiryService {

    private final CustomerInquiryRepository repo;

    @Autowired
    public CustomerInquiryService(CustomerInquiryRepository repo) {
        this.repo = repo;
    }

    public List<CustomerInquiry> getAllCustomerInquiries() {
        return repo.findAll();
    }

    public Optional<CustomerInquiry> getCustomerInquiryById(Long id) {
        return repo.findById(id);
    }

    public CustomerInquiry addCustomerInquiry(CustomerInquiry customerInquiry) {
        return repo.save(customerInquiry);
    }

    public CustomerInquiry updateCustomerInquiry(Long id, CustomerInquiry updatedCustomerInquiry) {
        Optional<CustomerInquiry> existingCustomerInquiry = repo.findById(id);
        if (existingCustomerInquiry.isPresent()) {
            CustomerInquiry customerInquiry = existingCustomerInquiry.get();
            customerInquiry.setVendor(updatedCustomerInquiry.getVendor());
            customerInquiry.setUser(updatedCustomerInquiry.getUser());
            customerInquiry.setSenderEmail(updatedCustomerInquiry.getSenderEmail());
            customerInquiry.setSenderMobile(updatedCustomerInquiry.getSenderMobile());
            customerInquiry.setInquiredDate(updatedCustomerInquiry.getInquiredDate());
            customerInquiry.setInquiredTime(updatedCustomerInquiry.getInquiredTime());
            customerInquiry.setMessage(updatedCustomerInquiry.getMessage());
            customerInquiry.setIpAddress(updatedCustomerInquiry.getIpAddress());
            return repo.save(customerInquiry);
        } else {
            throw new RuntimeException("CustomerInquiry not found with id " + id);
        }
    }

    public void deleteCustomerInquiry(Long id) {
        repo.deleteById(id);
    }

	public boolean existsByUserId(User user) {
		return repo.existsByUser(user);
	}
}
