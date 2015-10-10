/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author Diego
 */
public class Simplex {

    int nVariaveis, nRestricoes, qtdVarFase1, qtdColunasFase1, saiDaBase, entraNaBase;
    double Aumentada[][], Aumentada2[][], B[][], Binversa[][];
    ArrayList<Integer> indicesFase1;
    ArrayList<Integer> indicesNaoIncluir;
    double b[], xB[], xN[], cRelativos[], y[], backupCustos[];
    boolean precisaFase1;
    boolean semSolução;
    String motivoSemSolução;
    public boolean paraFase1 = false;
    public boolean paraFase2 = false;
    String sinalRestricoes[];
    String variaveis[];
    String variaveis2[];



    String tipoFuncao;

    int indicesB[], indicesN[];
    int indicesN2[];

    StringTokenizer tokenizer;

    
    
    public void criaMatrizAumentada(int m, int n)
    {
        Aumentada = new double[m+1][n];
        variaveis = new String[n];
        
    }

    public void criaMatrizFase1(int m, int n){
        int contNovasVarArtif = variaveis.length + 1;
        Aumentada2 = new double[m+1][n+qtdColunasFase1];
        //indicesFase1 = new int[qtdVarFase1];
        variaveis2 = new String[variaveis.length+qtdColunasFase1];
        indicesN2 = new int[indicesN.length+qtdColunasFase1];
        indicesFase1 = new ArrayList<Integer>();
        indicesNaoIncluir = new ArrayList<Integer>();

        for(int i=0; i<m+1; i++)
            for(int j=0; j<n; j++){
                if(i==nRestricoes)
                    Aumentada2[i][j] = 0;
                else
                    Aumentada2[i][j] = Aumentada[i][j];
            }

        for(int i=0; i<nVariaveis; i++)
            indicesN2[i] = indicesN[i];

        for(int i=0; i<variaveis.length; i++)
            variaveis2[i] = variaveis[i];

        int iFase1 = 0;
        for(int i=0; i<indicesB.length; i++)
            if(!sinalRestricoes[i].equals("<="))
                indicesFase1.add(i);
            
        
        //coloca -1 para quem eh diferente de <=
        for(int i=0; i<nRestricoes; i++)
            for(int j=0; j<nRestricoes; j++){
                if((i==j)&&(sinalRestricoes[i].equals(">=")))
                    Aumentada2[i][j] = -1;
            }

        //joga as variáveis de folga para o fim da matriz
        //e no lugar destas colunas entram as variaveis artificiais
        int indice = nRestricoes+nVariaveis;
        for(int i=0; i<nRestricoes; i++){
                if(sinalRestricoes[i].equals(">="))
                {
                    //indicesFase1[i]=i;
                    for(int linha=0; linha<=nRestricoes; linha++)
                    {
                        Aumentada2[linha][indice] = Aumentada2[linha][i];
                        indicesN2[indice-nRestricoes] = indice;
                        if((linha==i)||(linha==nRestricoes))
                            Aumentada2[linha][i] = 1;
                    }
                    variaveis2[indice] = variaveis[i];
                    variaveis2[i] = "x" + contNovasVarArtif;
                    contNovasVarArtif++;
                    indice++;
                }
                else
                if(sinalRestricoes[i].equals("="))
                    for(int linha=0; linha<=nRestricoes; linha++)
                    {
                        if((linha==i)||(linha==nRestricoes))
                            Aumentada2[linha][i] = 1;
                    }
            }

        System.out.println("Indices B: ");
        for(int i=0; i<indicesB.length; i++)
            System.out.print(indicesB[i]+ " ");
        System.out.println();

        
        System.out.println("Indices N2: ");
        for(int i=0; i<indicesN2.length; i++)
            System.out.print(indicesN2[i]+ " ");
        System.out.println();

        System.out.println("Variáveis2: ");
        for(int i=0; i<variaveis2.length; i++)
            System.out.print(variaveis2[i]+ " ");
        System.out.println();

        System.out.println("Indices fase 1: ");
        for(Integer c : indicesFase1)
            System.out.print(c+" ");
        System.out.println();

        System.out.println("\n\nAumentada2");
        imprimeMatriz(m+1, n+qtdColunasFase1, Aumentada2);

    }

    public boolean precisaFase1()
    {
        qtdVarFase1 = 0;
        qtdColunasFase1 = 0;
        boolean result = false;
        for(int i=0; i<nRestricoes; i++)
            if(!(sinalRestricoes[i].equals("<=")))
            {
                result = true;
                setPrecisaFase1(true);
                qtdVarFase1++;
                if((sinalRestricoes[i].equals(">=")))
                    qtdColunasFase1++;
            }

        return result;
    }

    public void setPrecisaFase1(boolean b)
    {
        precisaFase1 = b;
    }

    public void processaEntrada(String s){
        sinalRestricoes = new String[nRestricoes];
        b = new double[nRestricoes];
        indicesB = new int[nRestricoes];
        indicesN = new int[nVariaveis];
        backupCustos = new double[nVariaveis+nRestricoes];

        tokenizer = new StringTokenizer(s);
        
        for(int i=0; i<nRestricoes; i++){
            for(int j=0; j<nRestricoes; j++)
            {
                if(i==j)
                    Aumentada[i][j]=1;
                else
                    Aumentada[i][j]=0;
            }
        }

        tipoFuncao = tokenizer.nextToken();
        System.out.println(tipoFuncao);

        if(tipoFuncao.equals("min"))
            for(int j=nRestricoes; j<nRestricoes+nVariaveis; j++){
                Aumentada[nRestricoes][j] = Double.parseDouble(tokenizer.nextToken());
            }
        else
            for(int j=nRestricoes; j<nRestricoes+nVariaveis; j++){
                Aumentada[nRestricoes][j] = -1 * Double.parseDouble(tokenizer.nextToken());
            }
        
        for(int i=0; i<nRestricoes; i++)
        {
            for(int j=nRestricoes; j<nRestricoes+nVariaveis+2; j++)
            {
                if(j < nRestricoes+nVariaveis)
                    Aumentada[i][j] = Double.parseDouble(tokenizer.nextToken());
                if (j == nRestricoes+nVariaveis)
                    sinalRestricoes[i]=tokenizer.nextToken();
                if (j > nRestricoes+nVariaveis)
                    b[i]= Double.parseDouble(tokenizer.nextToken());
            }
        }

        
        

        for(int i=0; i<nRestricoes+nVariaveis; i++)
            backupCustos[i] = Aumentada[nRestricoes][i];

              
        imprimeMatriz(nRestricoes+1, nRestricoes+nVariaveis, Aumentada);

        System.out.println();
        for(int i=0; i<nRestricoes; i++)
            System.out.print(b[i]+ " ");

        System.out.println();

        for(int i=0; i<nRestricoes; i++)
            System.out.print(sinalRestricoes[i]+ " ");


        for(int i=0; i<nRestricoes+nVariaveis; i++)
        {
            if(i<nRestricoes)
            {
                indicesB[i]=i;
                variaveis[i] = "x" + (nVariaveis + 1 + i);
            }
            else
            {
                indicesN[i-nRestricoes]=i;
                variaveis[i] = "x" + (1 + i - nRestricoes);
            }
        }

        System.out.println("Variáveis: ");
        for(int i=0; i<variaveis.length; i++)
            System.out.print(variaveis[i]+ " ");
        System.out.println();
            
    }


    //---------------------------------------------------
    //Passos do algoritmo simplex

    //Cálculo da solução básica
    public void passo1()
    {
        System.out.println("\n\n-----------------Início passo 1");
        B = new double[nRestricoes][nRestricoes];
        Binversa = new double[nRestricoes][nRestricoes];
        xB = new double[nRestricoes];
        
        //copia os dados da matriz aumentada para B do passo 1
        for(int i=0; i<nRestricoes; i++)
            for(int j=0; j<nRestricoes; j++)
                B[i][j] = Aumentada[i][j];

        System.out.println("\nB:");
        for(int i=0; i<nRestricoes; i++)
        {
            for(int j=0; j<nRestricoes; j++)
                    System.out.print(B[i][j]+ " ");
            System.out.println();
        }
        //---------------------------------------------------

        //calcula a inversa de B
            Binversa = inverte(B);
        //---------------------------------------------------

        //calcula o vetor xB
            for(int i=0; i<nRestricoes; i++)
                for(int j=0; j<nRestricoes; j++)
                    xB[i] = xB[i] + b[j] * Binversa[i][j];
        //---------------------------------------------------
        System.out.println("\nB inversa:");
        for(int i=0; i<nRestricoes; i++)
        {
                for(int j=0; j<nRestricoes; j++)
                    System.out.print(Binversa[i][j]+ " ");
            System.out.println();
        }

        System.out.println("\nxB:");
        for(int j=0; j<nRestricoes; j++)
            System.out.print(xB[j]+ " ");
        System.out.println();
        
        System.out.println("--------------------Fim passo 1");
    }

    //Cálculo dos custos relativos
    public void passo2()
    {
        System.out.println("-----------------Início passo 2");
        //2.1 vetor multiplicador simplex
        double lambTransp[] = new double[nRestricoes];
        double cTB[] = new double[nRestricoes];
        double cN[];
        

        
        cN = new double[nVariaveis];
        cRelativos = new double[nVariaveis];
        

        //copia os custos da matriz aumentada para o cTB do passo 2
        for(int i=0; i<nRestricoes; i++)
            cTB[i] = Aumentada[nRestricoes][i];

        System.out.println("\ncTB:");
            for(int j=0; j<nRestricoes; j++)
                System.out.print(cTB[j]+ " ");
        System.out.println();

        //----------------------------------------------------------


        //calcula o vetor lambda transposto
            for(int i=0; i<nRestricoes; i++)
                for(int j=0; j<nRestricoes; j++)
                    lambTransp[i] = lambTransp[i] + cTB[j] * Binversa[j][i];
        //----------------------------------------------------------

        System.out.println("\nlambda transposto:");
            for(int j=0; j<nRestricoes; j++)
                System.out.print(lambTransp[j]+ " ");
        System.out.println();


        //2.2 custos relativos
        //copia os custos da matriz aumentada para o cN do passo 2
        for(int i=nRestricoes; i<nRestricoes+nVariaveis; i++)
            cN[i-nRestricoes] = Aumentada[nRestricoes][i];
        //----------------------------------------------------------

        //encontra os custos relativos
        for(int i=0; i<cRelativos.length; i++)
            cRelativos[i]= cN[i]-(lambTranspVEZESaNj(lambTransp, pegaColuna(nRestricoes+i)));

        System.out.println("\nCustos relativos");
        for(int i=0; i<cRelativos.length; i++)
            System.out.print(cRelativos[i]+" ");
        System.out.println("");

        //2.3 determinação da variável a entrar na base
        double menor = 9999;
        for(int i=0; i<cRelativos.length; i++)
            if(cRelativos[i]<menor)
            {
                menor = cRelativos[i];
                entraNaBase = i+nRestricoes;
            }
        
        System.out.println("\nA variável: " + variaveis[entraNaBase]+" entra na base");

        System.out.println("Índice: " + entraNaBase);

        System.out.println("--------------------Fim passo 2");
    }

    public double lambTranspVEZESaNj(double[] a, double[] b)
    {
        double retorno= 0;

        for(int i=0; i<nRestricoes; i++)
        {
            retorno = retorno + (a[i]*b[i]);
        }
        return retorno;
    }

    public double[] pegaColuna(int index)
    {
        double[] retorno = new double[nRestricoes];
        for(int i=0; i<nRestricoes; i++)
        {
            if(precisaFase1)
                retorno[i] = Aumentada2[i][index];
            else
                retorno[i] = Aumentada[i][index];
        }
        return retorno;
    }

    //teste de otimalidade
    public void passo3()
    {
        paraFase2=true;
        System.out.println("-----------------Início passo 3");
        //Verifica se ainda existe algum custo relativo negativo
            for(int i=0; i<cRelativos.length; i++)
            {
                if(cRelativos[i]<0)
                    paraFase2 = false;
            }
        if(!paraFase2)
            System.out.println("\nSolução não ótima, vá para o passo 4\n");

        
        if(!paraFase2)
        {
            System.out.println("--------------------Fim passo 3");
            passo4();
        }
        else
        {
            paraFase2 = true;
            System.out.println("Não existem mais custos relativos negativos.\nSolução atual é ótima:");
            
            System.out.println("--------------------Fim passo 3");

        }

        
        
    }

    //cálculo da direção simplex
    public void passo4()
    {
        System.out.println("-----------------Início passo 4");
        y = new double[nRestricoes];
        double[] coluna = new double[nRestricoes];
        coluna = pegaColuna(entraNaBase);

        //encontra o y
        for(int i=0; i<nRestricoes; i++)
            for(int j=0; j<nRestricoes; j++)
                y[i]= y[i] + (Binversa[i][j]*coluna[j]);

        System.out.println("Vetor y:");
        for(int i=0; i<nRestricoes; i++)
            System.out.print(y[i]+" ");
        System.out.println();
        
        System.out.println("--------------------Fim passo 4");
        passo5();
    }

    //determinação do passo e variável a sair da base
    public void passo5()
    {
        System.out.println("-----------------Início passo 5");
        int nPositivos = 0;
        double epsilon = 9999;
        int indiceEpsilon = 0;
        double[] conjunto;
        int[] indices;

        //conta números positivos no vetor y
        for(int i=0; i<y.length; i++)
            if(y[i]>0)
                nPositivos++;
        conjunto = new double[nPositivos];
        indices = new int[nPositivos];

        int indiceY=0;
        for(int i=0; i<y.length; i++)
            if(y[i]>0)
            {
                conjunto[indiceY] = xB[i]/y[i];
                indices[indiceY] = i;
                indiceY++;
            }


        if(nPositivos == 0)
        {
            semSolução = true;
            motivoSemSolução = "Problema não tem solução" +
                              "\nótima finita f(x) --> -infinito\n(Fase 2)";
            paraFase1 = true;
            paraFase2 = true;
        }
        else
        {
            for(int i=0; i<conjunto.length; i++)
                System.out.println(xB[indices[i]]+"/"+y[indices[i]]+" = "+conjunto[i]);

            for(int i=0; i<conjunto.length; i++)
                if(conjunto[i]< epsilon)
                {
                    epsilon = conjunto[i];
                    indiceEpsilon = i;
                    saiDaBase = indices[i];
                }
                

            System.out.println("\nMínimo: "+xB[saiDaBase]+"/"+y[saiDaBase]+" = "+conjunto[indiceEpsilon]);
            System.out.println("A variável "+variaveis[saiDaBase] + " sai da base");
            System.out.println("Indice:" + saiDaBase);
            
        System.out.println("--------------------Fim passo 5");
        passo6();  

        }

        
    }

    //atualização: nova partição básica, troque a i-ésima coluna de B
    // pela k-ésima coluna de N
    public void passo6()
    {
        System.out.println("-----------------Início passo 6");
        double aux = 0.0;
        String auxVar;

        //atualiza colunas de Aumentada
            for(int i=0; i<=nRestricoes; i++)
            {
                aux = Aumentada[i][saiDaBase];
                Aumentada[i][saiDaBase] = Aumentada[i][entraNaBase];
                Aumentada[i][entraNaBase] = aux;
            }
        imprimeMatriz(nRestricoes+1, nVariaveis+nRestricoes, Aumentada);

        auxVar = variaveis[saiDaBase];
        variaveis[saiDaBase] = variaveis[entraNaBase];
        variaveis[entraNaBase] = auxVar;

        System.out.println();
        for(int i=0; i<variaveis.length; i++)
            System.out.print(variaveis[i]+" ");
        System.out.println();


        System.out.println("--------------------Fim passo 6");
    }



    public void passo1Fase1()
    {
        System.out.println("\n\n-----------------Início passo 1");
        
        if(indicesFase1.isEmpty())
        {
            System.out.println("Terminou fase1");
            paraFase1 = true;
        }
        else
        {
            B = new double[nRestricoes][nRestricoes];
            Binversa = new double[nRestricoes][nRestricoes];
            xB = new double[nRestricoes];

            //copia os dados da matriz aumentada para B do passo 1
            for(int i=0; i<nRestricoes; i++)
                for(int j=0; j<nRestricoes; j++)
                    B[i][j] = Aumentada2[i][j];


            System.out.println("\nB:");
            for(int i=0; i<nRestricoes; i++)
            {
                for(int j=0; j<nRestricoes; j++)
                        System.out.print(B[i][j]+ " ");
                System.out.println();
            }
            //---------------------------------------------------

            //calcula a inversa de B
                Binversa = inverte(B);
            //---------------------------------------------------

            //calcula o vetor xB
                for(int i=0; i<nRestricoes; i++)
                    for(int j=0; j<nRestricoes; j++)
                        xB[i] = xB[i] + b[j] * Binversa[i][j];
            //---------------------------------------------------
            System.out.println("\nB inversa:");
            for(int i=0; i<nRestricoes; i++)
            {
                    for(int j=0; j<nRestricoes; j++)
                        System.out.print(Binversa[i][j]+ " ");
                System.out.println();
            }

            System.out.println("\nxB:");
            for(int j=0; j<nRestricoes; j++)
                System.out.print(xB[j]+ " ");
            System.out.println();

            System.out.println("--------------------Fim passo 1");

            passo2Fase1();
        }
    }

    //Cálculo dos custos relativos
    public void passo2Fase1()
    {
        System.out.println("-----------------Início passo 2");
        //2.1 vetor multiplicador simplex
        double lambTransp[] = new double[nRestricoes];
        double cTB[] = new double[nRestricoes];
        double cN[];


        cN = new double[nVariaveis+qtdColunasFase1];
        cRelativos = new double[nVariaveis+qtdColunasFase1];


        //copia os custos da matriz aumentada para o cTB do passo 2
        for(int i=0; i<nRestricoes; i++)
            cTB[i] = Aumentada2[nRestricoes][i];


        System.out.println("\ncTB:");
            for(int j=0; j<nRestricoes; j++)
                System.out.print(cTB[j]+ " ");
        System.out.println();

        //----------------------------------------------------------


        //calcula o vetor lambda transposto
            for(int i=0; i<nRestricoes; i++)
                for(int j=0; j<nRestricoes; j++)
                    lambTransp[i] = lambTransp[i] + cTB[j] * Binversa[j][i];
        //----------------------------------------------------------

        System.out.println("\nlambda transposto:");
            for(int j=0; j<nRestricoes; j++)
                System.out.print(lambTransp[j]+ " ");
        System.out.println();


        //2.2 custos relativos
        //copia os custos da matriz aumentada para o cN do passo 2
        for(int i=nRestricoes; i<nRestricoes+nVariaveis+qtdColunasFase1; i++)
            cN[i-nRestricoes] = Aumentada2[nRestricoes][i];

        //----------------------------------------------------------

        //encontra os custos relativos
        for(int i=0; i<cRelativos.length; i++)
            cRelativos[i]= cN[i]-(lambTranspVEZESaNj(lambTransp, pegaColuna(nRestricoes+i)));

        System.out.println("Custos relativos");
        for(int i=0; i<cRelativos.length; i++)
            System.out.print(cRelativos[i]+" ");
        System.out.println("");

        //2.3 determinação da variável a entrar na base
        double menor = 9999;
        for(int i=0; i<cRelativos.length; i++)
            if(cRelativos[i]<menor)
            {
                menor = cRelativos[i];
                entraNaBase = i+nRestricoes;
            }

        System.out.println("A variável: " + variaveis2[entraNaBase]+" entra na base");


        System.out.println("Índice: " + entraNaBase);

        System.out.println("--------------------Fim passo 2");

        passo3Fase1();
    }

    public void passo3Fase1()
    {
        paraFase1=true;
        System.out.println("-----------------Início passo 3");
        //Verifica se ainda existe algum custo relativo negativo
            for(int i=0; i<cRelativos.length; i++)
            {
                if(cRelativos[i]<0)
                    paraFase1 = false;
            }
        System.out.println("--------------------Fim passo 3");
        if(!paraFase1)
        {
            passo4Fase1();
        }
        else
        {
            paraFase1 = true;
            paraFase2 = true;
            semSolução = true;
            motivoSemSolução = "Problema sem solução factível. " +
                              "\nNão existe um candidato a entrar na base\n(Fase 1)";
        }
    }

    //cálculo da direção simplex
    public void passo4Fase1()
    {
        System.out.println("-----------------Início passo 4");
        y = new double[nRestricoes];
        double[] coluna = new double[nRestricoes];
        coluna = pegaColuna(entraNaBase);

        //encontra o y
        for(int i=0; i<nRestricoes; i++)
            for(int j=0; j<nRestricoes; j++)
                y[i]= y[i] + (Binversa[i][j]*coluna[j]);

        System.out.println("Vetor y:");
        for(int i=0; i<nRestricoes; i++)
            System.out.print(y[i]+" ");
        System.out.println();

        System.out.println("--------------------Fim passo 4");
        passo5Fase1();
    }

    //determinação do passo e variável a sair da base
    public void passo5Fase1()
    {
        System.out.println("-----------------Início passo 5");
        int nPositivos = 0;
        double epsilon = 9999;
        int indiceEpsilon = 0;
        double[] conjunto;
        int[] indices;

        //conta números positivos no vetor y
        for(int i=0; i<y.length; i++)
            if(y[i]>0)
                nPositivos++;
        conjunto = new double[nPositivos];
        indices = new int[nPositivos];

        int indiceY=0;
        for(int i=0; i<y.length; i++)
            if(y[i]>0)
            {
                conjunto[indiceY] = xB[i]/y[i];
                indices[indiceY] = i;
                indiceY++;
            }


        if(nPositivos == 0)
        {
            semSolução = true;
            motivoSemSolução = "Problema não tem solução. " +
                               "\nNão existe um candidato a sair da base\n(Fase 1)";
            paraFase1 = true;
            paraFase2 = true;
        }
        else
        {
            for(int i=0; i<conjunto.length; i++)
                System.out.println(xB[indices[i]]+"/"+y[indices[i]]+" = "+conjunto[i]);

            for(int i=0; i<conjunto.length; i++)
                if(conjunto[i]< epsilon)
                {
                    epsilon = conjunto[i];
                    indiceEpsilon = i;
                    saiDaBase = indices[i];
                }


            System.out.println("\nMínimo: "+xB[saiDaBase]+"/"+y[saiDaBase]+" = "+conjunto[indiceEpsilon]);
            System.out.println("A variável "+variaveis2[saiDaBase] + " sai da base");
            System.out.println("Indice:" + saiDaBase);

        }
        System.out.println("--------------------Fim passo 5");
        passo6Fase1();
    }

    //atualização: nova partição básica, troque a i-ésima coluna de B
    // pela k-ésima coluna de N
    public void passo6Fase1()
    {
        System.out.println("-----------------Início passo 6");
        double aux = 0.0;
        String auxVar;

       
            //Atualiza colunas de Aumentada2
            for(int i=0; i<=nRestricoes; i++)
            {
                aux = Aumentada2[i][saiDaBase];
                Aumentada2[i][saiDaBase] = Aumentada2[i][entraNaBase];
                Aumentada2[i][entraNaBase] = aux;
            }
        

        imprimeMatriz(nRestricoes+1, nVariaveis+nRestricoes+qtdColunasFase1, Aumentada2);
        

        auxVar = variaveis2[saiDaBase];
        variaveis2[saiDaBase] = variaveis2[entraNaBase];
        variaveis2[entraNaBase] = auxVar;

        System.out.println();
        for(int i=0; i<variaveis2.length; i++)
            System.out.print(variaveis2[i]+" ");
        System.out.println();

        if(indicesFase1.contains((Integer) saiDaBase))
        {
            indicesNaoIncluir.add(entraNaBase);
            indicesFase1.remove((Integer) saiDaBase);
        }


        System.out.println("--------------------Fim passo 6");
    }

    public String soluçãoFinal()
    {
        String retorno = "";

        if(semSolução)
        {
            retorno = motivoSemSolução;
        }
        else
        {
        retorno += "Matriz B: \n";
            for(int i=0; i<nRestricoes; i++)
            {
                for(int j=0; j<nRestricoes; j++)
                    retorno += Aumentada[i][j] + "    ";
                retorno += "\n";
            }
        retorno += "\n";
        retorno += "Matriz N: \n";
            for(int i=0; i<nRestricoes; i++)
            {
                for(int j=nRestricoes; j<nRestricoes+nVariaveis; j++)
                    retorno += Aumentada[i][j] + "    ";
                retorno += "\n";
            }
        retorno += "\n";

        for(int i=0; i<variaveis.length; i++)
            retorno+= variaveis[i] + " ";

        retorno += "\n";

        retorno += "Solução ótima: \n";
        double solucaoOtima = 0;
        double solucaoParcial = 0;
            for(int i=0; i<xB.length; i++)
            {
                    retorno += String.format("%.2f", xB[i]) + " * " + String.format("%.2f", Aumentada[nRestricoes][i]) + " = ";
                    solucaoParcial += solucaoParcial + (xB[i]*Aumentada[nRestricoes][i]);
                    retorno += String.format("%.2f", solucaoParcial);
                    solucaoOtima += solucaoParcial;
                    solucaoParcial = 0;
                    retorno += "\n";
            }
        retorno += "\n";
        retorno += "f(x) = " + String.format("%.2f", solucaoOtima);

        }

        return retorno;
    }


    public void arrumaMatrizFase2()
    {
        for(Integer c : indicesNaoIncluir)
            System.out.print(c+ " ");

        int coluna=0;
        for(int j=0; j<nRestricoes+nVariaveis+qtdColunasFase1; j++)
        {
            for(int i=0; i<=nRestricoes; i++)
            {
                if(!indicesNaoIncluir.contains((Integer) j))
                {
                    Aumentada[i][coluna]=Aumentada2[i][j];
                    if(i==nRestricoes)
                        coluna++;
                }
                //else
                    //System.out.println("A coluna" + j + "não foi copiada");
            }  
        }

        int indice=0;
        
        for(int i=0; i<variaveis2.length; i++)
        {
            if(!indicesNaoIncluir.contains((Integer) i))
            {
                variaveis[indice]=variaveis2[i];
                    indice++;
            }
        }

        for(int i=0; i<variaveis.length; i++)
        {
            System.out.print(variaveis[i]+" ");
        }

        for(int i=1; i<=nVariaveis; i++)
            for(int j=0; j<variaveis.length; j++)
                if(variaveis[j].equals("x"+i))
                    Aumentada[nRestricoes][j] = backupCustos[nRestricoes+i-1];

        precisaFase1 = false;
        System.out.println("terminou de arrumar a matriz");
        imprimeMatriz(nRestricoes+1, nRestricoes+nVariaveis, Aumentada);

    }

    public void imprimeMatriz(int m, int n, double d[][]) {
        for (int i = 0; i < m; ++i) {
            System.out.println();
            for (int j = 0; j < n; ++j) {
                System.out.print(d[i][j] + " ");
            }
        }
    }

   

    public double[][] inverte(double a[][]) {
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
                                //System.out.println("Dividiu "+i+","+k+" por"+" divisor "+divisor);
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
                           // System.out.println("N:"+a[i][k] +" = " + a[i][k] + "-"+"("+ a[i][k]+"*"+a[j][k]+") = " + (a[i][k] - (a[i][k]*a[j][k])));
                           // System.out.println("I:"+inversa[i][k] +" = " + inversa[i][k] + "-"+"("+ a[i][k]+"*"+inversa[j][k]+") = "+(inversa[i][k] - (a[i][k]*inversa[j][k])+"\n"));

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



            //System.out.println("Normal:");
            //imprimeMatriz(a.length, a);
            //System.out.println();
            //System.out.println("Inversa");
            //imprimeMatriz(inversa.length, inversa);

        return inversa;
    }

    public int getNRestrições() {
        return nRestricoes;
    }

    public void setNRestrições(int nRestrições) {
        this.nRestricoes = nRestrições;
    }

    public int getNVariáveis() {
        return nVariaveis;
    }

    public void setNVariáveis(int nVariáveis) {
        this.nVariaveis = nVariáveis;
    }
}
