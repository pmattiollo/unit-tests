package br.com.pmattiollo.builders;

import static br.com.pmattiollo.builders.FilmeBuilder.umFilme;

import java.util.Arrays;
import java.util.Date;

import br.com.pmattiollo.entidades.Locacao;
import br.com.pmattiollo.entidades.Usuario;
import br.com.pmattiollo.utils.DataUtils;

public class LocacaoBuilder {

	private Locacao locacao;

	private LocacaoBuilder(Locacao locacao) {
		this.locacao = locacao;
	}

	public static LocacaoBuilder umaLocacao() {
		Locacao locacao = new Locacao();
		Usuario usuario = new Usuario("Usuário 1");
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		locacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(1));
		locacao.setValor(4.0);
		locacao.setFilmes(Arrays.asList(umFilme().agora()));

		LocacaoBuilder builder = new LocacaoBuilder(locacao);
		return builder;
	}

	public LocacaoBuilder comDataDeRetorno(Date data) {
		locacao.setDataRetorno(data);
		return this;
	}

	public LocacaoBuilder comUsuario(Usuario usuario) {
		locacao.setUsuario(usuario);
		return this;
	}

	public LocacaoBuilder atrasada() {
		locacao.setDataLocacao(DataUtils.obterDataComDiferencaDias(-4));
		locacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(-2));
		return this;
	}

	public Locacao agora() {
		return locacao;
	}

}
