
package com.libreria.repositorios;

import com.libreria.entidades.Editorial;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorialRepositorio extends JpaRepository<Editorial, String> {
   Optional<Editorial> findByNombre(@Param("nombre") String nombre);
}
