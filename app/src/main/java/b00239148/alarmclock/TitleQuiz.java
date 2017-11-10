package b00239148.alarmclock;

/**
 * Created by dennisalt on 10/11/2017.
 */

public class TitleQuiz extends SongQuiz {
    private String[] answers;       //Could be titles or links to song covers??

    TitleQuiz(){
        super.songTitle = "";
    }

    TitleQuiz(String title){
        readAnswers();
    }

    public void fillDummyAnsw()
    {
        answers = new String[4];
        for (int i=0; i<4;i++){
            answers[i] = "Answer: " + i;
        }
    }


    @Override
    protected void readAnswers() {

    }

    @Override
    public String[] getAnswers() {
        fillDummyAnsw();
        return answers;
    }
}
