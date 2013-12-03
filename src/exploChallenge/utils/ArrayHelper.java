package exploChallenge.utils;

public class ArrayHelper {
	//function to generate an array of bytes filled by zeros
	public static byte[] newZeroByteArray(int size) {
		byte[] array = new byte[size];
		for (int i = 0; i < size; i++)
			array[i] = 0;
		return array;
	} 
	 
}
 