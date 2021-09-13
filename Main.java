import java.util.ArrayList;
import java.nio.file.*;
import java.io.*;

public class Main{
    public static void main(String[] args) {

        //Definição de valores do Gerador Aleatório
        double seed = 21; //precisa ser random TODO5
        double a = 1644525;
        double M = Math.pow(2, 32);
        double c = 1013904223;
        ArrayList ListaFinal = new ArrayList<Integer>();
        ArrayList parametros = new ArrayList<Integer>();
        ArrayList tempos = new ArrayList<Integer>();
        ArrayList escalonador = new ArrayList<Integer>();
        ArrayList eventos = new ArrayList<Integer>();
        ArrayList eventosNome = new ArrayList<String>();

        //Chamada de método Gerador
        ListaFinal = PseudoGenerator(seed, a, M, c, ListaFinal);


        //Gerar um .txt com os números
        //gravaTxt(ListaFinal);
        //System.out.println(ListaFinal);

        //Definição de valores fila
        String eventoInicial = "CHEGADAINICIAL";
        parametros.add(0); // 0fila = 0; -> fila começa em zero
        parametros.add(0.0); // 1double tempo = 0; //conta o tempo
        parametros.add(1); // 2int servidores = 1; //num de servidores
        parametros.add(3); // 3int capacidade = 3; //capacity
        parametros.add(0); // 4int perda = 0; //perda
        parametros.add(2); // 5chegadas (
        parametros.add(4); // 6chegadas )
        parametros.add(3); // 7atendimento (
        parametros.add(5); // 8atendimento )

        //Começa a simulação
        interpreter(eventoInicial, ListaFinal, parametros, tempos, escalonador, eventos, eventosNome);
    }

    public static void interpreter (String evento, ArrayList l, ArrayList p, ArrayList t, ArrayList e, ArrayList even, ArrayList evenNm){

        int fila = (int) p.get(0);
        Double tempoaux = (Double) (p.get(1));
        int eventos = even.size();
        int servidores = (int) p.get(2);
        int capacidade = (int) p.get(3);
        int perda = (int) p.get(4);
        int chA = 2;
        int chB = 4;
        int atA = 3;
        int atB = 5;
        double tempo = 0;

        if (eventos < l.size()){

            switch(evento){

                case "CHEGADA":{

                    double sorteio = rnd(chA, chB, (double) l.get(eventos));
                    tempo = tempoaux + sorteio;

                    e.add(tempo);
                    evenNm.add(evento);

                    even.add(0); // gastpu um aleatório

                    System.out.println("\n Chegada agendada -> " + " " + tempo);
                } break;

                case "SAIDA":{
                    
                    double sorteio = rnd(atA, atB, (double) l.get(eventos));
                    tempo = tempoaux + sorteio;

                    e.add(tempo);
                    evenNm.add(evento);

                    even.add(0); // gastou um aleatório

                    System.out.println("\n Saida agendada ->" + tempo + " \n");
                } break;

                case "CHEGADAINICIAL":{

                    double sorteio = 0;
                    tempo = tempoaux + sorteio;

                    e.add(3.0); //primeiro tempo
                    evenNm.add("CHEGADA");

                    System.out.println("\n Primeira Chegada -> " + 3 + " \n");

                    escalonador(0, l, p, t, e, even, evenNm);

                    interpreter("SOLICITA", l, p, t, e, even, evenNm);
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

                    System.out.println("O menor é: " + menor.doubleValue() + " na posição: " + pos);

                    escalonador(pos, l, p, t, e, even, evenNm);
                } break;
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

            switch((String) evenNm.get(pos)){

                case "CHEGADA":{

                    System.out.println("\n Chegada Realizada");

                    if ( fila < capacidade ){
                          fila++;
                          p.set(0, fila);
                        if( fila <= (servidores)){
                             interpreter("SAIDA", l, p, t, e, even, evenNm);
                             }
                        } else {
                            perda++;
                            p.set(4, perda);
                        }

                        interpreter("CHEGADA", l, p, t, e, even, evenNm);
                } break;

                case "SAIDA":{

                    System.out.println("\n Saida Realizada");

                    fila--;
                    p.set(0, fila);
                    if (fila >= servidores){
                        interpreter("SAIDA", l, p, t, e, even, evenNm);
                    }
                } break;
            }

                Double soma = (double) e.get(pos);
                p.set(1, tempo + soma);
                e.remove(pos);
                evenNm.remove(pos);
                System.out.println(e);
                System.out.println(evenNm);
                System.out.println(p);

                interpreter("SOLICITA", l, p, t, e, even, evenNm);
    }

    public static double rnd(int a, int b, double ru){

        double aux = (b - a) * ru + a;

        return aux;
    }


    public static ArrayList PseudoGenerator(double seed, double a, double M, double c, ArrayList L){

        double pseed = 0;
        double uni = 0;

        while( L.size() < 999 ){
            
            pseed = seed;
            seed = (a * pseed + c) % M;
            uni = seed/M;
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