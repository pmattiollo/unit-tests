package br.com.pmattiollo.daos;

import java.util.List;

import br.com.pmattiollo.entidades.Locacao;

public interface LocacaoDAO {

	public void salvar(Locacao locacao);

	public List<Locacao> obterLocacoesPendentes();

}
