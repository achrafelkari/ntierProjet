package tp.ejb.utils;

/**
 *@author Gary Lewandowski
 * Methods to generates a random string of characters and to mutate a string
 * into a new one.
 *@version 0.1 27 February 2002
 *@version 0.2 12 February 2004
 */

import java.util.Random; // for Random

/**
 * All methods are static; we do not expect anyone to instantiate this class;
 * rather we expect the methods to be called as utility methods, much as Math
 * functions are called.
 */
public class RandomString {
	/**
	 * Reduced character set for mutations is lowercase letters plus digits plus
	 * space, ., !, ? and -
	 */
	public final static char ReducedCharacterSet[] = { 'a', 'b', 'c', 'd', 'e',
			'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
			's', 't', 'u', 'v', 'w', 'x', 'y', 'z', ' ', '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9', '.', '!', '?', '-' };
	
	
	
	
	public final static char AlphaCharacterSet[] = { 'a', 'b', 'c', 'd', 'e',
		'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
		's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};	
	
	
	/**
	 * Default MUTATION_RATE for this class is 10%. That is very high in terms
	 * of evolution. For real genetic algorithms that do crossover, the rate is
	 * usually lower.
	 */
	public final static double MUTATION_RATE = 0.1; // 10% mutation
	// rate!

	private final static int NUMBER_OF_PRINTABLE_CHARACTERS = 95;
	private final static int FIRST_PRINTABLE_ASCII_VALUE = 32;
	public static Random randomGenerator = new Random();

	/**
	 * Generate a single random, printable character
	 * 
	 * @return character with ascii value 32 - 126
	 */
	public static char randomChar() {
		/*
		 * arcane: printable characters are ascii 32 - 126 (that's 95
		 * characters) so we generate 0-95 and add 32 to get this range
		 */
		int val = randomGenerator.nextInt(NUMBER_OF_PRINTABLE_CHARACTERS)
				+ FIRST_PRINTABLE_ASCII_VALUE;
		return (char) val; // cast int into a character
	}
	
	
	public static int randomInt(int max) {
		return randomGenerator.nextInt(max);
	}

	/**
	 * Generate a single random, printable character from a reduced set of
	 * characters -- lowercase, digits, space, ., ?, !, and -
	 * 
	 * @return character from reduced set of printable characters
	 */
	public static char randomCharReduced() {
		int val = randomGenerator.nextInt(ReducedCharacterSet.length);
		return ReducedCharacterSet[val]; // cast int into a character
	}
	
	
	public static char randomCharAlpha() {
		int val = randomGenerator.nextInt(AlphaCharacterSet.length);
		return AlphaCharacterSet[val]; // cast int into a character
	}

	/**
	 * Constructs a random string of printable characters.
	 * 
	 * @param n
	 *            The desired length of the random string.
	 * @return String of size n holding random printable characters.
	 */
	public static String generateString(int n) {
		/* Assumes n is >= 0 */
		String result = "";

		for (int i = 0; i < n; i++) {
			result = result + randomChar();
		}
		return result;
	}

	/**
	 * Generates a string using a reduced set of printable characters Dangerous
	 * because input phrase might not match this set.
	 * 
	 * @param n
	 *            the length of string to be generated
	 * @return randomly generated string
	 */
	public static String generateStringReduced(int n) {
		/* Assumes n is >= 0 */
		String result = "";

		for (int i = 0; i < n; i++) {
			result = result + randomCharReduced();
		}
		return result;
	}
	
	public static String generateStringAlpha(int n) {
		/* Assumes n is >= 0 */
		String result = "";

		for (int i = 0; i < n; i++) {
			result = result + randomCharAlpha();
		}
		return result;
	}

	/**
	 * Mutates the source string at the given rate. i.e. at the given
	 * probability, a character is modified in the successor to be a new random
	 * printable character.
	 * 
	 * @param String
	 *            source Original string
	 * @param double
	 *            rate mutation rate (double between 0 and 1)
	 * @return successor string mutated from source
	 */
	public static String generateMutation(String source, double rate) {
		String result = "";
		for (int i = 0; i < source.length(); i++) {
			/* generate value between 0.00 and 1.0 */
			double probability = randomGenerator.nextDouble();
			if (probability < rate) /* mutate! */
				result = result + randomChar();
			else
				result = result + source.charAt(i);
		}
		return result;
	}

	/**
	 * Mutates the source string at the given rate. i.e. at the given
	 * probability, a character is modified in the successor to be a new random
	 * printable character from the reduced set.
	 * 
	 * @param String
	 *            source Original string
	 * @param double
	 *            rate mutation rate: double between 0 and 1
	 * @return successor string mutated from source
	 */
	public static String generateMutationReduced(String source, double rate) {
		String result = "";
		for (int i = 0; i < source.length(); i++) {
			/* generate value between 0.00 and 1.0 */
			double probability = randomGenerator.nextDouble();
			if (probability < rate) /* mutate! */
				result = result + randomCharReduced();
			else
				result = result + source.charAt(i);
		}
		return result;
	}

}
