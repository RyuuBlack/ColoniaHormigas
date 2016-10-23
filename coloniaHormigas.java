import java.util.*;
import java.text.DecimalFormat;

public class  coloniaHormigas {
	static DecimalFormat formateador = new DecimalFormat("0.00");
	public static Scanner leer;
	static int costo=0;
		public static void main(String[] args) {
			
			leer = new Scanner(System.in);
			int[][] Red={{ 0, 4, 6, 3,-1,-1,-1,-1,-1,-1,-1},
					     { 4, 0, 5,-1, 3,-1,-1,-1,-1,-1,-1},
					     { 6, 5, 0, 4, 2, 2, 5,-1,-1,-1,-1},
					     { 3,-1, 4, 0,-1,-1, 6,-1,-1,-1,-1},
					     {-1, 3, 2,-1, 0, 2,-1, 4,-1,-1,-1},
					     {-1,-1, 2,-1, 2, 0, 1, 2, 5,-1,-1},
					     {-1,-1, 5, 6,-1, 1, 0,-1, 2, 5,-1},
					     {-1,-1,-1,-1, 4, 2,-1, 0, 2,-1, 7},
					     {-1,-1,-1,-1,-1, 5, 2, 2, 0, 3, 8},
					     {-1,-1,-1,-1,-1,-1, 5,-1, 3, 0, 4},
					     {-1,-1,-1,-1,-1,-1,-1, 7, 8, 4, 0}};// Red de transporte
			
			double [][] feromona = new double[Red.length][Red.length];
			int Num_hormigas; // Número de hormigas que se van a pasar por la red para determina el costo minimo
			int nodoFinal;
			
			
			Mostrar_red(Red); 
			
			System.out.println("Definir el número de hormigas");
			Num_hormigas = leer.nextInt();	
			
			System.out.println("Escribir Nodo final");
			nodoFinal = leer.nextInt();
			
			nodoFinal-=1;
			
			  // iniciar martiz feromona
	   		for (int i = 0; i < feromona.length; i++) {
	   			
	   			for (int j = 0; j < feromona.length; j++) {
	   				feromona[i][j]=1;
	   			}
	   			
	   		}	
			
			for (int i = 0; i < Num_hormigas; i++) {
				
		    System.out.println("Hormiga "+(i+1));
				recorrer(Red,nodoFinal,feromona);
				System.out.println();
				
			}
			
			int nodo_sol = 0;
			System.out.println("Camino Final");
			double mayor_temp;
			int temp_index_sol;
			int costo_final=0;
			int columna=0;
			while(nodo_sol != nodoFinal)
			{
				System.out.print((nodo_sol+1) + " --> ");
				
				mayor_temp = feromona[nodo_sol][0];
				temp_index_sol = 0;
				
				for(int i = 1; i < feromona.length; i++)
				{
					if(feromona[nodo_sol][i] > mayor_temp)
					{
						mayor_temp = feromona[nodo_sol][i];
						temp_index_sol = i;
						columna=i;
					}	
				}
				costo_final+=Red[nodo_sol][columna];
				nodo_sol = temp_index_sol;
			}
			System.out.println(11);
			System.out.println("Costo Final del camino "+costo_final);
			
			
	}
	
public static void Mostrar_red(int Red[][]){
	System.out.println("Red de transporte");
	
	for (int i = 0; i < Red.length; i++) {  // Muestra la Red de transporte 
	
		for (int j = 0; j < Red.length; j++) {
			System.out.print(Red[i][j]+"\t");
		}
		System.out.println();
	}
	
	System.out.println();
}

public static void recorrer(int Red[][], int nodoFinal, double feromona[][]){

	// nodo de la red 
	int nodo=0;     
	// nodos que ha visitado la hormiga 
	ArrayList<Integer> Nodos_visitados = new ArrayList<Integer>();
	// red temporal para recorrido de las hormigas
	int [][] red_temporal = new int[Red.length][Red.length];
	double feromona_antes[][]= new double [feromona.length][feromona.length];
	
	    
	       //Genera copia temporal
	   	   clonar_Red(Red,red_temporal); 
	   	   clonar_feromona(feromona,feromona_antes);
	      
	 System.out.print("Ruta 1"); 
	 Nodos_visitados.add(0);
	while(nodo != nodoFinal){
	   				
		nodo = elegir_camino(red_temporal,nodo,feromona,feromona_antes,Red,Nodos_visitados);
		
		if (nodo==-1) {
			System.out.print("Hormiga muerta");
			break;
		}
		System.out.print(+(nodo+1));
		
	} //End while
	System.out.println();
	System.out.println("costo del camino "+costo);
	costo=0;
	Nodos_visitados.clear();	
	}

public static void  clonar_Red(int Red[][],int red_temporal[][]){
	
	for (int i = 0; i < red_temporal.length; i++) {
		
		for (int j = 0; j < red_temporal.length; j++) {
			red_temporal[i][j]=Red[i][j];
		}
		
	}
	
}

public static void clonar_feromona(double feromona[][],double feromona_antes[][]){
	for (int i = 0; i < feromona.length; i++) {
			
		for (int j = 0; j < feromona.length; j++) {
			feromona_antes[i][j]=feromona[i][j];
		}
			
		}

}

public static int elegir_camino(int red_temporal[][], int nodo, double feromona[][],double feromona_antes[][],int Red[][],ArrayList<Integer>Nodos_visitados){
		
		double evaluacion=0;
		double random;    
		Random r = new Random(); 
		ArrayList<Integer> NodosBuenos = new ArrayList<Integer>();
		double feromonas=0;
		int nodo_siguiente = -1;
		double variacion_feromona=0;
		int indice;
		System.out.print(" --> ");
		//Obtener nodos viables
		
		
		for (int j = 0; j < red_temporal.length; j++) {
			
			if (red_temporal[nodo][j]>0) {
				NodosBuenos.add(j); // agrega la posición del nodo bueno  
			}	
			
		}
		
		
		//Sumar el total de feromona de los nodos viables, para la posterior normalización
		for (int i = 0; i < NodosBuenos.size(); i++) {
			evaluacion+=feromona[nodo][NodosBuenos.get(i)];
		}
		
		
		random=r.nextDouble();
		
		
		for (int i = 0; i < NodosBuenos.size(); i++) {
		
			feromonas += (feromona[nodo][NodosBuenos.get(i)] / evaluacion);
		
			
			//Si el random está en el rango
			if ( random < feromonas ) {
					nodo_siguiente = NodosBuenos.get(i);
				break;
			}	
		}
		
		Nodos_visitados.add(nodo_siguiente);
		// eliminar de la red temporal la columna actual
		for (int i = 0; i < red_temporal[nodo].length; i++) {  
			red_temporal[i][nodo]=-1;
		}
		
		NodosBuenos.clear();
		
		if (nodo_siguiente==-1) {
		   
		}
		else{
			variacion_feromona=(Red[nodo][nodo_siguiente]*0.01);
		}
		
		if (nodo_siguiente==-1) {
			
			for (int i = 0; i < feromona.length; i++) {
				
				for (int j = 0; j < feromona.length; j++) {
					
					feromona[i][j]=feromona_antes[i][j];
					
				}
				
			}
			
		}
		else{
			
			for (int i = 1; i < Nodos_visitados.size(); i++) {
				
				if (Nodos_visitados.get(i-1)==nodo && Nodos_visitados.get(i)==nodo_siguiente) {
					
					feromona[Nodos_visitados.get(i-1)][Nodos_visitados.get(i)]+=1;
					
				}
				else{
					
					feromona[Nodos_visitados.get(i-1)][Nodos_visitados.get(i)]-=variacion_feromona;
					
				}
				
			}
			
		}
		if(nodo_siguiente==-1){
			
		}
		else{
		indice=Red[nodo][nodo_siguiente];
		costo=costo + indice;
		}
		nodo=nodo_siguiente;
		
		return nodo_siguiente;
	}

}