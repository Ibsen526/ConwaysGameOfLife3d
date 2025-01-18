import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.Vector;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Panel extends JPanel {
	private static final long serialVersionUID = 2L;
	
	private final int PANEL_WIDTH = 1200; //680 = 17 * 40
	private final int PANEL_HEIGHT = 580; //680
	private Random rand;
	
	private short startX;
	private short startY;
	
	private final short arrLength = 110;
	private short[] arrVal = new short[arrLength];
	
	//private Timer timer;

	private int runI; //Counter variables for sorting
	private int runJ; 

	//Quick
	private Vector<Integer> vecSma = new Vector<Integer>();
	private Vector<Integer> vecBig = new Vector<Integer>();
	private int[] piStEn = new int[3]; // pivot, start, end
	private int lastPiv;
	
	//Radix
	private String[] arrStr = new String[arrLength];
	//private Vector<String>[] tArr = new Vector[10];
	private ArrayList<String>[] tArr = new ArrayList[10];
	private int strSize;
	
	//Shaker 
	private Boolean state;
	private Boolean swapped;
	
	//Merge
	private Vector<Short> vecLeft = new Vector<Short>();
	private Vector<Short> vecRight = new Vector<Short>();
	private int stepSize;
	
	//private JPanel PanBut;
	//private JPanel PanGra;

	Panel () {
		//Panel = new JPanel(new GridLayout(1,7));
		this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		this.setBounds(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
		this.setLayout(new FlowLayout());

		//calendar = Calendar.getInstance();
		//date = new Date(0);
		rand = new Random();
		//timer = new Timer();
		
		init();
		
		for (int i = 0; i < arrLength; i++) {
			arrVal[i] = 20;
		}
		arrVal[10] = 50;
		
		startX = 52;
		startY = PANEL_HEIGHT-2*50;
		
		repaint();
	}
	
	public void init() {
		runI = 0;
		runJ = 0;
		
	//For Quick sort
		lastPiv = 0;
		piStEn[0] = 0; // pivot
		piStEn[1] = 0; // start
		piStEn[2] = arrLength - 1; //end
	    vecSma.add(piStEn[1]);
	    vecBig.add(piStEn[2]);
	    
	//For Radix Sort
	    //Initialize the array of the list
	    for (int i = 0; i < 10; i++) {
			tArr[i] = new ArrayList<String>();
		}
	    
	//For Shaker Sort
	    state = false;
	    swapped = false;
	    
	//For Merge Sort
	    stepSize = 1;
	}
	
	public void paint(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;
		
		g2D.setColor(Color.white);
		g2D.fillRect(0,0,PANEL_WIDTH,PANEL_HEIGHT-90);

		g2D.setColor(Color.black);
		g2D.drawRect(50, 50, PANEL_WIDTH-2*50, PANEL_HEIGHT-3*50);
		
		for (int i = 0; i < arrLength; i++) {
			g2D.fillRect(startX+i*10, startY-arrVal[i]*5, 7, arrVal[i]*5);
		}
	}


	public Boolean comp(MouseEvent arg0, JButton[] arrButton) {
		short temp = 0;
		if (arg0.getComponent() == arrButton[0]) {
			//---  Selection Sort  ---
				//Suitable for small arrays
			
			int indSwap = runI;
			for(int j = runI; j < arrLength; j++) {
				if(arrVal[j] < arrVal[indSwap]) {
					indSwap = j;
				}
			}
			
			temp = arrVal[runI];
			arrVal[runI] = arrVal[indSwap];
			arrVal[indSwap] = temp;

			runI++;

			repaint();
			
			if(runI == arrLength) {
				runI = 0;
				runJ = 0;
				return true;
			}
			
		}
		else if (arg0.getComponent() == arrButton[1]) {
			//---  Insertion Sort  ---
				//Suitable for small arrays and a bit better than Selection

			runI++;

			if(runI >= arrLength)
				return true;

			int tInd = runI;
			
			for (int i = runI - 1; i >= 0; i--) {
				if(arrVal[tInd] < arrVal[i]) {
					//Swap the current value and arrVal[i]; arrVal gets pushed back one and current one ahead
					short t = arrVal[i];
					arrVal[i] = arrVal[tInd];
					arrVal[tInd] = t;
					tInd = i;
				}
				else break; //Break the loop, cause if the first isnt smaller then the rest is also bigger
			}
			
			repaint();
		}
		else if (arg0.getComponent() == arrButton[2]) {
			//---  Quick Sort  ---
				//Fast in big arrays
			
	        // Sort in the current part of the array, but only if it contains something
	        if (vecSma.size() != 0) {
	            lastPiv = piStEn[0]; // Save the last pivot, for indentification of the unchanged values
	            
	            piStEn[1] = (int) vecSma.get(vecSma.size() - 1);
	            piStEn[2] = (int) vecBig.get(vecBig.size() - 1);
	            piStEn[0] = partition(arrVal, piStEn[1], piStEn[2]);

	            // Delete the vector parts that are already done
	            vecSma.remove(vecSma.size()-1);
	            vecBig.remove(vecBig.size()-1);
	        }
	        else
	            return true;

	        // Create new vector entries
	        if (piStEn[1] < piStEn[2] && lastPiv != piStEn[0]) { // If start < end AND the last and current pivot arent the same
	            // Splits a successfull swap in to new arrays
	            vecSma.add(piStEn[1]);
			    vecBig.add(piStEn[0]);
	            vecSma.add(piStEn[0]);
			    vecBig.add(piStEn[2]);
	        }
	        else if (piStEn[1] < piStEn[2] && lastPiv == piStEn[0]) { // If start < end AND the last and current pivot arent the same
	            // Decimate an unchanged array in a smaller array without the unchanged value
	            vecSma.add(piStEn[1]);
			    vecBig.add(piStEn[2] - 1);
	        }
			
		    repaint();
		}
		else if (arg0.getComponent() == arrButton[3]) {
			//---  Heap Sort  ---
				/* Small memory usage.
				 * This is an iterativ implementation, the same goes for quick. There is 
				 * also an rekursive, but for Java Swing this is harder to accomplish
				 * because the paint-Function is called after the Sort is done.
				 */

		    if (runI >= arrLength) { //If all elements of the array are at the right place
		    	return true;
		    }
		    
	        Boolean endCond = false;
	        int indPar = 0;

	        //Build a max heap
	        while (!endCond) {

	            for (int i = indPar * 2 + 1; i < indPar * 2 + 3; i++) { // i darf nur 1 oder 2 sein, da jeder parent nur 2 children hat
	                if (i >= arrLength - runI) {
	                    endCond = true;
	                    break;
	                }
	                if (arrVal[indPar] < arrVal[i]) {
	                    short t = arrVal[indPar];
	                    arrVal[indPar] = arrVal[i];
	                    arrVal[i] = t;
	                }
	            }

	            if (arrVal[(indPar - 1) / 2] < arrVal[indPar])  //If grandparent < getauschtes element
	            {
	            	//If something is swapped and the new value is also smaller than the grandparent
	                indPar = (indPar - 1) / 2; //Nimmt den vorletzten Parent
	            }
	            else 
	                indPar++; // Nächstes Parent Element

	        }

	        //Swap the first and last digit of heap
	        short t = arrVal[0];
	        arrVal[0] = arrVal[arrLength - runI - 1];
	        arrVal[arrLength - runI - 1] = t;

	        runI++; //Counter for how many values are in the right place
			
			repaint();
		}
		else if (arg0.getComponent() == arrButton[4]) {
			//---  Radix Sort (LSD)  ---
				//Not very good to illustate, because it only has n Rekursions (n = length of max value)
				//With max of 86 -> n = 2 -> only 2 Rekursions
			
		//Also possible, but not as insightful to the viewer
			//Initialize the sort array on the first run
		    /*if(runI == 0) {
			    //Convert int arr to string arr
			    for (int i = 0; i < arrLength; i++) {
			        arrStr[i] = String.valueOf(arrVal[i]);
			        if (arrStr[i].length() > strSize)
			            strSize = arrStr[i].length();
			    }

			    //Expand the values onto the largest size
			    for (int i = 0; i < arrLength; i++)
			        while (arrStr[i].length() < strSize) //Inserts a 0, if the size of the Value is smaller than the max size until both are equal
			        	arrStr[i] = "0" + arrStr[i];
		    }
		    
	        //Clear the vector
	        for (int i = 0; i < 10; i++)
	            tArr[i].clear();

	        //Place the array 
	        for (int i = 0; i < arrLength; i++) {
	            //From the vector get the currently investigated value, but only the digit which is currently in use. 
	            //"- '0'" is to convert the char to an int
	            //Then push the value of the array into the vector
	            tArr[arrStr[i].charAt(strSize - runI - 1) - '0'].add(arrStr[i]);
	        }
	        
	        //Push temporary array back into string array
	        int arrInd = 0;
	        for (int i = 0; i < 10; i++) {
	            int j = 0;
	            while (j < tArr[i].size()) {
	                arrStr[arrInd] = tArr[i].get(j);
	                j++;
	                arrInd++;
	            }
	        }
	        
	        runI++; 
	        
		    //Convert string array back into integers
		    for (int i = 0; i < arrLength; i++)
		    	arrVal[i] = Short.parseShort(arrStr[i]);

	    	repaint();
	    	
	    	//End condition is met
		    if (runI >= strSize)
			    return true;*/
		    
		    
		    
		    
	    //More or less In place, better for visualisation
			
		    //Initialize the sort array on the first run
		    if(runI == 0) {
			    //Convert int arr to string arr
			    for (int i = 0; i < arrLength; i++) {
			        arrStr[i] = String.valueOf(arrVal[i]);
			        if (arrStr[i].length() > strSize)
			            strSize = arrStr[i].length();
			    }

			    //Expand the values onto the largest size
			    for (int i = 0; i < arrLength; i++)
			        while (arrStr[i].length() < strSize) //Inserts a 0, if the size of the Value is smaller than the max size until both are equal
			        	arrStr[i] = "0" + arrStr[i];
		    }

	        //Place the array 
	        tArr[arrStr[runJ].charAt(strSize - runI - 1) - '0'].add(arrStr[runJ]);

	        //Push temporary array back into string array
	        int arrInd = 0;
	        for (int i = 0; i < 10; i++) {
	            int j = 0;
	            while (j < tArr[i].size()) {
	                arrStr[arrInd] = tArr[i].get(j);
	                j++;
	                arrInd++;
	            }
	        }
	        
	        runJ++;

	        if(runJ == arrLength) { 
	        	runI++; 
	        	runJ = 0;
		        for (int i = 0; i < 10; i++)
		            tArr[i].clear();
	        }
	        

		    //Convert string array back into integers
		    for (int i = 0; i < arrLength; i++)
		    	arrVal[i] = Short.parseShort(arrStr[i]);

	    	repaint();
	    	
	    	//End condition is met
		    if (runI >= strSize)
			    return true;
		    
		}
		else if (arg0.getComponent() == arrButton[5]) {
			//---  Cocktail Shaker Sort  ---
			
			if(!state) {
				if(runI == 0) { 
					swapped = false;
				}
				
				//Swap adjacent cells, if the first is bigger than the second
				if(arrVal[runI] > arrVal[runI+1]) {
					short t = arrVal[runI];
					arrVal[runI] = arrVal[runI+1];
					arrVal[runI+1] = t;
					swapped = true;
				}

				runI++; //Forward pass
				
				//The end of the array is reached
				if(runI == arrLength - 1) {
					runI--;
					state = true;
					//End condition is met, because nothing in the stage has swapped
					if(swapped == false)
						return true;
				}
			}
			else {
				if(runI == arrLength-2) {
					swapped = false;
				}
				
				if(arrVal[runI] > arrVal[runI+1]) {
					short t = arrVal[runI];
					arrVal[runI] = arrVal[runI+1];
					arrVal[runI+1] = t;
					swapped = true;
				}

				runI--; //Backward pass
				
				if(runI == 0) {
					state = false;
					//End condition is met, because nothing in the stage has swapped
					if(swapped == false)
						return true;
				}

			}
			
			repaint();
		}
		else if (arg0.getComponent() == arrButton[6]) {
			//---  Merge Sort  ---
			
	        int count = 0;

            while (count < arrLength-1) 
            {
                vecLeft.clear();
                vecRight.clear();

                //Push left array into the list
                int tMax = count + stepSize;
                while (tMax > arrLength) //Change the max size, if the vector cant be filled completely
                    tMax--;
                
                for (int i = count; i < tMax; i++) // + stepSize
                	vecLeft.add(arrVal[i]);

                //Push right array into the list
                tMax = count + 2 * stepSize;
                while (tMax > arrLength)
                    tMax--;

                for (int i = count + stepSize; i < tMax; i++) 
                	vecRight.add(arrVal[i]);

                
                int i = 0, j = 0, k = count; //counterLeft, counterRight, counterArray

                //Merges the main parts, but leaves one side partially completed,
                //which is handled in the while-loops below
                while (i < vecLeft.size() && j < vecRight.size()) {
                    if (vecLeft.get(i) < vecRight.get(j)) {
                        arrVal[k] = vecLeft.get(i);
                        i++; k++;
                    }
                    else {
                    	arrVal[k] = vecRight.get(j);
                        j++; k++;
                    }
                }

                //Merge the left over parts (just write it in, because the half is already sorted)
                while (i < vecLeft.size()) {
                	arrVal[k] = vecLeft.get(i);
                    i++; k++;
                }

                //Merge the left over parts
                while (j < vecRight.size()) {
                	arrVal[k] = vecRight.get(j);
                    j++; k++;
                }

                count += 2*stepSize;
            }
            
            stepSize *= 2;
	        
			repaint();
			
			//End condition
	        if (stepSize >= arrLength-1) 
	        	return true;
	        	
		}
		else if (arg0.getComponent() == arrButton[7]) {
			System.out.println("Generate Values");
			
			for (int i = 0; i < arrLength; i++) {
				arrVal[i] = (short) (rand.nextInt(85)+1);
			}

			repaint();
		}
		
		return false;
	}
	
	private int partition(short arr[], int low, int high)
	{
	    int pivVal = arr[high]; // Value of the pivot element 
	    int smallest = (low - 1); // Index of smaller element and indicates the most right position of pivot found so far

	    for (int j = low; j <= high - 1; j++)
	    {
	        // If current element is smaller than the pivot, change the 'smallest' index and swap the new smallest with the checked value
	        if (arr[j] <= pivVal)
	        {
	            smallest++; // Increment index of smaller element 
	            //swap(&arr[indNow], &arr[j]);

	            short t = arr[smallest]; 
	            arr[smallest] = arr[j];
	            arr[j] = t;
	        }
	    }
	    //swap(&arr[indNow + 1], &arr[high]);

	    short t = arr[smallest + 1];
	    arr[smallest + 1] = arr[high];
	    arr[high] = t;

	    return (smallest + 1); //Return the smallest + 1 as it is the index one before the new pivot
	}
	
}
