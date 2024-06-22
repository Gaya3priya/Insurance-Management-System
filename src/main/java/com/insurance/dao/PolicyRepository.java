package com.insurance.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.insurance.entities.Policy;
import com.insurance.entities.User;

public interface PolicyRepository extends JpaRepository<Policy, Integer> {
	//fetching policies of particular agent by using this repository
	//pagination...
	
	@Query("from Policy as p where p.user.id=:userId")
	public Page<Policy> findPoliciesByUser(@Param("userId")int userId,Pageable pageable);//currentPage-page,policy per page-5
	//search
    @Query
	public List<Policy> findByNameContainingAndUser(String name,User user);
    
    
}
