package com.eazybytes.eazyschool.repository;

import com.eazybytes.eazyschool.model.EazyClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EazyClassRepository extends JpaRepository<EazyClass,Integer> {
    // Entity Type is EazyClass
    // Primary key  is Integer
}