package br.com.pmattiollo;

import br.com.pmattiollo.exception.NaoPodeDividirPorZero;

public class Calculadora {

	public int somar(int a, int b) {
		System.out.println("Passei pelo método somar");
		return a + b;
	}

	public int subtrair(int a, int b) {
		return a - b;
	}

	public int dividir(int a, int b) throws NaoPodeDividirPorZero {
		if (b == 0) {
			throw new NaoPodeDividirPorZero();
		}

		return a / b;
	}

	public void imprime() {
		System.out.println("Passei aqui");
	}

}
