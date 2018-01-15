package br.com.pmattiollo.servicos;

import static br.com.pmattiollo.builders.FilmeBuilder.umFilme;
import static br.com.pmattiollo.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.com.pmattiollo.builders.UsuarioBuilder.umUsuario;
import static br.com.pmattiollo.matchers.MeusMatchers.caiNumaSegunda;
import static br.com.pmattiollo.matchers.MeusMatchers.ehHoje;
import static br.com.pmattiollo.matchers.MeusMatchers.ehHojeComDiferencaDeDias;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.com.pmattiollo.entidades.Filme;
import br.com.pmattiollo.entidades.Locacao;
import br.com.pmattiollo.entidades.Usuario;
import br.com.pmattiollo.exception.FilmeSemEstoqueException;
import br.com.pmattiollo.exception.LocadoraException;
import br.com.pmattiollo.utils.DataUtils;

public class LocacaoServiceTest {

	private LocacaoService service;

	@Rule
	public ErrorCollector errorCollector = new ErrorCollector();

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void inicializar() {
		service = new LocacaoService();
	}

	@Test
	public void deveAlugarFilme() throws Exception {
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		// Cenário
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Stream.of(umFilme().comValor(5.0).agora()).collect(Collectors.toList());

		// Ação
		Locacao locacao = service.alugarFilmes(usuario, filmes);

		// Verificação
		errorCollector.checkThat(locacao.getValor(), is(5.0));
		errorCollector.checkThat(locacao.getValor(), is(not(6.0)));
		errorCollector.checkThat(locacao.getDataLocacao(), ehHoje());
		errorCollector.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDeDias(1));
	}

	/**
	 * Teste de uma locação sem estoque
	 * 
	 * Forma Elegante - Funciona muito bem quando se tem certeza da única
	 * exceção que será lançada e quando a mensagem não é importante
	 * 
	 * @throws Exception
	 */
	@Test(expected = FilmeSemEstoqueException.class)
	public void naoDeveAlugarFilmeSemEstoque() throws Exception {
		// Cenário
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Stream.of(umFilmeSemEstoque().agora()).collect(Collectors.toList());

		// Ação
		service.alugarFilmes(usuario, filmes);
	}

	/**
	 * Teste de uma locação com o usuário vazio
	 * 
	 * Forma Robusta - É a mais completa, pois permite que o fluxo de execução
	 * continue após lançamento da exceção e mais condições sejam tratadas
	 * 
	 * @throws FilmeSemEstoqueException
	 */
	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
		// Cenário
		List<Filme> filmes = Stream.of(umFilme().agora()).collect(Collectors.toList());

		try {
			service.alugarFilmes(null, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), is("Usuário vazio"));
		}
	}

	/**
	 * Teste de uma locação com o filme vazio
	 * 
	 * Forma Nova - É uma forma bem completa, porém não permite que o fluxo de
	 * execução continue após o lançamento da exceção
	 * 
	 * @throws FilmeSemEstoqueException
	 * @throws LocadoraException
	 */
	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {
		// Cenário
		Usuario usuario = umUsuario().agora();

		expectedException.expect(LocadoraException.class);
		expectedException.expectMessage("Filme vazio");

		service.alugarFilmes(usuario, null);
	}

	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		// Cenário
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Stream.of(umFilme().agora()).collect(Collectors.toList());

		// Ação
		Locacao locacao = service.alugarFilmes(usuario, filmes);

		// Verificação
		Assert.assertThat(locacao.getDataRetorno(), caiNumaSegunda());
	}

}
