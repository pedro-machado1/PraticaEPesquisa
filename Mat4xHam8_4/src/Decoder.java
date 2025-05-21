public class Decoder {
	public static void decodingSE(CodeStructWithErrror hamMat) { 	// ApplyHammingOnRows
		boolean done = false;
		for(int k=0; k<4; k++) {
			for (int i = 0; i < 4; i++) {
				if (hamMat.EAr[k][i] != 0){
					int add = hamMat.EAr[k][i];
					if(add==3 || (add >= 5 && add <= 7))
					{
						add = (add==3) ? 0 : add - 4;
						hamMat.D[k][add] = hamMat.D[k][add]==0 ? 1 : 0;
						done = true;
					}
				}
				else {
					break;
				}
			}

		}
		if(done)
			hamMat.recomputeControlVariables();
	}
}
