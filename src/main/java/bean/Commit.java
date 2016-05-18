package bean;

import br.com.metricminer2.domain.Modification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gaeta on 16/05/2016.
 */
public class Commit {

    private String hash;
    private long timestamp;
    private String author;
    private ArrayList<ClassDetails> modifiedClasses;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ArrayList<ClassDetails> getModifiedClasses() {
        return modifiedClasses;
    }

    public void setModifiedClasses(ArrayList<ClassDetails> modifiedClasses) {
        this.modifiedClasses = modifiedClasses;
    }

    @Override
    public String toString() {
        return "["+hash+"\t"+timestamp+"\t"+author+"\t"+modifiedClasses.toString()+"]";
    }
}
