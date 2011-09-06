package pl.mgrProject.action;

import java.util.Date;
import java.util.List;

import pl.mgrProject.model.PrzystanekTabliczka;

public class Odpowiedz {

	List<PrzystanekTabliczka> ptList;
	List<Date> dateList;
	String info;

	public Odpowiedz(List<PrzystanekTabliczka> ptList, List<Date> dateList,
			String info) {
		
		this.ptList = ptList;
		this.dateList = dateList;
		this.info = info;

	}

	public List<PrzystanekTabliczka> getPtList() {
		return ptList;
	}

	public void setPtList(List<PrzystanekTabliczka> ptList) {
		this.ptList = ptList;
	}

	public List<Date> getDateList() {
		return dateList;
	}

	public void setDateList(List<Date> dateList) {
		this.dateList = dateList;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	
}
