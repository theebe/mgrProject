package pl.mgrProject.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.validator.Length;
import org.hibernate.validator.Min;
import org.hibernate.validator.NotNull;

@Entity
@Table(name="KONFIGURACJA")
@NamedQueries({
	@NamedQuery(name="konfiguracjaPoNazwie", query="SELECT konf FROM Konfiguracja konf WHERE konf.name LIKE :nazwa")
	})

public class Konfiguracja {
	private Long id;
	private Integer version;
	private String name;
	private Double predkoscPasazera;
	private Integer liczbaWatkow;
	private Integer odlegloscPrzystankow;
	private Integer odlegloscDoStartStop;
	private Integer nieskonczonosc;
	
	
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

	private void setVersion(Integer version) {
		this.version = version;
	}

	@Length(max = 20)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull
	@Min(value=0, message="Predkosc nie moze byc mniejsza od 0")
	public Double getPredkoscPasazera() {
		return predkoscPasazera;
	}
	
	public void setPredkoscPasazera(Double predkoscPasazera) {
		this.predkoscPasazera = predkoscPasazera;
	}
	
	@NotNull
	@Min(value=0, message="Liczba watkow nie moze byc mniejsza od 0")
	public Integer getLiczbaWatkow() {
		return liczbaWatkow;
	}

	public void setLiczbaWatkow(Integer liczbaWatkow) {
		this.liczbaWatkow = liczbaWatkow;
	}

	@NotNull
	@Min(value=0, message="Odeglosc miedzy prystankami nie moze byc mniejsza od 0")
	public Integer getOdlegloscPrzystankow() {
		return odlegloscPrzystankow;
	}

	public void setOdlegloscPrzystankow(Integer odlegloscPrzystankow) {
		this.odlegloscPrzystankow = odlegloscPrzystankow;
	}
	
	@NotNull
	@Min(value=0, message="Algorytm nie obsluguje wartosci ujemnych")
	public Integer getNieskonczonosc() {
		return nieskonczonosc;
	}

	public void setNieskonczonosc(Integer nieskonczonosc) {
		this.nieskonczonosc = nieskonczonosc;
	}
	
	@NotNull
	@Min(value=0, message="Odeglosc nie moze byc mniejsza od 0")
	public Integer getOdlegloscDoStartStop() {
		return odlegloscDoStartStop;
	}

	public void setOdlegloscDoStartStop(Integer odlegloscDoStartStop) {
		this.odlegloscDoStartStop = odlegloscDoStartStop;
	}

}
