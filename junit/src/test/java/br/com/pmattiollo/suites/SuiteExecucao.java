package br.com.pmattiollo.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.com.pmattiollo.CalculadoraTest;
import br.com.pmattiollo.servicos.CalculoValorLocacaoTest;
import br.com.pmattiollo.servicos.LocacaoServiceTest;

@RunWith(Suite.class)
@SuiteClasses({CalculadoraTest.class, CalculoValorLocacaoTest.class, LocacaoServiceTest.class})
public class SuiteExecucao {

}
