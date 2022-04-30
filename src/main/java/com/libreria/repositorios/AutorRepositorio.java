
package com.libreria.repositorios;

import com.libreria.entidades.Autor;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepositorio extends JpaRepository <Autor, String>{
    Optional<Autor> findByNombre(@Param("nombre") String nombre);
}
