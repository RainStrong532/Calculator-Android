package com.strength.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
//import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    AppCompatButton btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9,
            btn_dot, btn_equal, btn_plus, btn_minus, btn_mul, btn_div, btn_clear;

    String workings = "";
    String result = "";
    String operators = "";
    TextView txt_workings, txt_result;

    private final String TAG = MainActivity.class.toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_0 = findViewById(R.id.btn_zero);
        btn_1 = findViewById(R.id.btn_one);
        btn_2 = findViewById(R.id.btn_two);
        btn_3 = findViewById(R.id.btn_three);
        btn_4 = findViewById(R.id.btn_four);
        btn_5 = findViewById(R.id.btn_five);
        btn_6 = findViewById(R.id.btn_six);
        btn_7 = findViewById(R.id.btn_seven);
        btn_8 = findViewById(R.id.btn_eight);
        btn_9 = findViewById(R.id.btn_nine);
        btn_plus = findViewById(R.id.btn_plus);
        btn_minus = findViewById(R.id.btn_minus);
        btn_div = findViewById(R.id.btn_divide);
        btn_mul = findViewById(R.id.btn_multiply);
        btn_clear = findViewById(R.id.btn_clear);
        btn_dot = findViewById(R.id.btn_dot);
        btn_equal = findViewById(R.id.btn_equal);

        txt_workings = findViewById(R.id.txt_workings);
        txt_result = findViewById(R.id.txt_result);

        txt_workings.setText(workings);
        txt_result.setText(result);


        btn_0.setOnClickListener((View view) -> addNumber(view));
        btn_1.setOnClickListener((View view) -> addNumber(view));
        btn_2.setOnClickListener((View view) -> addNumber(view));
        btn_3.setOnClickListener((View view) -> addNumber(view));
        btn_4.setOnClickListener((View view) -> addNumber(view));
        btn_5.setOnClickListener((View view) -> addNumber(view));
        btn_6.setOnClickListener((View view) -> addNumber(view));
        btn_7.setOnClickListener((View view) -> addNumber(view));
        btn_8.setOnClickListener((View view) -> addNumber(view));
        btn_9.setOnClickListener((View view) -> addNumber(view));

        btn_dot.setOnClickListener((View view) -> addDot());

        btn_plus.setOnClickListener((View view) -> addOperator(view));
        btn_minus.setOnClickListener((View view) -> addOperator(view));
        btn_mul.setOnClickListener((View view) -> addOperator(view));
        btn_div.setOnClickListener((View view) -> addOperator(view));

        btn_equal.setOnClickListener((View view) -> calculate());

        btn_clear.setOnClickListener((View view) -> clear());
    }

    private void addDot() {
        char last = Character.MIN_VALUE;
        if (!workings.isEmpty()) {
            last = workings.charAt(workings.length() - 1);
        }
        if (last != Character.MIN_VALUE && isNumber(last)) {
            workings += ".";
            txt_workings.setText(workings);
        }
    }

    private void addOperator(View view) {
        char last = Character.MIN_VALUE;
        if (!workings.isEmpty()) {
            last = workings.charAt(workings.length() - 1);
        }

        String res = txt_result.getText().toString();
        if (last != Character.MIN_VALUE && isNumber(last)) {
            if(res.length() > 0 ){
                workings = res;
                operators = "";
            }
            switch (view.getId()) {
                case R.id.btn_plus:
                    workings += "+";
                    operators += "+";
                    break;
                case R.id.btn_minus:
                    workings += "-";
                    operators += "-";
                    break;
                case R.id.btn_multiply:
                    workings += "x";
                    operators += "x";
                    break;
                case R.id.btn_divide:
                    workings += "/";
                    operators += "/";
                    break;
            }
            txt_workings.setText(workings);
        }
    }

    private boolean isNumber(char last) {
        String pattern = "[0-9]";
        return Pattern.matches(pattern, String.valueOf(last));
    }

    private String calculating(String s) {
        String[] mathString = s.split("[+\\-x/]");
        ArrayList<BigDecimal> numbers = new ArrayList<>();
        BigDecimal res = BigDecimal.valueOf(0);

        for (int i = 0; i < mathString.length; i++) {
            BigDecimal tmp = new BigDecimal(mathString[i]);
            numbers.add(tmp);
        }

        String op = operators;
        while (op.contains("x") || op.contains("/")) {
            int a = op.indexOf("x");
            int b = op.indexOf("/");

            int c;
            if (a >= 0 && b >= 0) {
                c = Math.min(a, b);
            } else if (a >= 0) c = a;
            else c = b;

            BigDecimal tmp = calc(numbers.get(c), numbers.get(c + 1), op.charAt(c));
            numbers.set(c, tmp);
            numbers.remove(c + 1);
            String sTmp = op.substring(0, c) + op.substring(c + 1);
            op = sTmp;

        }
        while (!op.isEmpty()) {
            BigDecimal tmp = calc(numbers.get(0), numbers.get(1), op.charAt(0));
            numbers.set(0, tmp);
            numbers.remove(1);
            op = op.substring(1);
        }
        return String.valueOf(numbers.get(0));
    }

    private void calculate() {
        char last = Character.MIN_VALUE;
        if (!workings.isEmpty()) {
            last = workings.charAt(workings.length() - 1);
            if (isOperator(last)) {
                workings = workings.substring(0, workings.length() - 1);
                operators = operators.substring(0, operators.length() - 1);
                txt_workings.setText(workings);
            }
        }
        if (!workings.isEmpty()) {
            try {
                result = calculating(workings);
            } catch (Exception e) {
                result = e.getMessage();
            }
            txt_result.setText(result);
        }
    }


    private BigDecimal calc(BigDecimal se, BigDecimal fi, char c) {
        BigDecimal res = BigDecimal.valueOf(0);
        switch (c) {
            case '+':
                res = se.add(fi);
                break;
            case '-':
                res = se.subtract(fi);
                break;
            case 'x':
                res =  se.multiply(fi);
                break;
            case '/':
                res = se.divide(fi);
                break;
            default:
                break;
        }
        return res;
    }

    private boolean isOperator(char last) {
        if (last == '.' || last == '+' || last == '-' || last == 'x' || last == '/') return true;
        return false;
    }

    private void clear() {
        workings = "";
        result = "";
        operators = "";
        txt_workings.setText(workings);
        txt_result.setText(result);
    }

    private void addNumber(View view) {
        if(!txt_result.getText().toString().isEmpty()){
            txt_workings.setText("");
            txt_result.setText("");
            operators = "";
            workings = "";
            result = null;
        }
        switch (view.getId()) {
            case R.id.btn_zero:
                workings += "0";
                break;
            case R.id.btn_one:
                workings += "1";
                break;
            case R.id.btn_two:
                workings += "2";
                break;
            case R.id.btn_three:
                workings += "3";
                break;
            case R.id.btn_four:
                workings += "4";
                break;
            case R.id.btn_five:
                workings += "5";
                break;
            case R.id.btn_six:
                workings += "6";
                break;
            case R.id.btn_seven:
                workings += "7";
                break;
            case R.id.btn_eight:
                workings += "8";
                break;
            case R.id.btn_nine:
                workings += "9";
                break;
        }
        txt_workings.setText(workings);
    }
}