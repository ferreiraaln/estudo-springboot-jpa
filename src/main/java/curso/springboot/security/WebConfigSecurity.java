package curso.springboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import curso.springboot.services.UsuarioDetailService;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UsuarioDetailService usuarioDetailService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.csrf()
			.disable() // Desativa as configurções padrão de memória
			.authorizeRequests() //Permitir restringir acessos
			.antMatchers(HttpMethod.GET, "/").permitAll()//Qualqer usuáeio acessa a página inicial
			//.antMatchers(HttpMethod.GET, "/cadastropessoa").hasAnyRole("ADMIN") 
			.anyRequest().authenticated()
			.and().formLogin().permitAll() //permite qualquer usuário
			.loginPage("/login")
			.defaultSuccessUrl("/cadastropessoa")
			.failureUrl("/login?error=true")
			.and().logout().logoutSuccessUrl("/login") //mapeia a URL de logout e invalida o usuário autenticado
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
	}
	
	@Override //Cria autenticação o usuário no banco de dados
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(usuarioDetailService)
		.passwordEncoder(new BCryptPasswordEncoder());
	}
		
	@Override //Ignora URL especificas
	public void configure( WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/materialize/**");
	}
}
