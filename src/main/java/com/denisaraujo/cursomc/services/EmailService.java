package com.denisaraujo.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.denisaraujo.cursomc.domain.Pedido;

public interface EmailService {

	void sendOrderConfimationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
	
}
