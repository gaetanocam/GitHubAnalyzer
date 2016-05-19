package util;

import br.com.metricminer2.domain.ChangeSet;
import br.com.metricminer2.scm.SCM;
import br.com.metricminer2.scm.commitrange.CommitRange;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by Placido Russo on 19/05/2016.
 */
public class ReverseAllCommits implements CommitRange {
    public ReverseAllCommits() {
    }

    public List<ChangeSet> get(SCM scm) {
        return Lists.reverse(scm.getChangeSets());
    }
}
