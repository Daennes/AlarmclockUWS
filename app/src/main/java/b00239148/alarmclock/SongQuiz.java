package b00239148.alarmclock;

/**
 * Created by dennisalt on 09/11/2017.
 * Class which represents the one Quiz
 * Everytime am new Question is generated an instance of this class can be used
 * First Idea of Architecture
 */

public abstract class SongQuiz {

    //Sketch, not final
    protected String songTitle = "";

    protected MySpotify mySpotify;

    //Constructor
    SongQuiz(){
        this.songTitle = "";
    }



    //Methods...
    public void readTitle(){
        songTitle = "";
    }


    protected abstract void readAnswers();
    public abstract <T>T getAnswers();

}
