public class CodeStruct {
	public int D[][] = new int[4][4];
	public int Cr[][] = new int[4][3];
	public int Pr[] = new int[4];

	public static void main(String[] args) throws Exception {
		CodeStruct cd = new CodeStruct(new int[][]{ {0, 0 , 1 , 0},{0, 0 , 0 , 0}, {0, 0 , 0 , 0}, {0, 0 , 0 , 0}});
		String s = cd.toString();
		System.out.println(s);
	}
	public CodeStruct(int D[][]) throws Exception {
		for(int r=0; r<4; r++)
			for(int c=0; c<4; c++)
				this.D[r][c] = D[r][c];
		encodeRow();
		encodeColumm();
	}
	private void encodeRow() {
		for(int k=0; k<4; k++) {
			Cr[k][0] = D[k][1] ^ D[k][2] ^ D[k][3];
			Cr[k][1] = D[k][0] ^ D[k][2] ^ D[k][3];
			Cr[k][2] = D[k][0] ^ D[k][1] ^ D[k][3];
		}
	}
	private  void encodeColumm() {
		for(int l=0; l<4; l++) {
			Pr[l] = D[0][l] ^ D[1][l] ^ D[2][l] ^ D[3][l];
		}
	}
	public boolean isEqual(CodeStruct hamMat) {
		for(int k=0; k<4; k++) {
			for(int j=0; j<4; j++) {
				if(D[k][j] != hamMat.D[k][j])
					return false;
			}
		}
		return true;
	}
	public String toString() {
		String str = "";
		for(int k=0; k<4; k++) {
			str = str + "[" + D[k][0] + " " + D[k][1] + " " + D[k][2] + " " + 
						D[k][3] + "][" + Cr[k][0] + " " + Cr[k][1] + " " + 
						Cr[k][2] + "] " + "\n";
		}
		str = str + "[" + Pr[0] + " " + Pr[1] + " " + Pr[2] + " " + Pr[3] + "]" ;
		return str;
	}
}
