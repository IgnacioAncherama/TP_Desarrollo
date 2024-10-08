package com.example.TPEscuela.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.List;


import com.example.TPEscuela.models.Tema;
import com.example.TPEscuela.services.TemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.TPEscuela.models.Curso;
import com.example.TPEscuela.services.CursoService;
import com.example.TPEscuela.services.DocenteService;
import com.example.TPEscuela.models.Docente;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {
	@Autowired
	private CursoService cursoService;
	@Autowired
	private TemaService temaService;
	@Autowired
	private DocenteService docenteService;

	@GetMapping
	public ArrayList<Curso> getCursos() {
		return this.cursoService.getCursos();
	}

	@PostMapping
	public Curso saveCurso(@RequestBody Curso Curso) {
		return this.cursoService.saveCurso(Curso);
	}

	@GetMapping(path = "/{id}")
	public Optional<Curso> getCursoById(@PathVariable("id") Long id) {
		return this.cursoService.getById(id);
	}

	@DeleteMapping(path = "/{id}")
	public String deleteById(@PathVariable("id") Long id) {
		boolean ok = this.cursoService.deleteCurso(id);

		if (ok) {
			return "Curso con id" + id + "eliminado";
		} else {
			return "error";
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Curso> updateCurso(@PathVariable Long id, @RequestBody Curso cursoDetails) {
		Optional<Curso> curso = cursoService.getById(id);
		if (curso.isPresent()) {
			Curso cursoToUpdate = curso.get();
			cursoToUpdate.setfechaInicio(cursoDetails.getfechaInicio());
			cursoToUpdate.setfechaFin(cursoDetails.getfechaFin());
			cursoToUpdate.setPrecio(cursoDetails.getPrecio());
			cursoToUpdate.setDocente(cursoDetails.getDocente());
			cursoToUpdate.setTema(cursoDetails.getTema());
			Curso updatedCurso = cursoService.saveCurso(cursoToUpdate);
			return ResponseEntity.ok(updatedCurso);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Curso> partialUpdateCurso(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
		Optional<Curso> cursoOptional = cursoService.getById(id);

		if (cursoOptional.isPresent()) {
			Curso cursoToUpdate = cursoOptional.get();

			updates.forEach((key, value) -> {
				switch (key) {
					case "fecha_inicio":
						cursoToUpdate.setfechaInicio(LocalDate.parse((String) value)); // Conversión de String a Date
						break;
					case "fecha_fin":
						cursoToUpdate.setfechaFin(LocalDate.parse((String) value)); // Conversión de String a Date
						break;
					case "precio":
						cursoToUpdate.setPrecio(Double.parseDouble(value.toString())); // Conversión segura a Double
						break;
					case "tema":
						// Suponiendo que 'tema' es un ID de un objeto Tema
						Tema tema = temaService.getById(Long.parseLong(value.toString())).orElse(null);
						cursoToUpdate.setTema(tema);
						break;
					case "docente":
						// Suponiendo que 'docente' es un ID de un objeto Docente
						Docente docente = docenteService.getById(Long.parseLong(value.toString())).orElse(null);
						cursoToUpdate.setDocente(docente);
						break;
				}
			});

			Curso updatedCurso = cursoService.saveCurso(cursoToUpdate);
			return ResponseEntity.ok(updatedCurso);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	@GetMapping("/fecha-fin")
	public ResponseEntity<List<Curso>> getCursosByFechaFin(@RequestParam(value = "fecha", required = true) String fechaFin) {
		try {
			// Intentar convertir la fecha de String a LocalDate
			LocalDate localDate = LocalDate.parse(fechaFin);
			List<Curso> cursos = cursoService.getByFechaFin(localDate);
			return ResponseEntity.ok(cursos);
		} catch (Exception e) {
			// Si hay algún error en la conversión de fecha, devolver un Bad Request
			return ResponseEntity.badRequest().body(null);
		}
	}

/*	DEVUELVE MAS DE UN CURSO

	// Endpoint para obtener los alumnos de los cursos vigentes de un docente
	@GetMapping("/docente/{docenteId}/alumnos")
	public ResponseEntity<List<Alumno>> getAlumnosByDocente(@PathVariable Long docenteId) {
		List<Curso> cursosVigentes = cursoService.getCursosVigentesByDocente(docenteId);

		List<Alumno> alumnos = new ArrayList<>();
		for (Curso curso : cursosVigentes) {
			alumnos.addAll(curso.getAlumnos()); // Agregar los alumnos de cada curso vigente
		}

		return ResponseEntity.ok(alumnos);
	}

	// ¡DOS IGUALES REVISAR Y FIJARSE CUAL!!

	@GetMapping("/docente/{docenteid}/alumnos")
	public ResponseEntity<List<Alumno>> getAlumnosByDocenteId(@PathVariable Long docenteid) {
		List<Alumno> alumnos = cursoService.getAlumnosByDocenteId(docenteid);
		return ResponseEntity.ok(alumnos);

 */

}






