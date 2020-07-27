package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 
 * @author Marwan
 *
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public interface RootRepository<T, ID> extends JpaRepository<T, ID> {

}
