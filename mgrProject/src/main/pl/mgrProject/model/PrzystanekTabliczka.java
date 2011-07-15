package pl.mgrProject.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.Name;

@Entity
@Name("przystanekTabliczka")
@Table(name="PRZYSTANEK_TABLICZKI")
@NamedQueries({
	//wyciaga z bazy wszystkie Tabliczki
	@NamedQuery(name="wszystkieTabliczki", query="SELECT OBJECT(przystTabl) FROM PrzystanekTabliczka przystTabl")
	})
public class PrzystanekTabliczka implements Serializable{


	private Long id;
	private Integer version;
	private Linia linia;
	private Przystanek przystanek;
	private Przystanek nastepnyPrzystanek;

	private Set<Odjazd> odjazdy = new HashSet<Odjazd>();
	
	//private Map<TypDnia, Set<> >
	
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
	
	@ManyToOne
	@NotNull
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
	
	@ManyToOne
	public Przystanek getNastepnyPrzystanek() {
		return nastepnyPrzystanek;
	}
	public void setNastepnyPrzystanek(Przystanek nastepnyPrzystanek) {
		this.nastepnyPrzystanek = nastepnyPrzystanek;
	}
	
	@OneToMany(mappedBy="przystanekTabliczka")
	public Set<Odjazd> getOdjazdy() {
		return odjazdy;
	}
	
	public void setOdjazdy(Set<Odjazd> odjazdy) {
		this.odjazdy = odjazdy;
	}
	
	

	

}
