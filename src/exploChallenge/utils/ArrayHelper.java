package exploChallenge.utils;

public class ArrayHelper {

	public static byte[] newZeroByteArray(int size) {
		byte[] array = new byte[size];
		for (int i = 0; i < size; i++)
			array[i] = 0;
		return array;
	} 
	 
}
 