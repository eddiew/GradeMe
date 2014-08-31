package net.eddiew.grademe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by WangE on 8/30/2014.
 */
public class MathTest {
    String testName;
    MathQuestion[] questions = new MathQuestion[10];
    View testLayout;

    public MathTest(String name, int oper, Context c) {
        testLayout = LayoutInflater.from(c).inflate(R.layout.math_test_layout, null);
        testName = name;

        ArrayList<String> left = new ArrayList<String>();
        ArrayList<String> right = new ArrayList<String>();

        for(int i=0; i <questions.length; i++) {
            MathQuestion q = new MathQuestion(oper);
            questions[i] = q;

            if (i < questions.length / 2)
                left.add(q.body);
            else
                right.add(q.body);
        }

        ((ListView) testLayout.findViewById(R.id.listView)).setAdapter(new ArrayAdapter<String>(c,
                android.R.layout.simple_list_item_1, left));
        ((ListView) testLayout.findViewById(R.id.listView2)).setAdapter(new ArrayAdapter<String>(c,
                android.R.layout.simple_list_item_1, right));

        ((TextView) testLayout.findViewById(R.id.textView2)).setText(name);
    }

    public class MathQuestion {
        public String body;
        public int answer;

        public MathQuestion(int oper) {
            Random randGen = new Random();

            int first, last;
            first = randGen.nextInt(8)+1;
            last = randGen.nextInt(8)+1;

            switch (oper)
            {
                case 0:
                    answer = first + last;
                    body = first+" + "+last;
                    break;
                case 1:
                    answer = first - last;
                    body = first+" - "+last;
                    break;
                case 2:
                    answer = first * last;
                    body = first+" x "+last;
                    break;
                case 3:
                    answer = last;
                    body = first*last+" / "+first;
                    break;
            }
        }
    }
}
