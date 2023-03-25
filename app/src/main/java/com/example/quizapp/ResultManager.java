package com.example.quizapp;

import android.content.Context;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ResultManager {
    String file = "results.txt";
    FileOutputStream fos;
    FileInputStream fis;


    public String[] openResults(Context context) {
        String[] results = new String[2];
        try {
            fis = context.openFileInput(file);
            InputStreamReader inputStreamReader =
                    new InputStreamReader(fis, StandardCharsets.UTF_8);
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String L = reader.readLine();
                stringBuilder.append(L);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                results = stringBuilder.toString().split(" ");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return results;
    }



    public void saveResults(Context context, int correct, int totalNumOfQues) {
        String[] temp = openResults(context);
        if (!temp[0].equals("null")) {
            correct += Integer.parseInt(temp[0]);
            totalNumOfQues += Integer.parseInt(temp[1]);
        }
        String results = correct + " " + totalNumOfQues;
        try {
            fos = context.openFileOutput(file, Context.MODE_PRIVATE);
            fos.write(results.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteResults(Context context) {
        try {
            fos = context.openFileOutput(file, Context.MODE_PRIVATE);
            fos.write(("").getBytes());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }  finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

