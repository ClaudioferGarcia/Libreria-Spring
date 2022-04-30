package com.libreria.servicios;

import com.libreria.entidades.Autor;
import com.libreria.entidades.Editorial;
import com.libreria.entidades.Libro;
import com.libreria.errores.ErrorServicio;
import com.libreria.repositorios.LibroRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LibroServicio {

    @Autowired
    private LibroRepositorio libroRepositorio;
    @Autowired
    private AutorServicio autorServicio;
    @Autowired
    private EditorialServicio editorialServicio;

    @Transactional(rollbackFor = {Exception.class})
    public Libro guardarLibro(Long Isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados,
            Integer ejemplaresRestantes, String idAutor, String idEditorial) throws ErrorServicio {

        validar(Isbn, titulo, anio, ejemplares, ejemplaresPrestados, ejemplaresRestantes, idAutor, idEditorial);

        Libro libro = new Libro();
        libro.setIsbn(Isbn);
        libro.setTitulo(titulo);
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresPrestados(ejemplaresPrestados);
        libro.setEjemplaresRestantes(ejemplaresRestantes);

        Autor autor = autorServicio.consultarPorId(idAutor);
        libro.setAutor(autor);
        Editorial editorial = editorialServicio.buscarPorId(idEditorial);
        libro.setEditorial(editorial);

        libro.setActivo(true);
        libro.setAlta(new Date());
        return libroRepositorio.save(libro);

    }

    @Transactional(rollbackFor = {Exception.class})
    public void eliminar(String id) throws ErrorServicio {

        buscarPorId(id);
        libroRepositorio.deleteById(id);
    }
    @Transactional(readOnly = true)
    public List<Libro> buscarLibroPorIdAutor(String id){
           return libroRepositorio.buscarLibroPorIdAutor(id);
    }
    
    @Transactional(readOnly = true)
    public List<Libro> buscarLibroPorIdEditorial(String id){
           return libroRepositorio.buscarLibroPorIdEditorial(id);
    }
    
    @Transactional(readOnly = true)
    public Libro buscarPorId(String id) throws ErrorServicio {

        if (id == null || id.isEmpty()) {
            throw new ErrorServicio("Debe ingresar ID existente");
        }

        Optional<Libro> respuesta = libroRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();

            return libro;

            // o retornar directamente
            //return respuesta.get();
        } else {

            throw new ErrorServicio("No se encontro el libro en base de datos");
        }
    }
    
    @Transactional(readOnly = true)
    public Libro buscarPorTitulo(String id, String titulo) throws ErrorServicio{
        
       Libro libro = buscarPorId(id);
       libro.getTitulo();
       return libro;
    }

    @Transactional(readOnly = true)
    public List<Libro> listar() {
        return libroRepositorio.findAll();
    }

    @Transactional(rollbackFor = {Exception.class})
    public Libro editarTitulo(String id, String titulo) throws ErrorServicio {

        if (titulo == null || titulo.isEmpty()) {
            throw new ErrorServicio("Debe ingresar título");
        }
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setTitulo(titulo);
            libro.setActivo(true);
            libro.setAlta(new Date());

           return  libroRepositorio.save(libro);
           
        } else {
            throw new ErrorServicio("No se ha encontrado el Libro");
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public Libro editarEjemplares(String id, Integer ejemplares, Integer ejemplaresPrestados, Integer ejemplaresRestantes) throws ErrorServicio {

        if (ejemplares == null || ejemplares < 0) {
            throw new ErrorServicio("Debe ingresar cantidad de ejemplares");
        }

        if (ejemplaresPrestados == null || ejemplaresPrestados < 0) {
            throw new ErrorServicio("Debe ingresar cantidad de ejemplares prestados");
        }

        if (ejemplaresRestantes == null || ejemplaresRestantes < 0) {
            throw new ErrorServicio("Debe ingresar cantidad de ejemplares restantes");
        }

        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setEjemplares(ejemplares);
            libro.setEjemplaresPrestados(ejemplaresPrestados);
            libro.setEjemplaresRestantes(ejemplaresRestantes);
            libro.setActivo(true);
            libro.setAlta(new Date());

          return  libroRepositorio.save(libro);
          
        } else {
            throw new ErrorServicio("No se ha encontrado el Libro");
        }
    }

    private void validar(Long Isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados,
            Integer ejemplaresRestantes, String idAutor, String idEditorial) throws ErrorServicio {

        if (Isbn == null) {
            throw new ErrorServicio("Debe ingresar Isbn");
        }

        if (titulo == null || titulo.isEmpty()) {
            throw new ErrorServicio("El titulo no puede ser nulo");
        }

        if (anio == null || anio < 1) {
            throw new ErrorServicio("Debe ingresar año");
        }

        if (ejemplares == null || ejemplares < 1) {
            throw new ErrorServicio("Debe ingresar cantidad de ejemplares");
        }

        if (ejemplaresPrestados == null || ejemplaresPrestados < 1) {
            throw new ErrorServicio("Debe ingresar ejemplares prestados");
        }

        if (ejemplaresRestantes == null || ejemplaresRestantes < 1) {
            throw new ErrorServicio("Debe ingresar ejemplares restantes");
        }

        if (idAutor.isEmpty()) {
            throw new ErrorServicio("Debe ingresar Autor");
        }
        if (idEditorial.isEmpty()) {
            throw new ErrorServicio("Debe ingresar Editorial");
        }
    }
}
