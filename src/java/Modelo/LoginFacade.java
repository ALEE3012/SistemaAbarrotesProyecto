package Modelo;

import Modelo.UsuarioDAO;

public class LoginFacade {

    private UsuarioDAO dao = new UsuarioDAO();

    public Usuario iniciarSesion(String usuario, String clave) {
        return dao.validar(usuario, clave);
    }
}