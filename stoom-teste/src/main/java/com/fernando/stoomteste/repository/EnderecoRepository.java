package com.fernando.stoomteste.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fernando.stoomteste.models.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
	
	Endereco findById(long id);
	
	
}
