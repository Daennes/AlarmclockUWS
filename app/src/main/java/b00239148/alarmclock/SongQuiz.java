package b00239148.alarmclock;

/**
 * Created by dennisalt on 09/11/2017.
 * Class wich represents the one Quiz
 * Everytime am new Question is generated an instance of this class can be used
 * First Idea of Architecture
 */

public abstract class SongQuiz {

    //Sketch, not final
    protected String songTitle = "";

    //Constructor
    SongQuiz(){
        this.songTitle = "";
    }



    //Methods...
    protected abstract void readAnswers();
    public abstract <T>T getAnswers();

}
