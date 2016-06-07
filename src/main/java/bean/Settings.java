package bean;

/**
 * Created by gaeta on 10/05/2016.
 */
public class Settings {

    private String onlineRrepositoryPath;
    private String[] branchesNames;

    public String[] getBranchesNames() {
		return branchesNames;
	}

	public void setBranchesNames(String[] branchesNames) {
		this.branchesNames = branchesNames;
	}

	public String getRepositoryPath() {
        return onlineRrepositoryPath;
    }

	public void setRepositoryPath(String repositoryPath) {
        this.onlineRrepositoryPath = repositoryPath;
    }
}
