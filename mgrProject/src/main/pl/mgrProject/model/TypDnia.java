package pl.mgrProject.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.jboss.seam.annotations.Name;


public enum TypDnia {
	DZIEN_POWSZEDNI,
	SOBOTY,
	SWIETA
}
