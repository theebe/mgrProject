package pl.mgrProject.action;

import java.util.Date;
import java.util.List;

import pl.mgrProject.model.PrzystanekTabliczka;

/**
 * Klasa przechowujaca odpowiedz algorytmu.
 * @author bat
 *
 */
public class Odpowiedz {

	List<PrzystanekTabliczka> ptList;
	List<Date> dateList;
	String info;

	/** 
	 * Konstruktor 
	 * Listy ptList oraz dateList musza miec ten sam rozmiar.
	 * @param ptList lista tabliczek przystankowych do odwiedzenia
	 * @param dateList lista godzin, przypisanych do kazdej tabliczki
	 * @param info informacja w postaci Stringu (np. informacja o bledzie)
	 */
	public Odpowiedz(List<PrzystanekTabliczka> ptList, List<Date> dateList,
			String info) {
		
		this.ptList = ptList;
		this.dateList = dateList;
		this.info = info;

	}

	/**
	 * Pobiera liste Tabliczek przystankowych
	 * @return
	 */
	public List<PrzystanekTabliczka> getPtList() {
		return ptList;
	}

	public void setPtList(List<PrzystanekTabliczka> ptList) {
		this.ptList = ptList;
	}

	/**
	 * Pobiera liste godzin 
	 * @return
	 */
	public List<Date> getDateList() {
		return dateList;
	}

	public void setDateList(List<Date> dateList) {
		this.dateList = dateList;
	}

	/**
	 * Pobiera informacje 
	 * @return String
	 */
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	
}
