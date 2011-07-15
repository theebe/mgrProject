package pl.mgrProject.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import org.hibernate.validator.*;
import org.hibernate.HibernateException;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

/**
 * Klasa encja mapuj¹ca tabelê LINIE
 * @author bat
 *
 */
@Entity
@Name("linia")
@Table(name="LINIE")
@NamedQueries({
	//wyciaga z bazy wszystkie przystanki
	@NamedQuery(name="wszystkieLinie", query="SELECT OBJECT(lin) FROM Linia lin"),
	@NamedQuery(name="liniePoNumerze", query="SELECT OBJECT(lin) FROM Linia lin where lin.numer = :numer")
	})
public class Linia {
	
	private Long id;
	private Integer version;
	private Integer numer;
	
	private List<Przystanek> przystanki = new ArrayList<Przystanek>();
	
	private Set<PrzystanekTabliczka> przystanekTabliczka = new HashSet<PrzystanekTabliczka>();

	@Transient //niewidzialny dla bazy danych
	@In EntityManager mgrDatabase;
	
	@PrePersist //trigger przed createm i updatem w bazie
	public void beforeCreate() throws Exception{
		//pobiera liczbe lini o tym samym numerze
		Integer liczba = (Integer)mgrDatabase.createQuery("SELECT COUNT(l.numer) FROM Linia l WHERE l.numer = :numer").setParameter("numer", this.numer).getSingleResult();
		
		// liczba == 0, 1, 2
		if( liczba >= 3 && liczba <0) 
			//wyrzuca wyjatek, nie zapisze sie do bazy
			throw new HibernateException("Juz istnieja dwie linie o tym numerze. Nowa linia nie zostala dodana");
	}
	
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
	
	@NotNull
	@Range(min=0, max=999, message="Wartoœæ musi byæ z przedzia³u od 0 do 999")
	public Integer getNumer() {
		return numer;
	}
	public void setNumer(Integer numer) {
		this.numer = numer;
	}
	
	
	/**
	 * Relacja wiele do wielu z przystankami.
	 * Wystêpuje dodatkowa kolumna numeruj¹ca kolejnoœæ przystanków (kolejnoœæ przejezdzania przez przystanki)
	 * Relacja zastêpuje relacje dotyczace przystanu poczatkowego i koncowego
	 * @return
	 */
	@ManyToMany
	@OrderColumn
	public List<Przystanek> getPrzystanki() {
		return przystanki;
	}

	
	public void setPrzystanki(List<Przystanek> przystanki) {
		this.przystanki = przystanki;
	}

	@OneToMany(mappedBy = "linia")
	public Set<PrzystanekTabliczka> getPrzystanekTabliczka() {
		return przystanekTabliczka;
	}

	public void setPrzystanekTabliczka(Set<PrzystanekTabliczka> przystanekTabliczka) {
		this.przystanekTabliczka = przystanekTabliczka;
	}
	
	
}
