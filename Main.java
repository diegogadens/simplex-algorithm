/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simplex;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Diego
 */
public class Main {
    public static telaPrincipal tela;
    public static Simplex oSimplex;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }


        tela = new telaPrincipal();
        tela.setVisible(true);

         oSimplex = new Simplex();
        

        emEspera();


        
    }
    
    public static void emEspera()
    {
        System.out.println("Em espera");
        while(tela.espera)
        {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        executaSimplex();
    }

    public static void executaSimplex()
    {
        System.out.println("Executando simplex");
        oSimplex.setNRestrições(tela.nRest());
        oSimplex.setNVariáveis(tela.nVar());

        oSimplex.criaMatrizAumentada(tela.nRest(), tela.nRest() + tela.nVar());

        oSimplex.processaEntrada(tela.restricoes());

        System.out.println(oSimplex.precisaFase1()?"Precisa fase 1":"Não precisa fase 1");

        if(oSimplex.precisaFase1())
        {
            oSimplex.criaMatrizFase1(tela.nRest(), tela.nRest() + tela.nVar());
            while(!oSimplex.paraFase1)
                oSimplex.passo1Fase1();
            
            if(oSimplex.paraFase1 && !oSimplex.paraFase2)
            {
            System.out.println("ARRUMA A MATRIZ E EXECUTA FASE 2");
                oSimplex.arrumaMatrizFase2();
            }

            while(!oSimplex.paraFase2)
            {
                oSimplex.passo1();
                oSimplex.passo2();
                oSimplex.passo3();
            }
        
            
        }
        else
        {
            while(!oSimplex.paraFase2)
            {
                oSimplex.passo1();
                oSimplex.passo2();
                oSimplex.passo3();
            }
        
        
        }

        System.out.println();
        tela.setTextoExecução(oSimplex.soluçãoFinal());
        
        System.out.println("Fim (Main)");
        tela.setEspera(true);
        emEspera();
    }
}
