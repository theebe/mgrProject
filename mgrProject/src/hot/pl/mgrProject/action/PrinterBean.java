package pl.mgrProject.action;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

@Stateless
@Name("printer")
public class PrinterBean implements Printer {

	
	@Logger
	private Log logger;
	
	@In
	private EntityManager mgrDatabase;
	
	@EJB(beanName = "PrzystanekTabliczkaDAOBean")
	private PrzystanekTabliczkaDAO przystanekTabliczkaDAO;
	
	@EJB(beanName = "PrzystanekDAOBean")
	private PrzystanekDAO przystanekDAO;
	
	@EJB(beanName = "LiniaDAOBean")
	private LiniaDAO liniaDAO;
	
	
	@Override
	public byte[] getRozkladAsPDF() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getLiniaAsPDF(Integer liniaId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getPrzystanekAsPDF(Integer przystanekId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char[] getRozkladAsHTML() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char[] getLiniaAsHTML(Integer liniaId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char[] getPrzystanekAsHTML(Integer przystanekId) {
		// TODO Auto-generated method stub
		return null;
	}

}
