package pl.mgrProject.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Type;
import org.hibernate.validator.NotNull;
import org.postgis.Point;

/**
 * Klasa encja, mapuj¹ca tabelê PRZYSTANKI Przystanek posiada lokalizacje,
 * nazwe, typ (autobusowy lub tramwajowy) oraz liste wrzystkich tabliczek
 * znajdujacych sie na tym przystanku (linii zatrzymujacych sie na nim)
 * 
 * @author bat
 * 
 */
@Entity
@Table(name = "PRZYSTANKI")
@NamedQueries({
		// wyciaga z bazy wszystkie przystanki
		@NamedQuery(name = "wszystkiePrzystanki", query = "SELECT OBJECT(przyst) FROM Przystanek przyst ORDER BY przyst.nazwa"),
		@NamedQuery(name = "przystankiPoNazwie", query = "SELECT OBJECT(przyst) FROM Przystanek przyst where przyst.nazwa like :nazwa"),
		@NamedQuery(name = "przystankiPoLinii", query = "SELECT przystTabl.przystanek FROM PrzystanekTabliczka przystTabl WHERE przystTabl.linia = :linia") })
@XmlRootElement
public class Przystanek implements Serializable {

	private static final long serialVersionUID = 4361572293720566801L;
	private Long id;
	private Integer version;
	private Point location;
	private String nazwa;
	private TypKomunikacji typ;

	private List<PrzystanekTabliczka> przystanekTabliczki;

	/**
	 * Id
	 * 
	 * @return
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
	 * Vresion
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
	 * Nazwa przystanku
	 * 
	 * @return
	 */
	@NotNull(message = "Nazwa nie mo¿e byæ pusta")
	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	/**
	 * Lokalizacja przystanku
	 * 
	 * @return Point
	 */
	@Type(type = "org.postgis.hibernate.GeometryType")
	@Column(name = "location", columnDefinition = "Geometry")
	@NotNull(message = "Lokacja nie mo¿e byæ pusta")
	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	/**
	 * Tabliczki przystankowe znajdujace sie na tym przystanku Linie wraz z
	 * odjazdami zatrzymujace sie na tym przystanku
	 * 
	 * @return
	 */
	@OneToMany(mappedBy = "przystanek", cascade = { CascadeType.MERGE,
			CascadeType.REFRESH })
	public List<PrzystanekTabliczka> getPrzystanekTabliczki() {
		return przystanekTabliczki;
	}

	public void setPrzystanekTabliczki(
			List<PrzystanekTabliczka> przystanekTabliczki) {
		this.przystanekTabliczki = przystanekTabliczki;
	}

	public void setTyp(TypKomunikacji typ) {
		this.typ = typ;
	}

	/**
	 * przystanek tramwajowy czy autobusowy
	 * 
	 * @return
	 */
	@NotNull
	@Enumerated(EnumType.STRING)
	public TypKomunikacji getTyp() {
		return typ;
	}

}
