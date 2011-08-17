package pl.mgrProject.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Type;
import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.Name;
import org.postgis.Point;

/**
 * Klasa encja, mapuj¹ca tabelê PRZYSTANKI
 * @author bat
 *
 */
@Entity
@Table(name = "PRZYSTANKI")
@NamedQueries({
	//wyciaga z bazy wszystkie przystanki
	@NamedQuery(name="wszystkiePrzystanki", query="SELECT OBJECT(przyst) FROM Przystanek przyst ORDER BY przyst.nazwa"),
	@NamedQuery(name="przystankiPoNazwie", query="SELECT OBJECT(przyst) FROM Przystanek przyst where przyst.nazwa like :nazwa"),
	@NamedQuery(name="przystankiPoLinii", query="SELECT przystTabl.przystanek FROM PrzystanekTabliczka przystTabl WHERE przystTabl.linia = :linia")
	})
	
public class Przystanek implements Serializable {

 
	private static final long serialVersionUID = 4361572293720566801L;
	private Long id; 
	private Integer version;
	private Point location;
	private String nazwa;
	private TypKomunikacji typ;

	private Set<PrzystanekTabliczka> przystanekTabliczki;
	

	@Id @GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Version
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}


	@NotNull(message = "Nazwa nie mo¿e byæ pusta")
	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	@Type(type = "org.postgis.hibernate.GeometryType")
	@Column(name="location", columnDefinition="Geometry")
	@NotNull(message = "Lokacja nie mo¿e byæ pusta")
	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	

	@OneToMany(mappedBy="przystanek", cascade={CascadeType.MERGE, CascadeType.REFRESH})
	public Set<PrzystanekTabliczka> getPrzystanekTabliczki() {
		return przystanekTabliczki;
	}

	public void setPrzystanekTabliczki(Set<PrzystanekTabliczka> przystanekTabliczki) {
		this.przystanekTabliczki = przystanekTabliczki;
	}

	
	
	public void setTyp(TypKomunikacji typ) {
		this.typ = typ;
	}

	@NotNull
	@Enumerated(EnumType.STRING)
	public TypKomunikacji getTyp() {
		return typ;
	}

	
	

}
