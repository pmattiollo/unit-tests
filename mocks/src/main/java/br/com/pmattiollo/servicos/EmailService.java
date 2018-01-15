package br.com.pmattiollo.servicos;

import br.com.pmattiollo.entidades.Usuario;

public interface EmailService {

	public void notificarAtraso(Usuario usuario);

}
