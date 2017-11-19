package b00239148.alarmclock;

import java.util.ArrayList;

/**
 * Created by dennisalt on 12/11/2017.
 */

public class   CoverQuiz extends SongQuiz {


    private ArrayList<String[]> answers = new ArrayList<String[]>();       //Could be titles or links to song covers??

    CoverQuiz(){
        super.songTitle = "Which is the right song cover?";
    }

    /*CoverQuiz(String title){
        readAnswers();
    }*/

    public void fillDummyAnsw()
    {
        String[] tempString = new String[2];
        for (int i=0; i<4;i++){
            answers.add(new String[] {"Cover: " + i, "false"});
        }
        answers.set(1, new String[] {"Cover: " + 1, "true"});
    }


    //TODO Implement methods cover

    @Override
    protected void readAnswers() {

    }

    @Override
    public ArrayList<String[]> getAnswers() {
        fillDummyAnsw();
        return answers;
    }
}
