package br.com.pmattiollo.servicos;

import br.com.pmattiollo.entidades.Filme;

public interface Desconto {

	/**
	 * Calcula o desconto de acordo com o valor do Filme e com a ordem de
	 * locação. Por exemplo: o 2º filme recebe 25% de desconto, o 3º filme
	 * recebe 50% e assim por diante
	 * 
	 * @param filme
	 *            - {@link Filme} locado
	 * @param ordem
	 *            - ordem do Filme locado
	 * @return o desconto sobre o filme locado
	 */
	public double calculaDesconto(Filme filme, int ordem);

	/**
	 * Define o próximo desconto a ser aplicado
	 * 
	 * @param -
	 *            {@link Desconto} proximo
	 */
	public void setProximo(Desconto proximo);

}
