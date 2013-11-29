/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package algoritmos;

import java.util.HashMap;

/**
 *
 * @author Pc
 */
public class Usuario {
    long id;
    String nombre;
    //ArrayList<Valoracion> valoraciones;
    HashMap<Long,Valoracion> valoraciones;
    float notamedia;
    
    Usuario(int indiceusu) {
        id=indiceusu; 
        valoraciones=new HashMap<>();
    }
    long getIdUsuario(){return id;}
    float getMedia(){return notamedia;}
    HashMap<Long,Valoracion> getValoraciones(){return valoraciones;}
    void setMedia(float media){notamedia=media;}
}
