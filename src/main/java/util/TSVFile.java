package util;

/**
 * Created by 123max on 11/05/2016.
 */
import br.com.metricminer2.persistence.PersistenceMechanism;
import java.io.PrintStream;

public class TSVFile implements PersistenceMechanism {
    private PrintStream ps;

    public TSVFile(String fileName) {
        try {
            this.ps = new PrintStream(fileName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public TSVFile(String path, String name) {
        this(path + name);
    }

    public synchronized void write(Object... line) {
        boolean first = true;
        Object[] pars = line;
        int size = line.length;

        for(int i = 0; i < size; ++i) {
            if(!first) {
                this.ps.print("\t");
            }

            this.ps.print(pars[i]);
            first = false;
        }

        this.ps.println();
        this.ps.flush();
    }

    public void close() {
        this.ps.close();
    }
}
