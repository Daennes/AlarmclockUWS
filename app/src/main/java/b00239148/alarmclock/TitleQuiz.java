package b00239148.alarmclock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dennisalt on 10/11/2017.
 */

public class TitleQuiz extends SongQuiz {
    private ArrayList<String[]> answers = new ArrayList<String[]>();       //Could be titles or links to song covers??

    TitleQuiz(){
        super.songTitle = "Which is the right song title?";
    }

    TitleQuiz(String title){
        readAnswers();
    }

    public void fillDummyAnsw()
    {
        String[] tempString = new String[2];
        for (int i=0; i<4;i++){
            answers.add(new String[] {"Answer: " + i, "false"});
        }
        answers.set(1, new String[] {"Answer: " + 1, "true"});
    }

    //TODO Implement methods
    @Override
    protected void readAnswers() {
        //Create an object of the MySpotify class
        //get the Answer data

    }

    @Override
    public ArrayList<String[]> getAnswers() {
        fillDummyAnsw();
        return answers;
    }
}
