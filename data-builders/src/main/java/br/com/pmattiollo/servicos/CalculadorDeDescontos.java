package br.com.pmattiollo.servicos;

import java.util.List;

import br.com.pmattiollo.entidades.Filme;

public class CalculadorDeDescontos {

	public double calcula(List<Filme> filmes) {
		Desconto semDesconto = new SemDesconto();
		Desconto descontoNoSegundoItem = new DescontoNoSegundoFilme();
		Desconto descontoNoTerceiroItem = new DescontoNoTerceiroFilme();
		Desconto descontoNoQuartoItem = new DescontoNoQuartoFilme();
		Desconto descontoAPartirdoQuintoItem = new DescontoAPartirDoQuintoFilme();

		semDesconto.setProximo(descontoNoSegundoItem);
		descontoNoSegundoItem.setProximo(descontoNoTerceiroItem);
		descontoNoTerceiroItem.setProximo(descontoNoQuartoItem);
		descontoNoQuartoItem.setProximo(descontoAPartirdoQuintoItem);

		double valorTotal = 0;
		double valorDoDesconto = 0;
		for (int i = 0;i < filmes.size();i++) {
			Filme filme = filmes.get(i);

			valorTotal += filme.getPrecoLocacao();
			valorDoDesconto += semDesconto.calculaDesconto(filme, i);
		}

		return valorTotal - valorDoDesconto;
	}

}
