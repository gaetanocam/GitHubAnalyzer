import bean.ClassDetails;
import br.com.metricminer2.domain.Commit;
import br.com.metricminer2.domain.Modification;
import br.com.metricminer2.domain.ModificationType;
import br.com.metricminer2.parser.jdt.JDTRunner;
import br.com.metricminer2.persistence.PersistenceMechanism;
import br.com.metricminer2.scm.CommitVisitor;
import br.com.metricminer2.scm.SCMRepository;

import java.io.*;
import java.util.*;

/**
 * Created by Placido Russo on 10/05/2016.
 */
public class MyVisitor implements CommitVisitor {
	// List of list of classDetails present in each commit
	// Repository status for each commit
	private HashMap<String, ArrayList<ClassDetails>> prevModificationsMap;
	private ArrayList<String> allClasses;
	private String[] branchesNames;

	public MyVisitor(String[] branchesNames) {
		allClasses = new ArrayList<String>();
		this.branchesNames = branchesNames;
	}

	public void process(SCMRepository scmRepository, Commit commit, PersistenceMechanism writer) {
		
		Set<String> branches = commit.getBranches();
		
		boolean isInBranch = false;
		
		for(String branch : branches){
			for(String selectedBranch : branchesNames){
				if(branch.equals(selectedBranch)){
					isInBranch = true;
					break;
				}
			}

			if(isInBranch){
				break;
			}
		}
		
		if(!isInBranch){
			return;
		}

		// key = path, value = list of classDetails
		HashMap<String, ArrayList<ClassDetails>> modificationsMap = new HashMap<String, ArrayList<ClassDetails>>();
		if (prevModificationsMap != null) {
			// copying modificationsMap of previous commit into current
			// modificationsMap
			modificationsMap = prevModificationsMap;

			// set to false modified parameter for each class
			Set<String> keySet = modificationsMap.keySet();
			for (String key : keySet) {
				ArrayList<ClassDetails> prevList = modificationsMap.get(key);
				for (ClassDetails prevClass : prevList) {
					prevClass.setModified(false);
				}
			}
		}

		for (Modification m : commit.getModifications()) {

			ArrayList<ClassDetails> currentList;

			if (!m.fileNameEndsWith(".java"))
				continue;

			System.out.println(m.getFileName());

			JavaClassVisitor visitor = new JavaClassVisitor(m.getNewPath());
			new JDTRunner().visit(visitor, new ByteArrayInputStream(m.getSourceCode().getBytes()));

			currentList = visitor.getModifiedClasses();

			String diff = m.getDiff();
			String[] lines = diff.split("\n");
			int lineCounter = 0;

			if (diff.equals("-- TOO BIG --")) {
				for (ClassDetails currentClass : currentList) {
					currentClass.setModified(true);
				}
			} else {
				for (String line : lines) {
					int startLine = 0;
					String trimmedLine = line.trim();
					if (trimmedLine.startsWith("@@")) {
						startLine = Integer.parseInt(line.substring(line.indexOf('+') + 1, line.lastIndexOf(',')));
						lineCounter = startLine - 1;
					} else if (trimmedLine.startsWith("+") && !trimmedLine.startsWith("+++")) {
						lineCounter++;
						for (ClassDetails currentClass : currentList) {
							if (!currentClass.isModified() && lineCounter >= currentClass.getStartIndex()
									&& lineCounter <= currentClass.getEndIndex()) {
								currentClass.setModified(true);
							}
						}
					} else if (trimmedLine.startsWith("-") && !line.startsWith("---")) {
						// when a line is removed
						for (ClassDetails currentClass : currentList) {
							if (!currentClass.isModified() && lineCounter >= currentClass.getStartIndex()
									&& lineCounter <= currentClass.getEndIndex()) {
								currentClass.setModified(true);
							}
						}
					} else {
						lineCounter++;
					}

				}
			}

			// Update history of commits
			// first commit case
			if (prevModificationsMap == null)
				modificationsMap.put(m.getNewPath(), currentList);
			else {
				// 3 cases:
				// 1- file added
				if (m.getType() == ModificationType.ADD) {
					modificationsMap.put(m.getNewPath(), currentList);
				}
				// 2- file deleted
				else if (m.getType() == ModificationType.DELETE) {
					modificationsMap.remove(m.getNewPath());
				}
				// 3- file modified
				else if (m.getType() == ModificationType.MODIFY) {
					modificationsMap.replace(m.getNewPath(), currentList);
				}
			}
		}

		String line = "";
		Set<String> keys = modificationsMap.keySet();
		for (String key : keys) {
			ArrayList<ClassDetails> classes = modificationsMap.get(key);
			for (int i = 0; i < classes.size(); i++) {
				ClassDetails classDetails = classes.get(i);
				int modificationNumber = classDetails.isModified() ? 1 : 0;
				line = line + (" " + classDetails.getPath() + "/" + classDetails + " " + modificationNumber);
				allClasses.add(classDetails.getPath() + "/" + classDetails.getName());
			}

		}

		writer.write(commit.getDate().getTimeInMillis(), line);

		// add modificationsMap to list of commits
		prevModificationsMap = modificationsMap;
	}


	public String name() {
		return "MyVisitor";
	}

	public ArrayList<String> getAllClasses() {
		return allClasses;
	}
}
