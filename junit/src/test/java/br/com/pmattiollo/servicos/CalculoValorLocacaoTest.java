package br.com.pmattiollo.servicos;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import br.com.pmattiollo.entidades.Filme;
import br.com.pmattiollo.entidades.Locacao;
import br.com.pmattiollo.entidades.Usuario;
import br.com.pmattiollo.exception.FilmeSemEstoqueException;
import br.com.pmattiollo.exception.LocadoraException;
import br.com.pmattiollo.servicos.LocacaoService;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

	private LocacaoService service;

	@Parameter(value = 0)
	public List<Filme> filmes;

	@Parameter(value = 1)
	public Double valorLocacao;

	@Parameter(value = 2)
	public String cenario;

	@Before
	public void inicializar() {
		service = new LocacaoService();
	}

	private static Filme filme1 = new Filme("Filme 1", 2, 4.0);
	private static Filme filme2 = new Filme("Filme 2", 2, 4.0);
	private static Filme filme3 = new Filme("Filme 3", 2, 4.0);
	private static Filme filme4 = new Filme("Filme 4", 2, 4.0);
	private static Filme filme5 = new Filme("Filme 5", 2, 4.0);
	private static Filme filme6 = new Filme("Filme 6", 2, 4.0);
	private static Filme filme7 = new Filme("Filme 7", 2, 4.0);

	@Parameters(name = "{2}")
	public static Collection<Object[]> getParametros() {
		return Stream.of(
				new Object[][] {{Stream.of(filme1, filme2).collect(Collectors.toList()), 8.0, "2 Filmes: Sem desconto"},
						{Stream.of(filme1, filme2, filme3).collect(Collectors.toList()), 11.0,
								"3 Filmes: 25% de desconto do 3º Filme"},
						{Stream.of(filme1, filme2, filme3, filme4).collect(Collectors.toList()), 13.0,
								"4 Filmes: 50% de desconto do 5º Filme"},
						{Stream.of(filme1, filme2, filme3, filme4, filme5).collect(Collectors.toList()), 14.0,
								"5 Filmes: 75% de desconto do 5º Filme"},
						{Stream.of(filme1, filme2, filme3, filme4, filme5, filme6).collect(Collectors.toList()), 14.0,
								"6 Filmes: 100% a partir do 6º Filme"},
						{Stream.of(filme1, filme2, filme3, filme4, filme5, filme6, filme7).collect(Collectors.toList()),
								14.0, "7 Filmes: 100% a partir do 6º Filme"}

				}).collect(Collectors.toList());
	}

	@Test
	public void deveCalcularValorDaLocacaoConsiderandoDescontos() throws FilmeSemEstoqueException, LocadoraException {
		// Cenário
		Usuario usuario = new Usuario("Usuario 1");

		// Ação
		Locacao locacao = service.alugarFilmes(usuario, filmes);

		// Verificação
		assertThat(locacao.getValor(), is(valorLocacao));
	}

}
