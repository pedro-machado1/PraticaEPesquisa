import java.util.ArrayList;

public class CodeStructWithErrror extends CodeStruct
{
	public int recCr[][] = new int[4][3];
	public int recPr[] = new int[4];

	public int sCr[][] = new int[4][3];
	public int sPr[] = new int[4];
	public int sCrq[] = new int[4];

	public int Aux[] = new int[4];
	public int Aux2[] = new int[4];
 	public int EAr[][] = new int[4][4];
	public boolean SEr[] = new boolean[4];
	public boolean DEr[] = new boolean[4];

	public CodeStructWithErrror(CodeStruct initialCodeStruct, int[] errorPattern) throws Exception {
		super(initialCodeStruct.D);
		setErrorPattern(errorPattern);
		recomputeControlVariables();
	}
	public void recomputeControlVariables() {
		recomputeCheckBitsAndParity();
		computeSyndromes();
		computeErrorAddress();
		computeSE_DE();
	}
	public void recomputeCheckBitsAndParity() {
		for(int k=0; k<4; k++) {
			recCr[k][0] = D[k][1] ^ D[k][2] ^ D[k][3];
			recCr[k][1] = D[k][0] ^ D[k][2] ^ D[k][3];
			recCr[k][2] = D[k][0] ^ D[k][1] ^ D[k][3];
		}
		for (int c=0; c<4; c++){
			recPr[c] =  D[0][c] ^ D[1][c] ^ D[2][c] ^ D[3][c];
		}
	}
	public void computeSyndromes() {
		for(int k=0; k<4; k++) {
			sPr[k] = Pr[k] == recPr[k] ? 0 : 1;
			for(int t=0; t<3; t++)
				sCr[k][t] = Cr[k][t] == recCr[k][t] ? 0 : 1;
		}
		for(int k=0; k<4; k++)
			sCrq[k] = (sCr[k][0]==1 || sCr[k][1]==1 || sCr[k][2]==1) ? 1 : 0;
	}
	public void computeErrorAddress() {
		int count = 0; int c = 0;
		ArrayList<Integer> arrayC = new ArrayList<>();
		ArrayList<Integer> arrayP = new ArrayList<>();
		for (int k = 0; k < 4; k++) {
			Aux[k] = sCr[k][0] * 4 + sCr[k][1] * 2 + sCr[k][2];
			if (Aux[k] != 0) {
				count++;
				arrayC.add(k);
			}
		}
		for (int i = 0; i < 4; i++) {
			if (sPr[i] == 1) arrayP.add(i);
		}

		if (count == 0){
//			errro no checkbit;

		}
		if (count == 1){
			for (int k : arrayP) {
				k=mapParityToPosition(k);
				EAr[arrayC.getFirst()][c] = k;
				c++;
			}

		}
		if (count == 2) {
			if (arrayP.isEmpty()) {
				for (int i = 0; i < 2; i++) {
					EAr[arrayC.get(i)][0] = Aux[arrayC.get(i)];
				}
			}
			if (arrayP.size() == 1) {
				// chute
				EAr[arrayC.get(0)][0] = Aux[arrayC.get(0)];
				EAr[arrayC.get(1)][0] = Aux[arrayC.get(0)];
				int k = arrayP.get(0);
					k=mapParityToPosition(k);
					EAr[arrayC.get(1)][1] = k;
			}
			if (arrayP.size() == 2) {
				for (int i = 0; i < 4; i++) {
					EAr[i][0] = Aux[i];
				}
			}
			if (arrayP.size() == 3) {
				// chute
				for (int k : arrayP) {
					k = mapParityToPosition(k);
					if (c == 0) {
						EAr[arrayC.get(0)][0] = k;
					}
					if (c == 1) {
						EAr[arrayC.get(0)][1] = k;
					}
					if (c == 2) EAr[arrayC.get(1)][0] = k;
					c++;
				}
			}
			if  (arrayP.size() == 4) {
				// chute (6 casos)
//				int n1 = Aux[arrayC.get(0)] ;
				for (int k : arrayP) {
					k = mapParityToPosition(k);
					if (c == 0) {
						EAr[arrayC.get(0)][0] = k;
					}
					if (c == 1) {
						EAr[arrayC.get(0)][1] = k;
					}
					if (c==2) {
						EAr[arrayC.get(1)][0] = k;
					}
					if (c==3) {
						EAr[arrayC.get(1)][1] = k;
					}
					c++;
				}

			}

		}
		if (count == 3){
			if (arrayP.isEmpty()){
				// chute (33%)
				int n1 = arrayC.get(0); int n2 = arrayC.get(1); int n3 = arrayC.get(2);
				if ((Aux[n1] ^ Aux[n2]) == Aux[n3]) {
					EAr[n1][0] = Aux[n1];
					EAr[n2][0] = Aux[n2];
					EAr[n3][0] = Aux[n1];
					EAr[n3][1] = Aux[n2];
				}

			}
			if (arrayP.size() == 1){
				for (int i = 0; i < 3; i++) {
					EAr[arrayC.get(i)][0] = Aux[arrayC.get(i)];
				}
			}
			if (arrayP.size() == 2){
				int n1 = -1; int n2= -1; boolean naocontem = false; int necessario;
				for (int i =0; i<3; i++){
					for (int j = i+1; j < 4; j++) {
						 if(Aux[i] == Aux[j]){
							 n1 = i;
							 n2 = j;
							 break;
						 }
					}
					if (n1 != -1) break;
				}
				if (n1 != -1){
					Integer obj1 = n1;
					Integer obj2 = n2;
					Integer position1 = mapPositionToParity(Aux[n1]);
					Integer position2 = mapPositionToParity(Aux[n2]);
					Integer position3 = mapPositionToParity(Aux[arrayC.getFirst()]);
					arrayC.remove(obj1);
					arrayC.remove(obj2);

					EAr[arrayC.getFirst()][0] = Aux[arrayC.getFirst()];
					// problema
					int n3 = arrayC.getFirst();
					Integer obj3 =n3;
					EAr[n1][0] = Aux[n1];
					EAr[n2][0] = Aux[arrayC.getFirst()];
					arrayP.remove(position1);
					EAr[n2][1] = mapParityToPosition(arrayP.getFirst());
				}
				else {
						Integer obj1 = mapPositionToParity(Aux[0]);
						Integer obj2 = mapPositionToParity(Aux[1]);
						Integer obj3 = mapPositionToParity(Aux[2]);

						for (int i = 0; i < 3; i++) {
							if (arrayP.contains(mapPositionToParity(Aux[i]))) {
								EAr[i][0] = Aux[i];
								arrayC.remove(i);
								Integer num1 = mapPositionToParity(Aux[i]);
								arrayP.remove(num1);
								n1 = i;
								break;
							}
						}
						n1 = arrayC.getFirst();
						n2 = arrayC.get(1);
						int numero= mapParityToPosition(arrayP.getFirst());
							int aux = (numero + 3) % 7;
							if (Aux[n1] == aux) {
								EAr[n2][0] = numero;
								EAr[n2][1] = 3;
								EAr[n1][0] = Aux[n1];
							}
							aux = (numero + 5) % 7;
							if (Aux[n1] == aux) {
								EAr[n2][0] = numero;
								EAr[n2][1] = aux;
								EAr[n1][0] = Aux[n1];
							}
							aux = (numero + 6) % 7;
							if (Aux[n1] == aux) {
								EAr[n2][0] = numero;
								EAr[n2][1] = 6;
								EAr[n1][0] = Aux[n1];
							}
							aux = (numero + 7) % 7;
							if (Aux[n1] == aux) {
								EAr[n2][0] = numero;
								EAr[n2][1] = 7;
								EAr[n1][0] = Aux[n1];
							}
							Aux2[n2] = recCr[n2][0] * 4 + recCr[n2][1] * 2 + recCr[n2][2];
							aux = (numero + 3) % 7;
							if (Aux[n2] == aux) {
								EAr[n1][0] = numero;
								EAr[n1][1] = 3;
								EAr[n2][0] = Aux[n2];
							}
							aux = (numero + 5) % 7;
							if (Aux[n2] == aux) {
								EAr[n1][0] = numero;
								EAr[n1][1] = aux;
								EAr[n2][0] = Aux[n2];
							}
							aux = (numero + 6) % 7;
							if (Aux[n2] == aux) {
								EAr[n1][0] = numero;
								EAr[n1][1] = 6;
								EAr[n2][0] = Aux[n2];
							}
							aux = (numero + 7) % 7;
							if (Aux[n2] == aux) {
								EAr[n1][0] = numero;
								EAr[n1][1] = 7;
								EAr[n2][0] = Aux[n2];
							}

				}
			}
			if (arrayP.size() == 3){
				for (int i = 0; i < 3; i++) {
					EAr[arrayC.get(i)][0] = Aux[arrayC.get(i)];
				}
			}
			if (arrayP.size() == 4) {
				int n1 = -1; int n2= -1; boolean naocontem = false; int necessario;
				for (int i =0; i<3; i++){
					for (int j = i+1; j < 4; j++) {
						if(Aux[i] == Aux[j]){
							n1 = i;
							n2 = j;
							break;
						}
					}
					if (n1 != -1) break;
				}
				if (n1 != -1) {
					arrayC.remove((Integer) n1);
					arrayC.remove((Integer) n2);
					EAr[arrayC.getFirst()][0] = Aux[arrayC.getFirst()];
					arrayP.remove((Integer) mapPositionToParity(Aux[arrayC.getFirst()]));
					EAr[n1][0] = Aux[n1];
					arrayP.remove((Integer) mapPositionToParity(Aux[n1]));
					EAr[n2][0] = mapParityToPosition(arrayP.getFirst());
					EAr[n2][1] = mapParityToPosition(arrayP.get(1));
				}
				else {
					for (int i = 0; i < 4; i++) {
						if (Aux[i] == 4 || Aux[i] == 1 || Aux[i] == 2) {
							n1 = i;
							arrayC.remove((Integer) n1);
							break;
						}
					}
					EAr[arrayC.get(0)][0] = Aux[arrayC.get(0)];
					EAr[arrayC.get(1)][0] = Aux[arrayC.get(1)];
					arrayP.remove((Integer) mapPositionToParity(Aux[arrayC.get(0)]));
					arrayP.remove((Integer) mapPositionToParity(Aux[arrayC.get(1)]));
					if (n1 == -1) ;
					else {
						EAr[n1][0] = mapParityToPosition(arrayP.get(0));
						EAr[n1][1] = mapParityToPosition(arrayP.get(1));
					}
				}
			}
		}
		if (count == 4){
			if (arrayP.isEmpty()){
				for (int i = 0; i < 4; i++) {
					EAr[arrayC.get(i)][0] = Aux[arrayC.get(i)];
				}
			}
			if (arrayP.size() == 1){
//				for (int i = 0; i < 4; i++) {
//					if (1 == arrayP.get(mapPositionToParity(Aux[arrayC.get(i)]))){
//
//					}
//				}
			}
			if (arrayP.size() == 2){
				// melhorar
				for (int i = 0; i < 4; i++) {
					EAr[arrayC.get(i)][0] = Aux[arrayC.get(i)];
				}
			}
			if (arrayP.size() == 3){
				int n1 = -1; int n2= -1; boolean naocontem = false; int necessario;
				for (int i =0; i<3; i++){
					for (int j = i+1; j < 4; j++) {
						if(Aux[i] == Aux[j]){
							n1 = i;
							n2 = j;
							break;
						}
					}
					if (n1 != -1) break;
				}
				if (n1 != -1) {
					arrayC.remove((Integer) n1);
					arrayC.remove((Integer) n2);
					EAr[arrayC.getFirst()][0] = Aux[arrayC.getFirst()];
					EAr[arrayC.get(1)][0] = Aux[arrayC.get(1)];
					EAr[n1][0] = Aux[n1];
					arrayP.remove((Integer) mapPositionToParity(Aux[n1]));
					int n3 = -1;
					if (!arrayP.remove((Integer) mapPositionToParity(Aux[arrayC.get(0)]))) {
						n3 = Aux[arrayC.getFirst()];
					}
					if (!arrayP.remove((Integer) mapPositionToParity(Aux[arrayC.get(1)]))) {
						n3= Aux[arrayC.get(1)];
					}
					if (n3 == -1) ;
					else {
						EAr[n2][0] = mapParityToPosition(arrayP.getFirst());
						EAr[n2][1] = n3;
					}
				}
				else {
					for (int i = 0; i < 4; i++) {
						if (Aux[i] == 4 || Aux[i] == 1 || Aux[i] == 2) {
							n1 = i;
							arrayC.remove((Integer) n1);
							break;
						}
					}
					EAr[arrayC.get(0)][0] = Aux[arrayC.get(0)];
					EAr[arrayC.get(1)][0] = Aux[arrayC.get(1)];
					EAr[arrayC.get(2)][0] = Aux[arrayC.get(2)];
                    if (!arrayP.remove((Integer) mapPositionToParity(Aux[arrayC.get(0)]))) {
                        n2 = Aux[arrayC.getFirst()];
                    }
                    if (!arrayP.remove((Integer) mapPositionToParity(Aux[arrayC.get(1)]))) {
                        n2= Aux[arrayC.get(1)];
                    }
                    if (!arrayP.remove((Integer) mapPositionToParity(Aux[arrayC.get(2)]))) {
                        n2= Aux[arrayC.get(2)];
                    }
					if (n1 == -1 || n2 == -1) ;
					else {
						EAr[n1][0] = mapParityToPosition(arrayP.get(0));
						EAr[n1][1] = n2;
					}

				}
			}
			if (arrayP.size() == 4){
				// melhorar
				for (int i = 0; i < 4; i++) {
					EAr[arrayC.get(i)][0] = Aux[arrayC.get(i)];
				}
			}
		}


	}


	public void computeSE_DE() {
//		for(int k=0; k<4; k++) {
//			SEr[k] = sCrq[k]==1 && sPr[k]==1;
//			DEr[k] = sCrq[k]==1 && sPr[k]==0;
//		}
	}
	private void setErrorPattern(int[] errorPattern) throws Exception {
		for(int k = 0; k < errorPattern.length; k++)
			setError(errorPattern[k]);
	}
	private static int invertBit(int value) {
		return value == 0 ? 1 : 0;
	}
	private void setError(int errorPosition) throws Exception {
		int row, column;
		if(errorPosition < 16) {
			row = errorPosition / 4;
			column = errorPosition % 4;
			D[row][column] = invertBit(D[row][column]);
		}
		else if(errorPosition >= 16 && errorPosition < 28) {
			errorPosition -= 16;
			row = errorPosition / 3;
			column = errorPosition % 3;
			Cr[row][column] = invertBit(Cr[row][column]);
		}
		else if(errorPosition >= 28 && errorPosition < 32) {
			errorPosition -= 28;
			Pr[errorPosition] = invertBit(Pr[errorPosition]);
		}
		else
			throw new Exception("Error position = " + errorPosition);
	}
	public String toString() {
		String str = "   D0 1 2 3 C0 1 2 P sC0 1 2 sP sCq SE DE Add\n";
		for(int k=0; k<4; k++) {
			str = str + "D" + k + " [" + D[k][0] + " " + D[k][1] + " " + D[k][2] + " " + 
						D[k][3] + "][" + Cr[k][0] + " " + Cr[k][1] + " " + 
						Cr[k][2] + "]" + Pr[k];
			str = str + "  [" + sCr[k][0] + " " + sCr[k][1] + " " + sCr[k][2] + "  " + sPr[k] + "]";
			str = str + " " + sCrq[k] + "  ";
			str = str + "[" + (SEr[k]==true?1:0) + " " + (DEr[k]==true?1:0) + "] " + EAr[k] + "\n";
		}
		return str;
	}

	private int mapParityToPosition(int k) {
		switch (k) {
			case 0: return 3;
			case 1: return 5;
			case 2: return 6;
			case 3: return 7;
			default: return k;
		}
	}
	private int mapPositionToParity(int k) {
		switch (k) {
			case 3: return 0;
			case 5: return 1;
			case 6: return 2;
			case 7: return 3;
			default: return k;
		}
	}

}

