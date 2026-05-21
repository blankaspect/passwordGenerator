/*====================================================================*\

Preferences.java

Class: user preferences.

\*====================================================================*/


// PACKAGE


package uk.blankaspect.passwordgenerator;

//----------------------------------------------------------------------


// CLASS: USER PREFERENCES


class Preferences
{

////////////////////////////////////////////////////////////////////////
//  Instance variables
////////////////////////////////////////////////////////////////////////

	private	boolean	includeOnlyAscii;
	private	boolean	saveCharParams;
	private	boolean	savePasswordLength;

////////////////////////////////////////////////////////////////////////
//  Constructors
////////////////////////////////////////////////////////////////////////

	public Preferences()
	{
		// Initialise instance variables
		includeOnlyAscii = true;
	}

	//------------------------------------------------------------------

	public Preferences(
		boolean	includeOnlyAscii,
		boolean	saveCharParams,
		boolean	savePasswordLength)
	{
		// Initialise instance variables
		this.includeOnlyAscii = includeOnlyAscii;
		this.saveCharParams = saveCharParams;
		this.savePasswordLength = savePasswordLength;
	}

	//------------------------------------------------------------------

////////////////////////////////////////////////////////////////////////
//  Instance methods
////////////////////////////////////////////////////////////////////////

	public boolean includeOnlyAscii()
	{
		return includeOnlyAscii;
	}

	//------------------------------------------------------------------

	public void includeOnlyAscii(
		boolean	onlyAscii)
	{
		includeOnlyAscii = onlyAscii;
	}

	//------------------------------------------------------------------

	public boolean saveCharParams()
	{
		return saveCharParams;
	}

	//------------------------------------------------------------------

	public void saveCharParams(
		boolean	save)
	{
		saveCharParams = save;
	}

	//------------------------------------------------------------------

	public boolean savePasswordLength()
	{
		return savePasswordLength;
	}

	//------------------------------------------------------------------

	public void savePasswordLength(
		boolean	save)
	{
		savePasswordLength = save;
	}

	//------------------------------------------------------------------

}

//----------------------------------------------------------------------
