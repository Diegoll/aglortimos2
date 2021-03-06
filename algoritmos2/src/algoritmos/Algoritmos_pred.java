/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package algoritmos;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 *
 * @author Pc
 */
public class Algoritmos_pred {
    
    
    
    
    public Parametros testIAmasA(int n, HashMap<Long, TreeSet<ItemSim>> modeloSimilitud, List<Usuario> test) {
        // Variables auxiliares:
        Iterator<Usuario> it1 = test.iterator();
        Usuario u;
        double mediaP;
        long idP;
        double valoracionEstimada;
        int valoracionReal;
        double MAE = 0;
        int numEstimacionesRealizadas = 0;
        double dif;
        TreeSet<ItemSim> vecinos;
        int numEstimacionesImposibles = 0;
        int numEstimacionesPosibles = 0;
        
        
        // Nota: cargamos todas las medias de las peliculas a memoria para acelerar la ejecución
        HashMap<Long,Double> medias = getMediasPeliculasBD_HashMap(instancia);
        
        int cont = 0;
        // 1. Recorremos cada usuario de la partición test.
        while (it1.hasNext()){
            u = it1.next();
            //System.out.println(" Usuario "+cont+" de "+test.size());
            ++cont;
            
            // 2. Recorremos cada valoración del usuario en cuestión.
            for (Map.Entry<Long,Valoracion> e : u.getValoraciones().entrySet()) {


                idP = e.getValue().idpeli;

                // 3. Calculamos la valoracion real y la estimada.
                valoracionReal = e.getValue().getValor();
                mediaP = medias.get(idP);
                vecinos = modeloSimilitud.get(idP);
                valoracionEstimada = calcularPrediccionIAmasA(n, u, mediaP,vecinos);

                // 4. Comprobamos si hemos podido hacer la predicción
                if (valoracionEstimada != -1){
                   // 5. Acumulamos el MAE
                   dif = valoracionEstimada - valoracionReal*1.0;
                   if (dif > 0){
                      MAE = MAE + dif;
                   }else{
                      MAE = MAE + dif*(-1);
                   }
                }else{
                    ++numEstimacionesImposibles;
                }
            }
            
            numEstimacionesPosibles += u.getValoraciones().size();
        }
        
        numEstimacionesRealizadas = numEstimacionesPosibles - numEstimacionesImposibles;
        if (numEstimacionesRealizadas != 0){
            MAE = MAE/(numEstimacionesRealizadas*1.0);
        }else{
            MAE = 0;
        }
        
        return new Parametros(MAE,(numEstimacionesRealizadas*1.0)/numEstimacionesPosibles);
        
    }
    
    
    
    public Parametros testWA(HashMap<Long, TreeSet<ItemSim>> modeloSimilitud, List<Usuario> test, GestorPersistencia instancia) {
        // Variables auxiliares:
        Iterator<Usuario> it1 = test.iterator();
        Usuario u;
        long idP;
        double valoracionEstimada;
        int valoracionReal;
        double MAE = 0;
        int numEstimacionesRealizadas = 0;
        double dif;
        TreeSet<ItemSim> vecinos;
        int numEstimacionesImposibles = 0;
        int numEstimacionesPosibles = 0;
        
        // Nota: cargamos todas las medias de las peliculas a memoria para acelerar la ejecución
        HashMap<Long,Double> medias = getMediasPeliculasBD_HashMap(instancia);
        
        // 1. Recorremos cada usuario de la partición test.
        int cont = 0;
        while (it1.hasNext()){
            u = it1.next();
            //System.out.println(" Usuario "+cont+" de "+test.size());
            ++cont;
            // 2. Recorremos cada valoración del usuario en cuestión.
             for (Map.Entry<Long,Valoracion> e : u.getValoraciones().entrySet()) {
                 idP = e.getValue().getIdPelicula();
                 
                 // 3. Calculamos la valoracion real y la estimada.
                 valoracionReal = e.getValue().getValor();
                 vecinos = modeloSimilitud.get(idP);
                 valoracionEstimada = calcularPrediccionWA(u,vecinos, medias);
                 
                 // 4. Comprobamos si hemos podido hacer la predicción
                 if (valoracionEstimada != -1){
                    // 5. Acumulamos el MAE
                    dif = valoracionEstimada - valoracionReal*1.0;
                    if (dif > 0){
                       MAE = MAE + dif;
                    }else{
                       MAE = MAE + dif*(-1);
                    }
                 }else{
                     ++numEstimacionesImposibles;
                 }
             }
             numEstimacionesPosibles += u.getValoraciones().size();
        }
        
        numEstimacionesRealizadas = numEstimacionesPosibles - numEstimacionesImposibles;
        if (numEstimacionesRealizadas != 0){
            MAE = MAE/(numEstimacionesRealizadas*1.0);
        }else{
            MAE = 0;
        }
        
        return new Parametros(MAE,(numEstimacionesRealizadas*1.0)/numEstimacionesPosibles);
        
    }

    
}
