import bean.ClassDetails;
import bean.Commit;
import br.com.metricminer2.MetricMiner2;
import br.com.metricminer2.RepositoryMining;
import br.com.metricminer2.Study;
import br.com.metricminer2.scm.GitRepository;
import br.com.metricminer2.scm.commitrange.Commits;
import util.TSVFile;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Main implements Study {
    ArrayList<Commit> commits;

    public static void main(String[] args) {
        new MetricMiner2().start(new Main());
    }

    public void execute() {
        MyVisitor myVisitor = new MyVisitor();

        new RepositoryMining()
                .in(GitRepository.singleProject(Costants.LOCAL_REPO_PATH))
                .through(Commits.all())
                .process(myVisitor,
                        new TSVFile(Costants.LOCAL_REPO_PATH+"\\"+Costants.FILE_NAME))
                .mine();

        commits = myVisitor.getCommits();

        long startInterval = 1463585400;
        long endInterval = 1463594400;
        ArrayList<ClassDetails> classesInInterval = new ArrayList<>();

        for(Commit commit : commits){

            if(commit.getTimestamp() >= startInterval &&  commit.getTimestamp() <= Calendar.getInstance().getTimeInMillis()){

                List<ClassDetails> currentClasses = commit.getModifiedClasses();

                for(ClassDetails modifiedClass : currentClasses){
                    classesInInterval.add(modifiedClass);
                }

            }
        }


    }
}
