package com.example.demo.service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.example.demo.repository.RootRepository;

import lombok.extern.log4j.Log4j2;

/**
 * 
 * @author Marwan
 *
 * @param <T>
 * @param <ID>
 * 
 *             Generic DAOServiceIml for CRUD
 */
@Log4j2
public abstract class DAOServiceImpl<T, ID extends Serializable> implements DAOService<T, ID> {

	public final RootRepository<T, ID> repository;
	private static Validator validator;

	public DAOServiceImpl(RootRepository<T, ID> repository) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		this.repository = repository;
	}

	/**
	 * @return java.util.List<T>
	 * 
	 */
	@Override
	public List<T> getAll() {
		log.info("getAll()");
		List<T> asList = repository.findAll();
		log.info(asList);
		return asList;
	}

	/**
	 * @param entity of type T
	 * @return the created entity
	 * 
	 * @throw ConstraintViolationException if JPA validation failed
	 */

	@Override
	public T create(T entity) {
		Set<ConstraintViolation<T>> constraintViolations = validator.validate(entity);
		if (constraintViolations.isEmpty()) {
			log.info(String.format("create(%s)", entity.getClass().getSimpleName()));
			entity = repository.save(entity);
			log.info(entity);
			return entity;
		} else
			throw new ConstraintViolationException(constraintViolations);
	}

	/**
	 * @param entity of type T
	 * @return the updated entity
	 * 
	 */
	@Override
	public T update(T entity) {
		Set<ConstraintViolation<T>> constraintViolations = validator.validate(entity);
		if (constraintViolations.isEmpty()) {
			log.info(String.format("update(%s)", entity.getClass().getSimpleName()));
			entity = repository.save(entity);
			log.info(entity);
			return entity;
		} else
			throw new ConstraintViolationException(constraintViolations);
	}

	/**
	 * delete by entity
	 * 
	 * @return deleted Entity
	 */
	@Override
	public T delete(T entity) {
		log.info(String.format("delete(%s)", entity.getClass().getSimpleName()));
		repository.delete(entity);
		log.info(entity);
		return entity;
	}

	/**
	 * delete by ID
	 * 
	 * @return deleted Entity ID
	 */
	@Override
	public ID delete(ID id) {
		log.info(String.format("ID delete(ID id) :: %s", id));
		repository.deleteById(id);
		log.info(String.format("Entity with 'ID :: %s' deleted Success", id));
		return id;

	}

}
