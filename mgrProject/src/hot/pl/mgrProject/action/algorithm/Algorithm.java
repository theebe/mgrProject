package pl.mgrProject.action.algorithm;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import org.postgis.Point;

import pl.mgrProject.model.Przystanek;
import pl.mgrProject.model.PrzystanekTabliczka;

/**
 * Interfejs do algorytmu wyszukiwania tras
 * @author bat
 *
 */
@Local
public interface Algorithm {

	/**
	 * Uruchamia alogrytm
	 * @return poprawnosc przeprowadzonych operacji
	 */
	public Boolean run();
	
	/**
	 * Ustawia punkt startowy 
	 * @param p punkst starotwy
	 * @return czy operacja wykonala sie poprawnie
	 */
	public Boolean setStartPoint(Point p);
	
	/**
	 * Ustawia punkt koncowy
	 * @param p punkt koncowy
	 * @return 
	 */
	public Boolean setStopPoint(Point p);
	
	/**
	 * Pobiera obliczona sciezke 
	 * @return lista tabliczek przystankowych
	 */
	public List<PrzystanekTabliczka> getPath();
	
	/**
	 * Wyszukuje najblizszy przystanek do punktu 
	 * @param Punkt
	 * @return Przystanek
	 */ 

	public Przystanek getClosestTo(Point point);
	
	/**
	 * Ustawia czas startu
	 * @param startTime godzina startu
	 */
	public void setStartTime(Date startTime);
	
	/**
	 * Pobiera liste godzin na poszczegolnych przystankach
	 * @return lista godzin
	 */
	public List<Date> getHours();
	
	/**
	 * Pobiera liste przystankow z istniejacymi rozkladami jazdy posortowanych wedlug odleglosci od punktu 'p'.
	 * @param p Punkt wzgledem ktorego ma sie odbyc wyszukiwanie.
	 * @param distance Odleglosc w jakiej maja byc szukane przystanki. Jesli wartosc wnosi 0 to odleglosc jest pobierana z bazy danych.
	 * @return Lista przystankow.
	 */
	public List<Przystanek> getClosestList(Point p, int distance);
	public boolean isHoliday(Calendar date);
}