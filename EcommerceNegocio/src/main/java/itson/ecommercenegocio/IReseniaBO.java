/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.ecommercenegocio;

import itson.ecommercedominio.Resenia;
import itson.ecommercedominio.dtos.UsuarioDTO;
import itson.ecommercenegocio.excepciones.NegocioException;

/**
 *
 * @author santi
 */
public interface IReseniaBO {
    
    public void moderarResenia(String idReseniaString, UsuarioDTO usuarioSolicitante) throws NegocioException;
    public void crearResenia(Resenia resenia) throws NegocioException;

}
