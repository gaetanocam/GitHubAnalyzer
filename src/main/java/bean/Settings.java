package bean;

/**
 * Created by gaeta on 10/05/2016.
 */
public class Settings {

    private String onlineRrepositoryPath;
    private String branchName;

    public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getRepositoryPath() {
        return onlineRrepositoryPath;
    }

	public void setRepositoryPath(String repositoryPath) {
        this.onlineRrepositoryPath = repositoryPath;
    }
}
