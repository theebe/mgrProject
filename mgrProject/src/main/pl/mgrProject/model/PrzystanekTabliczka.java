package pl.mgrProject.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
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

import org.hibernate.annotations.Index;
import org.hibernate.validator.Min;
import org.hibernate.validator.NotNull;

/**
 * Tabliczna przystankowa przyporzadkowana jest dla jednego przystanku i jednej
 * linii, posiada wiele godzin odjazdow. Oznacza, i� dana linia przez dany
 * przystanek mo�e przejezdza� wiele razy w ciagu dnia.
 * 
 * @author bat
 * 
 */
@Entity
@Table(name = "PRZYSTANEK_TABLICZKI")
@NamedQueries({
		// wyciaga z bazy wszystkie Tabliczki
		@NamedQuery(name = "wszystkieTabliczki", query = "SELECT OBJECT(przystTabl) FROM PrzystanekTabliczka przystTabl"),
		@NamedQuery(name = "tabliczkiPoPrzystanku", query = "SELECT przystTabl FROM PrzystanekTabliczka przystTabl WHERE przystTabl.przystanek = :przystanek"),
		@NamedQuery(name = "tabliczkiPoLinii", query = "SELECT przystTabl FROM PrzystanekTabliczka przystTabl WHERE przystTabl.linia = :linia") })

public class PrzystanekTabliczka implements Serializable {

	private Long id;
	private Integer version;
	private Linia linia;

	private Przystanek przystanek;

	private PrzystanekTabliczka nastepnyPrzystanek;
	private PrzystanekTabliczka poprzedniPrzystanek;

	private int czasDoNastepnego;

	private List<Odjazd> odjazdy;

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
	 * Version
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
	 * Linia jaka jest przyporzadkowana do tej tabliczki
	 * 
	 * @return
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@NotNull
	@JoinColumn(name = "linia_id", insertable = false, updatable = false, nullable = false)
	@Index(name = "liniaIndex")
	public Linia getLinia() {
		return linia;
	}

	public void setLinia(Linia linia) {
		this.linia = linia;
	}

	/**
	 * Przystanek przyporzadkowany do tej tabliczki
	 * 
	 * @return
	 */
	@ManyToOne
	@NotNull
	@Index(name = "przystanekIndex")
	public Przystanek getPrzystanek() {
		return przystanek;
	}

	public void setPrzystanek(Przystanek przystanek) {
		this.przystanek = przystanek;
	}

	/**
	 * Lista godzin odjazdow linii z tego przystanku
	 * 
	 * @return
	 */
	@OneToMany(mappedBy = "przystanekTabliczka", cascade = { CascadeType.ALL })
	public List<Odjazd> getOdjazdy() {
		if (odjazdy == null)
			odjazdy = new ArrayList<Odjazd>();

		return odjazdy;
	}

	public void setOdjazdy(List<Odjazd> odjazdy) {
		this.odjazdy = odjazdy;
	}

	/**
	 * Dodaje odjazd danej linii do danego przystanku
	 * 
	 * @param o
	 */
	public void addOdjazd(Odjazd o) {
		getOdjazdy().add(o);

	}

	/**
	 * Kolejny przystanek na linii
	 * 
	 * @return
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nastepnyPrzystanek_id")
	public PrzystanekTabliczka getNastepnyPrzystanek() {
		return nastepnyPrzystanek;
	}

	public void setNastepnyPrzystanek(PrzystanekTabliczka nastepnyPrzystanek) {
		this.nastepnyPrzystanek = nastepnyPrzystanek;
	}

	public void setPoprzedniPrzystanek(PrzystanekTabliczka poprzedniPrzystanek) {
		this.poprzedniPrzystanek = poprzedniPrzystanek;
	}

	/**
	 * Poprzedni przystanek tej linii
	 * 
	 * @return
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "poprzedniPrzystanek_id")
	public PrzystanekTabliczka getPoprzedniPrzystanek() {
		return poprzedniPrzystanek;
	}

	public void setCzasDoNastepnego(int czasDoNastepnego) {
		this.czasDoNastepnego = czasDoNastepnego;
	}

	/**
	 * Odleglosc w minutach do nastepnego przystanku
	 * 
	 * @return
	 */
	@Min(message = "Minimum: 0", value = 0)
	public int getCzasDoNastepnego() {
		return czasDoNastepnego;
	}
}
