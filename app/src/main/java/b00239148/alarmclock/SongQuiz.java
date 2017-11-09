package b00239148.alarmclock;

/**
 * Created by dennisalt on 09/11/2017.
 * Class wich represents the one Quiz
 * Everytime am new Question is generated an instance of this class can be used
 * First Idea of Architecture
 */

public class SongQuiz {

    //Sketch, not final
    private String songTitle = "";
    private String[] answers;           //Could be titles or links to song covers??

    //Constructor
    SongQuiz(String title, String[] answs){
        this.songTitle = title;
        this.answers = new String[answs.length];

        for (int i=0; i<answs.length; i++)
            this.answers[i] = answs[i];
    }


    //Methods...

}
