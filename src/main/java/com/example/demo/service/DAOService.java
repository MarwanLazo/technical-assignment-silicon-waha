package com.example.demo.service;

import java.io.Serializable;
import java.util.List;

public interface DAOService<T, ID extends Serializable> {

	List<T> getAll();

	T create(T e);

	T update(T e);

	T delete(T t);

	ID delete(ID username);

}
