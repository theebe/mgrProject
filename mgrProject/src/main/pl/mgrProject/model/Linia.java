package pl.mgrProject.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.NotNull;
import org.hibernate.validator.Range;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

/**
 * Klasa encyjna mapujaca tabelê LINIE Klasa przedstawia linie autobusowe i
 * tramwajowe.
 * 
 * @author bat
 * 
 */
@Entity
@Table(name = "LINIE")
@NamedQueries({
		// wyciaga z bazy wszystkie przystanki
		@NamedQuery(name = "wszystkieLinie", query = "SELECT OBJECT(lin) FROM Linia lin"),
		@NamedQuery(name = "liniePoNumerze", query = "SELECT OBJECT(lin) FROM Linia lin where lin.numer = :numer"),
		@NamedQuery(name = "liniePoPrzystanku", query = "SELECT przystTabl.linia FROM PrzystanekTabliczka przystTabl WHERE przystTabl.przystanek = :przystanek") })
public class Linia {

	private Long id;
	private Integer version;
	private Integer numer;
	private TypKomunikacji typ;

	private List<PrzystanekTabliczka> przystanekTabliczka;

	@Transient
	// niewidzialny dla bazy danych
	@In
	EntityManager mgrDatabase;

	/**
	 * id
	 * 
	 * @return Id
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * version
	 * 
	 * @return
	 */
	@Version
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * Numer linii
	 * 
	 * @return
	 */
	@NotNull
	@Range(min = 0, max = 999, message = "Wartoœæ musi byæ z przedzia³u od 0 do 999")
	public Integer getNumer() {
		return numer;
	}

	public void setNumer(Integer numer) {
		this.numer = numer;
	}

	/**
	 * Lista przystankow (kolejnoœæ zachowana)
	 * 
	 * @return
	 */
	@OneToMany(cascade = { CascadeType.REMOVE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@OrderColumn(name = "przystanektabliczka_order")
	@JoinColumn(name = "linia_id", nullable = false)
	public List<PrzystanekTabliczka> getPrzystanekTabliczka() {
		if (this.przystanekTabliczka == null) {
			this.przystanekTabliczka = new ArrayList<PrzystanekTabliczka>();
		}
		return przystanekTabliczka;
	}

	public void setPrzystanekTabliczka(
			List<PrzystanekTabliczka> przystanekTabliczka) {
		this.przystanekTabliczka = przystanekTabliczka;
	}

	/**
	 * Dodaje nowy przystanek na koniec linii
	 * 
	 * @param pt
	 */
	public void addPrzystanekTabliczka(PrzystanekTabliczka pt) {
		if (this.przystanekTabliczka == null) {
			this.przystanekTabliczka = new ArrayList<PrzystanekTabliczka>();
		}
		this.przystanekTabliczka.add(pt);
	}

	public void setTyp(TypKomunikacji typ) {
		this.typ = typ;
	}

	/**
	 * linia tramwajowa czy linia autobusowa
	 * 
	 * @return
	 */
	@NotNull
	@Enumerated(EnumType.STRING)
	public TypKomunikacji getTyp() {
		return typ;
	}

}
