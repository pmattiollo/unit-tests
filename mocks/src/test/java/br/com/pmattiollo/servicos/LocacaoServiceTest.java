package br.com.pmattiollo.servicos;

import static br.com.pmattiollo.builders.FilmeBuilder.umFilme;
import static br.com.pmattiollo.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.com.pmattiollo.builders.LocacaoBuilder.umaLocacao;
import static br.com.pmattiollo.builders.UsuarioBuilder.umUsuario;
import static br.com.pmattiollo.matchers.MeusMatchers.caiNumaSegunda;
import static br.com.pmattiollo.matchers.MeusMatchers.ehHoje;
import static br.com.pmattiollo.matchers.MeusMatchers.ehHojeComDiferencaDeDias;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.pmattiollo.daos.LocacaoDAO;
import br.com.pmattiollo.entidades.Filme;
import br.com.pmattiollo.entidades.Locacao;
import br.com.pmattiollo.entidades.Usuario;
import br.com.pmattiollo.exception.FilmeSemEstoqueException;
import br.com.pmattiollo.exception.LocadoraException;
import br.com.pmattiollo.utils.DataUtils;

public class LocacaoServiceTest {

	@Mock
	private LocacaoDAO dao;

	@Mock
	private SPCService spcService;

	@Mock
	private EmailService emailService;

	@InjectMocks
	private LocacaoService locacaoService;

	@Rule
	public ErrorCollector errorCollector = new ErrorCollector();

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void inicializar() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void deveAlugarFilme() throws Exception {
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		// Cenário
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Stream.of(umFilme().comValor(5.0).agora()).collect(Collectors.toList());

		// Ação
		Locacao locacao = locacaoService.alugarFilmes(usuario, filmes);

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
		locacaoService.alugarFilmes(usuario, filmes);
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
			locacaoService.alugarFilmes(null, filmes);
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

		locacaoService.alugarFilmes(usuario, null);
	}

	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		// Cenário
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Stream.of(umFilme().agora()).collect(Collectors.toList());

		// Ação
		Locacao locacao = locacaoService.alugarFilmes(usuario, filmes);

		// Verificação
		assertThat(locacao.getDataRetorno(), caiNumaSegunda());
	}

	@Test
	public void naoDeveAlugarFilmeParaNegativadoNoSPC() throws Exception {
		// Cenário
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Stream.of(umFilme().agora()).collect(Collectors.toList());

		when(spcService.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);

		try {
			// Ação
			locacaoService.alugarFilmes(usuario, filmes);

			// Verificação
			fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), is("Usuário negativado"));
		}

		verify(spcService).possuiNegativacao(usuario);
	}

	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() {
		// Cenário
		Usuario usuario1 = umUsuario().agora();
		Usuario usuario2 = umUsuario().comNome("Usuário em dia").agora();
		Usuario usuario3 = umUsuario().comNome("Outro atrasado").agora();
		List<Locacao> locacoes = Stream.of(umaLocacao().comUsuario(usuario1).atrasada().agora(),
				umaLocacao().comUsuario(usuario2).agora(), umaLocacao().comUsuario(usuario3).atrasada().agora(),
				umaLocacao().comUsuario(usuario3).atrasada().agora()).collect(Collectors.toList());
		when(dao.obterLocacoesPendentes()).thenReturn(locacoes);

		// Ação
		locacaoService.notificarAtrasos();

		// Verificação
		verify(emailService, times(3)).notificarAtraso(Mockito.any(Usuario.class));
		verify(emailService).notificarAtraso(usuario1);
		verify(emailService, never()).notificarAtraso(usuario2);
		verify(emailService, times(2)).notificarAtraso(usuario3);
		verifyNoMoreInteractions(emailService);
	}

	@Test
	public void deveTratarErroNoSPC() throws Exception {
		// Cenário
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		when(spcService.possuiNegativacao(usuario)).thenThrow(new Exception("Falha catastrófica"));

		// Verificação
		expectedException.expect(LocadoraException.class);
		expectedException.expectMessage("Problemas com SPC, tente novamente");

		// Ação
		locacaoService.alugarFilmes(usuario, filmes);
	}

	@Test
	public void deveProrrogarUmaLocacao() {
		// Cenário
		Locacao locacao = umaLocacao().agora();

		// Ação
		locacaoService.prorrogarLocacao(locacao, 3);

		// Verificação
		ArgumentCaptor<Locacao> argumentCaptor = ArgumentCaptor.forClass(Locacao.class);
		verify(dao).salvar(argumentCaptor.capture());
		Locacao locacaoCapturada = argumentCaptor.getValue();

		errorCollector.checkThat(locacaoCapturada.getValor(), is(12.0));
		errorCollector.checkThat(locacaoCapturada.getDataLocacao(), ehHoje());
		errorCollector.checkThat(locacaoCapturada.getDataRetorno(), ehHojeComDiferencaDeDias(3));
	}

}
