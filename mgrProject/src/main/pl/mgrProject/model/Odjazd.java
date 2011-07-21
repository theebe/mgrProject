package pl.mgrProject.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.validator.NotNull;
import org.hibernate.validator.Range;
import org.jboss.seam.annotations.Name;

@Entity
@Name("odjazd")
@Table(name="ODJAZDY")
@NamedQueries({
	//wyciaga z bazy wszystkie odjazdy z przystanków
	@NamedQuery(name="wszystkieOdjazdy", query="SELECT odj FROM Odjazd odj"),
	@NamedQuery(name="odjazdyZPrzystanku", query="SELECT odj FROM Odjazd odj WHERE odj.przystanekTabliczka.przystanek = :przystanek "),
	@NamedQuery(name="odjazdyLini", query="SELECT odj FROM Odjazd odj WHERE odj.przystanekTabliczka.linia = :linia"),
	@NamedQuery(name="odjazdyLiniZPrzystanku", query="SELECT odj FROM Odjazd odj WHERE  odj.przystanekTabliczka.przystanek = :przystanek AND odj.przystanekTabliczka.linia = :linia")
	})
public class Odjazd {

	private Long id;
	private Integer version;
	private TypDnia typDnia;
	private Date czas;
	private PrzystanekTabliczka przystanekTabliczka;
	
	
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
	
	@Enumerated(EnumType.STRING)
	public TypDnia getTypDnia() {
		return typDnia;
	}
	
	public void setTypDnia(TypDnia typDnia) {
		this.typDnia = typDnia;
	}
	
	
	@ManyToOne
	@NotNull(message="Odjazd musi byæ przypisany do przystanku i lini")
	public PrzystanekTabliczka getPrzystanekTabliczka() {
		return przystanekTabliczka;
	}
	
	public void setPrzystanekTabliczka(PrzystanekTabliczka przystanekTabliczka) {
		this.przystanekTabliczka = przystanekTabliczka;
	}
	
	
	public void setCzas(Date czas) {
		this.czas = czas;
	}
	
	@NotNull
	public Date getCzas() {
		return czas;
	}
	
}
