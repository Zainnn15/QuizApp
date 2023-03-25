package com.example.quizapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Question[] questions = new Question[7];
    int[] colours = new int[7];
    Question currQuestion;
    int totalQuestions = 7;
    int array = 0;
    int progress = 0;
    String[] contents = new String[2];
    ResultManager FManager;

    Button start;
    Button trueB;
    Button falseB;
    ProgressBar questionsdone;

    int correct;
    int wrong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FManager = new ResultManager();

        start = findViewById(R.id.button3);
        trueB = findViewById(R.id.button);
        falseB = findViewById(R.id.button2);
        questionsdone = findViewById(R.id.progressBar);

        //question initialization
        questions[0] = new Question(getResources().getString(R.string.question1),true);
        questions[1] = new Question(getResources().getString(R.string.question2),true);
        questions[2] = new Question(getResources().getString(R.string.question3),true);
        questions[3] = new Question(getResources().getString(R.string.question4),true);
        questions[4] = new Question(getResources().getString(R.string.question5),true);
        questions[5] = new Question(getResources().getString(R.string.question6),false);
        questions[6] = new Question(getResources().getString(R.string.question7),true);


        //color initialization
        colours[0] = getResources().getColor(R.color.red);
        colours[1] = getResources().getColor(R.color.yellow);
        colours[2] = getResources().getColor(R.color.orange);
        colours[3] = getResources().getColor(R.color.teal_700);
        colours[4] = getResources().getColor(R.color.purple_200);
        colours[5] = getResources().getColor(R.color.purple_500);
        colours[6] = getResources().getColor(R.color.teal_700);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initQuiz();
            }
        });
        trueB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });
        falseB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        switch (item.getItemId()) {
            case R.id.average:
                contents = FManager.openResults(MainActivity.this);

                if(contents[0] == null || contents[0].equals("null")) {
                    builder.setMessage(R.string.openResultsError)
                            .setNegativeButton(R.string.okay, null);
                }
                else {
                    builder.setMessage(getResources().getString(R.string.openResultsMessage) + contents[0] + "/" + contents[1])
                            .setNegativeButton(R.string.okay, null);
                }
                AlertDialog averageDialog = builder.create();
                averageDialog.show();

                return true;
            case R.id.numOfQuest:
                NumberPicker numberPicker = new NumberPicker(MainActivity.this);
                numberPicker.setMaxValue(10);
                numberPicker.setMinValue(1);

                builder.setView(numberPicker)
                        .setTitle(R.string.changeNumTitle)
                        .setMessage(R.string.changeNumMessage)
                        .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                totalQuestions = numberPicker.getValue();
                                initQuiz();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int j) {
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            case R.id.delete:
                FManager.deleteResults(MainActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void initQuiz() {
        start.setText(getResources().getString(R.string.button3Restart));
        Collections.shuffle(Arrays.asList(questions));
        shuffleColours(colours);
        currQuestion = questions[0];
        QuestionsFragment.question.setText(currQuestion.text);
        QuestionsFragment.question.setBackgroundColor(colours[0]);
        progress = 0;
        array = 0;
        correct = 0;
        wrong = 0;
        questionsdone.setProgress(progress);
    }

    public void shuffleColours(int[] colourArray) {
        Random rand = new Random();
        for (int i = 0; i < colourArray.length; i++) {
            int randomIndex = rand.nextInt(colourArray.length);
            int temp = colourArray[randomIndex];
            colourArray[randomIndex] = colourArray[i];
            colourArray[i] = temp;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.quiz_settings, menu);
        return true;
    }


    public void checkAnswer(boolean val) {
        if(currQuestion == null) {
            Toast.makeText(getApplicationContext(), R.string.trueFalseError, Toast.LENGTH_LONG).show();
            return;
        }
        else if (val == currQuestion.answer) {
            correct++;
            Toast.makeText(getApplicationContext(), R.string.right, Toast.LENGTH_SHORT).show();
        }
        else {
            wrong++;
            Toast.makeText(getApplicationContext(), R.string.wrong, Toast.LENGTH_SHORT).show();
        }
        array++;
        if (array < totalQuestions) {
            currQuestion = questions[array];
            QuestionsFragment.question.setText(currQuestion.text);
            QuestionsFragment.question.setBackgroundColor(colours[array]);
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(getResources().getString(R.string.completionMessagePart1) + " " + correct + " " +
                            getResources().getString(R.string.completionMessagePart2)  + " " + totalQuestions + " " +
                            getResources().getString(R.string.completionMessagePart3))
                    .setTitle(R.string.completionTitle)
                    .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            FManager.saveResults(MainActivity.this, correct, totalQuestions);
                            initQuiz();
                        }
                    })
                    .setNegativeButton(R.string.okay,  new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            initQuiz();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        progress += (100/totalQuestions);
        questionsdone.setProgress(progress);
    }

}