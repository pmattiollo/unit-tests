package br.com.pmattiollo.servicos;

import br.com.pmattiollo.entidades.Filme;

public class DescontoAPartirDoQuintoFilme implements Desconto {

	@Override
	public double calculaDesconto(Filme filme, int ordem) {
		if (ordem >= 5) {
			return filme.getPrecoLocacao();
		}

		return 0;
	}

	@Override
	public void setProximo(Desconto proximo) {}

}
