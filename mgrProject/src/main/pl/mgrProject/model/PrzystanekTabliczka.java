package pl.mgrProject.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.Name;


@Entity
@Name("przystanekTabliczka")
@Table(name = "PRZYSTANEK_TABLICZKI")
@NamedQueries({
		// wyciaga z bazy wszystkie Tabliczki
		@NamedQuery(name = "wszystkieTabliczki", query = "SELECT OBJECT(przystTabl) FROM PrzystanekTabliczka przystTabl"),
		@NamedQuery(name = "tabliczniPoPrzystanku", query = "SELECT przystTabl FROM PrzystanekTabliczka przystTabl WHERE przystTabl.przystanek = :przystanek"), 
		@NamedQuery(name = "tabliczkiPoLinii", query = "SELECT przystTabl FROM PrzystanekTabliczka przystTabl WHERE przystTabl.linia = :linia") 
		})
public class PrzystanekTabliczka implements Serializable {

	private Long id;
	private Integer version;
	private Linia linia;

	private Przystanek przystanek;

	private PrzystanekTabliczka nastepnyPrzystanek;
	private PrzystanekTabliczka poprzedniPrzystanek;

	private Set<Odjazd> odjazdy;

	@Id
	@GeneratedValue
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

	@ManyToOne
	@NotNull
	@JoinColumn(name = "linia_id", insertable = false, updatable = false, nullable = false)
	public Linia getLinia() {
		return linia;
	}

	public void setLinia(Linia linia) {
		this.linia = linia;
	}

	@ManyToOne
	@NotNull
	public Przystanek getPrzystanek() {
		return przystanek;
	}

	public void setPrzystanek(Przystanek przystanek) {
		this.przystanek = przystanek;
	}

	@OneToMany(mappedBy = "przystanekTabliczka", fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	public Set<Odjazd> getOdjazdy() {
		if (odjazdy == null)
			odjazdy = new HashSet<Odjazd>();

		return odjazdy;
	}

	public void setOdjazdy(Set<Odjazd> odjazdy) {
		this.odjazdy = odjazdy;
	}

	public void addOdjazd(Odjazd o) {
		getOdjazdy().add(o);

	}

	@OneToOne
	@JoinColumn(name="nastepnyPrzystanek_id")
	public PrzystanekTabliczka getNastepnyPrzystanek() {
		return nastepnyPrzystanek;
	}

	public void setNastepnyPrzystanek(PrzystanekTabliczka nastepnyPrzystanek) {
		this.nastepnyPrzystanek = nastepnyPrzystanek;
	}

	
	public void setPoprzedniPrzystanek(PrzystanekTabliczka poprzedniPrzystanek) {
		this.poprzedniPrzystanek = poprzedniPrzystanek;
	}

	@OneToOne
	@JoinColumn(name="poprzedniPrzystanek_id")
	public PrzystanekTabliczka getPoprzedniPrzystanek() {
		return poprzedniPrzystanek;
	}
}
