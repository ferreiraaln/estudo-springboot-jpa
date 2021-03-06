package curso.springboot.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import curso.springboot.model.Pessoa;
import curso.springboot.model.Telefone;
import curso.springboot.repository.PessoaRepository;
import curso.springboot.repository.TelefoneRepository;
import curso.springboot.validations.ValidaForm;

@Controller
public class PessoaController {
	
	@Autowired
	private PessoaRepository PessoaRepository;

	@Autowired
	private TelefoneRepository telefoneRepository;

	@RequestMapping(method = RequestMethod.GET, value = "/cadastropessoa")
	public ModelAndView inicio() {
		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		Iterable<Pessoa> pessoaIt = PessoaRepository.findAll();
		andView.addObject("pessoas", pessoaIt);
		andView.addObject("pessoaobj", new Pessoa());
		return andView;
	}
	
	@RequestMapping(method=RequestMethod.POST, value = "**/salvarpessoa")
	public ModelAndView salvar(@Valid Pessoa pessoa, BindingResult bindingResult) {
		
		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		Iterable<Pessoa> pessoaIt = PessoaRepository.findAll();
		andView.addObject("msg", "Salvo com sucesso!");
		
		if(bindingResult.hasErrors()) {
			List<String> msg = new ValidaForm(bindingResult.getAllErrors()).getMsg();
			andView.addObject("pessoaobj", pessoa);
			andView.addObject("msg", msg);
		}else {
			PessoaRepository.save(pessoa);
			andView.addObject("pessoaobj", new Pessoa());
		}
		
		andView.addObject("pessoas", pessoaIt);
		
		return andView;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/listapessoa")
	public ModelAndView pessoas() {
		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		
		Iterable<Pessoa> pessoaIt = PessoaRepository.findAll();
		andView.addObject("pessoas", pessoaIt);
		andView.addObject("pessoaobj", new Pessoa());
		
		return andView;
	}
	
	@GetMapping("/editarpessoa/{idpessoa}")
	public ModelAndView editarpessoa(@PathVariable("idpessoa") Long idpessoa) {
		
		Optional<Pessoa> pessoa = PessoaRepository.findById(idpessoa);
		
		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		andView.addObject("pessoaobj", pessoa.get());
		
		return andView;
	}
	
	@GetMapping("excluirpessoa/{idpessoa}")
	public ModelAndView exclurpessoa(@PathVariable("idpessoa") Long idpessoa) {
		
		PessoaRepository.deleteById(idpessoa);
		
		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		andView.addObject("pessoas", PessoaRepository.findAll());
		andView.addObject("pessoaobj", new Pessoa());
			
		return andView;
	}
	
	
	@PostMapping("**/pesquisaPessoa")
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa) {
		 	
		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		
		andView.addObject("pessoas", PessoaRepository.findPessoaByName(nomepesquisa));
		andView.addObject("pessoaobj", new Pessoa());
		
		return andView;
	}
	
	@GetMapping("/telefones/{idpessoa}")
	public ModelAndView telefones(@PathVariable("idpessoa") Long idpessoa) {
		
		Optional<Pessoa> pessoa = PessoaRepository.findById(idpessoa);
		
		ModelAndView andView = new ModelAndView("cadastro/telefones");
		andView.addObject("pessoaobj", pessoa.get());
		andView.addObject("telefones", telefoneRepository.getTelefones(idpessoa));
		
		return andView;
	}
	
	@PostMapping("**/addfonePessoa/{pessoaid}")
	public ModelAndView addFonePessoa(@Valid Telefone telefone,BindingResult bindingResult,@PathVariable("pessoaid") Long pessoaid) {
		 
		Pessoa pessoa  = PessoaRepository.findById(pessoaid).get();
		telefone.setPessoa(pessoa);
	
		ModelAndView andView = new ModelAndView("cadastro/telefones");
		andView.addObject("pessoaobj", pessoa);
		andView.addObject("telefones", telefoneRepository.getTelefones(pessoaid));
		andView.addObject("msg", "Telefone salvo com sucesso!");
		
		if(bindingResult.hasErrors()) {
			List<String> msg = new ValidaForm(bindingResult.getAllErrors()).getMsg();
			andView.addObject("msg", msg);
		}else {
			telefoneRepository.save(telefone);
		}
		
		return andView;
	}
	
	@GetMapping("/excluirfone/{idfone}")
	public ModelAndView excluirFone(@PathVariable("idfone") Long idfone) {
		
		Pessoa pessoa = telefoneRepository.findById(idfone).get().getPessoa();
		telefoneRepository.deleteById(idfone);
		
		ModelAndView andView = new ModelAndView("cadastro/telefones");
		andView.addObject("pessoaobj", pessoa);
		andView.addObject("telefones", telefoneRepository.getTelefones(pessoa.getId()));
		
		return andView;
	}
}
