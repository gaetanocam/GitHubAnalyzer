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

        System.out.println("HISTORY:");
        System.out.println(myVisitor.getCommitsHistory().toString());
        System.out.println("HISTORY OF MODIFIED CLASSES: ");
        for (HashMap<String, ArrayList<ClassDetails>> map :
                myVisitor.getCommitsHistory()) {
            Set<String> keySet = map.keySet();
            System.out.print("{");
            for (String key : keySet) {
                System.out.print("[");
                for (ClassDetails currentClass : map.get(key)) {
                    if (currentClass.isModified())
                        System.out.print(currentClass+" ");
                }
                System.out.print("]");
            }
            System.out.print("} ");
        }
    }
}
