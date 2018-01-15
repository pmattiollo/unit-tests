package br.com.pmattiollo.servicos;

import br.com.pmattiollo.entidades.Filme;

public class DescontoNoTerceiroFilme implements Desconto {

	private Desconto proximo;

	@Override
	public double calculaDesconto(Filme filme, int ordem) {
		if (ordem == 3) {
			return filme.getPrecoLocacao() * 0.50;
		}

		return proximo.calculaDesconto(filme, ordem);
	}

	@Override
	public void setProximo(Desconto proximo) {
		this.proximo = proximo;
	}

}
