/*====================================================================*\

CharCategory.java

Enumeration: character categories.

\*====================================================================*/


// PACKAGE


package uk.blankaspect.passwordgenerator;

//----------------------------------------------------------------------


// IMPORTS


import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

import uk.blankaspect.common.string.StringUtils;

//----------------------------------------------------------------------


// ENUMERATION: CHARACTER CATEGORIES


/**
 * This is an enumeration of categories of characters.  The categories are associated with disjoint subsets of printable
 * characters from the ASCII character encoding.  The union of the categories corresponds to the Unicode code points
 * from U+0020 to U+007E inclusive.
 * <p>
 * The enumeration may be extended with categories that are associated with disjoint subsets of the Unicode <a
 * href="https://en.wikipedia.org/wiki/Plane_(Unicode)#Basic_Multilingual_Plane">Basic Multilingual Plane (BMP)</a>; for
 * example, the letters of the <a href="https://en.wikipedia.org/wiki/Latin-1_Supplement">Latin-1 Supplement</a> from
 * U+00C0 to U+00FF inclusive, omitting the signs U+00D7 and U+00F7).  The {@linkplain #key key} of each additional
 * category must be unique.
 * </p>
 */

enum CharCategory
{

////////////////////////////////////////////////////////////////////////
//  Constants
////////////////////////////////////////////////////////////////////////

	/**
	 * The space character of the ASCII character encoding.
	 */
	ASCII_SPACE
	(
		"Space"
	)
	{
		@Override
		protected char[] initChars()
		{
			return Ascii.SPACE;
		}
	},

	/**
	 * Decimal digits of the ASCII character encoding.
	 */
	ASCII_DIGIT
	(
		"Digits"
	)
	{
		@Override
		protected char[] initChars()
		{
			return Ascii.DIGITS;
		}
	},

	/**
	 * Upper-case letters of the ASCII character encoding.
	 */
	ASCII_LETTER_UPPER_CASE
	(
		"Letters, upper case"
	)
	{
		@Override
		protected char[] initChars()
		{
			return Ascii.LETTERS_UPPER_CASE;
		}
	},

	/**
	 * Lower-case letters of the ASCII character encoding.
	 */
	ASCII_LETTER_LOWER_CASE
	(
		"Letters, lower case"
	)
	{
		@Override
		protected char[] initChars()
		{
			return Ascii.LETTERS_LOWER_CASE;
		}
	},

	/**
	 * Characters of the ASCII character encoding that don't belong to a more specific category.
	 */
	ASCII_OTHER
	(
		"Other ASCII characters"
	)
	{
		@Override
		protected char[] initChars()
		{
			// Create bit array in which each set bit corresponds to a required character
			int numFlags = Ascii.MAX_CHAR + 1;
			BitSet charFlags = new BitSet(numFlags);
			charFlags.set(Ascii.MIN_CHAR, numFlags);
			for (char[] chars : List.of(Ascii.SPACE, Ascii.DIGITS, Ascii.LETTERS_UPPER_CASE, Ascii.LETTERS_LOWER_CASE))
			{
				for (int i = 0; i < chars.length; i++)
					charFlags.clear(chars[i]);
			}

			// Create array of characters from bit array
			char[] chars = new char[charFlags.cardinality()];
			int index = 0;
			for (int i = charFlags.nextSetBit(0); i >= 0; i = charFlags.nextSetBit(i + 1))
				chars[index++] = (char)i;
			return chars;
		}
	};

	interface Ascii
	{
		char	MIN_CHAR	= '\u0020';
		char	MAX_CHAR	= '\u007E';

		char[]	SPACE				= { ' ' };
		char[]	DIGITS				= charSequence('0', '9');
		char[]	LETTERS_UPPER_CASE	= charSequence('A', 'Z');
		char[]	LETTERS_LOWER_CASE	= charSequence('a', 'z');
	}

////////////////////////////////////////////////////////////////////////
//  Instance variables
////////////////////////////////////////////////////////////////////////

	/** The key that is associated with this category. */
	private	String	key;

	/** The textual representation of this category. */
	private	String	text;

	/** An array containing the characters of this category. */
	private	char[]	chars;

////////////////////////////////////////////////////////////////////////
//  Constructors
////////////////////////////////////////////////////////////////////////

	/**
	 * Creates a new instance of an enumeration constant for a category of characters.
	 *
	 * @param text
	 *          the textual representation of the category.
	 */

	private CharCategory(
		String	text)
	{
		// Initialise instance variables
		key = StringUtils.toCamelCase(name());
		this.text = text;
	}

	//------------------------------------------------------------------

////////////////////////////////////////////////////////////////////////
//  Class methods
////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the category that is associated with the specified key.
	 *
	 * @param  key
	 *           the key whose associated category is desired.
	 * @return the category that is associated with {@code key}, or {@code null} if there is no such category.
	 */

	public static CharCategory forKey(
		String	key)
	{
		return Arrays.stream(values()).filter(value -> value.key.equals(key)).findFirst().orElse(null);
	}

	//------------------------------------------------------------------

	/**
	 * Creates and returns an array containing a sequence of consecutive characters from the specified start character
	 * to the specified end character (inclusive).
	 *
	 * @param  startChar
	 *           the first character in the sequence.
	 * @param  endChar
	 *           the last character in the sequence.
	 * @return an array containing a sequence of consecutive characters from {@code startChar} to {@code endChar}
	 *         inclusive.
	 */

	private static char[] charSequence(
		char	startChar,
		char	endChar)
	{
		// Validate arguments
		if (startChar > endChar)
			throw new IllegalArgumentException("Start and end characters are out of order");

		// Create and return array containing sequence of consecutive characters
		char[] chars = new char[endChar - startChar + 1];
		int index = 0;
		for (char ch = startChar; ch <= endChar; ch++)
			chars[index++] = ch;
		return chars;
	}

	//------------------------------------------------------------------

////////////////////////////////////////////////////////////////////////
//  Abstract methods
////////////////////////////////////////////////////////////////////////

	/**
	 * Returns an array containing the characters of this category.  This method is called by {@link #chars()} to
	 * initialise the {@link #chars} variable lazily.
	 *
	 * @return an array containing the characters of this category.
	 */

	protected abstract char[] initChars();

	//------------------------------------------------------------------

////////////////////////////////////////////////////////////////////////
//  Instance methods : overriding methods
////////////////////////////////////////////////////////////////////////

	/**
	 * {@inheritDoc}
	 */

	@Override
	public String toString()
	{
		return text;
	}

	//------------------------------------------------------------------

////////////////////////////////////////////////////////////////////////
//  Instance methods
////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the key that is associated with this category.
	 *
	 * @return the key that is associated with this category.
	 */

	public String key()
	{
		return key;
	}

	//------------------------------------------------------------------

	/**
	 * Returns a new array containing the characters of this category.
	 *
	 * @return a new array containing the characters of this category.
	 */

	public char[] chars()
	{
		if (chars == null)
			chars = initChars();
		return chars.clone();
	}

	//------------------------------------------------------------------

}

//----------------------------------------------------------------------
