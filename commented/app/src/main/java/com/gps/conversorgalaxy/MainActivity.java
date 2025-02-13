package com.gps.conversorgalaxy;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.widget.TextViewCompat;

import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {
    //This variable is used to set the maximum amount of digits for the typed number.
    final int MAX_INPUT_ZONE = 4;
    //Final variable to name the file where preferences will be saved.
    final String APP_PREFERENCES = "App preferences";
    /*Final Strings that will be used as id of the items that we will save in
    sharedPreferences. Deeply explained */
    final String LIST_OPTION = "List Option";
    final String SEND_DELETE = "Send Delete";
    final String SELECTED_LIST_NAME = "Selected list name";
    /*All checkboxes used. Classic, g2 and g3 are the checkboxes that will be
    used to select the list we need to work with. list will allow us to start and
    stop recording and sorting out the actions performed. sendDeleteCheckBox is the
    one that will allow us to swipe the actions of the bottom left and right corners.
     */
    CheckBox classic, g2, g3, sendDeleteCheckBox, list;
    //The scrollView that allows to scroll the
    ScrollView scrollView;
    /*All textViews used. result will print the converted value provided by the user,
    which will be displayed in the input text view. resultsRecord will append each
    conversion given by the system, and it will be placed in the scrollView that will
    allow us to scroll the textView without any problem. sendDeleteText will be displayed
    close to sendDeleteCheckBox to indicate its function and the actual state of that.
    helpTextWindow will display a help text explaining the basic instructions of the app.
   */
    TextView result, resultsRecord, input, sendDeleteText, helpTextWindow;
    /*Main action buttons. They will be used to send and delete a number typed in
    input textView.*/
    Button leftCorner, rightCorner;
    //Variable used to store the main number inserted.
    Double i;
    /*Variable to store the keywords for the system to set what kind of formulas must
    be applied when it is about to convert the number inserted.*/
    String selectedList;
    /*Variable to store the keywords for the system to set what kind of conversion the
     the user is looking for. If its value is "galaxy" it means that the user inserted
     a number lower than 1000, so a conversion from any one of the list classic, g2, or
     g3 to microtech is being looked for. If the number is higher than 1000, it will be
     upside down, the user wants a conversion from microtech to classic, g2, or g3.
     */
    String selectedConversion;
    /*The variable that we will use to modify the main formulas according to selectedList
    and selectedConversion.*/
    int modifier = 0;
    /*The tree sets that will be used to sort the numbers inserted by the user when the
    list option is set to true*/
    TreeSet<String> listSetClassic;
    TreeSet<String> listSetG2;
    TreeSet<String> listSetDimension;
    /*We start the sharedPreferences for the system, and the editor to handle them.*/
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    //DONE Make the left / right handed checker and the whole system.
    //DONE Change the icon of app
    //DONE Make the shared preferences system.
    //DONE Sign it at the end.
    //DONE work on the positioning of the checkers, not in chain mode, to make classic bigger
    //DONE Toma notas de     <group
    //        android:pivotX="12"
    //        android:pivotY="12"
    //        android:rotation="180">
    //DONE android:screenOrientation="portrait" you set the default orientation mode.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*A helper to extend the support of AppCompat on any activity, then we set the default
        night mode to the final AppCompatActivity's variable MODE_NIGHT_NO, which is an int variable
        equal to 1.*/
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //Mapping the object with its xml configuration:
        //CheckBoxes -
        classic = findViewById(R.id.classic);
        g2 = findViewById(R.id.g2);
        g3 = findViewById(R.id.Dimension);
        sendDeleteCheckBox = findViewById(R.id.enviarBorrarCheckBox);
        list = findViewById(R.id.list);
        //ScrollView -
        scrollView = findViewById(R.id.verticalScrollView);
        //TextViews -
        result = findViewById(R.id.result);
        resultsRecord = findViewById(R.id.resultsRecord);
        input = findViewById(R.id.input);
        sendDeleteText = findViewById(R.id.enviarBorrarTextView);
        helpTextWindow = findViewById(R.id.helpTextWindow);
        //Buttons -
        leftCorner = findViewById(R.id.leftCorner);
        rightCorner = findViewById(R.id.rightCorner);
        //Starting TreeSets.
        listSetClassic = new TreeSet<>();
        listSetG2 = new TreeSet<>();
        listSetDimension = new TreeSet<>();

        /*We start by making a new SharedPreference item that will load the specified
        preferences file, in the specified mode. MODE_PRIVATE sets this objet not reachable
        from outside of this app.*/
        sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        /*Once the whole app is set up, we load loadingState void to load the saved
        preferences or to load default values in case there are not preferences.*/
        loadingState();

        /*
        Next conditional will allow us to auto size the some textviews. Conditional is based on
        gathering the current version of the device anc comparing it with android version Oreo,
        which is the version where the xlm commands autoSizeMaxTextSize="100sp"
        autoSizeMinTextSize="1sp", autoSizeStepGranularity="1sp" and autoSizeTextType="uniform"
        were introduced.
        Once we are inside the conditional we cast a textview and we link it to customTitle layout
        textview. Then, we will use the TextViewCompat helper to perform the
        setAutoSizeTextTypeUniformWithConfiguration void over the specified textview, with the
        specified autoSizeTextType, the type of auto size. Must be one of AUTO_SIZE_TEXT_TYPE_NONE
        or AUTO_SIZE_TEXT_TYPE_UNIFORM. AUTO_SIZE_TEXT_TYPE_UNIFORM will auto size text, and
        AUTO_SIZE_TEXT_TYPE_NONE won't, plus the preferred configuration for autoSizeMinTextSize
        autoSizeMaxTextSize, and autoSizeStepGranularity.
        */
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            TextView customTitle = findViewById(R.id.customTitle);
            TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(customTitle,
                    1, 100, 1,
                    TypedValue.COMPLEX_UNIT_SP);
            TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(result,
                    1, 100, 1,
                    TypedValue.COMPLEX_UNIT_SP);
            TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(input,
                    1, 100, 1,
                    TypedValue.COMPLEX_UNIT_SP);
            TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(sendDeleteText,
                    1, 100, 1,
                    TypedValue.COMPLEX_UNIT_SP);
            TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(helpTextWindow,
                    1, 100, 1,
                    TypedValue.COMPLEX_UNIT_SP);
        }
    }

    void saveState() {
        /*This void will store in this app's shared preferences file the variables
        that must be loaded once the app starts.*/
        editor.putBoolean(LIST_OPTION, list.isChecked());
        editor.putBoolean(SEND_DELETE, sendDeleteCheckBox.isChecked());
        editor.putString(SELECTED_LIST_NAME, selectedList);
        editor.apply();
    }

    void loadingState() {
        /*The state of sendDeleteCheckBox will be set according to SEND_DELETE shared
        preferences, or to default value if there is not SEND_DELETE item. Same will happen with
        list and selectedList, which will load LIST_OPTION and SELECTED_LIST_NAME from shared
        preferences. After we load the preferences saved by the user, we will load a switch
        based on selected list, which will set the state of each one of the list checkboxes. If
        selectedList is equals to " - classic: ", this means that classic's checkbox should be
        selected, so it should not be clickable, and g2 and Dimension should be displayed as
        not checked. Finally, if sendDeleteCheckBox is checked, it will load SendDeleteSwapper(),
        (explained later), and if list is checked result will print R.string.hint_for_listCheck_true
        */
        sendDeleteCheckBox.setChecked(sharedPreferences.getBoolean(SEND_DELETE, false));
        list.setChecked(sharedPreferences.getBoolean(LIST_OPTION, false));
        selectedList = sharedPreferences.getString(SELECTED_LIST_NAME, getString(R.string.Dimension_end));
        editor.apply();
        switch (selectedList) {
            case " - classic: ":
                classic.setChecked(true);
                classic.setClickable(false);
                g2.setChecked(false);
                g3.setChecked(false);
                break;
            case " - g2: ":
                g2.setChecked(true);
                g2.setClickable(false);
                classic.setChecked(false);
                g3.setChecked(false);
                break;
            case " - Dimension: ":
                g3.setChecked(true);
                g3.setClickable(false);
                classic.setChecked(false);
                g2.setChecked(false);
                break;
        }
        if (sendDeleteCheckBox.isChecked())
            sendDeleteSwapper();
        if (list.isChecked())
            result.setText(R.string.hint_for_listCheck_true);
    }

    void scrollViewToBottom() {
        /*This method will scroll the scroll view that holds the resultsRecord to the bottom part.
        Since this method will be loaded outside of UI thread, we must implement a Runnable to be
        able to add this action to the queue of actions to be performed by the UI thread. Inside the
        implemented method run() we will scroll the scroll view.*/
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    void mainEngine() {
        /*
        First we start by making a new String variable that will store the input number. Then
        system will check if the length of that variable is bigger than 0 in order to keep
        with the code or not.
        If it is bigger we will turn input text into a Double type of variable, and then we will
        check if it is bigger or smaller than 1000. System needs to check this in order to know
        which kind of variables must be applied. If number is smaller than 1000 it means that the
        user wants to get the galaxy converted number, from a low number zone to its value in
        microtech, for example, input = 10, galaxy = 1012. If it is bigger than 1000, it means
        that user wants to get up side down situation. We will set selectedConversion to galaxy
        or to normal, according to the desired conversion that the user wants to achieve. This
        will be useful for the system when formulas are being applied.
         */
        String text = input.getText().toString();
        if (!text.isEmpty()) {
            i = Double.parseDouble(text);
            if (i < 1000) {
                selectedConversion = "galaxy";
                galaxyZones(i);
            } else {
                selectedConversion = "normal";
                normalZone(i);
            }
        }
    }

    void print(int x, int y) {
        /*Print void starts by storing in a String variable the message that will be printed
        in result textView. Then we check if list checkBox is checked, if it is not will
        print this message in result, and in resultsRecord + a line jump. If list is checked,
        then we get in main action.
        */
        String message = getString(R.string.microtech_start) + x +
                selectedList + y;
        if (list.isChecked()) {
            /*We start by making 3 boolean variables that will store the value returned by
            the .add() method of each type of TreeSet. At the begging of code we made 3
            treeset, each one with the name of each type of conversion; now, according to
            the actual selectedList value, we will store on them the message to be printed.
            This means that if the actual selected list is " - g2: ", then we will add
            message to listSetG2, if this message contains a string that was already saved
            previously, add() method won't save the message and will report false; if there
            is no coincidence, then it will report true. Then, we will check the value of
            these 3 boolean variables, if there's no true variable, it means that that the
            zone checked was checked previously, so a "zone already checked" message will be
            printed. If one of them is true, then we will load an enhanced for loop for each
            listset, printing their members and a line jump for each one. This way we will
            print all the zones that were checked from all the kinds of galaxy, everytime
            that the user types a non already checked zone. In order to perform all this
            action, we will use an StringBuilder. Since we need to be able to sort the
            messages according to its natural order, we can't just append, we need to sort
            the whole message that will be printed, and then we set it in resultsRecord. For
            that we append all messages stored in all treeSets in the StringBuilder, and then
            resultsRecord.setText(sB.toString()); finally, we will also print the message in
            result text view.
             */
            boolean classic = false, g2 = false, dimension = false;
            switch (selectedList) {
                case " - classic: ":
                    classic = listSetClassic.add(message);
                    break;
                case " - g2: ":
                    g2 = listSetG2.add(message);
                    break;
                case " - Dimension: ":
                    dimension = listSetDimension.add(message);
                    break;
            }
            if ((classic || g2 || dimension)) {
                StringBuilder sB = new StringBuilder();
                for (String s : listSetClassic) {
                    sB.append(s);
                    sB.append(getString(R.string.line_jump));
                }
                for (String s : listSetG2) {
                    sB.append(s);
                    sB.append(getString(R.string.line_jump));
                }
                for (String s : listSetDimension) {
                    sB.append(s);
                    sB.append(getString(R.string.line_jump));
                }
                resultsRecord.setText(sB.toString());
                result.setText(message);
            } else
                result.setText(R.string.zone_already_checked);
        } else {
            result.setText(message);
            resultsRecord.append(message + getString(R.string.line_jump));
        }
    }

    void selector(int i) {
        /*This void will help us to apply the modifiers needed to the formulas for microtech
        to classic and classic to microtech, to get the right microtech to g2 and g3, and from
        g2 and g3 to microtech conversions. The modifiers will be chosen according to
        selectedList values.
         */
        switch (selectedList) {
            case " - classic: ":
                //We don't apply modifiers because the formulas are thought for classic conversion
                modifier = 0;
                break;
            case " - g2: ":
                /*G2 is a galaxy whose zones miss the 1005-1008, and they end in 1058. This means
                that:
                 */
                if ((0 < i) && (i < 5))
                    /*From zone 0 non included, to 5 non included, modifier will be 0 because
                    the conversion will be like in classic: 1 = 1001, 2 = 1002 etc.
                     */
                    modifier = 0;
                else if (selectedConversion.equals("galaxy") && ((4 < i) && (i < 45))) {
                    /*If the user needs the Microtech value out of g2 one, we will first add 4 to
                    this g2 value, and then calculate the classic to microtech conversion for that
                    number. Since from 1005 to 1008 both included, there's no g2 value, we will
                    always be g2 = Classic - 4; so classic = g2 + 4.
                    g2 = 43 >>> 43 + 4 = 47 = Classic > Microtech: 1057
                     */
                    modifier = 4;
                } else if (selectedConversion.equals("normal") && ((8 < i) && (i < 49))) {
                    /*If the user needs the g2 value out of the microtech one, we will
                    need to calculate the microtech to classic, and then subtract 4 to classic's
                    value. Since from 1005 to 1008 both included, there's no g2 value, we will
                    always be g2 = Classic - 4.
                    Microtech: 1057, Classic = 47, g2 = 43
                    That's why the range of i in this conditional must be from 4 to 49 both not
                    included, because if selected conversion is equals to normal, and the
                    gotten i after converting is bigger or smaller than those ranges it will mean
                    that it will be or between 1005 to 1008, or bigger than the top limit:
                    Microtech: 1058, Classic = 48, g2 = 48
                     */
                    modifier = 4;
                } else {
                    /*Finally, if none of the previous situation is met, this will mean that
                    the modifier must be -i, in order to make i to be 0. We do this because when
                    the user needs the microtech value from normal one, the void loaded is
                    galaxyZones, and in this void the modifier is applied at the begging. If none
                    of the previous situations is met, then the number typed is bigger than 44,
                    and there's no number bigger than 44 and smaller than 1001 with a value in
                    microtech for this galaxy. Remember that if the number typed by user would
                    be bigger than 1000, then the void loaded would be normalZone, and the rules
                    followed would be the ones in previous else if in this switch case.
                     */
                    modifier = (int) -i;
                }
                break;
            case " - Dimension: ":
                /*G3, or AKA Dimension, is a galaxy whose zones miss from 17 to 32 both
                included. From 1001 = 1, to 1018 = 16, the conversion will be same than
                in classic; from 17 to 32 there wont be conversion to microtech, so, zone
                number 33 will be equals to next one to 1018, the number 1021. This will
                make us to be always +16 from classic:
                 */
                if (((16 < i) && (i < 33)) && selectedConversion.equals("galaxy"))
                    /*Since there's no conversion from 17 to 32, all numbers typed from 16
                     and 33 non included won't report result, so if this is the situation,
                     we will set modifier as -i, in order to get i = 0, to provoke the
                     galaxyZones to report there's no a galaxy zone with that number.
                     */
                    modifier = (int) -i;
                else if ((16 < i) && (i < 529))
                    /*Since in previous if we set the "&& selectedConversion.equals("galaxy")"
                    condition, the only situation where we will get a from 17 to 32 number
                    will be after gathering the classic converted number from a galaxy one, so,
                    since the formula in that part of normalZone void is y -= modifier; the result
                    will be  dimension = classic converted normal zone - ( -16) */
                    modifier = -16;
                else
                    /*All other situations will be considered equals than classic with classic
                    formulas limitation, in this case, form 1 to 16.
                     */
                    modifier = 0;
                break;
        }
    }

    void galaxyZones(double i) {
        /*This void will be used to get the microtech value from normal one of whatever list.
         */
        if (i == 0)
            /*There will never exist a zone number 0, so if the user types 0 "no zone with that
            number" will be reported.
            */
            result.setText(R.string.fail_message);
        else {
            /*System starts by casting the Double type of i, (which is needed for the normalZone()
            void), to an int, since we will work with integer values. Then we load selector(y)
            void to know the modifier to be added to i. Then we make two variables, j and x.
            We will use j to add to the final result the proper bus number according to the
            range of numbers where i belongs. X will be used to store the casted to int value of
            final result of i after formulas. We need to keep y = (int)i and x = (int)i separated
            because y will be used to represent the normal number of the classic/g2/dimension list,
            and x will be used to represent the microtech value of that type of system.
            Inside print void:

            String message = getString(R.string.microtech_start) + x +
                selectedList + y;

            We will make a boolean variable that will be set to true, and that will only be changed
            to false when the user typed a non existing zone, in which case, this variable will be
            used to avoid whole calculating/printing part.

            The organization of galaxy is about 16 blocks of 8 zones on each buss of the
            station, (1001-8; 1011-18; 1021-28 [...] 1151-58), so, what we have is groups of 128
            zones for each buss. The way galaxy has to identify the whole address of the zone
            is the next one:
            -Bus number:
              Bus 1 = 1000
              Bus 2 = 2000
              Bus 3 = 3000
              Bus 4 = 4000
            -Rio number:
              Rio 0 (main station) = 00
              Rio 1 = 01
              Rio 2 = 02
              [...]
              Rio 14 = 14
              Rio 15 = 15
            -Number of place in the board:
              Zone number 1 = 1
              Zone number 2 = 2
              [...]
              Zone number 8 = 8
            According to this, if you have a zone in the position 6, in the Rio 5, in the bus 3,
            you will know the Microtech zone:
            Bus 3  = 3000
            Rio 5  =  05
            Zone 3 =    3
            Result = 3053

            Since the formula for converting from normal value to microtech is always the same
            and the only thing that changes is the bus number:

            i (microtech zone) = j (bus number) + (Math.floor((i - 1) / 8) * 2) + i;

            Zone 1115 - 1000 = 115
            Zone 2115 - 2000 = 115

            What we do is: i - (128 * (n - 1)), where n is equals to the number of bus.

            If user types zone 129:
                128 < 129 < 256, so bus number 2
                129 - (128 * (2 - 1)) > 129 - 128 = 1.
                if (i % 8 == 0) = false (explained further)
                i  = j + (Math.floor(i / 8) * 2) + i
                i = 2000 + (Math.floor(1 / 8) * 2) + 1
                i = 2000 + (Math.floor(0) * 2) + 1
                i = 2000 + 0 + 1;
                i = 2001;

            We basically calculate the conversion like if we would be calculating the microtech
            zone from normal number in bus number 1, but changing the bus number. Because of that
            we subtract 128, 256 or 384 from i on each conditional below according to the number
            of zone, (we already explained we can know the number of bus from the number of zone).
            This conditional will work with whatever kind of system because:
            - Classic modifier = 0
            - G2 modifier 4 >>> g2 = Classic - 4 >>> Getting g2 to Microtech: classic = g2 + 4;
              classic to microtech.
            - G3 modifier -16 >>> g3 = Classic + 16 >>> Getting g3 to Microtech: classic = g3 - 16;
              classic to microtech.

            If i is not in anyone of these ranges, then it means that the zone doesn't exist, so
            we will load the else part and we will set succeed as false to avoid the calculating
            and printing part.
             */
            int y = (int) i;
            selector(y);
            i += modifier;
            int j = 0;
            int x;
            boolean succeed = true;
            if ((0 < i) && (i <= 128)) {
                j = 1000;
            } else if ((128 < i) && (i <= 256)) {
                i -= 128;
                j = 2000;
            } else if ((256 < i) && (i <= 384)) {
                i -= 256;
                j = 3000;
            } else if ((384 < i) && (i <= 512)) {
                i -= 384;
                j = 4000;
            } else {
                result.setText(R.string.fail_message);
                succeed = false;
            }
            if (succeed) {
                /*If succeed is still true it means that the user typed a valid zone, so
                we have to apply the formulas. Formulas will change according to the number
                of zone.
                As we know, zones are physically connected in groups of 8 members, and we know that
                we have 16 groups. We can see that we miss 2 numbers while counting zones in
                microtech numbers: 1001-1008, no number 1009 and 1010 since the ninth and tenth
                zones will be connected to first Rio in the position 1 and 2, so the zones 9 and 10,
                that should take the position 1009 and 1010, will be the microtech zones 1011 and
                1012. From now, the zone number 11, won't be the number 1011 in microtech, it will
                be the 1013, and so on till we reach the zone number 16 which is the number 1018 in
                microtech. Now, the zone 17 won't be the zone 1019, since it has to be connected to
                the next Rio in the position 1, the zone 17 will be the microtech zone 1021.
                The final reading is that we have to add to the normal zone n*2, where n is the
                number of groups of 8 zones.

                Normal zone 45:

                  45 / 2 = 5.625

                  It is needed 5.625 groups of 8 zones to store those 45 zones. 5 is the amount
                  of filled groups of 8 zones, and 0'625 is how filled is the last group. We will
                  multiply 5 * 2 because you will miss 2 for each filled group, we don't care
                  about 0.625 because it means that Rio 5 is still not complete not complete,
                  (remember that 5 filled groups means 8 zones in main station pcb, and 8 in 4
                  Rios, so 0.625 represents the capacity of Rio 5).

                  Groups:

                  (Group 0 = Station pcb; groups 1, 2, 3, 4, 5 = Rios)

                  0 -2 missed- 1 -2 missed- 2 -2 missed- 3 -2 missed- 4 -2 missed- 5 -2 missed-
                    -8 zones--   -8 zones--   -8 zones--   -8 zones--   -8 zones--   .5 zones..

                  It is underlined the amount of 2 missed for the amount of filled groups, it is
                  marked with points the "2 missed" we are not counting since the last group is
                  still not full because it is at the 0.625 of its capacity.

                  5 * 2 = 10
                  45 + 10 = 55
                  Bus 1 = 1000
                  1000 + 55 = 1055
                  Microtech = 1055 == Classic = 45.

                  This is the basic working principle, but there's a situation that must be
                  considered. In previous example, if instead of zone 45, we use zone 48:

                  48 / 8 = 6
                  6 * 2 = 12
                  48 + 12 = 60
                  Bus 1 = 1000
                  1000 + 60 = 1060
                  Microtech = 1060 =/= Classic = 48.

                  The right result would be 1058 = 48. The problem here appears because we are
                  counting the amount of times we have to multiply 2 according to the number of
                  filled groups of 8 zones. When we divide 48 / 8, we get that we filled 6 groups
                  so, it should be added 6*2 to the next zone, the number 49, not to the number 48.
                  In order to fix this part of the formula, we set a conditional using the Modulus
                  operator. The modulus operator finds the modulus of its first operand with
                  respect to the second.  That is, it produces the remainder of dividing the first
                  value by the second value.  For example:

                  22 % 6 = 4 because 22 / 6 = 3 with a remainder of 4

                  If reminder == 0, it means that the zone we are working with was stored in
                  the last spot of the last group of 8 zones, the last Rio, in which situation
                  we set i-1 when we divide by 8. We do this in order to multiply by 2 the number
                  of times we missed 2 while reaching that Rio:

                  Groups for 48 zones:
                  (Group 0 = Station pcb; groups 1, 2, 3, 4, 5 = Rios)
                  0 -2 missed- 1 -2 missed- 2 -2 missed- 3 -2 missed- 4 -2 missed- 5 -2 missed-
                    -8 zones--   -8 zones--   -8 zones--   -8 zones--   -8 zones--   -8 zones--
                  6 * 2 = 12
                  48 + 12 = 60 = error.

                  Groups for 48 - 1 zones:
                  (Group 0 = Station pcb; groups 1, 2, 3, 4, 5 = Rios)
                  0 -2 missed- 1 -2 missed- 2 -2 missed- 3 -2 missed- 4 -2 missed- 5 -2 missed-
                    -8 zones--   -8 zones--   -8 zones--   -8 zones--   -8 zones--   .7 zones..
                  5 * 2 = 10
                  48 + 10 = 58 = Right.
                  Bus 1 = 1000
                  1000 + 58 = 1058
                  Microtech = 1058 == Classic = 48.

                  Finally, we store in x the value of i casted to int, and we set as parameters
                  of print void x, which is the microtech result, and y which is the normal zone
                  number*/

                if (i % 8 == 0)
                    i = j + (Math.floor((i - 1) / 8) * 2) + i;
                else
                    i = j + (Math.floor(i / 8) * 2) + i;

                x = (int) i;
                print(x, y);
            }
        }
    }

    void normalZone(Double i) {
        /*This void will be used to gather the normal zone number from the microtech number. We
        will start by setting a conditional which check if the typed number is in the specified
        ranges of the g2 list. This is because this list ends in the microtech number 1058 and
        misses the zones 1005 to 1008. If the typed number meets these conditions a "There's no
        a zone with that number" message will be printed, otherwise we will get in the else.
        */
        if ((selectedList.equals(getString(R.string.g2_end))) && ((1058 < i)
                || ((1004 < i) && (i <= 1008))))
            result.setText(R.string.fail_message);
        else {
            /*Since we need to work with different parts of the number, we will make a
            StringBuilder from the toString part of the Double. Once we have the StringBuilder
            We will make a new String from the .substring(1, 3). We use those positions, 1 and 3,
            because we will be getting the position number 2 and 3 in the number; since we start
            counting from 0, the position number 1 in the example number 2468 will be 4, and the
            position 6 will be the number 3. Position 3 will be equals to number 6, instead of
            number 8, (we start counting from 0), because when we set the range in the
            .substring(x, y) the method includes the number x, but not the number y, so in order to
            set the end of the substring we have to set end position +1. By doing this we get the
            two middle numbers, which they actually are the number of Rio in use. From here we can
            easily get the amount of 2 zones we missed while counting zones, (Check the galaxyZones
            void to know the working principle for galaxy to number zones). Once we have this
            substring, we will store the number version of it, through Integer.parseInt(subString)
            multiplied by 2, in the int variable j.
            Then we store in the substring, the number in the position (3, 4), (which is the last
            number), from the stringBuilder, and will store the number version of it, through
            Integer.parseInt(subString), in the int variable K. Then we make 2 new int variables:
            y that will hold the normal number of zone gotten from the microtech number, and x that
            will hold the microtech number of zone casted to int to avoid the xxxx.xx format, to
            get just the left part of number xxxx.
            We will make a boolean variable that will be set to true, and that will only be changed
            to false when the user typed a non existing zone, in which case, this variable will be
            used to avoid whole calculating/printing part.
             */
            StringBuilder stringBuilder = new StringBuilder(i.toString());
            String substring = stringBuilder.substring(1, 3);
            int j = Integer.parseInt(substring) * 2;
            substring = stringBuilder.substring(3, 4);
            int k = Integer.parseInt(substring);
            int y;
            int x = i.intValue();
            boolean succeed = true;
            if ((k == 0) || (k == 9)) {
                /*If k is equals to 9 or 0, it means that the user typed a zone ended in those
                numbers, in which situation we print a "There is not a zone with that number",
                because according to the explanation given in the galaxyZones void, zones in
                galaxy are stored in main station pcb from 1 to 8, and the all others in Rios
                in groups of 8 zones. Finally succeed's value will be changed to false.
                 */
                result.setText(R.string.fail_message);
                succeed = false;
            }
            /* After first conditional system will perform an specific formula according to the
            range of i. We know that system adds to the number of zone the amount
            of times that 2 numbers were missed when the galaxy system numbers zones, multiplied
            by 2, when converting from normal to microtech, (check galaxyZones() explanations).
            In this kind of conversion, what it is also done is to subtract 128 * n, where n is
            the number of the bus - 1, because system gets the right part of the converted number
            through formulas applied for a classic to microtech conversion operation. Finally, the
            result is added to the proper bus value, example below:

            From Classic to microtech, zone 47:
            47 / 8 = 5.875
            5 * 2 = 10
            47 + 10 = 57
            Bus 1 = 1000
            1000 + 57= 1057
            Microtech = 1057 == Classic = 47.

            Now, for a conversion microtech to classic, system will perform the upside down action:

            From microtech to Classic, zone = 1154:
            Bus number 1 = 1000
            1154 - 1000 = 154
            j = 15 * 2 = 30
            154 - 30 = 124 = Right

            If i is not in any of the range set in the conditional, a "There is not a zone with
            that number" will be printed.
            Finally, if succeed is still true, we will store in y variable the intValue() of i,
            which will already be the converted to classic zone, and we will load selector with y
            as parameter, which will evaluate which kind of modifier will be applied to y, according
            to the selected list and the range of number. Then the void will end loading print void
            with x and y -= modifier as parameters.
             */
            else if ((1000 < i) && (i <= 1158)) {
                i = (i - 1000) - j;
            } else if ((2000 < i) && (i <= 2158)) {
                i = ((i - 2000) - j) + 128;
            } else if ((3000 < i) && (i <= 3158)) {
                i = ((i - 3000) - j) + 256;
            } else if ((4000 < i) && (i <= 4158)) {
                i = (((i - 4000) - j) + 384);
            } else {
                result.setText(R.string.fail_message);
                succeed = false;
            }
            if (succeed) {
                y = i.intValue();
                selector(y);
                y -= modifier;
                print(x, y);
            }
        }
    }

    //---------------------------------------------------------------------

    /*Voids below are the onClick voids for each number button. They have a conditional inside
    with its else. The working principle is easy: if the length of what's written in input
    textView is smaller than the value of MAX_INPUT_ZONE, which its value is 4, it will append
    the proper text, the number of the button. If this condition is not met, it means that the
    actual size of input is equals to MAX_INPUT_ZONE, in which situation instead of appending
    the number of the button, it will set as input text its button number. This is done to
    achieve the possibility to just keep typing without the need to delete x amount of numbers
    to fix any mistake while typing the zone.
     */
    public void one(View view) {
        if (input.getText().length() < MAX_INPUT_ZONE)
            input.append("1");
        else
            input.setText("1");
    }

    public void two(View view) {
        if (input.getText().length() < MAX_INPUT_ZONE)
            input.append("2");
        else
            input.setText("2");
    }

    public void tree(View view) {
        if (input.getText().length() < MAX_INPUT_ZONE)
            input.append("3");
        else
            input.setText("3");
    }

    public void four(View view) {
        if (input.getText().length() < MAX_INPUT_ZONE)
            input.append("4");
        else
            input.setText("4");
    }

    public void five(View view) {
        if (input.getText().length() < MAX_INPUT_ZONE)
            input.append("5");
        else
            input.setText("5");
    }

    public void six(View view) {
        if (input.getText().length() < MAX_INPUT_ZONE)
            input.append("6");
        else
            input.setText("6");
    }

    public void seven(View view) {
        if (input.getText().length() < MAX_INPUT_ZONE)
            input.append("7");
        else
            input.setText("7");
    }

    public void eight(View view) {
        if (input.getText().length() < MAX_INPUT_ZONE)
            input.append("8");
        else
            input.setText("8");
    }

    public void nine(View view) {
        if (input.getText().length() < MAX_INPUT_ZONE)
            input.append("9");
        else
            input.setText("9");
    }

    public void zero(View view) {
        if (input.getText().length() < MAX_INPUT_ZONE)
            input.append("0");
        else
            input.setText("0");
    }

    //---------------------------------------------------------------------

    void send() {
        /*This void will deal with the send action. It will load MainEngine, which will
        calculate/print everything. Then it will empty input's text field, to reload its
        hint, and once the scroll view that holds the resultsRecord gets scrolled to the
        bottom, it will load saveState void.*/
        mainEngine();
        input.setText("");
        scrollViewToBottom();
        saveState();
    }

    void delete() {
        /*This void will deal with the delete action. The idea is to delete just one character
        in the input's text field, everytime that the user presses on delete. It starts by
        storing in a String variable s, the string from inputs textview. Then we make 2 variables:
        first one will be final and will represent the starting point of the substring that will
        be made, the second one will be the final point of the substring that will be made. The
        second one will be equals to the s string length - 1; this is because what we will do
        will be to make a new StringBuilder with the s string, where we stored the input state, as
        parameter, and we will make a new substring with the dimensions of (SUBSTRING_START,
        subStringEnd). In this situation, the subString made will be equals to the s string but
        with one character less. Finally the substring will be printed in input through input's
        setText void.
         */
        String s = input.getText().toString();
        final int SUBSTRING_START = 0;
        int subStringEnd = s.length() - 1;
        StringBuilder stringBuilder = new StringBuilder(s);
        String subString = stringBuilder.substring(SUBSTRING_START, subStringEnd);
        input.setText(subString);
    }


    //---------------------------------------------------------------------
    /*These two voids are the onClick void for the two bottom corners buttons of the keyboard.
    Their action will depend on the state of sendDeleteCheckBox. By default, if sendDeleteCheckBox
    is not checked, the rightCorner will delete and the left corner will send. As soon as
    sendDeleteCheckBox is checked, both buttons will perform upside down action. The actions will
    be performed through send() and delete() voids. Since the rightCorner will be by default the
    one sending, we set the conditional related with sendDeleteCheckBox and the conditional for
    the length of input with the delete void action. In the other hand, for the leftCorner, the
    send() action will be bound to the sendDeleteCheckBox, and the delete() void will be under
    the length conditional. The length conditional controls that the input text length is bigger
    than 0 in order to perform delete() void, otherwise it would drop an exception of null pointer.

    Finally, both corners display an icon based on their actions. This icon will be swapped by
    sendDeleteSwapper. If sendDeleteCheckBox is checked, the left corner will display the send
    icon, the right one will display the backspace one, and the text of displayed by
    sendDeleteCheckBox will display Enviar / Borrar; if it is not checked, everything will go up
    side down.
     */

    public void rightCorner(View view) {
        if (sendDeleteCheckBox.isChecked() && (input.getText().length() > 0)) {
            delete();
        } else
            send();
    }

    public void leftCorner(View view) {
        if (sendDeleteCheckBox.isChecked())
            send();
        else {
            if (input.getText().length() > 0)
                delete();
        }
    }

    void sendDeleteSwapper() {
        if (sendDeleteCheckBox.isChecked()) {
            sendDeleteText.setText(getString(R.string.enviar_borrar));
            leftCorner.setBackgroundResource(R.drawable.ic_baseline_send_24);
            rightCorner.setBackgroundResource(R.drawable.ic_baseline_backspace_24);
        } else {
            sendDeleteText.setText(getString(R.string.borrar_enviar));
            leftCorner.setBackgroundResource(R.drawable.ic_baseline_backspace_24);
            rightCorner.setBackgroundResource(R.drawable.ic_baseline_send_24);
        }
    }

    //---------------------------------------------------------------------

    /*Next voids are the classic, g2 and Dimension onClick for their checkboxes. When they
    where clicked, checked, everyone of them will set themselves as not clickable anymore, (to
    avoid the situation where there is not any checkbox checked), and will set the other
    checkboxes as clickable and not checked. Then, selectedList will be updated with a text based
    on the checkbox, result will be set as empty, and saveState will be loaded to save the current
    state of the app.*/

    public void classic(View view) {
        classic.setClickable(false);
        g2.setClickable(true);
        g3.setClickable(true);
        g2.setChecked(false);
        g3.setChecked(false);
        selectedList = getString(R.string.classic_end);
        result.setText("");
        saveState();
    }

    public void g2(View view) {
        g2.setClickable(false);
        classic.setClickable(true);
        classic.setChecked(false);
        g3.setClickable(true);
        g3.setChecked(false);
        selectedList = getString(R.string.g2_end);
        result.setText("");
        saveState();
    }

    public void Dimension(View view) {
        g3.setClickable(false);
        classic.setClickable(true);
        classic.setChecked(false);
        g2.setClickable(true);
        g2.setChecked(false);
        selectedList = getString(R.string.Dimension_end);
        result.setText("");
        saveState();
    }

    //---------------------------------------------------------------------

    void listEngine() {
        /*This void will change the message printed in result according to the actual state of list
        checkbox. If list is checked, it will print a message to notify the user that the zones
        introduced will be sorted and that, if the zone introduced was already stored, it will be
        reported. If list is not checked, then it will report that there won't be more reporting
        and sorting.*/
        if (list.isChecked())
            result.setText(R.string.hint_for_listCheck_true);
        else
            result.setText(R.string.hint_for_listCheck_false);
    }

    public void list(View view) {
        //onClick method for list checkBox. System wil load listEngine() void and saveState() void.
        listEngine();
        saveState();
    }

    public void sendDeleteCheckBox(View view) {
        /*onClick method for sendDeleteCheckBox. It will load sendDeleteSwapper void, and saveState
        void.
        */
        sendDeleteSwapper();
        saveState();
    }

    public void help(View view) {
        /*This void is the onClick method for the help button, and for helpTextWindow. It will
        change the visibility of helpTextView according to its actual state. When its state is
        visible, it will turn it into INVISIBLE, and upside down.
        */
        if (helpTextWindow.getVisibility() == View.VISIBLE)
            helpTextWindow.setVisibility(View.INVISIBLE);
        else
            helpTextWindow.setVisibility(View.VISIBLE);
    }
}