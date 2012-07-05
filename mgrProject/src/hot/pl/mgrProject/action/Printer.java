package pl.mgrProject.action;

import javax.ejb.Local;

@Local
public interface Printer {

	public byte[] getRozkladAsPDF();
	public byte[] getLiniaAsPDF(Integer liniaId);
	public byte[] getPrzystanekAsPDF(Integer przystanekId);
	
	public char[] getRozkladAsHTML();
	public char[] getLiniaAsHTML(Integer liniaId);
	public char[] getPrzystanekAsHTML(Integer przystanekId);
	
	
}
