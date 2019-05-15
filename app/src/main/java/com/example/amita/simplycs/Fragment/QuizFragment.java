package com.example.amita.simplycs.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Chronometer;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.amita.simplycs.Adapter.QuizWrapper;
import com.example.amita.simplycs.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizFragment extends Fragment
{

    Chronometer simpleChronometer;
    TextView submit,duration,ques,total_ques;
    int time=0;
    View rootview;


    private TextView previousButton,nextButton;

    private WebView quizQuestion;

    private RadioGroup radioGroup;

    private RadioButton radioButton;


    private RadioButton optionOne;

    private RadioButton optionTwo;

    private RadioButton optionThree;

    private RadioButton optionFour;

    private int currentQuizQuestion;

    private int quizCount;

    int quesCount=1;

    private QuizWrapper firstQuestion;

    private List<QuizWrapper> parsedObject;

    String Test_id,test_duration,test_marks;
    String URL;
    String User_id;
    public static final String PREFS_NAME = "login";


    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootview = inflater.inflate(R.layout.fragment_quiz, container, false);

        Toolbar toolbar = rootview.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getFragmentManager().popBackStack();
            }
        });

        SharedPreferences sp = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        User_id = sp.getString("User", "");
        URL = getString(R.string.url);

        Bundle bundle=getArguments();
        Test_id=String.valueOf(bundle.getString("test_id"));
        test_duration=String.valueOf(bundle.getString("test_duration"));
        test_marks=String.valueOf(bundle.getString("test_marks"));

        Initialize();

        simpleChronometer.start();

        total_ques.setText("/"+test_marks);

        new CountDownTimer(81200000, 1000) {

            public void onTick(long millisUntilFinished) {
                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;

                long elapsedHours = millisUntilFinished / hoursInMilli;
                millisUntilFinished = millisUntilFinished % hoursInMilli;

                long elapsedMinutes = millisUntilFinished / minutesInMilli;
                millisUntilFinished = millisUntilFinished % minutesInMilli;

                long elapsedSeconds = millisUntilFinished / secondsInMilli;
                millisUntilFinished = millisUntilFinished % secondsInMilli;

                String yy = String.format("%02d:%02d", elapsedMinutes, elapsedSeconds);
                duration.setText(yy);
            }

            public void onFinish() {
                duration.setText("Done!");
            }


        }.start();


        AsyncJsonObject asyncObject = new AsyncJsonObject();

        asyncObject.execute("");



        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int radioSelected = radioGroup.getCheckedRadioButtonId();


                radioButton = (RadioButton)rootview.findViewById(radioSelected);


                String userSelection = (String) radioButton.getText();

                int correctAnswerForQuestion = firstQuestion.getCorrectAnswer();

//                Toast.makeText(getActivity(),userSelection, Toast.LENGTH_LONG).show();

                if(userSelection.equals(String.valueOf(correctAnswerForQuestion))){

                    // correct answer

                    Toast.makeText(getActivity(), "You got the answer correct", Toast.LENGTH_LONG).show();

                    currentQuizQuestion++;

                    quesCount++;

                    ques.setText(String.valueOf(quesCount));

                    if(currentQuizQuestion >= quizCount){

                        Toast.makeText(getActivity(), "End of the Quiz Questions", Toast.LENGTH_LONG).show();

                        return;

                    }

                    else{

                        firstQuestion = parsedObject.get(currentQuizQuestion);



                        quizQuestion.loadData(firstQuestion.getQuestion(), "text/html", null);

                        String[] possibleAnswers = firstQuestion.getAnswers().split(",");

                        uncheckedRadioButton();

                        optionOne.setText(possibleAnswers[0]);

                        optionTwo.setText(possibleAnswers[1]);

                        optionThree.setText(possibleAnswers[2]);

                        optionFour.setText(possibleAnswers[3]);

                    }

                }

                else{

                    // failed question

                    Toast.makeText(getActivity(), "You chose the wrong answer", Toast.LENGTH_LONG).show();

                    return;

                }


            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentQuizQuestion--;

                if(currentQuizQuestion < 0){

                    return;

                }

//                uncheckedRadioButton();

                firstQuestion = parsedObject.get(currentQuizQuestion);

                quizQuestion.loadData(firstQuestion.getQuestion(), "text/html", null);

                String[] possibleAnswers = firstQuestion.getAnswers().split(",");

                optionOne.setText(possibleAnswers[0]);

                optionTwo.setText(possibleAnswers[1]);

                optionThree.setText(possibleAnswers[2]);

                optionFour.setText(possibleAnswers[3]);

                quesCount--;

                ques.setText(String.valueOf(quesCount));


            }
        });



        rootview.setFocusableInTouchMode(true);
        rootview.requestFocus();
        rootview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // DO WHAT YOU WANT ON BACK PRESSED
                    getFragmentManager().popBackStack();
                    return true;
                } else {
                    return false;
                }
            }
        });

        return rootview;
    }

    public void Initialize(){

        submit=(TextView)rootview.findViewById(R.id.submit);
        duration=(TextView)rootview.findViewById(R.id.timer);
        simpleChronometer = (Chronometer)rootview.findViewById(R.id.simpleChronometer);

        ques = (TextView)rootview.findViewById(R.id.ques);
        total_ques = (TextView)rootview.findViewById(R.id.total_ques);

        quizQuestion = (WebView) rootview.findViewById(R.id.quiz_question);

        radioGroup = (RadioGroup)rootview.findViewById(R.id.radioGroup);

        optionOne = (RadioButton)rootview.findViewById(R.id.radio0);

        optionTwo = (RadioButton)rootview.findViewById(R.id.radio1);

        optionThree = (RadioButton)rootview.findViewById(R.id.radio2);

        optionFour = (RadioButton)rootview.findViewById(R.id.radio3);

        previousButton = (TextView)rootview.findViewById(R.id.prevques);

        nextButton = (TextView)rootview.findViewById(R.id.nextques);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }



    private class AsyncJsonObject extends AsyncTask<String, Void, String> {

        private ProgressDialog progressDialog;

        @Override

        protected String doInBackground(String... params) {

            HttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());

            HttpPost httpPost = new HttpPost(URL+"api/QuestionList");

            httpPost.setHeader("Auth",User_id);
//            httpPost.setParams("Testid",params);

            List<NameValuePair> postParameters = new ArrayList<>();
            postParameters.add(new BasicNameValuePair("TestId", Test_id));
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(postParameters, HTTP.UTF_8));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            String jsonResult = "";

            try {

                HttpResponse response = httpClient.execute(httpPost);

                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();

                System.out.println("Returned Json object " + jsonResult.toString());

            } catch (ClientProtocolException e) {

// TODO Auto-generated catch block

                e.printStackTrace();

            } catch (IOException e) {

// TODO Auto-generated catch block

                e.printStackTrace();

            }

            return jsonResult;

    }

        @Override

        protected void onPreExecute() {

        // TODO Auto-generated method stub

            super.onPreExecute();

            progressDialog = ProgressDialog.show(getActivity(), "Downloading Quiz","Wait....", true);

        }

        @Override

        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            progressDialog.dismiss();

            System.out.println("Resulted Value: " + result);

            parsedObject = returnParsedJsonObject(result);

            if(parsedObject == null){

                return;

            }

            quizCount = parsedObject.size();

            firstQuestion = parsedObject.get(0);

            quizQuestion.loadData(firstQuestion.getQuestion(), "text/html", null);

            String[] possibleAnswers = firstQuestion.getAnswers().split(",");

            optionOne.setText(possibleAnswers[0]);

            optionTwo.setText(possibleAnswers[1]);

            optionThree.setText(possibleAnswers[2]);

            optionFour.setText(possibleAnswers[3]);

        }

        private StringBuilder inputStreamToString(InputStream is) {

            String rLine = "";

            StringBuilder answer = new StringBuilder();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            try {

                while ((rLine = br.readLine()) != null) {

                    answer.append(rLine);

                }

            } catch (IOException e) {

// TODO Auto-generated catch block

                e.printStackTrace();

            }

            return answer;

        }

    }

    private List<QuizWrapper> returnParsedJsonObject(String result){

        List<QuizWrapper> jsonObject = new ArrayList<QuizWrapper>();

        JSONObject resultObject = null;

        JSONArray jsonArray = null;

        QuizWrapper newItemObject = null;

        try {

            resultObject = new JSONObject(result);

            System.out.println("Testing the water " + resultObject.toString());

            jsonArray = resultObject.optJSONArray("test_questions");

        } catch (JSONException e) {

            e.printStackTrace();

        }

        if (jsonArray!=null) {

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonChildNode = null;

                try {

                    jsonChildNode = jsonArray.getJSONObject(i);

                    int id = jsonChildNode.getInt("id");

                    String question = jsonChildNode.getString("question");

                    String answerOptions = jsonChildNode.getString("possible_answers");

                    int correctAnswer = jsonChildNode.getInt("correct_answer");

                    newItemObject = new QuizWrapper(id, question, answerOptions, correctAnswer);

                    jsonObject.add(newItemObject);

                } catch (JSONException e) {

                    e.printStackTrace();

                }

            }
        }

        return jsonObject;

    }


    private int getSelectedAnswer(int radioSelected){

        int answerSelected = 0;

        if(radioSelected == R.id.radio0){

            answerSelected = 1;

        }

        if(radioSelected == R.id.radio1){

            answerSelected = 2;

        }

        if(radioSelected == R.id.radio2){

            answerSelected = 3;

        }

        if(radioSelected == R.id.radio3){

            answerSelected = 4;

        }

        return answerSelected;

    }

    private void uncheckedRadioButton(){

        optionOne.setChecked(false);

        optionTwo.setChecked(false);

        optionThree.setChecked(false);

        optionFour.setChecked(false);

    }


}
