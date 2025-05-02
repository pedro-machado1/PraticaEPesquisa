public class CodeStructWithErrror extends CodeStruct
{
	public int recCr[][] = new int[4][3];
	public int recPr[] = new int[4];

	public int sCr[][] = new int[4][3];
	public int sPr[] = new int[4];
	public int sCrq[] = new int[4];

	public int EAr[] = new int[4];
	public boolean SEr[] = new boolean[4];
	public boolean DEr[] = new boolean[4];

	public CodeStructWithErrror(CodeStruct initialCodeStruct, int errorPattern[]) throws Exception {
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
		for(int k=0; k<4; k++)
			EAr[k] = sCr[k][0] * 4 + sCr[k][1] * 2 + sCr[k][2];
	}

	public void computeSE_DE() {
		for(int k=0; k<4; k++) {
			SEr[k] = sCrq[k]==1 && sPr[k]==1;
			DEr[k] = sCrq[k]==1 && sPr[k]==0;
		}
	}
	private void setErrorPattern(int errorPattern[]) throws Exception {
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
}

