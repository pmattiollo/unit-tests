package br.com.pmattiollo.servicos;

import static br.com.pmattiollo.builders.FilmeBuilder.umFilme;
import static br.com.pmattiollo.builders.UsuarioBuilder.umUsuario;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.pmattiollo.daos.LocacaoDAO;
import br.com.pmattiollo.entidades.Filme;
import br.com.pmattiollo.entidades.Locacao;
import br.com.pmattiollo.entidades.Usuario;
import br.com.pmattiollo.exception.FilmeSemEstoqueException;
import br.com.pmattiollo.exception.LocadoraException;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

	@Mock
	private LocacaoDAO dao;

	@Mock
	private SPCService spcService;

	@Mock
	private EmailService emailService;

	@InjectMocks
	private LocacaoService locacaoService;

	@Parameter(value = 0)
	public List<Filme> filmes;

	@Parameter(value = 1)
	public Double valorLocacao;

	@Parameter(value = 2)
	public String cenario;

	@Before
	public void inicializar() {
		MockitoAnnotations.initMocks(this);
	}

	private static Filme filme1 = umFilme().agora();
	private static Filme filme2 = umFilme().agora();
	private static Filme filme3 = umFilme().agora();
	private static Filme filme4 = umFilme().agora();
	private static Filme filme5 = umFilme().agora();
	private static Filme filme6 = umFilme().agora();
	private static Filme filme7 = umFilme().agora();

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
		Usuario usuario = umUsuario().agora();

		// Ação
		Locacao locacao = locacaoService.alugarFilmes(usuario, filmes);

		// Verificação
		assertThat(locacao.getValor(), is(valorLocacao));
	}

}
