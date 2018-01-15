package br.com.pmattiollo.servicos;

import br.com.pmattiollo.entidades.Filme;

public class DescontoNoQuartoFilme implements Desconto {

	private Desconto proximo;

	@Override
	public double calculaDesconto(Filme filme, int ordem) {
		if (ordem == 4) {
			return filme.getPrecoLocacao() * 0.75;
		}

		return proximo.calculaDesconto(filme, ordem);
	}

	@Override
	public void setProximo(Desconto proximo) {
		this.proximo = proximo;

	}

}
