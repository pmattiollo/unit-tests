package br.com.pmattiollo.builders;

import br.com.pmattiollo.entidades.Filme;

public class FilmeBuilder {

	private Filme filme;

	private FilmeBuilder(Filme filme) {
		this.filme = filme;
	}

	public static FilmeBuilder umFilme() {
		Filme filme = new Filme();
		filme.setEstoque(2);
		filme.setNome("Filme");
		filme.setPrecoLocacao(4.0);

		FilmeBuilder builder = new FilmeBuilder(filme);
		return builder;
	}

	public static FilmeBuilder umFilmeSemEstoque() {
		Filme filme = new Filme();
		filme.setEstoque(0);
		filme.setNome("Filme");
		filme.setPrecoLocacao(4.0);

		FilmeBuilder builder = new FilmeBuilder(filme);
		return builder;
	}

	public FilmeBuilder semEstoque() {
		filme.setEstoque(0);
		return this;
	}

	public FilmeBuilder comValor(Double valor) {
		filme.setPrecoLocacao(valor);
		return this;
	}

	public Filme agora() {
		return filme;
	}

}
