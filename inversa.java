
package simplex;

import javax.swing.JOptionPane;

/**
 *
 * @author Diego
 */

public class inversa {

    public static double a[][] = {{2, 0, 0},
                                  {1, 1, 0},
                                  {1, 0, 1}};

    public static void main(String args[]) {
        double d[][] = inverte(a);

        int n = a.length;
       
    }

    public static double[][] inverte(double a[][]) {
        double inversa[][] = new double[a.length][a.length];

        //-----CRIA A IDENTIDADE---------
        for(int i=0; i<a.length; i++)
            for(int j=0; j<a.length; j++)
                if(i==j)
                    inversa[i][j]=1;
                else
                    inversa[i][j]=0;
        //-------------------------------

        for(int j=0; j<a.length; j++)
            for(int i=0; i<a.length; i++)
            {
                if(j<=i)
                {
                    if(i == j)
                    {
                        if(a[i][j] == 0)
                        {
                            //JOptionPane.showMessageDialog(null, "Zero no pivo, troca de colunas ainda não está implementada. "+ i + ","+j);
                            for(int linha=i+1; linha<a.length; linha++)
                            {
                                if(a[linha][j]!=0)
                                {
                                    double auxNormal[] = new double[a.length];
                                    double auxInversa[] = new double[a.length];

                                    for(int coluna=0; coluna<a.length; coluna++)
                                    {
                                        auxNormal[coluna] = a[i][coluna];
                                        a[i][coluna] = a[linha][coluna];
                                        a[linha][coluna] = auxNormal[coluna];

                                        auxInversa[coluna] = inversa[i][coluna];
                                        inversa[i][coluna] = inversa[linha][coluna];
                                        inversa[linha][coluna] = auxInversa[coluna];
                                    }
                                }
                            }
                        }

                        //ACHA O PIVO ij
                        if(a[i][j] != 1)
                        {
                         double divisor = a[i][j];
                            for(int k=0; k<a.length; k++)
                            {
                                a[i][k] = a[i][k]/divisor;
                                inversa[i][k] = inversa[i][k]/divisor;
                                System.out.println("Dividiu "+i+","+k+" por"+" divisor "+divisor);
                            }
                        }

                        
                    }
                    else
                    {
                        //Zera Elemento
                    if(a[i][j]!=0)
                    {
                        double operando = a[i][j];
                        for(int k=0; k<a.length; k++)
                        {
                            System.out.println("N:"+a[i][k] +" = " + a[i][k] + "-"+"("+ a[i][k]+"*"+a[j][k]+") = " + (a[i][k] - (a[i][k]*a[j][k])));
                            System.out.println("I:"+inversa[i][k] +" = " + inversa[i][k] + "-"+"("+ a[i][k]+"*"+inversa[j][k]+") = "+(inversa[i][k] - (a[i][k]*inversa[j][k])+"\n"));

                            a[i][k] = a[i][k] - (operando*a[j][k]);
                            inversa[i][k] = inversa[i][k] - (operando*inversa[j][k]);                            
                        }
                    }

                        //System.out.println(a[i][j]);
                    }
                }
            }

        for(int j=(a.length-1); j>=0; j--)
            for(int i=(a.length-1); i>=0; i--)
            {
                if(j>i)
                {
                    //Zera Elemento
                    if(a[i][j]!=0)
                    {
                    double operando = a[i][j];
                        for(int k=a.length-1; k>=0; k--)
                        {
                            a[i][k] = a[i][k] - (operando*a[j][k]);
                            inversa[i][k] = inversa[i][k] - (operando*inversa[j][k]);
                        }
                    }
                    //System.out.println(a[i][j]);
                }
            }



            System.out.println("Normal:");
            imprimeMatriz(a.length, a);
            System.out.println();
            System.out.println("Inversa");
            imprimeMatriz(inversa.length, inversa);

        return inversa;
    }

    public static void imprimeMatriz(int n, double d[][])
    {
        for (int i = 0; i < n; ++i)
        {
            System.out.println();
            for (int j = 0; j < n; ++j)
            {
                System.out.print(String.format("%.2f ", d[i][j]));
            }
        }
    }
}


