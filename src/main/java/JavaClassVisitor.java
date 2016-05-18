import bean.ClassDetails;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Placido Russo on 12/05/2016.
 */
public class JavaClassVisitor extends ASTVisitor {
    private String path;
    ArrayList<ClassDetails> modifiedClasses = new ArrayList<>();

    public JavaClassVisitor(String path) {
        this.path = path;
    }

    @Override
    public boolean visit(TypeDeclaration node) {
        int start = ((CompilationUnit) node.getRoot()).getLineNumber(node.getStartPosition());
        int end = ((CompilationUnit) node.getRoot()).getLineNumber(node.getLength() + node.getStartPosition());

        ClassDetails c = new ClassDetails();
        c.setPath(path);
        c.setName(node.getName().toString());
        c.setStartIndex(start);
        c.setEndIndex(end);

        modifiedClasses.add(c);

        /*
        System.out.println("TypeDeclaration: "+ node.getName());
        System.out.println("start: "+start);
        System.out.println("end: "+end);
        System.out.println(node.toString());
        */


        return super.visit(node);
    }

    public ArrayList<ClassDetails> getModifiedClasses() { return modifiedClasses; }
}
