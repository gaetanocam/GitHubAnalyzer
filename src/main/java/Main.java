import bean.ClassDetails;
import bean.Commit;
import br.com.metricminer2.MetricMiner2;
import br.com.metricminer2.RepositoryMining;
import br.com.metricminer2.Study;
import br.com.metricminer2.scm.GitRepository;
import br.com.metricminer2.scm.SCM;
import br.com.metricminer2.scm.SCMRepository;
import br.com.metricminer2.scm.commitrange.AllCommits;
import br.com.metricminer2.scm.commitrange.CommitRange;
import br.com.metricminer2.scm.commitrange.Commits;
import com.google.common.collect.Lists;
import util.ReverseAllCommits;
import util.TSVFile;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class Main implements Study {
    ArrayList<Commit> commits;

    public static void main(String[] args) {
        new MetricMiner2().start(new Main());
    }

    public void execute() {
        MyVisitor myVisitor = new MyVisitor();

        SCMRepository scm = GitRepository.singleProject(Costants.LOCAL_REPO_PATH);
        CommitRange commitRange = Commits.all();

        new RepositoryMining()
                .in(GitRepository.singleProject(Costants.LOCAL_REPO_PATH))
                //.through(Commits.all())
                .through(new ReverseAllCommits())
                .process(myVisitor,
                        new TSVFile(Costants.LOCAL_REPO_PATH+"\\"+Costants.FILE_NAME))
                .mine();

        commits = myVisitor.getCommits();

        HashSet<String> hashSet = new HashSet<>(myVisitor.getAllClasses());
        try {
            PrintWriter printWriter = new PrintWriter("classesList.txt", "UTF-8");

            for(String clas : hashSet){
                printWriter.append(clas + "\n");
            }

            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
