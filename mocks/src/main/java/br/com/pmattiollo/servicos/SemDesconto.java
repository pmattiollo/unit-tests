package br.com.pmattiollo.servicos;

import br.com.pmattiollo.entidades.Filme;

public class SemDesconto implements Desconto {

	private Desconto proximo;

	@Override
	public double calculaDesconto(Filme filme, int ordem) {
		if (ordem < 2) {
			return 0;
		}

		return proximo.calculaDesconto(filme, ordem);
	}

	@Override
	public void setProximo(Desconto proximo) {
		this.proximo = proximo;
	}

}
