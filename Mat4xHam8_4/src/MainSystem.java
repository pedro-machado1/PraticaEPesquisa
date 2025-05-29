public class MainSystem {
	private static int numErrors, initialElement = 0, numElements = 32;
	private static long numberOfDecodigns = 0, errorSeDecoding = 0;
	private static CodeStruct initialCodeStruct;
	private static CodeStructWithErrror hamMatWithErrors;

	private static void errorGenerator(int errorIndex, int[] errorPattern, int elementIndex) throws Exception
	{ 
		if(errorIndex == numErrors) 
		{ 
			hamMatWithErrors = new CodeStructWithErrror(initialCodeStruct, errorPattern);

// System.out.println(hamMatWithErrors);	// comentado  APENAS PARA DEPURA��O !!!

			Decoder.decodingSE(hamMatWithErrors);
			numberOfDecodigns++;
			
			if(!initialCodeStruct.isEqual(hamMatWithErrors))
				errorSeDecoding++;
			return; 
		} 
		if(elementIndex >= numElements) 
			return;
		errorPattern[errorIndex] = elementIndex; 
		errorGenerator(errorIndex+1, errorPattern, elementIndex+1); 
		errorGenerator(errorIndex, errorPattern, elementIndex+1); 
	}
	private static void setInitialCodeStruct() throws Exception {
		int D[][] = {	{0, 0, 0, 0}, 
						{0, 0, 0, 0},
						{0, 0, 0, 0},
						{0, 0, 0, 0}, };
		initialCodeStruct = new CodeStruct(D);
	}
	private static void printTestIdentification() {
		System.out.println("#Errors=" + numErrors);
	}
	private static void printResults() {
	    System.out.println("\n\tnumberOfDecodigns = " + numberOfDecodigns);
	    System.out.println("\terrorSeDecoding = " + errorSeDecoding);
	}
	private static void setNumberOfErrors(int nE) {
		numErrors = nE;
	}
	private static void resetSimulationData() {
		numberOfDecodigns = 0;
		errorSeDecoding = 0;
	}
	private static void setErrorInterval(int inicio, int fim) {
		initialElement = inicio;
		numElements = fim;
	}
	public static void main(String[] args) throws Exception {
		setInitialCodeStruct();
		setErrorInterval(0, 16);  // O default � (0, 32) ==> Todos os bits do HamMat
 		for(int numberOfErrors=0; numberOfErrors<=8; numberOfErrors++) 			// comentado APENAS PARA DEPURA��O !!!
		{
setNumberOfErrors(2);															// inserido APENAS PARA DEPURA��O !!!
			setNumberOfErrors(numberOfErrors);									// comentado APENAS PARA DEPURA��O !!!
			printTestIdentification();
			resetSimulationData();
			errorGenerator(0, new int[numErrors], initialElement);
			printResults();
		}
	}
}