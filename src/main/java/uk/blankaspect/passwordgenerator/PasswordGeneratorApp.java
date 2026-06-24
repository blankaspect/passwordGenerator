/*====================================================================*\

PasswordGeneratorApp.java

Class: password-generator application.

\*====================================================================*/


// PACKAGE


package uk.blankaspect.passwordgenerator;

//----------------------------------------------------------------------


// IMPORTS


import java.io.IOException;

import java.lang.invoke.MethodHandles;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.application.Application;
import javafx.application.Platform;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import javafx.css.PseudoClass;

import javafx.geometry.Bounds;
import javafx.geometry.Dimension2D;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Side;

import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;

import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import uk.blankaspect.common.basictree.IntNode;
import uk.blankaspect.common.basictree.ListNode;
import uk.blankaspect.common.basictree.MapNode;

import uk.blankaspect.common.build.BuildUtils;

import uk.blankaspect.common.cls.ClassUtils;

import uk.blankaspect.common.collection.CollectionUtils;

import uk.blankaspect.common.config.AppAuxDirectory;
import uk.blankaspect.common.config.AppConfig;

import uk.blankaspect.common.css.CssRuleSet;
import uk.blankaspect.common.css.CssSelector;

import uk.blankaspect.common.exception2.BaseException;
import uk.blankaspect.common.exception2.FileException;
import uk.blankaspect.common.exception2.LocationException;
import uk.blankaspect.common.exception2.UnexpectedRuntimeException;

import uk.blankaspect.common.function.IFunction1;
import uk.blankaspect.common.function.IProcedure0;

import uk.blankaspect.common.logging.ErrorLogger;

import uk.blankaspect.common.message.MessageConstants;

import uk.blankaspect.common.range2.IntegerRange;

import uk.blankaspect.common.resource.ResourceProperties;
import uk.blankaspect.common.resource.ResourceUtils;

import uk.blankaspect.common.string.StringUtils;

import uk.blankaspect.ui.jfx.button.Buttons;
import uk.blankaspect.ui.jfx.button.GraphicButton;
import uk.blankaspect.ui.jfx.button.ImageButton;
import uk.blankaspect.ui.jfx.button.ImageDataButton;

import uk.blankaspect.ui.jfx.clipboard.ClipboardUtils;

import uk.blankaspect.ui.jfx.combobox.SimpleComboBox;

import uk.blankaspect.ui.jfx.container.PaneStyle;

import uk.blankaspect.ui.jfx.dialog.ConfirmationDialog;
import uk.blankaspect.ui.jfx.dialog.ErrorDialog;
import uk.blankaspect.ui.jfx.dialog.SimpleModalDialog;

import uk.blankaspect.ui.jfx.exec.ExecUtils;

import uk.blankaspect.ui.jfx.filler.FillerUtils;

import uk.blankaspect.ui.jfx.font.Fonts;

import uk.blankaspect.ui.jfx.icon.Icons;

import uk.blankaspect.ui.jfx.image.ImageData;
import uk.blankaspect.ui.jfx.image.MessageIcon32;

import uk.blankaspect.ui.jfx.label.Labels;
import uk.blankaspect.ui.jfx.label.OverlayLabel;

import uk.blankaspect.ui.jfx.listview.ListViewEditor;
import uk.blankaspect.ui.jfx.listview.SimpleTextListView;

import uk.blankaspect.ui.jfx.range.IntegerRangePane;

import uk.blankaspect.ui.jfx.scene.SceneUtils;

import uk.blankaspect.ui.jfx.style.ColourProperty;
import uk.blankaspect.ui.jfx.style.FxProperty;
import uk.blankaspect.ui.jfx.style.FxPseudoClass;
import uk.blankaspect.ui.jfx.style.FxStyleClass;
import uk.blankaspect.ui.jfx.style.RuleSetBuilder;
import uk.blankaspect.ui.jfx.style.StyleConstants;
import uk.blankaspect.ui.jfx.style.StyleManager;

import uk.blankaspect.ui.jfx.text.TextUtils;

import uk.blankaspect.ui.jfx.textfield.FilterFactory;

import uk.blankaspect.ui.jfx.tooltip.TooltipDecorator;

import uk.blankaspect.ui.jfx.window.WindowDims;
import uk.blankaspect.ui.jfx.window.WindowState;

//----------------------------------------------------------------------


// CLASS: PASSWORD-GENERATOR APPLICATION


public class PasswordGeneratorApp
	extends Application
{

////////////////////////////////////////////////////////////////////////
//  Constants
////////////////////////////////////////////////////////////////////////

	/** The short name of the application. */
	private static final	String	SHORT_NAME	= "PasswordGenerator";

	/** The long name of the application. */
	private static final	String	LONG_NAME	= "Password generator";

	/** The name of the application when used as a key. */
	private static final	String	NAME_KEY	= StringUtils.firstCharToLowerCase(SHORT_NAME);

	/** The name of the file that contains the build properties of the application. */
	private static final	String	BUILD_PROPERTIES_FILENAME	= "build.properties";

	/** The filename of the CSS style sheet. */
	private static final	String	STYLE_SHEET_FILENAME	= NAME_KEY + "-%02d.css";

	/** A map from system-property keys to the default values of the corresponding delays (in milliseconds) in the
		<i>WINDOW_SHOWN</i> event handler of the main window. */
	private static final	Map<String, Integer>	MAIN_WINDOW_DELAYS	= Map.of
	(
		SystemPropertyKey.MAIN_WINDOW_DELAY_SIZE,     100,
		SystemPropertyKey.MAIN_WINDOW_DELAY_LOCATION,  25,
		SystemPropertyKey.MAIN_WINDOW_DELAY_OPACITY,   25
	);

	/** The minimum width of the main window. */
	private static final	double	MAIN_WINDOW_MIN_WIDTH	= 120.0;

	/** The margins that are applied to the visual bounds of each screen when determining whether the saved location of
		the main window is within a screen. */
	private static final	Insets	SCREEN_MARGINS	= new Insets(0.0, 32.0, 32.0, 0.0);

	/** The horizontal gap between adjacent components in a container. */
	private static final	double	CONTROL_H_GAP	= 6.0;

	/** The vertical gap between adjacent components in a container. */
	private static final	double	CONTROL_V_GAP	= 8.0;

	/** The padding around a category check box. */
	private static final	Insets	CATEGORY_CHECK_BOX_PADDING	= new Insets(3.0, 4.0, 3.0, 4.0);

	/** The margins around a check box. */
	private static final	Insets	CHECK_BOX_MARGINS	= new Insets(2.0, 0.0, 2.0, 0.0);

	/** The padding around the top button pane. */
	private static final	Insets	TOP_BUTTON_PANE_PADDING	= new Insets(2.0, 4.0, 2.0, 4.0);

	/** The padding around the control pane. */
	private static final	Insets	CONTROL_PANE_PADDING	= new Insets(8.0, 12.0, 8.0, 12.0);

	/** The number of columns of the <i>include</i> field. */
	private static final	int		INCLUDE_FIELD_NUM_COLUMNS	= 12;

	/** The number of columns of the <i>exclude</i> field. */
	private static final	int		EXCLUDE_FIELD_NUM_COLUMNS	= INCLUDE_FIELD_NUM_COLUMNS;

	/** The minimum length of a password. */
	private static final	int		MIN_PASSWORD_LENGTH		= 1;

	/** The maximum length of a password. */
	private static final	int		MAX_PASSWORD_LENGTH		= 1024;

	/** The initial minimum length of a password. */
	private static final	int		INITIAL_MIN_PASSWORD_LENGTH	= 12;

	/** The initial maximum length of a password. */
	private static final	int		INITIAL_MAX_PASSWORD_LENGTH	= 16;

	/** The prototype text of the password label. */
	private static final	String	PASSWORD_LABEL_PROTOTYPE_TEXT	= "0".repeat(16);

	/** The padding around the password label. */
	private static final	Insets	PASSWORD_LABEL_PADDING	= new Insets(3.0, 6.0, 3.0, 6.0);

	/** The padding around the password pane. */
	private static final	Insets	PASSWORD_PANE_PADDING	= new Insets(8.0, 12.0, 8.0, 12.0);

	/** The padding around a button. */
	private static final	Insets	BUTTON_PADDING	= new Insets(3.0, 12.0, 3.0, 12.0);

	/** The horizontal gap between adjacent buttons of the button pane. */
	private static final	double	BUTTON_PANE_H_GAP	= 12.0;

	/** The vertical gap between adjacent buttons of the button pane. */
	private static final	double	BUTTON_PANE_V_GAP	= 6.0;

	/** The padding around the button pane. */
	private static final	Insets	BUTTON_PANE_PADDING	= new Insets(6.0, 12.0, 6.0, 12.0);

	/** The key combination that issues the <i>add or update preset</i> command. */
	private static final	KeyCombination	KEY_COMBO_ADD_OR_UPDATE_PRESET	=
			new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN);

	/** The key combination that issues the <i>edit presets</i> command. */
	private static final	KeyCombination	KEY_COMBO_EDIT_PRESETS	=
			new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN);

	/** The key combination that issues the <i>edit preferences</i> command. */
	private static final	KeyCombination	KEY_COMBO_EDIT_PREFERENCES	=
			new KeyCodeCombination(KeyCode.F8);

	/** The key combination that fires the <i>copy</i> button. */
	private static final	KeyCombination	KEY_COMBO_COPY	=
			new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);

	/** The key combination that fires the <i>generate</i> button. */
	private static final	KeyCombination	KEY_COMBO_GENERATE	=
			new KeyCodeCombination(KeyCode.G, KeyCombination.CONTROL_DOWN);

	/** Miscellaneous strings. */
	private static final	String	CONFIG_ERROR_STR			= "Configuration error";
	private static final	String	PREFERENCES_STR				= "Preferences";
	private static final	String	PRESETS_STR					= "Presets";
	private static final	String	ADD_OR_UPDATE_PRESET_STR	= "Add or update preset";
	private static final	String	UPDATE_PRESET_STR			= "Update preset";
	private static final	String	EDIT_PRESETS_STR			= "Edit presets";
	private static final	String	REQUIRED_STR				= "Required";
	private static final	String	INCLUDE_STR					= "Include";
	private static final	String	CLEAR_INCLUDE_STR			= "Clear included characters";
	private static final	String	EXCLUDE_STR					= "Exclude";
	private static final	String	CLEAR_EXCLUDE_STR			= "Clear excluded characters";
	private static final	String	UNIQUE_CHARS_STR			= "Unique characters";
	private static final	String	LENGTH_STR					= "Length";
	private static final	String	LINK_UNLINK_STR				= "Link/unlink min and max length";
	private static final	String	PASSWORD_STR				= "Password";
	private static final	String	COPY_STR					= "Copy";
	private static final	String	GENERATE_STR				= "Generate";
	private static final	String	EXIT_STR					= "Exit";
	private static final	String	UPDATE_QUESTION_STR			= "Do you want to update it?";
	private static final	String	UPDATE_STR					= "Update";

	/** Tooltips. */
	private static final	String	COPY_TOOLTIP		= "Copy password to clipboard (Ctrl+C)";
	private static final	String	GENERATE_TOOLTIP	= "Generate password (Ctrl+G)";

	/** A list of character categories that determines the order in which their check boxes appear. */
	private static final	List<CharCategory>	CHAR_CATEGORIES	= List.of
	(
		CharCategory.ASCII_LETTER_LOWER_CASE,
		CharCategory.ASCII_LETTER_UPPER_CASE,
		CharCategory.ASCII_DIGIT,
		CharCategory.ASCII_SPACE,
		CharCategory.ASCII_OTHER
	);

	/** Default presets. */
	private static final	List<ParamSet>	DEFAULT_PRESETS	= List.of
	(
		ParamSet.of(
			"Hex digits, lower case",
			EnumSet.of(CharCategory.ASCII_DIGIT),
			"abcdef"
		),
		ParamSet.of(
			"Hex digits, upper case",
			EnumSet.of(CharCategory.ASCII_DIGIT),
			"ABCDEF"
		),
		ParamSet.of(
			"Base64",
			EnumSet.of(CharCategory.ASCII_LETTER_LOWER_CASE, CharCategory.ASCII_LETTER_UPPER_CASE,
					   CharCategory.ASCII_DIGIT),
			"+/"
		),
		ParamSet.of(
			"Base64url",
			EnumSet.of(CharCategory.ASCII_LETTER_LOWER_CASE, CharCategory.ASCII_LETTER_UPPER_CASE,
					   CharCategory.ASCII_DIGIT),
			"-_"
		)
	);

	/** The pseudo-class that is associated with the <i>enabled</i> state. */
	private static final	PseudoClass	PSEUDO_CLASS_ENABLED	=
			PseudoClass.getPseudoClass(PseudoClassKey.ENABLED);

	/** CSS colour properties. */
	private static final	List<ColourProperty>	COLOUR_PROPERTIES	= List.of
	(
		ColourProperty.of
		(
			FxProperty.BORDER_COLOUR,
			PaneStyle.ColourKey.PANE_BORDER,
			CssSelector.builder()
					.id(StyleConstants.NodeId.APP_MAIN_ROOT)
					.desc(StyleClass.TOP_BUTTON_PANE)
					.build()
		),
		ColourProperty.of
		(
			FxProperty.BACKGROUND_COLOUR,
			ColourKey.CATEGORY_CHECK_BOX_BACKGROUND,
			CssSelector.builder()
					.id(StyleConstants.NodeId.APP_MAIN_ROOT)
					.desc(StyleClass.CATEGORY_CHECK_BOX)
					.build()
		),
		ColourProperty.of
		(
			FxProperty.BACKGROUND_COLOUR,
			ColourKey.CATEGORY_CHECK_BOX_BACKGROUND_SELECTED,
			CssSelector.builder()
					.id(StyleConstants.NodeId.APP_MAIN_ROOT)
					.desc(StyleClass.CATEGORY_CHECK_BOX).pseudo(PseudoClassKey.ENABLED).pseudo(FxPseudoClass.SELECTED)
					.build()
		),
		ColourProperty.of
		(
			FxProperty.TEXT_FILL,
			ColourKey.PASSWORD_LABEL_TEXT,
			CssSelector.builder()
					.id(StyleConstants.NodeId.APP_MAIN_ROOT)
					.desc(StyleClass.PASSWORD_LABEL)
					.build()
		),
		ColourProperty.of
		(
			FxProperty.BACKGROUND_COLOUR,
			ColourKey.PASSWORD_LABEL_BACKGROUND,
			CssSelector.builder()
					.id(StyleConstants.NodeId.APP_MAIN_ROOT)
					.desc(StyleClass.PASSWORD_LABEL)
					.build()
		),
		ColourProperty.of
		(
			FxProperty.BORDER_COLOUR,
			ColourKey.PASSWORD_LABEL_BORDER,
			CssSelector.builder()
					.id(StyleConstants.NodeId.APP_MAIN_ROOT)
					.desc(StyleClass.PASSWORD_LABEL)
					.build()
		),
		ColourProperty.of
		(
			FxProperty.BORDER_COLOUR,
			PaneStyle.ColourKey.PANE_BORDER,
			CssSelector.builder()
					.id(StyleConstants.NodeId.APP_MAIN_ROOT)
					.desc(StyleClass.PASSWORD_PANE)
					.build()
		),
		ColourProperty.of
		(
			FxProperty.FILL,
			ColourKey.CLEAR_BUTTON_BACKGROUND,
			CssSelector.builder()
					.id(StyleConstants.NodeId.APP_MAIN_ROOT)
					.desc(Icons.StyleClass.CLEAR02_BACKGROUND)
					.build()
		),
		ColourProperty.of
		(
			FxProperty.STROKE,
			ColourKey.CLEAR_BUTTON_FOREGROUND,
			CssSelector.builder()
					.id(StyleConstants.NodeId.APP_MAIN_ROOT)
					.desc(Icons.StyleClass.CLEAR02_FOREGROUND)
					.build()
		)
	);

	/** CSS rule sets. */
	private static final	List<CssRuleSet>	RULE_SETS	= List.of
	(
		RuleSetBuilder.create()
				.selector(CssSelector.builder()
						.id(StyleConstants.NodeId.APP_MAIN_ROOT)
						.desc(StyleClass.TOP_BUTTON_PANE)
						.build())
				.borders(Side.BOTTOM)
				.build(),
		RuleSetBuilder.create()
				.selector(CssSelector.builder()
						.id(StyleConstants.NodeId.APP_MAIN_ROOT)
						.desc(StyleClass.PASSWORD_LABEL)
						.desc(FxStyleClass.TEXT)
						.build())
				.grayFontSmoothing()
				.build(),
		RuleSetBuilder.create()
				.selector(CssSelector.builder()
						.id(StyleConstants.NodeId.APP_MAIN_ROOT)
						.desc(StyleClass.PASSWORD_PANE)
						.build())
				.borders(Side.TOP, Side.BOTTOM)
				.build()
	);

	/** CSS style classes. */
	private interface StyleClass
	{
		String	PRESET_NAME_DIALOG_ROOT	= StyleConstants.APP_CLASS_PREFIX + "preset-name-dialog-root";

		String	CATEGORY_CHECK_BOX	= StyleConstants.CLASS_PREFIX + "category-check-box";
		String	PASSWORD_LABEL		= StyleConstants.CLASS_PREFIX + "password-label";
		String	PASSWORD_PANE		= StyleConstants.CLASS_PREFIX + "password-pane";
		String	TOP_BUTTON_PANE		= StyleConstants.CLASS_PREFIX + "top-button-pane";
	}

	/** Keys of CSS pseudo-classes. */
	public interface PseudoClassKey
	{
		String	ENABLED	= "enabled";
	}

	/** Keys of colours that are used in colour properties. */
	private interface ColourKey
	{
		String	PREFIX	= StyleManager.colourKeyPrefix(MethodHandles.lookup().lookupClass().getEnclosingClass());

		String	CATEGORY_CHECK_BOX_BACKGROUND			= PREFIX + "categoryCheckBox.background";
		String	CATEGORY_CHECK_BOX_BACKGROUND_SELECTED	= PREFIX + "categoryCheckBox.background.selected";
		String	CLEAR_BUTTON_BACKGROUND					= PREFIX + "clearButton.background";
		String	CLEAR_BUTTON_FOREGROUND					= PREFIX + "clearButton.foreground";
		String	PASSWORD_LABEL_BACKGROUND				= PREFIX + "passwordLabel.background";
		String	PASSWORD_LABEL_BORDER					= PREFIX + "passwordLabel.border";
		String	PASSWORD_LABEL_TEXT						= PREFIX + "passwordLabel.text";
	}

	/** Keys of properties. */
	private interface PropertyKey
	{
		String	APPEARANCE			= "appearance";
		String	CATEGORIES			= "categories";
		String	EXCLUDE				= "exclude";
		String	ID					= "id";
		String	INCLUDE				= "include";
		String	INCLUDE_ONLY_ASCII	= "includeOnlyAscii";
		String	INCLUDE_REQUIRED	= "includeRequired";
		String	LINKED				= "linked";
		String	MAIN_WINDOW			= "mainWindow";
		String	NAME				= "name";
		String	PARAMETERS			= "parameters";
		String	PASSWORD_LENGTH		= "passwordLength";
		String	PRESETS				= "presets";
		String	RANGE				= "range";
		String	REQUIRED			= "required";
		String	THEME				= "theme";
		String	UNIQUE_CHARS		= "uniqueChars";
	}

	/** Keys of system properties. */
	private interface SystemPropertyKey
	{
		String	MAIN_WINDOW_DELAY_LOCATION	= "mainWindowDelay.location";
		String	MAIN_WINDOW_DELAY_OPACITY	= "mainWindowDelay.opacity";
		String	MAIN_WINDOW_DELAY_SIZE		= "mainWindowDelay.size";
		String	USE_STYLE_SHEET_FILE		= "useStyleSheetFile";
	}

	/** Error messages. */
	private interface ErrorMsg
	{
		String	NO_AUXILIARY_DIRECTORY =
				"The location of the auxiliary directory could not be determined.";

		String	INVALID_CHARACTER =
				"The included character '%s' is not allowed in a password.";

		String	CONFLICTING_PRESET_NAME =
				"Preset: %s" + MessageConstants.LABEL_SEPARATOR + "A preset with that name already exists.";
	}

////////////////////////////////////////////////////////////////////////
//  Instance variables
////////////////////////////////////////////////////////////////////////

	/** The properties of the build of this application. */
	private	ResourceProperties			buildProperties;

	/** The string representation of the version of this application. */
	private	String						versionStr;

	/** User preferences. */
	private	Preferences					preferences;

	/** A list of presets. */
	private	ObservableList<ParamSet>	presets;

	/** The state of the main window. */
	private	WindowState					mainWindowState;

	/** The main window. */
	private	Stage						primaryStage;

	/** A map of check boxes for character categories. */
	private	Map<CharCategory, CheckBox>	categoryCheckBoxes;

	/** A map of check boxes for the <i>required</i> flags of character categories. */
	private	Map<CharCategory, CheckBox>	categoryRequiredCheckBoxes;

	/** The text field for included characters. */
	private	TextField					includeField;

	/** The check box for the <i>included characters required</i> flag. */
	private	CheckBox					includeRequiredCheckBox;

	/** The text field for excluded characters. */
	private	TextField					excludeField;

	/** The check box for the <i>unique characters</i> flag. */
	private	CheckBox					uniqueCharsCheckBox;

	/** The pane for the range of the length of a password. */
	private	IntegerRangePane			passwordLengthPane;

////////////////////////////////////////////////////////////////////////
//  Constructors
////////////////////////////////////////////////////////////////////////

	public PasswordGeneratorApp()
	{
	}

	//------------------------------------------------------------------

////////////////////////////////////////////////////////////////////////
//  Class methods
////////////////////////////////////////////////////////////////////////

	public static void main(
		String[]	args)
	{
		launch(args);
	}

	//------------------------------------------------------------------

	private static GraphicButton clearButton(
		TextField	field,
		String		tooltipText)
	{
		GraphicButton button = new GraphicButton(Icons.clear02(getColour(ColourKey.CLEAR_BUTTON_BACKGROUND),
															   getColour(ColourKey.CLEAR_BUTTON_FOREGROUND)),
												 tooltipText);
		button.setOnAction(event ->
		{
			field.requestFocus();
			field.clear();
		});
		button.disableProperty().bind(field.textProperty().isEmpty());
		return button;
	}

	//------------------------------------------------------------------

	private static String tooltipText(
		String			prefix,
		KeyCombination	keyCombo)
	{
		return prefix + " (" + keyCombo.getDisplayText() + ")";
	}

	//------------------------------------------------------------------

	/**
	 * Returns the delay (in milliseconds) that is defined the system property with the specified key.
	 *
	 * @param  key
	 *           the key of the system property.
	 * @return the delay (in milliseconds) that is defined the system property whose key is {@code key}, or a default
	 *         value if there is no such property or the property value is not a valid integer.
	 */

	private static int getDelay(
		String	key)
	{
		int delay = MAIN_WINDOW_DELAYS.get(key);
		String value = System.getProperty(key);
		if (value != null)
		{
			try
			{
				delay = Integer.parseInt(value);
			}
			catch (NumberFormatException e)
			{
				e.printStackTrace();
			}
		}
		return delay;
	}

	//------------------------------------------------------------------

	/**
	 * Returns the colour that is associated with the specified key in the colour map of the current theme of the
	 * {@linkplain StyleManager style manager}.
	 *
	 * @param  key
	 *           the key of the desired colour.
	 * @return the colour that is associated with {@code key} in the colour map of the current theme of the style
	 *         manager, or {@link StyleManager#DEFAULT_COLOUR} if there is no such colour.
	 */

	private static Color getColour(
		String	key)
	{
		return StyleManager.INSTANCE.getColourOrDefault(key);
	}

	//------------------------------------------------------------------

////////////////////////////////////////////////////////////////////////
//  Instance methods : overriding methods
////////////////////////////////////////////////////////////////////////

	@Override
	public void start(
		Stage	primaryStage)
	{
		// Make main window invisible until it is shown
		primaryStage.setOpacity(0.0);

		// Log stack trace of uncaught exception
		if (ClassUtils.isFromJar(getClass()))
		{
			Thread.setDefaultUncaughtExceptionHandler((thread, exception) ->
			{
				try
				{
					ErrorLogger.INSTANCE.write(exception);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			});
		}

		// Initialise instance variables
		preferences = new Preferences();
		presets = FXCollections.observableArrayList();
		mainWindowState = new WindowState(false, true);
		this.primaryStage = primaryStage;

		// Read build properties and initialise version string
		try
		{
			buildProperties =
					new ResourceProperties(ResourceUtils.normalisedPathname(getClass(), BUILD_PROPERTIES_FILENAME));
			versionStr = BuildUtils.versionString(getClass(), buildProperties);
		}
		catch (LocationException e)
		{
			e.printStackTrace();
		}

		// Create container for local variables
		class Vars
		{
			Configuration	config;
			BaseException	configException;
		}
		Vars vars = new Vars();

		// Read configuration file and decode configuration
		try
		{
			// Initialise configuration
			vars.config = new Configuration();

			// Read and decode configuration
			if (!AppConfig.noConfigFile())
			{
				// Read configuration file
				vars.config.read();

				// Decode configuration
				decodeConfig(vars.config.getConfig());
			}
		}
		catch (BaseException e)
		{
			vars.configException = e;
		}

		// Get style manager
		StyleManager styleManager = StyleManager.INSTANCE;

		// Select theme from system property
		String themeId = System.getProperty(StyleManager.SystemPropertyKey.THEME);
		if (!StringUtils.isNullOrEmpty(themeId))
			styleManager.selectThemeOrDefault(themeId);

		// Set ID and style-sheet filename on style manager
		if (Boolean.getBoolean(SystemPropertyKey.USE_STYLE_SHEET_FILE))
		{
			styleManager.setId(getClass().getSimpleName());
			styleManager.setStyleSheetFilename(STYLE_SHEET_FILENAME);
		}

		// Register the style properties of this class and its dependencies with the style manager
		styleManager.register(getClass(), COLOUR_PROPERTIES, RULE_SETS, PaneStyle.class);

		// Initialise 'Images' class
		Images.init();

		// Button: add or update preset
		ImageButton addOrUpdatePresetButton =
				Images.imageButton(Images.ImageId.PLUS,
								   tooltipText(ADD_OR_UPDATE_PRESET_STR, KEY_COMBO_ADD_OR_UPDATE_PRESET));
		addOrUpdatePresetButton.setOnAction(event -> onAddOrUpdatePreset());

		// Button: presets
		ImageButton presetsButton = Images.imageButton(Images.ImageId.PRESETS, PRESETS_STR);
		presetsButton.setOnAction(event ->
		{
			// Disable button
			presetsButton.setDisable(true);

			// Create context menu
			ContextMenu menu = new ContextMenu();

			// Add item to menu for each preset
			for (ParamSet preset : presets)
			{
				MenuItem menuItem = new MenuItem(preset.name);
				menuItem.setOnAction(event0 -> setParams(preset));
				menu.getItems().add(menuItem);
			}

			// Add separator
			if (!menu.getItems().isEmpty())
				menu.getItems().add(new SeparatorMenuItem());

			// Add menu item: edit presets
			MenuItem menuItem = new MenuItem(EDIT_PRESETS_STR);
			menuItem.setAccelerator(KEY_COMBO_EDIT_PRESETS);
			menuItem.setOnAction(event0 -> onEditPresets());
			menu.getItems().add(menuItem);

			// Enable button when context menu is hidden
			menu.setOnHidden(event0 -> presetsButton.setDisable(false));

			// Display context menu
			Bounds bounds = presetsButton.localToScreen(presetsButton.getLayoutBounds());
			menu.show(primaryStage, bounds.getMinX(), bounds.getMaxY());
		});

		// Disable 'presets' button if there are no presets
		presets.addListener((ListChangeListener<ParamSet>) change -> presetsButton.setDisable(presets.isEmpty()));

		// Button: preferences
		ImageButton preferencesButton =
				Images.imageButton(Images.ImageId.SETTINGS, tooltipText(PREFERENCES_STR, KEY_COMBO_EDIT_PREFERENCES));
		preferencesButton.setOnAction(event -> onEditPreferences());

		// Create pane for buttons
		HBox topButtonPane = new HBox(2.0, addOrUpdatePresetButton, presetsButton, preferencesButton);
		topButtonPane.setAlignment(Pos.CENTER_LEFT);
		topButtonPane.setPadding(TOP_BUTTON_PANE_PADDING);
		topButtonPane.setBorder(SceneUtils.createSolidBorder(getColour(PaneStyle.ColourKey.PANE_BORDER), Side.BOTTOM));
		topButtonPane.getStyleClass().add(StyleClass.TOP_BUTTON_PANE);
		GridPane.setHalignment(topButtonPane, HPos.LEFT);

		// Create control pane
		GridPane controlPane = new GridPane();
		controlPane.setHgap(CONTROL_H_GAP);
		controlPane.setVgap(CONTROL_V_GAP);
		controlPane.setAlignment(Pos.CENTER);
		controlPane.setPadding(CONTROL_PANE_PADDING);

		// Initialise column constraints
		ColumnConstraints column = new ColumnConstraints();
		column.setMinWidth(Region.USE_PREF_SIZE);
		column.setHalignment(HPos.RIGHT);
		controlPane.getColumnConstraints().add(column);

		column = new ColumnConstraints();
		column.setHalignment(HPos.LEFT);
		column.setHgrow(Priority.ALWAYS);
		controlPane.getColumnConstraints().add(column);

		column = new ColumnConstraints();
		column.setHalignment(HPos.LEFT);
		controlPane.getColumnConstraints().add(column);

		// Initialise row index
		int row = 0;

		// Create factory for category check boxes
		IFunction1<CheckBox, String> categoryCheckBoxFactory = text ->
		{
			CheckBox checkBox = new CheckBox(text);
			checkBox.setPadding(CATEGORY_CHECK_BOX_PADDING);
			checkBox.getStyleClass().add(StyleClass.CATEGORY_CHECK_BOX);
			if (styleManager.notUsingStyleSheet())
			{
				IProcedure0 update = () ->
				{
					String colourKey = (checkBox.isSelected() && !checkBox.isDisabled())
											? ColourKey.CATEGORY_CHECK_BOX_BACKGROUND_SELECTED
											: ColourKey.CATEGORY_CHECK_BOX_BACKGROUND;
					checkBox.setBackground(SceneUtils.createColouredBackground(getColour(colourKey)));
				};
				checkBox.disabledProperty().addListener(observable -> update.invoke());
				checkBox.selectedProperty().addListener(observable -> update.invoke());
			}
			else
			{
				checkBox.pseudoClassStateChanged(PSEUDO_CLASS_ENABLED, true);
				checkBox.disabledProperty().addListener((observable, oldDisabled, disabled) ->
						checkBox.pseudoClassStateChanged(PSEUDO_CLASS_ENABLED, !disabled));
			}
			return checkBox;
		};

		// Create check boxes for character categories and their 'required' flags
		categoryCheckBoxes = new EnumMap<>(CharCategory.class);
		categoryRequiredCheckBoxes = new EnumMap<>(CharCategory.class);
		for (CharCategory category : CHAR_CATEGORIES)
		{
			// Check box: category
			CheckBox categoryCheckBox = categoryCheckBoxFactory.invoke(category.toString());
			categoryCheckBox.setMaxWidth(Double.MAX_VALUE);
			categoryCheckBoxes.put(category, categoryCheckBox);

			// Disable 'required' check box if category check box is not selected
			CheckBox requiredCategoryCheckBox = categoryCheckBoxFactory.invoke(REQUIRED_STR);
			requiredCategoryCheckBox.disableProperty().bind(categoryCheckBox.selectedProperty().not());
			categoryRequiredCheckBoxes.put(category, requiredCategoryCheckBox);

			// Add components of row
			controlPane.addRow(row++, new Region(), categoryCheckBox, requiredCategoryCheckBox);
		}

		// Text field: included characters
		includeField = new TextField();
		includeField.setPrefColumnCount(INCLUDE_FIELD_NUM_COLUMNS);
		HBox.setHgrow(includeField, Priority.ALWAYS);

		// Pane: included characters
		HBox includePane = new HBox(2.0, includeField, clearButton(includeField, CLEAR_INCLUDE_STR));
		includePane.setAlignment(Pos.CENTER_LEFT);
		GridPane.setMargin(includePane, new Insets(0.0, -3.0, 0.0, 0.0));

		// Disable 'required' check box if 'include' field is empty
		includeRequiredCheckBox = categoryCheckBoxFactory.invoke(REQUIRED_STR);
		includeRequiredCheckBox.disableProperty().bind(includeField.textProperty().isEmpty());
		controlPane.addRow(row++, new Label(INCLUDE_STR), includePane, includeRequiredCheckBox);

		// Text field: excluded characters
		excludeField = new TextField();
		excludeField.setPrefColumnCount(EXCLUDE_FIELD_NUM_COLUMNS);
		HBox.setHgrow(excludeField, Priority.ALWAYS);

		// Pane: excluded characters
		HBox excludePane = new HBox(2.0, excludeField, clearButton(excludeField, CLEAR_EXCLUDE_STR));
		excludePane.setAlignment(Pos.CENTER_LEFT);
		GridPane.setMargin(excludePane, new Insets(0.0, -3.0, 0.0, 0.0));
		controlPane.addRow(row++, new Label(EXCLUDE_STR), excludePane);

		// Check box: unique characters
		uniqueCharsCheckBox = new CheckBox(UNIQUE_CHARS_STR);
		GridPane.setMargin(uniqueCharsCheckBox, CHECK_BOX_MARGINS);
		controlPane.add(uniqueCharsCheckBox, 1, row++);

		// Pane: password length
		passwordLengthPane = new IntegerRangePane(MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH, INITIAL_MIN_PASSWORD_LENGTH,
												  INITIAL_MAX_PASSWORD_LENGTH, true);
		GraphicButton linkButton = passwordLengthPane.linkButton();
		linkButton.setTooltipText(LINK_UNLINK_STR);
		GridPane.setColumnSpan(passwordLengthPane, 2);
		controlPane.addRow(row++, new Label(LENGTH_STR), passwordLengthPane);

		// Label: password
		OverlayLabel passwordLabel = new OverlayLabel.Primary();
		passwordLabel.setMinWidth(Region.USE_PREF_SIZE);
		passwordLabel.setMaxWidth(Double.MAX_VALUE);
		passwordLabel.setFont(Fonts.monoFont());
		passwordLabel.setEllipsisString("\u2026");
		passwordLabel.setPadding(PASSWORD_LABEL_PADDING);
		passwordLabel.setTextFill(getColour(ColourKey.PASSWORD_LABEL_TEXT));
		passwordLabel.setBackground(SceneUtils.createColouredBackground(
				getColour(ColourKey.PASSWORD_LABEL_BACKGROUND)));
		passwordLabel.setBorder(SceneUtils.createSolidBorder(getColour(ColourKey.PASSWORD_LABEL_BORDER)));
		Insets insets = passwordLabel.getInsets();
		passwordLabel.setPrefWidth(TextUtils.textWidthCeil(PASSWORD_LABEL_PROTOTYPE_TEXT) + insets.getLeft()
				+ insets.getRight());
		passwordLabel.setPopUpPadding(PASSWORD_LABEL_PADDING);
		passwordLabel.getStyleClass().add(StyleClass.PASSWORD_LABEL);
		HBox.setHgrow(passwordLabel, Priority.ALWAYS);

		// Pane: password
		HBox passwordPane = new HBox(CONTROL_H_GAP, Labels.hNoShrink(PASSWORD_STR), passwordLabel);
		passwordPane.setAlignment(Pos.CENTER);
		passwordPane.setPadding(PASSWORD_PANE_PADDING);
		passwordPane.setBorder(SceneUtils.createSolidBorder(getColour(PaneStyle.ColourKey.PANE_BORDER),
															Side.TOP, Side.BOTTOM));
		passwordPane.getStyleClass().add(StyleClass.PASSWORD_PANE);

		// Decode remaining configuration
		decodeConfig2(vars.config.getConfig());

		// Button: copy
		Button copyButton = Buttons.hNoShrink(COPY_STR);
		copyButton.setPadding(BUTTON_PADDING);
		copyButton.disableProperty().bind(passwordLabel.textProperty().isEmpty());
		copyButton.setOnAction(event ->
		{
			String text = passwordLabel.getText();
			if (!text.isEmpty())
			{
				try
				{
					ClipboardUtils.putTextThrow(text);
				}
				catch (BaseException e)
				{
					ErrorDialog.show(primaryStage, COPY_STR, e);
				}
			}
		});
		TooltipDecorator.addTooltip(copyButton, COPY_TOOLTIP);

		// Button: generate
		Button generateButton = Buttons.hExpansive(GENERATE_STR);
		generateButton.setPadding(BUTTON_PADDING);
		generateButton.setOnAction(event ->
		{
			try
			{
				// Validate included characters
				String chars = includeField.getText();
				for (int i = 0; i < chars.length(); i++)
				{
					char ch = chars.charAt(i);
					if (preferences.includeOnlyAscii() && !Generator.isValidCharacter(ch))
					{
						includeField.requestFocus();
						includeField.end();
						throw new BaseException(ErrorMsg.INVALID_CHARACTER, ch);
					}
				}

				// Create map of character categories and their 'required' flags
				Map<CharCategory, Boolean> categoriesRequired = new EnumMap<>(CharCategory.class);
				for (CharCategory category : categoryCheckBoxes.keySet())
				{
					if (categoryCheckBoxes.get(category).isSelected())
						categoriesRequired.put(category, categoryRequiredCheckBoxes.get(category).isSelected());
				}

				// Generate password and set it on label
				IntegerRange lengthRange = passwordLengthPane.range();
				String password = Generator.generatePassword(categoriesRequired, includeField.getText(),
															 includeRequiredCheckBox.isSelected(),
															 excludeField.getText(), uniqueCharsCheckBox.isSelected(),
															 lengthRange.lowerEndpoint(), lengthRange.upperEndpoint());
				passwordLabel.setText(password);
			}
			catch (BaseException e)
			{
				ErrorDialog.show(primaryStage, GENERATE_STR, e);
			}
		});
		TooltipDecorator.addTooltip(generateButton, GENERATE_TOOLTIP);

		// Button: exit
		Button exitButton = Buttons.hExpansive(EXIT_STR);
		exitButton.setPadding(BUTTON_PADDING);
		exitButton.setOnAction(event ->
				primaryStage.fireEvent(new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST)));

		// Create right button pane
		TilePane rightButtonPane = new TilePane(BUTTON_PANE_H_GAP, BUTTON_PANE_V_GAP, generateButton, exitButton);
		rightButtonPane.setPrefColumns(rightButtonPane.getChildren().size());
		rightButtonPane.setAlignment(Pos.CENTER_RIGHT);

		// Create button pane
		HBox buttonPane = new HBox(copyButton, FillerUtils.hBoxFiller(24.0), rightButtonPane);
		buttonPane.setAlignment(Pos.CENTER);
		buttonPane.setPadding(BUTTON_PANE_PADDING);

		// Create main pane
		VBox mainPane = new VBox(topButtonPane, controlPane, passwordPane, buttonPane);
		mainPane.setId(StyleConstants.NodeId.APP_MAIN_ROOT);

		// Create scene
		Scene scene = new Scene(mainPane);

		// Add accelerators to scene
		scene.getAccelerators().put(KEY_COMBO_ADD_OR_UPDATE_PRESET, this::onAddOrUpdatePreset);
		scene.getAccelerators().put(KEY_COMBO_EDIT_PRESETS,         this::onEditPresets);
		scene.getAccelerators().put(KEY_COMBO_EDIT_PREFERENCES,     this::onEditPreferences);
		scene.getAccelerators().put(KEY_COMBO_COPY,                 copyButton::fire);
		scene.getAccelerators().put(KEY_COMBO_GENERATE,             generateButton::fire);

		// Add style sheet to scene
		styleManager.addStyleSheet(scene);

		// Update images of image buttons when theme changes
		StyleManager.INSTANCE.themeProperty().addListener(observable ->
		{
			ImageData.updateImages();
			ImageDataButton.updateButtons();
		});

		// Set properties of main window
		primaryStage.setTitle(LONG_NAME + " " + versionStr);
		primaryStage.getIcons().addAll(Images.APP_ICON_IMAGES);

		// Set scene on main window
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();

		// When main window is shown, set its width and location after a delay
		primaryStage.setOnShown(event ->
		{
			// Get dimensions of window
			WindowDims dims = new WindowDims(primaryStage);

			// Set width of main window after a delay
			ExecUtils.afterDelay(getDelay(SystemPropertyKey.MAIN_WINDOW_DELAY_SIZE), () ->
			{
				// Update dimensions
				dims.update(false);

				// Set minimum dimensions of window
				dims.setMin(MAIN_WINDOW_MIN_WIDTH, 0.0);

				// Get size of window from saved state
				Dimension2D size = mainWindowState.getSize();

				// Set width of window
				if (size != null)
				{
					// Set width
					double width = size.getWidth();
					if (width <= 0.0)
						width = Math.max(MAIN_WINDOW_MIN_WIDTH, dims.w());
					primaryStage.setMinWidth(width);
					primaryStage.setWidth(width);

					// Set height (required for Linux/GNOME)
					double height = primaryStage.getHeight();
					primaryStage.setMinHeight(height);
					primaryStage.setHeight(height);
				}

				// Set location of main window after a delay
				ExecUtils.afterDelay(getDelay(SystemPropertyKey.MAIN_WINDOW_DELAY_LOCATION), () ->
				{
					// Get location of window from saved state
					Point2D location = mainWindowState.getLocation();

					// Invalidate location if top centre of window is not within a screen
					double width = primaryStage.getWidth();
					if ((location != null) && !SceneUtils.isWithinScreen(location.getX() + 0.5 * width, location.getY(),
																		 SCREEN_MARGINS))
						location = null;

					// If there is no location, centre window within primary screen
					if (location == null)
						location = SceneUtils.centreInScreen(width, primaryStage.getHeight());

					// Set location of window
					primaryStage.setX(location.getX());
					primaryStage.setY(location.getY());

					// Perform remaining initialisation after a delay
					ExecUtils.afterDelay(getDelay(SystemPropertyKey.MAIN_WINDOW_DELAY_OPACITY), () ->
					{
						// Set minimum width of window
						primaryStage.setMinWidth(MAIN_WINDOW_MIN_WIDTH);

						// Prevent height of window from changing
						double height = primaryStage.getHeight();
						primaryStage.setMinHeight(height);
						primaryStage.setMaxHeight(height);

						// Make window visible
						primaryStage.setOpacity(1.0);

						// Request focus on 'include' field
						includeField.requestFocus();

						// Report any configuration error
						if (vars.configException != null)
							ErrorDialog.show(primaryStage, SHORT_NAME + " : " + CONFIG_ERROR_STR, vars.configException);
					});
				});
			});
		});

		// Write configuration file when main window is closed
		if (vars.config != null)
		{
			primaryStage.setOnHiding(event ->
			{
				// Update state of main window
				mainWindowState.restoreAndUpdate(primaryStage);

				// Write configuration
				if (vars.config.canWrite())
				{
					try
					{
						// Encode configuration
						encodeConfig(vars.config.getConfig());

						// Write configuration file
						vars.config.write();
					}
					catch (FileException e)
					{
						ErrorDialog.show(primaryStage, SHORT_NAME + " : " + CONFIG_ERROR_STR, e);
					}
				}
			});
		}

		// Display main window
		primaryStage.show();
	}

	//------------------------------------------------------------------

////////////////////////////////////////////////////////////////////////
//  Instance methods
////////////////////////////////////////////////////////////////////////

	private void encodeConfig(
		MapNode	rootNode)
	{
		// Clear properties
		rootNode.clear();

		// Encode theme ID
		String themeId = StyleManager.INSTANCE.getThemeId();
		if (themeId != null)
			rootNode.addMap(PropertyKey.APPEARANCE).addString(PropertyKey.THEME, themeId);

		// Encode state of main window
		MapNode windowStateNode = mainWindowState.encodeTree();
		if (!windowStateNode.isEmpty())
			rootNode.add(PropertyKey.MAIN_WINDOW, windowStateNode);

		// Encode 'include only ASCII' flag
		rootNode.addBoolean(PropertyKey.INCLUDE_ONLY_ASCII, preferences.includeOnlyAscii());

		// Encode parameters
		if (preferences.saveCharParams())
			rootNode.add(PropertyKey.PARAMETERS, paramSet("").encode());

		// Encode password-length range
		if (preferences.savePasswordLength())
		{
			MapNode lengthNode = rootNode.addMap(PropertyKey.PASSWORD_LENGTH);
			IntegerRange range = passwordLengthPane.range();
			lengthNode.addInts(PropertyKey.RANGE, range.lowerEndpoint(), range.upperEndpoint());
			lengthNode.addBoolean(PropertyKey.LINKED, passwordLengthPane.linkButton().isSelected());
		}

		// Encode presets
		ListNode presetsNode = rootNode.addList(PropertyKey.PRESETS);
		for (ParamSet preset : presets)
			presetsNode.add(preset.encode());
	}

	//------------------------------------------------------------------

	private void decodeConfig(
		MapNode	rootNode)
	{
		// Decode theme ID
		String key = PropertyKey.APPEARANCE;
		if (rootNode.hasMap(key))
		{
			String themeId = rootNode.getMapNode(key).getString(PropertyKey.THEME, StyleManager.DEFAULT_THEME_ID);
			StyleManager.INSTANCE.selectThemeOrDefault(themeId);
		}

		// Decode state of main window
		key = PropertyKey.MAIN_WINDOW;
		if (rootNode.hasMap(key))
			mainWindowState.decodeTree(rootNode.getMapNode(key));

		// Decode 'include only ASCII' flag
		preferences.includeOnlyAscii(rootNode.getBoolean(PropertyKey.INCLUDE_ONLY_ASCII, true));

		// Set 'save character parameters' flag
		preferences.saveCharParams(rootNode.hasMap(PropertyKey.PARAMETERS));

		// Set 'save password length' flag
		preferences.savePasswordLength(rootNode.hasMap(PropertyKey.PASSWORD_LENGTH));
	}

	//------------------------------------------------------------------

	private void decodeConfig2(
		MapNode	rootNode)
	{
		// Decode parameters
		String key = PropertyKey.PARAMETERS;
		if (rootNode.hasMap(key))
			setParams(ParamSet.decode(rootNode.getMapNode(key)));

		// Decode password-length range
		key = PropertyKey.PASSWORD_LENGTH;
		if (rootNode.hasMap(key))
		{
			MapNode lengthNode = rootNode.getMapNode(key);
			key = PropertyKey.RANGE;
			if (lengthNode.hasList(key))
			{
				List<IntNode> nodes = lengthNode.getListNode(key).intNodes();
				if (nodes.size() >= 2)
					passwordLengthPane.setRange(nodes.get(0).getValue(), nodes.get(1).getValue());
			}
			key = PropertyKey.LINKED;
			if (lengthNode.hasBoolean(key))
				passwordLengthPane.linkButton().setSelected(lengthNode.getBoolean(key));
		}

		// Decode presets
		key = PropertyKey.PRESETS;
		if (rootNode.hasList(key))
		{
			ListNode presetsNode = rootNode.getListNode(key);
			for (MapNode node : presetsNode.mapNodes())
				presets.add(ParamSet.decode(node));
		}
		else
			presets.addAll(DEFAULT_PRESETS);
	}

	//------------------------------------------------------------------

	private ParamSet paramSet(
		String	name)
	{
		Map<CharCategory, Boolean> categories = new EnumMap<>(CharCategory.class);
		for (CharCategory category : categoryCheckBoxes.keySet())
		{
			if (categoryCheckBoxes.get(category).isSelected())
				categories.put(category, categoryRequiredCheckBoxes.get(category).isSelected());
		}

		String include = includeField.getText();
		boolean includeRequired = include.isEmpty() ? false : includeRequiredCheckBox.isSelected();

		String exclude = excludeField.getText();
		boolean uniqueChars = uniqueCharsCheckBox.isSelected();

		return new ParamSet(name, categories, include, includeRequired, exclude, uniqueChars);
	}

	//------------------------------------------------------------------

	private void setParams(
		ParamSet	params)
	{
		for (CharCategory category : categoryCheckBoxes.keySet())
		{
			categoryCheckBoxes.get(category).setSelected(params.categoriesRequired.containsKey(category));
			categoryRequiredCheckBoxes.get(category)
					.setSelected(params.categoriesRequired.getOrDefault(category, false));
		}

		includeField.requestFocus();
		if (params.include.isEmpty())
		{
			includeField.clear();
			includeRequiredCheckBox.setSelected(false);
		}
		else
		{
			includeField.setText(params.include);
			includeField.end();
			includeRequiredCheckBox.setSelected(params.includeRequired);
		}

		// Set text of 'exclude' field
		excludeField.setText(params.exclude);

		// Select 'unique characters' check box
		uniqueCharsCheckBox.setSelected(params.uniqueChars);
	}

	//------------------------------------------------------------------

	private void onAddOrUpdatePreset()
	{
		// Display dialog for name of preset
		List<String> names = presets.stream().map(preset -> preset.name).toList();
		String name = new NameDialog(primaryStage, ADD_OR_UPDATE_PRESET_STR, names, null).showDialog();
		if (name == null)
			return;

		// Test for name conflict
		for (int i = 0; i < presets.size(); i++)
		{
			if (name.equals(presets.get(i).name))
			{
				String message = String.format(ErrorMsg.CONFLICTING_PRESET_NAME, name) + "\n" + UPDATE_QUESTION_STR;
				if (ConfirmationDialog.show(primaryStage, UPDATE_PRESET_STR, MessageIcon32.QUESTION.get(), message,
											UPDATE_STR))
					presets.set(i, paramSet(name));
				return;
			}
		}

		// Create preset and add it to list
		presets.add(paramSet(name));
	}

	//------------------------------------------------------------------

	private void onEditPresets()
	{
		if (!presets.isEmpty())
		{
			List<ParamSet> result = new PresetDialog(primaryStage, EDIT_PRESETS_STR, presets).showDialog();
			if (result != null)
				presets.setAll(result);
		}
	}

	//------------------------------------------------------------------

	private void onEditPreferences()
	{
		Preferences result = PreferencesDialog.show(primaryStage, preferences);
		if (result != null)
			preferences = result;
	}

	//------------------------------------------------------------------

////////////////////////////////////////////////////////////////////////
//  Member classes : non-inner classes
////////////////////////////////////////////////////////////////////////


	// CLASS: CONFIGURATION


	/**
	 * This class implements the configuration of the application.
	 */

	private static class Configuration
		extends AppConfig
	{

	////////////////////////////////////////////////////////////////////
	//  Constants
	////////////////////////////////////////////////////////////////////

		/** The identifier of a configuration file. */
		private static final	String	ID	= "BFQZMT70299Y36BFWH3ODZUQP";

	////////////////////////////////////////////////////////////////////
	//  Constructors
	////////////////////////////////////////////////////////////////////

		/**
		 * Creates a new instance of the configuration of the application.
		 *
		 * @throws BaseException
		 *           if the configuration directory could not be determined.
		 */

		private Configuration()
			throws BaseException
		{
			// Call superclass constructor
			super(ID, NAME_KEY, SHORT_NAME, LONG_NAME);

			// Determine location of config file
			if (!noConfigFile())
			{
				// Get location of parent directory of config file
				AppAuxDirectory.Directory directory =
						AppAuxDirectory.getDirectory(NAME_KEY, getClass().getEnclosingClass());
				if (directory == null)
					throw new BaseException(ErrorMsg.NO_AUXILIARY_DIRECTORY);

				// Set parent directory of config file
				setDirectory(directory.location());
			}
		}

		//--------------------------------------------------------------

	}

	//==================================================================


	// CLASS: SET OF PARAMETERS FOR PASSWORD GENERATION


	private static class ParamSet
		implements Cloneable
	{

	////////////////////////////////////////////////////////////////////
	//  Constants
	////////////////////////////////////////////////////////////////////

		private static final	String	UNNAMED_STR	= "unnamed";

	////////////////////////////////////////////////////////////////////
	//  Class variables
	////////////////////////////////////////////////////////////////////

		private static	int	unnamedIndex;

	////////////////////////////////////////////////////////////////////
	//  Instance variables
	////////////////////////////////////////////////////////////////////

		private	String						name;
		private	Map<CharCategory, Boolean>	categoriesRequired;
		private	String						include;
		private	boolean						includeRequired;
		private	String						exclude;
		private	boolean						uniqueChars;

	////////////////////////////////////////////////////////////////////
	//  Constructors
	////////////////////////////////////////////////////////////////////

		private ParamSet(
			String						name,
			Map<CharCategory, Boolean>	categoriesRequired,
			String						include,
			boolean						includeRequired,
			String						exclude,
			boolean						uniqueChars)
		{
			// Initialise instance variables
			this.name = name;
			this.categoriesRequired = new EnumMap<>(categoriesRequired);
			this.include = include;
			this.includeRequired = includeRequired;
			this.exclude = exclude;
			this.uniqueChars = uniqueChars;
		}

		//--------------------------------------------------------------

	////////////////////////////////////////////////////////////////////
	//  Class methods
	////////////////////////////////////////////////////////////////////

		private static ParamSet of(
			String				name,
			Set<CharCategory>	categories,
			String				include)
		{
			Map<CharCategory, Boolean> categoriesRequired = new EnumMap<>(CharCategory.class);
			for (CharCategory category : categories)
				categoriesRequired.put(category, false);

			return new ParamSet(name, categoriesRequired, include, false, "", false);
		}

		//--------------------------------------------------------------

		private static ParamSet decode(
			MapNode	rootNode)
		{
			// Decode name
			String name = rootNode.getString(PropertyKey.NAME, "<" + UNNAMED_STR + ++unnamedIndex + ">");

			// Decode map of character categories and their 'required' flags
			Map<CharCategory, Boolean> categoriesRequired = new EnumMap<>(CharCategory.class);
			String key = PropertyKey.CATEGORIES;
			if (rootNode.hasList(key))
			{
				for (MapNode node : rootNode.getListNode(key).mapNodes())
				{
					CharCategory category = CharCategory.forKey(node.getString(PropertyKey.ID, ""));
					if (category != null)
						categoriesRequired.put(category, node.getBoolean(PropertyKey.REQUIRED, false));
				}
			}

			// Decode included characters
			String include = rootNode.getString(PropertyKey.INCLUDE, "");

			// Decode 'included characters required' flag
			boolean includeRequired = include.isEmpty()
											? false
											: rootNode.getBoolean(PropertyKey.INCLUDE_REQUIRED, false);

			// Decode excluded characters
			String exclude = rootNode.getString(PropertyKey.EXCLUDE, "");

			// Decode 'unique chars' flag
			boolean uniqueChars = rootNode.getBoolean(PropertyKey.UNIQUE_CHARS, false);

			// Create new set of parameters and return it
			return new ParamSet(name, categoriesRequired, include, includeRequired, exclude, uniqueChars);
		}

		//--------------------------------------------------------------

	////////////////////////////////////////////////////////////////////
	//  Instance methods : overriding methods
	////////////////////////////////////////////////////////////////////

		@Override
		public ParamSet clone()
		{
			try
			{
				ParamSet copy = (ParamSet)super.clone();
				copy.categoriesRequired = new EnumMap<>(categoriesRequired);
				return copy;
			}
			catch (CloneNotSupportedException e)
			{
				throw new UnexpectedRuntimeException(e);
			}
		}

		//--------------------------------------------------------------

	////////////////////////////////////////////////////////////////////
	//  Instance methods
	////////////////////////////////////////////////////////////////////

		private MapNode encode()
		{
			// Create root node
			MapNode rootNode = new MapNode();

			// Encode name
			if (!name.isEmpty())
				rootNode.addString(PropertyKey.NAME, name);

			// Encode map of character categories and their 'required' flags
			ListNode categoriesNode = rootNode.addList(PropertyKey.CATEGORIES);
			for (CharCategory category : categoriesRequired.keySet())
			{
				MapNode categoryNode = new MapNode();
				categoryNode.addString(PropertyKey.ID, category.key());
				if (categoriesRequired.get(category))
					categoryNode.addBoolean(PropertyKey.REQUIRED, true);
				categoriesNode.add(categoryNode);
			}

			// Create function to remove duplicate characters from string
			IFunction1<String, String> removeDuplicates = str ->
			{
				StringBuilder buffer = new StringBuilder(str.length());
				for (int i = 0; i < str.length(); i++)
				{
					char ch = str.charAt(i);
					boolean found = false;
					for (int j = 0; j < i; j++)
					{
						if (str.charAt(j) == ch)
						{
							found = true;;
							break;
						}
					}
					if (!found)
						buffer.append(ch);
				}
				return buffer.toString();
			};

			// Encode included characters and 'included characters required' flag
			if (!include.isEmpty())
			{
				rootNode.addString(PropertyKey.INCLUDE, removeDuplicates.invoke(include));
				if (includeRequired)
					rootNode.addBoolean(PropertyKey.INCLUDE_REQUIRED, true);
			}

			// Encode excluded characters
			if (!exclude.isEmpty())
				rootNode.addString(PropertyKey.EXCLUDE, removeDuplicates.invoke(exclude));

			// Encode 'unique chars' flag
			if (uniqueChars)
				rootNode.addBoolean(PropertyKey.UNIQUE_CHARS, true);

			// Return root node
			return rootNode;
		}

		//--------------------------------------------------------------

	}

	//==================================================================


	// CLASS: NAME DIALOG


	/**
	 * This class implements a modal dialog in which a name can be edited.
	 */

	private static class NameDialog
		extends SimpleModalDialog<String>
	{

	////////////////////////////////////////////////////////////////////
	//  Constants
	////////////////////////////////////////////////////////////////////

		/** The maximum length of a name. */
		private static final	int		MAX_NAME_LENGTH	= 40;

		/** The gap between adjacent horizontal components. */
		private static final	double	CONTROL_H_GAP	= 6.0;

		/** The preferred number of columns of the name field. */
		private static final	int		NAME_FIELD_NUM_COLUMNS	= 32;

		/** The padding around the control pane. */
		private static final	Insets	CONTROL_PANE_PADDING	= new Insets(4.0, 2.0, 4.0, 4.0);

		/** Miscellaneous strings. */
		private static final	String	NAME_STR		= "Name";
		private static final	String	CLEAR_NAME_STR	= "Clear name";

	////////////////////////////////////////////////////////////////////
	//  Instance variables
	////////////////////////////////////////////////////////////////////

		/** The result of this dialog. */
		private	String	result;

	////////////////////////////////////////////////////////////////////
	//  Constructors
	////////////////////////////////////////////////////////////////////

		/**
		 * Creates a new instance of a modal dialog in which a name can be edited.
		 *
		 * @param owner
		 *          the owner of the dialog, or {@code null} if the dialog has no owner.
		 * @param title
		 *          the title of the dialog.
		 * @param names
		 *          a list of names that will be set on the list view of the combo box.
		 * @param name
		 *          the initial name.
		 */

		private NameDialog(
			Window			owner,
			String			title,
			List<String>	names,
			String			name)
		{
			// Call superclass constructor
			super(owner, MethodHandles.lookup().lookupClass().getCanonicalName(), title);

			// Allow dialog to be resized
			setResizable(true);

			// Set style class on root node of scene graph
			getScene().getRoot().getStyleClass().add(StyleClass.PRESET_NAME_DIALOG_ROOT);

			// Create text field or combo box for name
			TextField field = null;
			Region nameComponent = null;
			if (CollectionUtils.isNullOrEmpty(names))
			{
				// Create text field
				field = new TextField(name);
				nameComponent = field;
			}
			else
			{
				// Create combo box
				SimpleComboBox<String> nameComboBox =
						new SimpleComboBox<>(SimpleComboBox.IDENTITY_STRING_CONVERTER, names)
				{
					@Override
					public void commitValue()
					{
						// do nothing
					}
				};
				if (name != null)
					nameComboBox.text(name);
				nameComboBox.commitOnFocusLost(true);
				nameComboBox.setMaxWidth(Double.MAX_VALUE);
				field = nameComboBox.textField();
				nameComponent = nameComboBox;
			}
			HBox.setHgrow(nameComponent, Priority.ALWAYS);

			// Set properties of name field
			TextField nameField = field;
			nameField.setPrefColumnCount(NAME_FIELD_NUM_COLUMNS);
			nameField.setTextFormatter(new TextFormatter<>(FilterFactory.lengthLimiter(MAX_NAME_LENGTH)));

			// Button: clear name field
			GraphicButton clearNameButton = clearButton(nameField, CLEAR_NAME_STR);
			HBox.setMargin(clearNameButton, new Insets(0.0, 0.0, 0.0, -4.0));

			// Create control pane
			HBox controlPane = new HBox(CONTROL_H_GAP, Labels.hNoShrink(NAME_STR), nameComponent, clearNameButton);
			controlPane.setAlignment(Pos.CENTER);
			controlPane.setPadding(CONTROL_PANE_PADDING);

			// Add control pane to content pane
			addContent(controlPane);

			// Create button: OK
			Button okButton = Buttons.hNoShrink(OK_STR);
			okButton.getProperties().put(BUTTON_GROUP_KEY, BUTTON_GROUP1);
			okButton.setOnAction(event ->
			{
				// Set result
				result = nameField.getText().strip();

				// Close dialog
				hide();
			});
			addButton(okButton, HPos.RIGHT);

			// Create procedure to disable 'OK' button if name is invalid
			IProcedure0 updateOkButton = () -> okButton.setDisable(StringUtils.isNullOrBlank(nameField.getText()));

			// Disable 'OK' button if name is invalid
			nameField.textProperty().addListener(observable -> updateOkButton.invoke());

			// Fire 'OK' button when 'Enter' key is pressed in name field
			nameField.setOnAction(event -> okButton.fire());

			// Update 'OK' button
			updateOkButton.invoke();

			// Create button: cancel
			Button cancelButton = Buttons.hNoShrink(CANCEL_STR);
			cancelButton.getProperties().put(BUTTON_GROUP_KEY, BUTTON_GROUP1);
			cancelButton.setOnAction(event -> requestClose());
			addButton(cancelButton, HPos.RIGHT);

			// Fire 'cancel' button if Escape key is pressed; fire 'OK' button if Ctrl+Enter is pressed
			setKeyFireButton(cancelButton, okButton);

			// When dialog is shown, select content of name field
			setOnShown(event -> Platform.runLater(() -> nameField.selectAll()));

			// Apply style sheet to scene
			applyStyleSheet();
		}

		//--------------------------------------------------------------

	////////////////////////////////////////////////////////////////////
	//  Instance methods : overriding methods
	////////////////////////////////////////////////////////////////////

		/**
		 * Prevents the height of this dialog from changing.
		 */

		@Override
		protected void onWindowShown()
		{
			// Call superclass method
			super.onWindowShown();

			// Prevent height of window from changing
			double height = prefHeight();
			setMinHeight(height);
			setMaxHeight(height);
		}

		//--------------------------------------------------------------

		/**
		 * {@inheritDoc}
		 */

		@Override
		protected String getResult()
		{
			return result;
		}

		//--------------------------------------------------------------

	}

	//==================================================================


	// CLASS: DIALOG FOR EDITING PRESETS


	private static class PresetDialog
		extends SimpleModalDialog<List<ParamSet>>
	{

	////////////////////////////////////////////////////////////////////
	//  Constants
	////////////////////////////////////////////////////////////////////

		/** The preferred width of the list view. */
		private static final	double	LIST_VIEW_WIDTH		= 240.0;

		/** The preferred height of the list view. */
		private static final	double	LIST_VIEW_HEIGHT	= 240.0;

		/** The padding around the list-view editor. */
		private static final	Insets	LIST_VIEW_EDITOR_PADDING	= new Insets(2.0);

		/** Miscellaneous strings. */
		private static final	String	RENAME_PRESET_STR			= "Rename preset";
		private static final	String	REMOVE_PRESET_STR			= "Remove preset";
		private static final	String	REMOVE_PRESET_QUESTION_STR	=
				"Preset: %s" + MessageConstants.LABEL_SEPARATOR + "Do you want to remove the preset?";
		private static final	String	REMOVE_STR					= "Remove";

	////////////////////////////////////////////////////////////////////
	//  Instance variables
	////////////////////////////////////////////////////////////////////

		/** The result of this dialog. */
		private	List<ParamSet>	result;

	////////////////////////////////////////////////////////////////////
	//  Constructors
	////////////////////////////////////////////////////////////////////

		private PresetDialog(
			Window			owner,
			String			title,
			List<ParamSet>	presets)
		{
			// Call superclass constructor
			super(owner, MethodHandles.lookup().lookupClass().getCanonicalName(), title);

			// Create list view of presets
			Window window = this;
			SimpleTextListView<ParamSet> listView = new SimpleTextListView<>(presets, preset -> preset.name);
			listView.setPrefSize(LIST_VIEW_WIDTH, LIST_VIEW_HEIGHT);
			listView.setMaxWidth(Region.USE_PREF_SIZE);
			if (!presets.isEmpty())
				listView.getSelectionModel().select(0);

			// Create editor for list view of presets
			ListViewEditor<ParamSet> listViewEditor = new ListViewEditor<>(listView, new ListViewEditor.IEditor<>()
			{
				@Override
				public Set<ListViewEditor.Action> getActions()
				{
					return EnumSet.of(ListViewEditor.Action.EDIT, ListViewEditor.Action.REMOVE,
									  ListViewEditor.Action.MOVE_UP, ListViewEditor.Action.MOVE_DOWN);
				}

				@Override
				public ParamSet edit(
					ListViewEditor.Action	action,
					ParamSet				preset)
				{
					// Display dialog for preset name
					String name = new NameDialog(window, RENAME_PRESET_STR, List.of(), preset.name).showDialog();
					if (name == null)
						return null;

					// Test for name conflict
					for (ParamSet prst : presets)
					{
						if (name.equals(prst.name))
						{
							if (prst != preset)
							{
								ErrorDialog.show(window, RENAME_PRESET_STR,
												 String.format(ErrorMsg.CONFLICTING_PRESET_NAME, name));
							}
							return null;
						}
					}

					// Create preset with new name and return it
					ParamSet newPreset = preset.clone();
					newPreset.name = name;
					return newPreset;
				}

				@Override
				public boolean canRemoveWithKeyPress()
				{
					return true;
				}

				@Override
				public boolean confirmRemove(
					ParamSet	preset)
				{
					return ConfirmationDialog.show(window, REMOVE_PRESET_STR, MessageIcon32.QUESTION.get(),
												   String.format(REMOVE_PRESET_QUESTION_STR, preset.name), REMOVE_STR);
				}
			},
			false);
			listViewEditor.setAlignment(Pos.CENTER);
			listViewEditor.setPadding(LIST_VIEW_EDITOR_PADDING);

			// Add list-view editor cto content pane
			addContent(listViewEditor);

			// Create button: OK
			Button okButton = Buttons.hNoShrink(OK_STR);
			okButton.getProperties().put(BUTTON_GROUP_KEY, BUTTON_GROUP1);
			okButton.setOnAction(event ->
			{
				// Set result
				result = listViewEditor.getItems();

				// Close dialog
				hide();
			});
			addButton(okButton, HPos.RIGHT);

			// Create button: cancel
			Button cancelButton = Buttons.hNoShrink(CANCEL_STR);
			cancelButton.getProperties().put(BUTTON_GROUP_KEY, BUTTON_GROUP1);
			cancelButton.setOnAction(event -> requestClose());
			addButton(cancelButton, HPos.RIGHT);

			// Fire 'cancel' button if Escape key is pressed; fire 'OK' button if Ctrl+Enter is pressed
			setKeyFireButton(cancelButton, okButton);

			// Apply style sheet to scene
			applyStyleSheet();
		}

		//--------------------------------------------------------------

	////////////////////////////////////////////////////////////////////
	//  Instance methods : overriding methods
	////////////////////////////////////////////////////////////////////

		/**
		 * {@inheritDoc}
		 */

		@Override
		protected List<ParamSet> getResult()
		{
			return result;
		}

		//--------------------------------------------------------------

	}

	//==================================================================

}

//----------------------------------------------------------------------
