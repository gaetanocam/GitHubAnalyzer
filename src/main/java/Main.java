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
import util.TSVFile;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Main implements Study {
    ArrayList<Commit> commits;

    public static void main(String[] args) {
        new MetricMiner2().start(new Main());
    }

    public void execute() {
        MyVisitor myVisitor = new MyVisitor();

        SCM scm = (SCM) GitRepository.singleProject(Costants.LOCAL_REPO_PATH);
        CommitRange commitRange = Commits.all();

        new RepositoryMining()
                .in(GitRepository.singleProject(Costants.LOCAL_REPO_PATH))
                //.through(Commits.all())
                .through((CommitRange) Lists.reverse(commitRange.get((SCM) scm)))
                .process(myVisitor,
                        new TSVFile(Costants.LOCAL_REPO_PATH+"\\"+Costants.FILE_NAME))
                .mine();

        commits = myVisitor.getCommits();

        System.out.println("HISTORY:");
        System.out.println(myVisitor.getCommitsHistory().toString());
    }
}
