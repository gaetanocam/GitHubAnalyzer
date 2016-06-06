import bean.Settings;
import br.com.metricminer2.MetricMiner2;
import br.com.metricminer2.RepositoryMining;
import br.com.metricminer2.Study;
import br.com.metricminer2.scm.GitRepository;
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
	private static String currentRepository;
	private static Settings settings;

	public static void main(String[] args) {

		File dir = new File(Costants.LOCAL_REPO_PATH);
		File outputDir = new File(Costants.OUTPUT_DIR_PATH);
		try {
			settings = new SettingsParser(Costants.SETTINGS_PATH).read();

			if(!outputDir.exists()){
				outputDir.mkdir();
			}
			else{
				emptyDir(outputDir);
			}



			if(settings.getRepositoryPath().trim().startsWith("http")){
				System.out.println("Downloading repository.............");
				currentRepository = settings.getRepositoryPath();

				if(!dir.exists()){
					dir.mkdir();
				}
				else{
					emptyDir(dir);
				}

				Git.cloneRepository()
						.setURI(currentRepository).setDirectory(dir)
						.call();

				currentRepository = Costants.LOCAL_REPO_PATH;

			}else{
				currentRepository = settings.getRepositoryPath();
			}


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
		MyVisitor myVisitor = new MyVisitor(settings.getBranchName());

		System.out.println("Parsing repository.............");

		new RepositoryMining()
				.in(GitRepository.singleProject(currentRepository))
				//.through(Commits.all())
				.through(new ReverseAllCommits())
				.process(myVisitor, new TSVFile(Costants.OUTPUT_DIR_PATH+"//commitsLog.tsv") )
				.mine();

		HashSet<String> hashSet = new HashSet<String>(myVisitor.getAllClasses());
		if(hashSet.size() > 0){
			try {
				PrintWriter printWriter = new PrintWriter(Costants.OUTPUT_DIR_PATH+"//classesList.txt", "UTF-8");

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
