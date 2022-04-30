
package com.libreria.repositorios;


import com.libreria.entidades.Libro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, String>{
    
 @Query("SELECT l FROM Libro l Where l.autor.idAutor like :id")
 public List<Libro> buscarLibroPorIdAutor(@Param ("id") String idAutor);
 
 @Query("SELECT l FROM Libro l Where l.editorial.idEditorial like :id")
  public List<Libro> buscarLibroPorIdEditorial(@Param ("id") String idEditorial);
}

