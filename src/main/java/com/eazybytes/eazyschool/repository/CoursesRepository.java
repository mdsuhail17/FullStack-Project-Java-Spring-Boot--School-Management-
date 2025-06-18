package com.eazybytes.eazyschool.repository;

import com.eazybytes.eazyschool.model.Courses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
//@RepositoryRestResource(path = "courses") // for chnage the rest api path in the hal explorer
@RepositoryRestResource(exported = false)// din not expose the courses repository in hal expolorer
public interface CoursesRepository extends JpaRepository<Courses, Integer> {

    // sorting
    // if we are extending the JpaRepository, and JpaRepository this will extend the ListPagingAndSortingRepository
    // so we are directly extending the ListPagingAndSortingRepository so we canuse sorting

    // sorting applies (static Sorting)
    List<Courses> findByOrderByNameDesc();

    //sorting applied(ststic sorting)
    List<Courses> findByOrderByName();

}
