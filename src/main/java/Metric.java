import bean.ClassDetails;
import bean.Commit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by gaeta on 19/05/2016.
 */
public class Metric {

    public static List<ClassDetails> ClassBetweenInterval(List<Commit> commits, long startInterval, long endInterval ){

        ArrayList<ClassDetails> classesInInterval = new ArrayList<>();
        for(Commit commit : commits){

            if(commit.getTimestamp() >= startInterval && commit.getTimestamp() <= Calendar.getInstance().getTimeInMillis()){

                List<ClassDetails> currentClasses = commit.getModifiedClasses();

                for(ClassDetails modifiedClass : currentClasses){
                    classesInInterval.add(modifiedClass);
                }

            }
        }

        return classesInInterval;
    }


}
