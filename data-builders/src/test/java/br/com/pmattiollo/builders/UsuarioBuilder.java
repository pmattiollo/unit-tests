package br.com.pmattiollo.builders;

import br.com.pmattiollo.entidades.Usuario;

public class UsuarioBuilder {

	private Usuario usuario;

	private UsuarioBuilder(Usuario usuario) {
		this.usuario = usuario;
	}

	public static UsuarioBuilder umUsuario() {
		Usuario usuario = new Usuario();
		usuario.setNome("Usuário");

		UsuarioBuilder builder = new UsuarioBuilder(usuario);
		return builder;
	}

	public Usuario agora() {
		return usuario;
	}

}
