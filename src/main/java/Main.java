import bean.ClassDetails;
import bean.Commit;
import bean.Settings;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

public class Main implements Study {
    ArrayList<Commit> commits;

    public static void main(String[] args) {
    	
    	File dir = new File(Costants.LOCAL_REPO_PATH);
    	File outputDir = new File(Costants.OUTPUT_DIR_PATH);
    	try {
			Settings settings = new SettingsParser(Costants.SETTINGS_PATH).read();
			
			if(!outputDir.exists()){
				outputDir.mkdir();
			}
			else{
				emptyDir(outputDir);
			}
			
			if(!dir.exists()){
	    		dir.mkdir();
	    	}
			else{
				emptyDir(dir);
			}
			System.out.println("Downloading repository.............");
			Git git = Git.cloneRepository()
					  .setURI(settings.getRepositoryPath() ).setDirectory(dir)
					  .call();
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidRemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        new MetricMiner2().start(new Main());
    }

    public void execute() {
        MyVisitor myVisitor = new MyVisitor();
        
        System.out.println("Parsing repository.............");

        SCMRepository scm = GitRepository.singleProject(Costants.LOCAL_REPO_PATH);
        CommitRange commitRange = Commits.all();

        new RepositoryMining()
                .in(GitRepository.singleProject(Costants.LOCAL_REPO_PATH))
                //.through(Commits.all())
                .through(new ReverseAllCommits())
                .process(myVisitor, new TSVFile(Costants.OUTPUT_DIR_PATH+"\\commitsLog.txt") )
                .mine();

        commits = myVisitor.getCommits();

        HashSet<String> hashSet = new HashSet<String>(myVisitor.getAllClasses());
        if(hashSet.size() > 0){
        	try {
                PrintWriter printWriter = new PrintWriter(Costants.OUTPUT_DIR_PATH+"\\classesList.txt", "UTF-8");

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
    
    private static void emptyDir(File dir){
    	if(dir.isDirectory()){
    		final File[] files = dir.listFiles();
    		for (File f: files) {
    			if(f.isDirectory()){
    				emptyDir(f);
    				f.delete();
    			}
    			else{
    				f.delete();
    			}
    		}
    			
    	}
    }
}
