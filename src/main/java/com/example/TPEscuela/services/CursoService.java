package com.example.TPEscuela.services;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;
import java.time.LocalDate;
import java.util.stream.Collectors;

import com.example.TPEscuela.models.Alumno;
import com.example.TPEscuela.models.Curso_alumnosDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.TPEscuela.models.Curso;
import com.example.TPEscuela.repository.ICursoRepository;

@Service
public class CursoService {

	@Autowired
	ICursoRepository cursoRepository;

	public Optional<Curso> getById(Long id) {
		return cursoRepository.findById(id);
	}

	public ArrayList<Curso> getCursos() {
		return (ArrayList<Curso>) cursoRepository.findAll();
	}

	public Curso saveCurso(Curso curso) {
		return cursoRepository.save(curso);
	}

	public Boolean deleteCurso(Long id) {
		try {
			cursoRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public ArrayList<Curso> getByFechaFin(LocalDate fechaFin) {
		return (ArrayList<Curso>) cursoRepository.findByFechaFin(fechaFin);
	}

	public List<Curso> getCursosVigentesByDocente(Long docenteId) {
		LocalDate now = LocalDate.now(); // Obtener la fecha actual
		return cursoRepository.findByDocenteIdAndFechaFinAfterAndFechaInicioBefore(docenteId, now, now);
	}

	public List<Curso> getCursosByDocenteId(Long docenteId) {
		return cursoRepository.findByDocente_Id(docenteId);
	}

	/*
	public List<Alumno> getAlumnosByDocenteId(Long docenteId) {
		List<Curso> cursos = getCursosByDocenteId(docenteId); // Utiliza el nuevo metodo
		List<Alumno> alumnos = new ArrayList<>();

		for (Curso curso : cursos) {
			alumnos.addAll(curso.getAlumnos());
		}
		return alumnos;
	}
	*/


	/*public Optional<List<Alumno>> getAlumnosDeCursosVigentes(Long docenteId) {
		List<Alumno> alumnos = cursoRepository.findAlumnosDeCursosVigentes(docenteId, LocalDate.now());

		// Si no hay alumnos, retornamos un Optional vacío
		if (alumnos.isEmpty()) {
			return Optional.empty();
		}

		// Si hay alumnos, retornamos la lista encapsulada en un Optional
		return Optional.of(alumnos);
	}*/

	public List<Curso_alumnosDTO> getAlumnosDeCursosVigentes(Long docenteId) {
		LocalDate fechaActual = LocalDate.now();
		// Buscar los cursos vigentes por docente
		List<Curso> cursosVigentes = cursoRepository.findByDocenteIdAndFechaFinAfter(docenteId, fechaActual);

		// Mapear los cursos a Curso_alumnosDTO con el ID del curso como clave
		return cursosVigentes.stream()
				.map(curso -> new Curso_alumnosDTO(curso.getId(), curso.getAlumnos()))  // Usar el ID del curso
				.collect(Collectors.toList());
	}
}
