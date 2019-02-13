package com.denisaraujo.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.denisaraujo.cursomc.domain.Cliente;
import com.denisaraujo.cursomc.repositories.ClienteRepository;
import com.denisaraujo.cursomc.security.UserSS;

@Service
public class UserDatailsServicesImpl implements UserDetailsService {

	@Autowired
	private ClienteRepository repoCliente;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Cliente cliente = repoCliente.findByEmail(email);
		
		if(cliente == null) {
			throw new UsernameNotFoundException(email);
		}
		
		return new UserSS(cliente.getId(), cliente.getEmail(), cliente.getSenha(), cliente.getPerfis());
	}
}
