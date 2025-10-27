package com.proyectoClinica.repository;

import com.proyectoClinica.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "pago")
public interface PagoRepository extends JpaRepository<Pago, Integer> {
}
