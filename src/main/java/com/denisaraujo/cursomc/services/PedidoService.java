package com.denisaraujo.cursomc.services;

import java.util.Date;
import java.util.Optional;
import java.util.function.IntFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.denisaraujo.cursomc.domain.Cliente;
import com.denisaraujo.cursomc.domain.PagamentoComBoleto;
import com.denisaraujo.cursomc.domain.Pedido;
import com.denisaraujo.cursomc.domain.enums.EstadoPagamento;
import com.denisaraujo.cursomc.repositories.ItemPedidoRepository;
import com.denisaraujo.cursomc.repositories.PagamentoRepository;
import com.denisaraujo.cursomc.repositories.PedidoRepository;
import com.denisaraujo.cursomc.repositories.ProdutoRepository;
import com.denisaraujo.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ClienteService clienteService; 
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private EmailService emailService;
	
	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);

		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName())); 
	}
	
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		
		//foreach java 8 LAMBDA expression
		//foi necessário atribuir um novo objeto pedido pois a funcao 
		//lambda nao comporta alteracao do objeto contido no loop, ou seja o objeto torna-se FINAL
		Pedido obj2 = obj;
		obj.getItens().forEach(itemPedido -> {
			itemPedido.setDesconto(0.0);
			itemPedido.setProduto(produtoService.find(itemPedido.getProduto().getId()));
			itemPedido.setPreco(itemPedido.getProduto().getPreco());
			itemPedido.setPedido(obj2);
			});
		
		itemPedidoRepository.saveAll(obj.getItens());
		emailService.sendOrderConfimationEmail(obj);
		
		return obj;
	}
}
