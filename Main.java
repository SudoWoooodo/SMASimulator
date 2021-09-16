import java.util.ArrayList;
import java.nio.file.*;
import java.io.*;

//TODO -> Conseguir fazer mais de uma run com seeds diferentes
//TODO -> Zerar as listas depois de cada run
//TODO -> Armazenar o tempo de cada execução e depois fazer a média
//TODO -> Corrigir e retirar detalhes irrelevantes do código
//TODO -> Armazenar o tempo que a fila ficou em cada estado/capacidade
//TODO -> Calcular a probabilidade que a fila ficou em cada estado

public class Main{
    public static void main(String[] args) {

        //Definição de valores do Gerador Aleatório
        int quantSeeds = 5; // Selecione a quantidade de seeds e também quantas vezes o programa irá rodar (>=1)
        int perSeed = 100; // Número de aleatórios por seed
        double a = 1644525;
        double M = Math.pow(2, 32);
        double c = 1013904223;
        
        //Criação dos Registradores 
        ArrayList ListaFinal = new ArrayList<Integer>();
        ArrayList parametros = new ArrayList<Integer>();
        ArrayList tempos = new ArrayList<Integer>();
        ArrayList escalonador = new ArrayList<Integer>();
        ArrayList eventos = new ArrayList<Integer>();
        ArrayList eventosNome = new ArrayList<String>();

        //Definição de seeds
        ArrayList seeds = new ArrayList<Integer>();
        seeds.add(1);  
        seeds.add(2);  
        seeds.add(3);  
        seeds.add(4);
        seeds.add(5); 

        //Chamada de método Gerador
        //ListaFinal = PseudoGenerator(perSeed, a, M, c, ListaFinal);


        //Gerar um .txt com os números
        //gravaTxt(ListaFinal);
        //System.out.println(ListaFinal);

        //Definição de valores fila
        String eventoInicial = "CHEGADAINICIAL";

        parametros.add(0);                // 0fila = 0; -> fila começa em zero
        parametros.add(0.0);             // 1double tempo = 0; //conta o tempo
        parametros.add(1);              // 2int servidores = 1; //num de servidores
        parametros.add(3);             // 3int capacidade = 3; //capacity
        parametros.add(0);            // 4int perda = 0; //perda
        parametros.add(2);           // 5chegadas (
        parametros.add(4);          // 6chegadas )
        parametros.add(3);         // 7atendimento (
        parametros.add(5);        // 8atendimento )
        parametros.add(perSeed); // 9perSeed
        parametros.add(quantSeeds); // 10
        parametros.add(a);      // 11
        parametros.add(M); //12
        parametros.add(c); //13

        //Começa a simulação
        int run = 0;

        while (quantSeeds > 0){

            run = run + 1;

            //define a seed inicial para essa run
            int and = (int) seeds.get(quantSeeds - 1);
            
            //rodar a lista 
            ListaFinal = PseudoGenerator(perSeed, a, M, c, ListaFinal, and);

            System.out.println("\nINFORMACOES RELEVANTES:\n");

            System.out.println("Run: " + run + "\n");
            System.out.println("Utilizando seed: " + and + "\n");
            System.out.println("Primeira Chegada no tempo: " + 3 + " \n");
            System.out.println("Fila com chegadas entre: " + parametros.get(5) + "..." + parametros.get(6) + "\n");
            System.out.println("Fila com atendimento entre: " + parametros.get(7) + "..." + parametros.get(8) + "\n");
            System.out.println("G/G/" + parametros.get(2) + "/" + parametros.get(3));

            interpreter(eventoInicial, ListaFinal, parametros, tempos, escalonador, eventos, eventosNome);

            //fazer um metodo pra zerar as listas e rodar dnv #TODO
            quantSeeds -= 1;
        }
    }

    public static void interpreter (String evento, ArrayList l, ArrayList p, ArrayList t, ArrayList e, ArrayList even, ArrayList evenNm){

        int fila = (int) p.get(0);
        Double tempoaux = (Double) (p.get(1));
        int eventos = even.size();
        int servidores = (int) p.get(2);
        int capacidade = (int) p.get(3);
        int perda = (int) p.get(4);
        int chA = (int) p.get(5);
        int chB = (int) p.get(6);
        int atA = (int) p.get(7);
        int atB = (int) p.get(8);
        int perSeed = (int) p.get(9);
        double a = (Double) p.get(11);
        double M = (Double) p.get(12);
        double c = (Double) p.get(13);

        double tempo = 0;

        if (eventos < perSeed){
            
            if (l.size() != 1){

            switch(evento){

                case "CHEGADA":{

                    double sorteio = rnd(chA, chB, (double) l.get(0));
                    tempo = tempoaux + sorteio;

                    e.add(tempo);
                    evenNm.add(evento);

                    even.add(0); // gasta um aleatório
                    l.remove(0);

                    System.out.println(" Chegada agendada <<<< " + " " + tempo + " \n");
                } break;

                case "SAIDA":{
                    
                    double sorteio = rnd(atA, atB, (double) l.get(0));
                    tempo = tempoaux + sorteio;

                    e.add(tempo);
                    evenNm.add(evento);

                    even.add(0); // gasta um aleatório
                    l.remove(0);

                    System.out.println(" Saida agendada >>>> " + tempo + " \n");
                } break;

                case "CHEGADAINICIAL":{

                    System.out.println("\n<><><><>   INICIANDO SIMULACAO  <><><><> \n" );

                    double sorteio = 0;
                    tempo = tempoaux + sorteio;

                    e.add(3.0); //primeiro tempo
                    evenNm.add("CHEGADA");

                    System.out.println("\n<<<< Chegada Inicial Agendada <<<<\n" );

                    escalonador(0, l, p, t, e, even, evenNm);

                } break;

                case "SOLICITA":{

                    int i = 0;
                    Double menor =  (Double) e.get(0);
                    int pos = 0;

                    while (i < e.size()){

                    if ( (Double) e.get(i) < menor){
                            menor = (Double) e.get(i);
                            pos = i;
                            i = 0;
                    } else {
                        i++;
                        }
                    }
                    System.out.println("PROXIMO EVENTO: " + "\n");
                    System.out.println("Escalonador" + "[" + pos + "] " + evenNm.get(pos) + " --- " + menor.doubleValue() + "\n");

                    escalonador(pos, l, p, t, e, even, evenNm);
                } break;
            } 
            } else {
                
                 l = PseudoGenerator(perSeed, a, M, c, l, (int) Math.round(((Double)l.get(0))));

            }
        }
    }

    public static void escalonador (int pos, ArrayList l, ArrayList p, ArrayList t, ArrayList e, ArrayList even, ArrayList evenNm){

        int fila = (int) p.get(0);
        Double tempo = (double) p.get(1);
        int eventos = even.size();
        int servidores = (int) p.get(2);
        int capacidade = (int) p.get(3);
        int perda = (int) p.get(4);

            switch(evenNm.get(pos).toString()){

                case "CHEGADA":{

                    System.out.println("<<<< Chegada Realizada <<<<\n");

                    if ( fila < capacidade ){
                          fila++;
                          p.set(0, fila);
                        if( fila <= (servidores)){
                            System.out.println(">>>> Agendando Saida >>>> \n");
                             interpreter("SAIDA", l, p, t, e, even, evenNm);
                             }
                        } else {
                            perda++;
                            p.set(4, perda);
                        }
                        System.out.println("<<<< Agendando Chegada <<<< \n");

                        interpreter("CHEGADA", l, p, t, e, even, evenNm);
                } break;

                case "SAIDA":{

                    System.out.println("\n >>>> Saida Realizada >>>> \n");

                    fila--;
                    p.set(0, fila);
                    if (fila >= servidores){
                        System.out.println(">>>> Agendando Saida >>>> \n");
                        interpreter("SAIDA", l, p, t, e, even, evenNm);
                    }
                } break;
            }

                Double soma = (double) e.get(pos);
                p.set(1, tempo + soma);
                e.remove(pos);
                evenNm.remove(pos);

                //Prints
                System.out.println("ESTADO DO ESCALONADOR: \n");

                for (int i = 0; i < e.size(); i++){
                    System.out.println(" Escalonador" + "[" + i + "]" + "----> " + evenNm.get(i) + " --------- " + e.get(i));
                }
                
                System.out.println("\nESTADOS GERAIS: \n");

                System.out.println("Numero de Eventos: " + p.get(0) + "\n"); 
                System.out.println("Tempo Total: " + p.get(1) + "\n"); 

                interpreter("SOLICITA", l, p, t, e, even, evenNm);
    }

    public static double rnd(int a, int b, double ru){

        double aux = (b - a) * ru + a;

        return aux;
    }


    public static ArrayList PseudoGenerator(int perSeed, double a, double M, double c, ArrayList L, int seed){

        if (perSeed > 100){
            perSeed = 100;
        }

        double qseed = seed;
        double pseed = 0;
        double uni = 0; 

        while( L.size() < perSeed ){
            
            pseed = qseed;
            qseed = (a * pseed + c) % M;
            uni = qseed/M;
            L.add(uni);

        }

        return L;
    }

    public static void gravaTxt(ArrayList L){

		File file = new File("Lista.txt");
		String nome = "";
		String conteudo;
        try	{
	        FileWriter f = new FileWriter (file, true);
	 
	        for(int i = 0; i < L.size(); i++){
	        	
	        	nome = String.valueOf(L.get(i)); // chama o atributo do objeto na posição i
                                conteudo = nome;
	        	conteudo += "\r\n";
	        	f.write(conteudo);
	        	
	         }
	        
	         f.close();

	    }catch (IOException e)  {
	        e.printStackTrace();
	    }
	}

}