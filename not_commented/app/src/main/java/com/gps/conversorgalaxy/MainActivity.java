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
    final int MAX_INPUT_ZONE = 4;
    final String APP_PREFERENCES = "App preferences";
    final String LIST_OPTION = "List Option";
    final String SEND_DELETE = "Send Delete";
    final String SELECTED_LIST_NAME = "Selected list name";

    CheckBox classic, g2, g3, sendDeleteCheckBox, list;
    ScrollView scrollView;
    TextView result, resultsRecord, input, sendDeleteText, helpTextWindow;
    Button leftCorner, rightCorner;
    Double i;
    String selectedList;
    String selectedConversion;
    int modifier = 0;
    TreeSet<String> listSetClassic;
    TreeSet<String> listSetG2;
    TreeSet<String> listSetDimension;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    //Done Copy the font size auto-change for different api configuration into blue notes project and make blue notes.
    //Done Copy the layout xml file in blue notes project

    //Done Check if the changes in end constraints in list checkbox, and start constraints in classic checkbox, to right guide line fits nice
    //Done in old phone, if not, change them back to parent one.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        classic = findViewById(R.id.classic);
        g2 = findViewById(R.id.g2);
        g3 = findViewById(R.id.Dimension);
        sendDeleteCheckBox = findViewById(R.id.sendDeleteCheckBox);
        list = findViewById(R.id.list);
        scrollView = findViewById(R.id.verticalScrollView);
        result = findViewById(R.id.result);
        resultsRecord = findViewById(R.id.resultsRecord);
        input = findViewById(R.id.input);
        sendDeleteText = findViewById(R.id.sendDeleteTextView);
        helpTextWindow = findViewById(R.id.helpTextWindow);
        leftCorner = findViewById(R.id.leftCorner);
        rightCorner = findViewById(R.id.rightCorner);
        listSetClassic = new TreeSet<>();
        listSetG2 = new TreeSet<>();
        listSetDimension = new TreeSet<>();
        sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        loadingState();

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
        editor.putBoolean(LIST_OPTION, list.isChecked());
        editor.putBoolean(SEND_DELETE, sendDeleteCheckBox.isChecked());
        editor.putString(SELECTED_LIST_NAME, selectedList);
        editor.apply();
    }

    void loadingState() {
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
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    void mainEngine() {
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
        String message = getString(R.string.microtech_start) + x +
                selectedList + y;
        if (list.isChecked()) {
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
        switch (selectedList) {
            case " - classic: ":
                modifier = 0;
                break;
            case " - g2: ":
                if ((0 < i) && (i < 5))
                    modifier = 0;
                else if (selectedConversion.equals("galaxy") && ((4 < i) && (i < 45))) {
                    modifier = 4;
                } else if (selectedConversion.equals("normal") && ((8 < i) && (i < 49))) {
                    modifier = 4;
                } else {
                    modifier = -i;
                }
                break;
            case " - Dimension: ":
                if (((16 < i) && (i < 33)) && selectedConversion.equals("galaxy"))
                    modifier = -i;
                else if ((16 < i) && (i < 529))
                    modifier = -16;
                else
                    modifier = 0;
                break;
        }
    }

    void galaxyZones(double i) {
        if (i == 0)
            result.setText(R.string.fail_message);
        else {
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
        if ((selectedList.equals(getString(R.string.g2_end))) && ((1058 < i)
                || ((1004 < i) && (i <= 1008))))
            result.setText(R.string.fail_message);
        else {
            StringBuilder stringBuilder = new StringBuilder(i.toString());
            String substring = stringBuilder.substring(1, 3);
            int j = Integer.parseInt(substring) * 2;
            substring = stringBuilder.substring(3, 4);
            int k = Integer.parseInt(substring);
            int y;
            int x = i.intValue();
            boolean succeed = true;
            if ((k == 0) || (k == 9)) {
                result.setText(R.string.fail_message);
                succeed = false;
            }
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

    void send() {
        mainEngine();
        input.setText("");
        scrollViewToBottom();
        saveState();
    }

    void delete() {
        String s = input.getText().toString();
        final int SUBSTRING_START = 0;
        int subStringEnd = s.length() - 1;
        StringBuilder stringBuilder = new StringBuilder(s);
        String subString = stringBuilder.substring(SUBSTRING_START, subStringEnd);
        input.setText(subString);
    }

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
            sendDeleteText.setText(getString(R.string.send_delete));
            leftCorner.setBackgroundResource(R.drawable.ic_baseline_send_24);
            rightCorner.setBackgroundResource(R.drawable.ic_baseline_backspace_24);
        } else {
            sendDeleteText.setText(getString(R.string.borrar_enviar));
            leftCorner.setBackgroundResource(R.drawable.ic_baseline_backspace_24);
            rightCorner.setBackgroundResource(R.drawable.ic_baseline_send_24);
        }
    }

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

    void listEngine() {
        if (list.isChecked())
            result.setText(R.string.hint_for_listCheck_true);
        else
            result.setText(R.string.hint_for_listCheck_false);
    }

    public void list(View view) {
        listEngine();
        saveState();
    }

    public void sendDeleteCheckBox(View view) {
        sendDeleteSwapper();
        saveState();
    }

    public void help(View view) {
        if (helpTextWindow.getVisibility() == View.VISIBLE)
            helpTextWindow.setVisibility(View.INVISIBLE);
        else
            helpTextWindow.setVisibility(View.VISIBLE);
    }
}