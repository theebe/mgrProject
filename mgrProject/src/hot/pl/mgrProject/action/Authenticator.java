package pl.mgrProject.action;

import javax.ejb.Local;

@Local
public interface Authenticator {

    boolean authenticate();

}
