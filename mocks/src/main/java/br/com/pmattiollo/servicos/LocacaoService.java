package br.com.pmattiollo.servicos;

import static br.com.pmattiollo.utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.pmattiollo.daos.LocacaoDAO;
import br.com.pmattiollo.entidades.Filme;
import br.com.pmattiollo.entidades.Locacao;
import br.com.pmattiollo.entidades.Usuario;
import br.com.pmattiollo.exception.FilmeSemEstoqueException;
import br.com.pmattiollo.exception.LocadoraException;
import br.com.pmattiollo.utils.DataUtils;

public class LocacaoService {

	private LocacaoDAO dao;
	private SPCService spcService;
	private EmailService emailService;

	public LocacaoService(LocacaoDAO dao, SPCService service, EmailService emailService) {
		this.dao = dao;
		this.spcService = service;
		this.emailService = emailService;
	}

	public Locacao alugarFilmes(Usuario usuario, List<Filme> filmes)
			throws FilmeSemEstoqueException, LocadoraException {
		if (usuario == null) {
			throw new LocadoraException("Usuário vazio");
		}

		if (filmes == null || filmes.isEmpty()) {
			throw new LocadoraException("Filme vazio");
		}

		if (filmes.removeIf(f -> f.getEstoque() == 0)) {
			throw new FilmeSemEstoqueException("Filme sem estoque");
		}

		boolean possuiNegativacao;
		try {
			possuiNegativacao = spcService.possuiNegativacao(usuario);
		} catch (Exception e) {
			throw new LocadoraException("Problemas com SPC, tente novamente");
		}

		if (possuiNegativacao) {
			throw new LocadoraException("Usuário negativado");
		}

		CalculadorDeDescontos calculador = new CalculadorDeDescontos();
		double valorTotalComDesconto = calculador.calcula(filmes);

		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		locacao.setValor(valorTotalComDesconto);

		// Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		if (DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
			dataEntrega = adicionarDias(dataEntrega, 1);
		}

		locacao.setDataRetorno(dataEntrega);

		// Salvando a locacao...
		dao.salvar(locacao);

		return locacao;
	}

	public void notificarAtrasos() {
		List<Locacao> locacoesPendentes = dao.obterLocacoesPendentes();
		locacoesPendentes.forEach(locacao -> {
			if (locacao.getDataRetorno().before(new Date())) {
				emailService.notificarAtraso(locacao.getUsuario());
			}
		});
	}

	public void prorrogarLocacao(Locacao locacao, int dias) {
		Locacao novaLocacao = new Locacao();
		novaLocacao.setDataLocacao(new Date());
		novaLocacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(dias));
		novaLocacao.setFilmes(locacao.getFilmes());
		novaLocacao.setUsuario(locacao.getUsuario());
		novaLocacao.setValor(locacao.getValor() * dias);
		dao.salvar(novaLocacao);
	}

}