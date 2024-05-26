//ADT PENTRU BAZA DE DATE
//STOCHEZ CURRENT ITME PENTRU DISPLAYED NAME IN MENIUL DE LOAD / SCORE SI LAYOUTUL E MAPA SERIALIZATA
public class CustomRecord {
    private String date;
    private int score;
    private String layout;

    public CustomRecord(String date,int score, String layout) {
        this.date = date;
        this.score = score;
        this.layout = layout;
    }

    public String getDate() {
        return date;
    }

    public int getScore() {
        return score;
    }

    public String getLayout() {
        return layout;
    }

    @Override
    public String toString() {
        return "Date: " + date + ", Score: " + score + ", Layout: " + layout;
    }
}
