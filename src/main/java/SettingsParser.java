import bean.Settings;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by gaeta on 10/05/2016.
 */
public class SettingsParser {

    private final String REPO_PATH_KEY = "repository_path";
    private final String BRANCH_NAME_KEY = "branch_name";

    BufferedReader br = null;

    public SettingsParser(String filePath) throws FileNotFoundException {
        br = new BufferedReader(new FileReader(filePath));
    }

    public Settings read(){

        Settings settings = new Settings();
        String currentLine = null;

        try {
            while((currentLine = br.readLine()) != null){
                String [] keyValuePair = currentLine.split("=");

                switch(keyValuePair[0]){
                    case REPO_PATH_KEY:{
                        settings.setRepositoryPath(keyValuePair[1]);
                        break;
                    }
                    case BRANCH_NAME_KEY:{
                    	settings.setBranchName(keyValuePair[1]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return settings;
    }
}
