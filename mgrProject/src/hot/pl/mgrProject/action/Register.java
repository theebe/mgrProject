package pl.mgrProject.action;

import javax.ejb.Local;

@Local
public interface Register {
	public void register();
	public void invalid();
	public boolean isRegistered();
	public String getVerify();
	public void setVerify(String verify);
	public void destroy();
}
