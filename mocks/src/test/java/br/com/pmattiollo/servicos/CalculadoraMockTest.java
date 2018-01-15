package br.com.pmattiollo.servicos;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.com.pmattiollo.Calculadora;

public class CalculadoraMockTest {

	@Mock
	private Calculadora calculadoraMock;

	@Spy
	private Calculadora calculadoraSpy;

	@Before
	public void inicializar() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void deveMostrarAsDiferencasEntreMockESpy() {
		// Cenário
		// when(calculadoraMock.somar(1, 2)).thenCallRealMethod();
		when(calculadoraMock.somar(1, 2)).thenReturn(3);
		// when(calculadoraSpy.somar(1, 2)).thenReturn(3);
		doReturn(5).when(calculadoraSpy).somar(1, 2);
		doNothing().when(calculadoraSpy).imprime();

		// Ação
		System.out.println("Mock com argumentos certos: " + calculadoraMock.somar(1, 2));
		System.out.println("Spy com argumentos certos: " + calculadoraSpy.somar(1, 2));

		System.out.println("Mock com argumentos errados: " + calculadoraMock.somar(1, 3));
		System.out.println("Spy com argumentos certos: " + calculadoraSpy.somar(1, 3));

		calculadoraMock.imprime();
		calculadoraSpy.imprime();
	}

	@Test
	public void teste() {
		Calculadora calculadora = Mockito.mock(Calculadora.class);
		ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);

		Mockito.when(calculadora.somar(argumentCaptor.capture(), Mockito.anyInt())).thenReturn(5);
		Assert.assertEquals(5, calculadora.somar(1, 10000));

		System.out.println(argumentCaptor.getValue());
	}

}
