package br.com.pmattiollo;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.pmattiollo.exception.NaoPodeDividirPorZero;

public class CalculadoraTest {

	private Calculadora calculadora;

	@Before
	public void inicializar() {
		calculadora = new Calculadora();
	}

	@Test
	public void deveSomarDoisValores() {
		// Cenário
		int a = 5;
		int b = 3;

		// Ação
		int resultado = calculadora.somar(a, b);

		// Verificação
		Assert.assertThat(resultado, is(8));
	}

	@Test
	public void deveSubtrairDoisValores() {
		// Cenário
		int a = 8;
		int b = 5;

		// Ação
		int resultado = calculadora.subtrair(a, b);

		// Verificação
		Assert.assertThat(resultado, is(3));
	}

	@Test
	public void deveDividirDoisValores() throws NaoPodeDividirPorZero {
		// Cenário
		int a = 6;
		int b = 3;

		// Ação
		int resultado = calculadora.dividir(a, b);

		// Verificação
		Assert.assertThat(resultado, is(2));
	}

	@Test(expected = NaoPodeDividirPorZero.class)
	public void deveLancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZero {
		// Cenário
		int a = 6;
		int b = 0;

		// Ação e verificacao
		calculadora.dividir(a, b);
	}

}
