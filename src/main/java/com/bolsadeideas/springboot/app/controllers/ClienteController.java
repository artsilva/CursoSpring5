package com.bolsadeideas.springboot.app.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.service.IClienteService;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

	@Value("${application.controllers.titulo}")
	private String titulo;
	
	@Autowired
	private IClienteService clienteService;
	
	@RequestMapping(value="/listar", method=RequestMethod.GET)
	public String listar(Model model ) {
		model.addAttribute("titulo", titulo);
		model.addAttribute("clientes", clienteService.findAll());
		return "listar";
	}
	
	@RequestMapping(value="/form")
	public String crear(Map<String, Object> model) {
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "formulario de Cliente");
		return "form";
	}
	
//	@Valid habilita la validacion del objeto	
	@RequestMapping(value="/form", method=RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario");
			return "form";
		}
		clienteService.save(cliente);
		return "redirect:/listar";
	}
	
	@RequestMapping(value="/form/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String, Object> model, SessionStatus status) {
		Cliente cliente = null;
		if (id>0) {
			cliente = clienteService.findOne(id);
		} else {
			return "redirect:listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Editar Cliente");
		
		clienteService.save(cliente);
		status.setComplete();
		return "form";
	}
	
	@RequestMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id) {
		if (id>0) {
			clienteService.delete(id);
		} 
		return "redirect:/listar";
	}
	
}
