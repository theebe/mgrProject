package pl.mgrProject.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
 * Klasa encja mapuj�ca tabel� LINIE
 * @author bat
 *
 */
@Entity
@Name("linia")
@Table(name="LINIE")
@NamedQueries({
	//wyciaga z bazy wszystkie przystanki
	@NamedQuery(name="wszystkieLinie", query="SELECT OBJECT(lin) FROM Linia lin"),
	@NamedQuery(name="liniePoNumerze", query="SELECT OBJECT(lin) FROM Linia lin where lin.numer = :numer"),
	@NamedQuery(name="liniePoPrzystanku", query="SELECT przystTabl.linia FROM PrzystanekTabliczka przystTabl WHERE przystTabl.przystanek = :przystanek")
	})
public class Linia {
	
	private Long id;
	private Integer version;
	private Integer numer;
	private TypKomunikacji typ;

	private List<PrzystanekTabliczka> przystanekTabliczka = new ArrayList<PrzystanekTabliczka>();

	@Transient //niewidzialny dla bazy danych
	@In EntityManager mgrDatabase;
	
	
	
	@PrePersist //trigger przed createm i updatem w bazie
	public void beforeCreate() throws HibernateException{
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
	@Range(min=0, max=999, message="Warto�� musi by� z przedzia�u od 0 do 999")
	public Integer getNumer() {
		return numer;
	}
	public void setNumer(Integer numer) {
		this.numer = numer;
	}
	
	
	@OneToMany(mappedBy = "linia")
	@OrderColumn(name="przystanektabliczka_order")
	public List<PrzystanekTabliczka> getPrzystanekTabliczka() {
		return przystanekTabliczka;
	}

	public void setPrzystanekTabliczka(List<PrzystanekTabliczka> przystanekTabliczka) {
		this.przystanekTabliczka = przystanekTabliczka;
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
