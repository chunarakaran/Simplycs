package com.example.amita.simplycs.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.amita.simplycs.Adapter.QuizWrapper;
import com.example.amita.simplycs.R;

import java.util.List;

public class QuizFragment extends Fragment
{

    private TextView quizQuestion;

    private RadioGroup radioGroup;

    private RadioButton optionOne;

    private RadioButton optionTwo;

    private RadioButton optionThree;

    private RadioButton optionFour;

    Button previousButton,nextButton;

    private int currentQuizQuestion;

    private int quizCount;

    private QuizWrapper firstQuestion;

    private List<QuizWrapper> parsedObject;

    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootview = inflater.inflate(R.layout.fragment_quiz, container, false);

        Initialization();

//        AsyncJsonObject asyncObject = new AsyncJsonObject();
//
//        asyncObject.execute("");

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                int radioSelected = radioGroup.getCheckedRadioButtonId();
//
//                int userSelection = getSelectedAnswer(radioSelected);
//
//                int correctAnswerForQuestion = firstQuestion.getCorrectAnswer();
//
//                if(userSelection == correctAnswerForQuestion){
//                    // correct answer
//
//                    Toast.makeText(getActivity(), "You got the answer correct", Toast.LENGTH_LONG).show();
//
//                    currentQuizQuestion++;
//
//                    if(currentQuizQuestion >= quizCount){
//
//                        Toast.makeText(getActivity(), "End of the Quiz Questions", Toast.LENGTH_LONG).show();
//
//                        return;
//
//                    }
//
//                    else{
//
//                        firstQuestion = parsedObject.get(currentQuizQuestion);
//
//                        quizQuestion.setText(firstQuestion.getQuestion());
//
//                        String[] possibleAnswers = firstQuestion.getAnswers().split(",");
//
//                        uncheckedRadioButton();
//
//                        optionOne.setText(possibleAnswers[0]);
//
//                        optionTwo.setText(possibleAnswers[1]);
//
//                        optionThree.setText(possibleAnswers[2]);
//
//                        optionFour.setText(possibleAnswers[3]);
//
//                    }
//
//                }
//
//                else{
//                    // failed question
//                    Toast.makeText(getActivity(), "You chose the wrong answer", Toast.LENGTH_LONG).show();
//                    return;
//
//                }

            }

        });


        previousButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

//                currentQuizQuestion--;
//
//                if(currentQuizQuestion < 0){
//
//                    return;
//
//                }
//
//                uncheckedRadioButton();
//
//                firstQuestion = parsedObject.get(currentQuizQuestion);
//
//                quizQuestion.setText(firstQuestion.getQuestion());
//
//                String[] possibleAnswers = firstQuestion.getAnswers().split(",");
//
//                optionOne.setText(possibleAnswers[0]);
//
//                optionTwo.setText(possibleAnswers[1]);
//
//                optionThree.setText(possibleAnswers[2]);
//
//                optionFour.setText(possibleAnswers[3]);

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

    public void Initialization()
    {
        quizQuestion = (TextView)rootview.findViewById(R.id.quiz_question);

        radioGroup = (RadioGroup)rootview.findViewById(R.id.radioGroup);

        optionOne = (RadioButton)rootview.findViewById(R.id.radio0);

        optionTwo = (RadioButton)rootview.findViewById(R.id.radio1);

        optionThree = (RadioButton)rootview.findViewById(R.id.radio2);

        optionFour = (RadioButton)rootview.findViewById(R.id.radio3);

        previousButton = (Button)rootview.findViewById(R.id.previousquiz);

        nextButton = (Button)rootview.findViewById(R.id.nextquiz);

    }


//    private class AsyncJsonObject extends AsyncTask<String, Void, String> {
//
//        private ProgressDialog progressDialog;
//
//        @Override
//
//        protected String doInBackground(String... params) {
//
//            HttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
//
//            HttpPost httpPost = new HttpPost("http://10.0.2.2/androidlogin/index.php");
//
//            String jsonResult = "";
//
//            try {
//
//                HttpResponse response = httpClient.execute(httpPost);
//
//                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
//
//                System.out.println("Returned Json object " + jsonResult.toString());
//
//            } catch (ClientProtocolException e) {
//
//// TODO Auto-generated catch block
//
//                e.printStackTrace();
//
//            } catch (IOException e) {
//
//// TODO Auto-generated catch block
//
//                e.printStackTrace();
//
//            }
//
//            return jsonResult;
//
//        }
//
//        @Override
//
//        protected void onPreExecute() {
//
//// TODO Auto-generated method stub
//
//            super.onPreExecute();
//
//            progressDialog = ProgressDialog.show(getActivity(), "Downloading Quiz","Wait....", true);
//
//        }
//
//        @Override
//
//        protected void onPostExecute(String result) {
//
//            super.onPostExecute(result);
//
//            progressDialog.dismiss();
//
//            System.out.println("Resulted Value: " + result);
//
//            parsedObject = returnParsedJsonObject(result);
//
//            if(parsedObject == null){
//
//                return;
//
//            }
//
//            quizCount = parsedObject.size();
//
//            firstQuestion = parsedObject.get(0);
//
//            quizQuestion.setText(firstQuestion.getQuestion());
//
//            String[] possibleAnswers = firstQuestion.getAnswers().split(",");
//
//            optionOne.setText(possibleAnswers[0]);
//
//            optionTwo.setText(possibleAnswers[1]);
//
//            optionThree.setText(possibleAnswers[2]);
//
//            optionFour.setText(possibleAnswers[3]);
//
//        }
//
//        private StringBuilder inputStreamToString(InputStream is) {
//
//            String rLine = "";
//
//            StringBuilder answer = new StringBuilder();
//
//            BufferedReader br = new BufferedReader(new InputStreamReader(is));
//
//            try {
//
//                while ((rLine = br.readLine()) != null) {
//
//                    answer.append(rLine);
//
//                }
//
//            } catch (IOException e) {
//
//// TODO Auto-generated catch block
//
//                e.printStackTrace();
//
//            }
//
//            return answer;
//
//        }
//
//    }
//
//    private List<QuizWrapper> returnParsedJsonObject(String result){
//
//        List<QuizWrapper> jsonObject = new ArrayList<QuizWrapper>();
//
//        JSONObject resultObject = null;
//
//        JSONArray jsonArray = null;
//
//        QuizWrapper newItemObject = null;
//
//        try {
//
//            resultObject = new JSONObject(result);
//
//            System.out.println("Testing the water " + resultObject.toString());
//
//            jsonArray = resultObject.optJSONArray("quiz_questions");
//
//        } catch (JSONException e) {
//
//            e.printStackTrace();
//
//        }
//
//        for(int i = 0; i < jsonArray.length(); i++){
//
//            JSONObject jsonChildNode = null;
//
//            try {
//
//                jsonChildNode = jsonArray.getJSONObject(i);
//
//                int id = jsonChildNode.getInt("id");
//
//                String question = jsonChildNode.getString("question");
//
//                String answerOptions = jsonChildNode.getString("possible_answers");
//
//                int correctAnswer = jsonChildNode.getInt("correct_answer");
//
//                newItemObject = new QuizWrapper(id, question, answerOptions, correctAnswer);
//
//                jsonObject.add(newItemObject);
//
//            } catch (JSONException e) {
//
//                e.printStackTrace();
//
//            }
//
//        }
//
//        return jsonObject;
//
//    }
//
//    private int getSelectedAnswer(int radioSelected){
//
//        int answerSelected = 0;
//
//        if(radioSelected == R.id.radio0){
//
//            answerSelected = 1;
//
//        }
//
//        if(radioSelected == R.id.radio1){
//
//            answerSelected = 2;
//
//        }
//
//        if(radioSelected == R.id.radio2){
//
//            answerSelected = 3;
//
//        }
//
//        if(radioSelected == R.id.radio3){
//
//            answerSelected = 4;
//
//        }
//
//        return answerSelected;
//
//    }
//
//    private void uncheckedRadioButton(){
//
//        optionOne.setChecked(false);
//
//        optionTwo.setChecked(false);
//
//        optionThree.setChecked(false);
//
//        optionFour.setChecked(false);
//
//    }

}
