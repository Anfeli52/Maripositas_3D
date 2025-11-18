package com.andres.mariposita3d.Service;

import com.andres.mariposita3d.Collection.Usuario;
import com.andres.mariposita3d.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("[DEBUG] loadUserByUsername called with email='" + email + "'");
        Usuario usuario = usuarioRepository.findByCorreo(email);
        if(usuario == null){
            System.out.println("[DEBUG] usuarioRepository.findByCorreo returned null for email='" + email + "'");
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        System.out.println("[DEBUG] Found usuario with correo='" + usuario.getCorreo() + "', id='" + usuario.getId() + "', rol='" + usuario.getRol() + "'");
        // For safety, avoid logging the password in production
        return usuario;
    }
}
