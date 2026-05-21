/*====================================================================*\

PreferencesDialog.java

Class: preferences dialog.

\*====================================================================*/


// PACKAGE


package uk.blankaspect.passwordgenerator;

//----------------------------------------------------------------------


// IMPORTS


import java.lang.invoke.MethodHandles;

import java.util.Objects;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import javafx.stage.Window;

import uk.blankaspect.common.function.IProcedure1;

import uk.blankaspect.ui.jfx.button.Buttons;

import uk.blankaspect.ui.jfx.container.LabelTitledPane;

import uk.blankaspect.ui.jfx.dialog.SimpleModalDialog;

import uk.blankaspect.ui.jfx.label.Labels;

import uk.blankaspect.ui.jfx.spinner.CollectionSpinner;

import uk.blankaspect.ui.jfx.style.StyleManager;

//----------------------------------------------------------------------


// CLASS: PREFERENCES DIALOG


/**
 * This class implements a modal dialog in which the user preferences of the application may be edited.
 */

class PreferencesDialog
	extends SimpleModalDialog<Preferences>
{

////////////////////////////////////////////////////////////////////////
//  Constants
////////////////////////////////////////////////////////////////////////

	/** The horizontal gap between adjacent components in a container. */
	private static final	double	CONTROL_H_GAP	= 6.0;

	/** The padding around the control pane. */
	private static final	Insets	CONTROL_PANE_PADDING	= new Insets(10.0, 12.0, 10.0, 12.0);

	/** Miscellaneous strings. */
	private static final	String	PREFERENCES_STR				= "Preferences";
	private static final	String	APPEARANCE_STR				= "Appearance";
	private static final	String	COLOUR_SCHEME_STR			= "Colour scheme";
	private static final	String	CHARACTERS_STR				= "Characters";
	private static final	String	INCLUDE_ONLY_ASCII_STR		= "Include only ASCII characters";
	private static final	String	SAVED_SETTINGS_STR			= "Saved settings";
	private static final	String	SAVE_CHAR_PARAMS_STR		= "Save character parameters";
	private static final	String	SAVE_PASSWORD_LENGTH_STR	= "Save password length";

////////////////////////////////////////////////////////////////////////
//  Instance variables
////////////////////////////////////////////////////////////////////////

	private	Preferences	result;

////////////////////////////////////////////////////////////////////////
//  Constructors
////////////////////////////////////////////////////////////////////////

	/**
	 * Creates a new instance of a modal dialog in which the user preferences of the application may be edited.
	 *
	 * @param owner
	 *          the window that will be the owner of this dialog, or {@code null} if the dialog has no owner.
	 */

	private PreferencesDialog(
		Window		owner,
		Preferences	preferences)
	{
		// Call superclass constructor
		super(owner, MethodHandles.lookup().lookupClass().getCanonicalName(), null, PREFERENCES_STR);

		// Create procedure to select theme
		StyleManager styleManager = StyleManager.INSTANCE;
		IProcedure1<String> selectTheme = id ->
		{
			if (id != null)
			{
				// Update theme
				styleManager.selectTheme(id);

				// Reapply style sheet to the scenes of all JavaFX windows
				styleManager.reapplyStylesheet();
			}
		};

		// Spinner: theme
		String themeId = styleManager.getThemeId();
		CollectionSpinner<String> themeSpinner =
				CollectionSpinner.leftRightH(HPos.CENTER, true, styleManager.getThemeIds(), themeId, null,
											 id -> styleManager.findTheme(id).name());
		themeSpinner.itemProperty().addListener((observable, oldId, id) -> selectTheme.invoke(id));

		// Control pane: appearance
		HBox appearanceControlPane = new HBox(CONTROL_H_GAP, Labels.hNoShrink(COLOUR_SCHEME_STR), themeSpinner);
		appearanceControlPane.setMaxWidth(Region.USE_PREF_SIZE);
		appearanceControlPane.setAlignment(Pos.CENTER_LEFT);
		appearanceControlPane.setPadding(CONTROL_PANE_PADDING);

		// Titled pane: appearance
		LabelTitledPane appearancePane = new LabelTitledPane(APPEARANCE_STR, appearanceControlPane);

		// Check box: include only ASCII
		CheckBox includeOnlyAsciiCheckBox = new CheckBox(INCLUDE_ONLY_ASCII_STR);
		includeOnlyAsciiCheckBox.setSelected(preferences.includeOnlyAscii());

		// Control pane: characters
		HBox charactersControlPane = new HBox(CONTROL_H_GAP, includeOnlyAsciiCheckBox);
		charactersControlPane.setMaxWidth(Region.USE_PREF_SIZE);
		charactersControlPane.setAlignment(Pos.CENTER_LEFT);
		charactersControlPane.setPadding(CONTROL_PANE_PADDING);

		// Titled pane: characters
		LabelTitledPane charactersPane = new LabelTitledPane(CHARACTERS_STR, charactersControlPane);

		// Check box: save character parameters
		CheckBox saveCharParamsCheckBox = new CheckBox(SAVE_CHAR_PARAMS_STR);
		saveCharParamsCheckBox.setSelected(preferences.saveCharParams());

		// Check box: save password length
		CheckBox savePasswordLengthCheckBox = new CheckBox(SAVE_PASSWORD_LENGTH_STR);
		savePasswordLengthCheckBox.setSelected(preferences.savePasswordLength());

		// Control pane: saved settings
		VBox savedSettingsControlPane = new VBox(8.0, saveCharParamsCheckBox, savePasswordLengthCheckBox);
		savedSettingsControlPane.setMaxWidth(Region.USE_PREF_SIZE);
		savedSettingsControlPane.setAlignment(Pos.CENTER_LEFT);
		savedSettingsControlPane.setPadding(CONTROL_PANE_PADDING);

		// Titled pane: saved settings
		LabelTitledPane savedSettingsPane = new LabelTitledPane(SAVED_SETTINGS_STR, savedSettingsControlPane);

		// Create content pane
		VBox contentPane = new VBox(6.0, appearancePane, charactersPane, savedSettingsPane);
		contentPane.setAlignment(Pos.TOP_CENTER);

		// Add control pane to content pane
		addContent(contentPane);

		// Create button: OK
		Button okButton = Buttons.hNoShrink(OK_STR);
		okButton.getProperties().put(BUTTON_GROUP_KEY, BUTTON_GROUP1);
		okButton.setOnAction(event ->
		{
			// Set result
			result = new Preferences(includeOnlyAsciiCheckBox.isSelected(), saveCharParamsCheckBox.isSelected(),
									 savePasswordLengthCheckBox.isSelected());

			// Close dialog
			requestClose();
		});
		addButton(okButton, HPos.RIGHT);

		// Create button: cancel
		Button cancelButton = Buttons.hNoShrink(CANCEL_STR);
		cancelButton.getProperties().put(BUTTON_GROUP_KEY, BUTTON_GROUP1);
		cancelButton.setOnAction(event -> requestClose());
		addButton(cancelButton, HPos.RIGHT);

		// Fire 'cancel' button if Escape key is pressed; fire 'OK' button if Ctrl+Enter is pressed
		setKeyFireButton(cancelButton, okButton);

		// When window is closed, restore old theme if dialog was not accepted
		setOnHiding(event ->
		{
			// If dialog was not accepted, restore old theme
			if ((result == null) && !Objects.equals(themeId, styleManager.getThemeId()))
				selectTheme.invoke(themeId);
		});

		// Apply new style sheet to scene
		applyStyleSheet();
	}

	//------------------------------------------------------------------

////////////////////////////////////////////////////////////////////////
//  Class methods
////////////////////////////////////////////////////////////////////////

	public static Preferences show(
		Window		owner,
		Preferences	preferences)
	{
		return new PreferencesDialog(owner, preferences).showDialog();
	}

	//------------------------------------------------------------------

////////////////////////////////////////////////////////////////////////
//  Instance methods : overriding methods
////////////////////////////////////////////////////////////////////////

	@Override
	protected Preferences getResult()
	{
		return result;
	}

	//------------------------------------------------------------------

}

//----------------------------------------------------------------------
