package query;

import java.rmi.UnexpectedException;

public interface Query {
    String parse(boolean safe) throws Exception;
}
