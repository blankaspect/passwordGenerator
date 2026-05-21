/*====================================================================*\

Generator.java

Class: password generator.

\*====================================================================*/


// PACKAGE


package uk.blankaspect.passwordgenerator;

//----------------------------------------------------------------------


// IMPORTS


import java.security.SecureRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import uk.blankaspect.common.exception2.BaseException;

//----------------------------------------------------------------------


// CLASS: PASSWORD GENERATOR


class Generator
{

////////////////////////////////////////////////////////////////////////
//  Constants
////////////////////////////////////////////////////////////////////////

	/** Error messages. */
	private interface ErrorMsg
	{
		String	FAILED_TO_INITIALISE_PRNG =
				"Failed to initialise a secure pseudo-random number generator.";

		String	NO_CHARACTERS =
				"No characters were specified.";

		String	TOO_MANY_REQUIRED_CATEGORIES =
				"The number of required character categories exceeds\nthe maximum password length.";

		String	TOO_FEW_CHARACTERS_FOR_UNIQUE_CHARACTERS =
				"The set of available characters is too small to generate\na password with unique characters.";
	}

////////////////////////////////////////////////////////////////////////
//  Class variables
////////////////////////////////////////////////////////////////////////

	private static	SecureRandom	prng;

////////////////////////////////////////////////////////////////////////
//  Constructors
////////////////////////////////////////////////////////////////////////

	private Generator()
	{
	}

	//------------------------------------------------------------------

////////////////////////////////////////////////////////////////////////
//  Class methods
////////////////////////////////////////////////////////////////////////

	public static boolean isValidCharacter(
		char	ch)
	{
		return (ch >= CharCategory.Ascii.MIN_CHAR) && (ch <= CharCategory.Ascii.MAX_CHAR);
	}

	//------------------------------------------------------------------

	public static String generatePassword(
		Map<CharCategory, Boolean>	categoriesRequired,
		String						includedChars,
		boolean						includedRequired,
		String						excludedChars,
		boolean						uniqueChars,
		int							minLength,
		int							maxLength)
		throws BaseException
	{
		// Initialise secure PRNG
		if (prng == null)
		{
			try
			{
				prng = SecureRandom.getInstanceStrong();
			}
			catch (Exception e)
			{
				throw new BaseException(ErrorMsg.FAILED_TO_INITIALISE_PRNG, e);
			}
		}

		// Add character sequence for each category
		List<CharSeq> charSeqs = new ArrayList<>();
		for (CharCategory category : categoriesRequired.keySet())
			charSeqs.add(new CharSeq(category.chars(), categoriesRequired.get(category)));

		// Add sequence for included characters
		if (!includedChars.isEmpty())
		{
			// Remove included characters from character-category sequences
			for (int i = 0; i < includedChars.length(); i++)
				removeChar(charSeqs, includedChars.charAt(i));

			// Add sequence for included characters
			charSeqs.add(new CharSeq(includedChars.toCharArray(), includedRequired));
		}

		// Remove excluded characters from sequences
		for (int i = 0; i < excludedChars.length(); i++)
			removeChar(charSeqs, excludedChars.charAt(i));

		// Test for characters
		int numInChars = 0;
		for (CharSeq charSeq : charSeqs)
			numInChars += charSeq.length;
		if (numInChars == 0)
			throw new BaseException(ErrorMsg.NO_CHARACTERS);

		// Test whether password is long enough for required characters
		int numRequiredChars =
				(int)charSeqs.stream().filter(charSeq -> charSeq.required && (charSeq.length > 0)).count();
		if (numRequiredChars > maxLength)
			throw new BaseException(ErrorMsg.TOO_MANY_REQUIRED_CATEGORIES);

		// Adjust minimum length of password
		if (minLength < numRequiredChars)
			minLength = numRequiredChars;

		// Test whether there are enough characters for a password with unique characters
		if (uniqueChars)
		{
			if (numInChars < minLength)
				throw new BaseException(ErrorMsg.TOO_FEW_CHARACTERS_FOR_UNIQUE_CHARACTERS);
			if (maxLength > numInChars)
				maxLength = numInChars;
		}

		// Allocate array for generated characters
		int numOutChars = minLength + ((minLength == maxLength) ? 0 : prng.nextInt(maxLength - minLength + 1));
		char[] outChars = new char[numOutChars];

		// Generate required characters
		int outIndex = 0;
		for (CharSeq charSeq : charSeqs)
		{
			if (charSeq.required && (charSeq.length > 0))
			{
				int i = prng.nextInt(charSeq.length);
				outChars[outIndex++] = charSeq.chars[i];
				if (uniqueChars)
				{
					charSeq.removeChar(i);
					--numInChars;
				}
			}
		}

		// Generate remaining characters
		while (outIndex < numOutChars)
		{
			int i = prng.nextInt(numInChars);
			int offset = 0;
			for (CharSeq charSeq : charSeqs)
			{
				if (i < offset + charSeq.length)
				{
					i -= offset;
					outChars[outIndex++] = charSeq.chars[i];
					if (uniqueChars)
					{
						charSeq.removeChar(i);
						--numInChars;
					}
					break;
				}
				offset += charSeq.length;
			}
		}

		// Move required characters
		for (int j = 0; j < numRequiredChars; j++)
		{
			int i = prng.nextInt(numOutChars);
			char ch = outChars[i];
			outChars[i] = outChars[j];
			outChars[j] = ch;
		}

		// Return password
		return new String(outChars);
	}

	//------------------------------------------------------------------

	private static void removeChar(
		List<CharSeq>	charSeqs,
		char			ch)
	{
		for (CharSeq charSeq : charSeqs)
		{
			for (int i = 0; i < charSeq.length; i++)
			{
				if (charSeq.chars[i] == ch)
				{
					charSeq.removeChar(i);
					break;
				}
			}
		}
	}

	//------------------------------------------------------------------

////////////////////////////////////////////////////////////////////////
//  Member classes : non-inner classes
////////////////////////////////////////////////////////////////////////


	// CLASS: CHARACTER SEQUENCE


	private static class CharSeq
	{

	////////////////////////////////////////////////////////////////////
	//  Instance variables
	////////////////////////////////////////////////////////////////////

		private char[]	chars;
		private	int		length;
		private	boolean	required;

	////////////////////////////////////////////////////////////////////
	//  Constructors
	////////////////////////////////////////////////////////////////////

		private CharSeq(
			char[]	chars,
			boolean	required)
		{
			// Initialise instance variables
			this.chars = chars;
			length = this.chars.length;
			this.required = required;
		}

		//--------------------------------------------------------------

	////////////////////////////////////////////////////////////////////
	//  Instance methods
	////////////////////////////////////////////////////////////////////

		private void removeChar(
			int	index)
		{
			// Validate index
			if ((index < 0) || (index >= length))
				throw new IndexOutOfBoundsException(index);

			// Replace character at index with last character and decrement length
			chars[index] = chars[--length];
		}

		//--------------------------------------------------------------

	}

	//==================================================================

}

//----------------------------------------------------------------------
