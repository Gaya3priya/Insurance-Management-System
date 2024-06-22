package com.insurance.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.insurance.dao.PolicyRepository;
import com.insurance.entities.Policy;

@Service
@Transactional
public class UserService {
   @Autowired
   private PolicyRepository repo;
   
   public List<Policy> listAll(){
	   return repo.findAll(Sort.by("policyID").ascending());
   }
}
